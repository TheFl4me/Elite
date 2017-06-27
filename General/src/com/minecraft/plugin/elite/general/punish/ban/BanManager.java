package com.minecraft.plugin.elite.general.punish.ban;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.general.punish.PunishManager;
import com.minecraft.plugin.elite.general.punish.report.ReportManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BanManager {
	
	private static Map<UUID, Ban> bans = new HashMap<>();
	private static Map<UUID, TempBan> tempbans = new HashMap<>();
	
	public static void clearHashMaps(UUID uuid) {
		bans.remove(uuid);
		tempbans.remove(uuid);
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
    			tempbans.remove(uuid);
    			try {
					saveUnban(uuid, "Expired");
				} catch (SQLException e) {
					e.printStackTrace();
				}
    		}
    	}
    	return null;
    }
	
	public static void ban(String bannerName, OfflinePlayer target, BanReason reason, String banDetails) {

		String kick_screen;
		long time = 0;
		UUID id = PunishManager.generateUUID();
		if(bans.containsKey(target.getUniqueId())) 
			bans.remove(target.getUniqueId());
		if(tempbans.containsKey(target.getUniqueId()))
			tempbans.remove(target.getUniqueId());
		if(reason.isTemp()) {
			time = PunishManager.computeTime((PunishManager.getPastBanIDs(target.getUniqueId()).size() + 1D), reason.getModifier());
			TempBan ban = new TempBan(bannerName, target.getUniqueId(), reason.toDisplayString(), banDetails, time, System.currentTimeMillis(), id);
			tempbans.put(target.getUniqueId(), ban);
			kick_screen = ban.getKickMessage();
		} else {
			Ban ban = new Ban(bannerName, target.getUniqueId(), reason.toDisplayString(), banDetails, System.currentTimeMillis(), id);
			bans.put(target.getUniqueId(), ban);
			kick_screen = ban.getKickMessage();
		}

		Database db = General.getDB();
		if(db.containsValue(General.DB_BANS, "target", target.getUniqueId().toString()))
			db.delete(General.DB_BANS, "target", target.getUniqueId());

		db.execute("INSERT INTO " + General.DB_BANS + " (target, id, tempban, time, banner, reason, details, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
				target.getUniqueId(), id, (reason.isTemp() ? 1 : 0), time, bannerName, reason.toString(), banDetails.substring(0, banDetails.length() -1), System.currentTimeMillis());

		ReportManager.clearReportsOnBan(target.getUniqueId());

		if(target.isOnline()) {
			ePlayer z = ePlayer.get(target.getUniqueId());
			z.sendMessage(GeneralLanguage.BAN_WARNING);
			final String kick = kick_screen;
			z.getPlayer().kickPlayer(kick);
		}

		System.out.println(Language.ENGLISH.get(GeneralLanguage.BAN_BANNED).replaceAll("%z", target.getName()));
		for(Player players : Bukkit.getOnlinePlayers()) {
			ePlayer all = ePlayer.get(players);
			String msg = all.getLanguage().get(GeneralLanguage.BAN_BANNED).replaceAll("%z", target.getName());

			if(all.getPlayer().hasPermission("eban.checkinfo"))
				try {
					all.sendHoverMessage(msg, PunishManager.banIDInfo(id, all.getLanguage()));
				} catch (SQLException e) {
					e.printStackTrace();
					all.sendHoverMessage(msg, all.getLanguage().get(GeneralLanguage.INFO_GUI_ERROR));
				}
			else
				all.getPlayer().sendMessage(msg);
		}
	}
	
	public static void reload() {
		bans.clear();
		tempbans.clear();
		
		Database db = General.getDB();
		
		try {
			ResultSet banRes = db.getConnection().createStatement().executeQuery("SELECT * FROM " + General.DB_BANS);
			while(banRes.next()) {
				String banner = banRes.getString("banner");
				UUID hacker = UUID.fromString(banRes.getString("target"));
				String reason = banRes.getString("reason");
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
 	
    public static void unbanPlayer(CommandSender unbanner, OfflinePlayer z) {
		
		Ban ban = bans.get(z.getUniqueId());
		TempBan tempBan = tempbans.get(z.getUniqueId());
		if(ban != null)
			bans.remove(z.getUniqueId());
		if(tempBan != null)
			tempbans.remove(z.getUniqueId());
		try {
			saveUnban(z.getUniqueId(), unbanner.getName());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println(Language.ENGLISH.get(GeneralLanguage.UNBAN_UNBANNED).replaceAll("%z", z.getName()).replaceAll("%p", unbanner.getName()));
		for(Player players : Bukkit.getOnlinePlayers()) {
			ePlayer all = ePlayer.get(players);
			all.sendClickMessage(all.getLanguage().get(GeneralLanguage.UNBAN_UNBANNED).replaceAll("%z", z.getName()).replaceAll("%p", unbanner.getName()), "/checkinfo " + z.getName(), true);
		}
	}

	private static void saveUnban(UUID uuid, String unbanner) throws SQLException {
		Database db = General.getDB();
		ResultSet res = db.select(General.DB_BANS, "target", uuid.toString());
		if(res.next()) {
			db.execute("INSERT INTO " + General.DB_BANHISTORY + " (tempban, time, banner, id, reason, details, date, target, unbanner, unbandate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
					(res.getBoolean("tempban") ? 1 : 0), res.getLong("time"), res.getString("banner"), res.getString("id"), res.getString("reason"), res.getString("details"), res.getLong("date"), res.getString("target"), unbanner, System.currentTimeMillis());
			db.delete(General.DB_BANS, "target", uuid);
		}
	}
}