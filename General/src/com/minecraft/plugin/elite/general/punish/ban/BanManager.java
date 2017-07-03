package com.minecraft.plugin.elite.general.punish.ban;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.general.punish.PunishManager;
import com.minecraft.plugin.elite.general.punish.PunishReason;
import com.minecraft.plugin.elite.general.punish.Temporary;
import com.minecraft.plugin.elite.general.punish.report.ReportManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BanManager {
	
	private static Map<UUID, Ban> bans = new HashMap<>();
	private static Map<UUID, TempBan> tempbans = new HashMap<>();
	private static Map<UUID, Collection<PastBan>> pastbans = new HashMap<>();

	public static Collection<PastBan> getPastBans(UUID uuid) {
		return pastbans.get(uuid);
	}

	public static Collection<PastBan> getPastBans(UUID uuid, PunishReason reason) {
		Collection<PastBan> tempList = new ArrayList<>();
		Collection<PastBan> pastBans = new ArrayList<>();
		if(pastBans != null)
			for(PastBan pastBan : getPastBans(uuid))
				if(pastBan.getReason() == reason)
					tempList.add(pastBan);
		return tempList;
	}

	public static PastBan getPastBan(UUID id) {
		for(UUID uuid : pastbans.keySet())
			for(PastBan pastBan : getPastBans(uuid))
				if(pastBan.getUniqueId().equals(id))
					return pastBan;
		return null;
	}

	public static Ban getBan(UUID uuid){
		
    	Ban ban = bans.get(uuid);
    	if(ban != null){
    		return ban;
    	}	
    	TempBan tempBan = tempbans.get(uuid);
    	if(tempBan != null){
    		if(!tempBan.hasExpired()) {
    			return tempBan;
    		} else {
    			unbanPlayer("System - Expired", Bukkit.getOfflinePlayer(uuid));
    		}
    	}
    	return null;
    }
	
	public static void ban(UUID id, String bannerName, OfflinePlayer target, PunishReason reason, String banDetails) {

		String kick_screen;
		long time = 0;
		if(bans.containsKey(target.getUniqueId())) 
			bans.remove(target.getUniqueId());
		if(tempbans.containsKey(target.getUniqueId()))
			tempbans.remove(target.getUniqueId());
		Ban ban;
		if(reason.isTemp()) {
			time = PunishManager.computeTime((BanManager.getPastBans(target.getUniqueId(), reason).size() + 1D), reason.getModifier(), reason.getUnit());
			ban = new TempBan(bannerName, target.getUniqueId(), reason, banDetails, time, System.currentTimeMillis(), id);
			tempbans.put(target.getUniqueId(), (TempBan) ban);
		} else {
			ban = new Ban(bannerName, target.getUniqueId(), reason, banDetails, System.currentTimeMillis(), id);
			bans.put(target.getUniqueId(), ban);
		}

		Database db = General.getDB();
		if(db.containsValue(General.DB_BANS, "target", target.getUniqueId().toString()))
			db.delete(General.DB_BANS, "target", target.getUniqueId());

		db.execute("INSERT INTO " + General.DB_BANS + " (target, id, tempban, time, banner, reason, details, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
				target.getUniqueId(), id, (ban instanceof Temporary ? 1 : 0), time, bannerName, reason.toString(), banDetails.trim(), System.currentTimeMillis());

		ReportManager.clearReportsOnBan(target.getUniqueId());

		if(target.isOnline()) {
			ePlayer z = ePlayer.get(target.getUniqueId());
			z.sendMessage(GeneralLanguage.BAN_WARNING);
			final String kick = ban.getKickMessage(z.getLanguage());
			z.getPlayer().kickPlayer(kick);
		}

		System.out.println(Language.ENGLISH.get(GeneralLanguage.BAN_BANNED).replaceAll("%z", target.getName()));
		for(Player players : Bukkit.getOnlinePlayers()) {
			ePlayer all = ePlayer.get(players);
			String msg = all.getLanguage().get(GeneralLanguage.BAN_BANNED).replaceAll("%z", target.getName());

			if(all.getPlayer().hasPermission("eban.checkinfo"))
				all.sendHoverMessage(msg, ban.getInfo(GeneralLanguage.BAN_INFO, all.getLanguage()));
			else
				all.getPlayer().sendMessage(msg);
		}
	}
	
	public static void reload() {
		bans.clear();
		tempbans.clear();
		pastbans.clear();
		
		Database db = General.getDB();
		
		try {
			ResultSet banRes = db.select(General.DB_BANS);
			while(banRes.next()) {
				String banner = banRes.getString("banner");
				UUID hacker = UUID.fromString(banRes.getString("target"));
				PunishReason reason = PunishReason.valueOf(banRes.getString("reason").toUpperCase());
				String details = banRes.getString("details");
				long date = banRes.getLong("date");
				long time = banRes.getLong("time");
				UUID id = UUID.fromString(banRes.getString("id"));
				boolean isTemp = banRes.getBoolean("tempban");
				if(isTemp) {
					TempBan tempBan = new TempBan(banner, hacker, reason, details, time, date, id);
					tempbans.put(hacker, tempBan);
				} else {
					Ban ban = new Ban(banner, hacker, reason, details, date, id);
					bans.put(hacker, ban);
				}
			}

			ResultSet pastBanRes = db.select(General.DB_BANHISTORY);
			while(pastBanRes.next()) {
				String banner = pastBanRes.getString("banner");
				UUID hacker = UUID.fromString(pastBanRes.getString("target"));
				PunishReason reason = PunishReason.valueOf(pastBanRes.getString("reason").toUpperCase());
				String details = pastBanRes.getString("details");
				long date = pastBanRes.getLong("date");
				long time = pastBanRes.getLong("time");
				UUID id = UUID.fromString(pastBanRes.getString("id"));
				String unbanner = pastBanRes.getString("unbanner");
				long unbandate = pastBanRes.getLong("unbandate");

				PastBan pastBan = new PastBan(id, hacker, reason, details, date, time, banner, unbanner, unbandate);
				addPastBan(hacker, pastBan);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
 	
    public static void unbanPlayer(String unbanner, OfflinePlayer z) {
		Ban ban = null;
		if(bans.containsKey(z.getUniqueId()))
			ban = bans.get(z.getUniqueId());
		else if(tempbans.containsKey(z.getUniqueId()))
			ban = tempbans.get(z.getUniqueId());

		if(ban != null) {
			Database db = General.getDB();
			final long currentTime = System.currentTimeMillis();
			final long time = (ban instanceof Temporary ? ((Temporary) ban).getTime() : 0);

			db.execute("INSERT INTO " + General.DB_BANHISTORY + " (tempban, time, banner, id, reason, details, date, target, unbanner, unbandate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
					(ban instanceof Temporary ? 1 : 0), time, ban.getPunisher(), ban.getUniqueId(), ban.getReason().toString(), ban.getDetails(), ban.getDate(), ban.getTarget(), unbanner, currentTime);

			PastBan pastBan = new PastBan(ban.getUniqueId(), ban.getTarget(), ban.getReason(), ban.getDetails(), ban.getDate(), time, ban.getPunisher(), unbanner, currentTime);
			addPastBan(z.getUniqueId(), pastBan);

			db.delete(General.DB_BANS, "target", z.getUniqueId());

			if(bans.containsKey(z.getUniqueId()))
				bans.remove(z.getUniqueId());
			if(tempbans.containsKey(z.getUniqueId()))
				tempbans.remove(z.getUniqueId());

			System.out.println(Language.ENGLISH.get(GeneralLanguage.UNBAN_UNBANNED).replaceAll("%z", z.getName()).replaceAll("%p", unbanner));
			for(Player players : Bukkit.getOnlinePlayers()) {
				ePlayer all = ePlayer.get(players);
				all.sendClickMessage(all.getLanguage().get(GeneralLanguage.UNBAN_UNBANNED).replaceAll("%z", z.getName()).replaceAll("%p", unbanner), "/checkinfo " + z.getName(), true);
			}
		}
	}

	public static void addPastBan(UUID uuid, PastBan pastBan) {
		Collection<PastBan> pastBansList = getPastBans(uuid);
		Collection<PastBan> tempPastBans = new ArrayList<>();
		if(pastBansList != null && !pastBansList.isEmpty())
			tempPastBans.addAll(pastBansList);
		tempPastBans.add(pastBan);
		if(pastbans.containsKey(uuid))
			pastbans.remove(uuid);
		pastbans.put(uuid, tempPastBans);
	}
}