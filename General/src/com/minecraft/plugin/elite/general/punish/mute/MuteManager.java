package com.minecraft.plugin.elite.general.punish.mute;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.general.punish.PunishManager;
import com.minecraft.plugin.elite.general.punish.PunishReason;
import com.minecraft.plugin.elite.general.punish.Temporary;
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

public class MuteManager {
	
	private static Map<UUID, Mute> mutes = new HashMap<>();
	private static Map<UUID, TempMute> tempmutes = new HashMap<>();
	private static Map<UUID, Collection<PastMute>> pastmutes = new HashMap<>();

	public static Collection<PastMute> getPastMutes(UUID uuid) {
		return pastmutes.get(uuid);
	}

	public static Collection<PastMute> getPastMutes(UUID uuid, PunishReason reason) {
		Collection<PastMute> tempList = new ArrayList<>();
		Collection<PastMute> pastMutes = getPastMutes(uuid);
		if(pastMutes != null)
			for(PastMute pastMute : getPastMutes(uuid))
				if(pastMute.getReason() == reason)
					tempList.add(pastMute);
		return tempList;
	}

	public static PastMute getPastMute(UUID id) {
		for(UUID uuid : pastmutes.keySet())
			for(PastMute pastMute : getPastMutes(uuid))
				if(pastMute.getUniqueId().equals(id))
					return pastMute;
		return null;
	}
	
	public static Mute getMute(UUID uuid) {
		Mute mute = mutes.get(uuid);
		if(mute != null)
			return mute;
		TempMute tempMute = tempmutes.get(uuid);
		if(tempMute != null) {
			if(!tempMute.hasExpired()) {
				return tempMute;
			} else {
				unmutePlayer("System - Expired", Bukkit.getOfflinePlayer(uuid));
			}
		}
		return null;
	}
	
	public static void mute(UUID id, String muterName, OfflinePlayer target, PunishReason reason, String muteDetails) {
		if(mutes.containsKey(target.getUniqueId())) 
			mutes.remove(target.getUniqueId());
		if(tempmutes.containsKey(target.getUniqueId()))
			tempmutes.remove(target.getUniqueId());
		long time = 0;
		Mute mute;
		if(reason.isTemp()) {
			time = PunishManager.computeTime((MuteManager.getPastMutes(target.getUniqueId(), reason).size() + 1D), reason.getModifier(), reason.getUnit());
			mute = new TempMute(muterName, target.getUniqueId(), reason, muteDetails, time, System.currentTimeMillis(), id);
			tempmutes.put(target.getUniqueId(), (TempMute) mute);
		} else {
			mute = new Mute(muterName, target.getUniqueId(), reason, muteDetails, System.currentTimeMillis(), id);
			mutes.put(target.getUniqueId(), mute);
		}

		Database db = General.getDB();
		if(db.containsValue(General.DB_MUTES, "target", target.getUniqueId().toString()))
			db.delete(General.DB_MUTES, "target", target.getUniqueId());

		db.execute("INSERT INTO " + General.DB_MUTES + " (target, id, tempmute, time, muter, reason, details, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
				target.getUniqueId(), id, (reason.isTemp() ? 1: 0), time, muterName, reason, muteDetails.trim(), System.currentTimeMillis());

		System.out.println(Language.ENGLISH.get(GeneralLanguage.MUTE_MUTED).replaceAll("%z", target.getName()));
		for(Player players : Bukkit.getOnlinePlayers()) {
			ePlayer all = ePlayer.get(players);
			String msg = all.getLanguage().get(GeneralLanguage.MUTE_MUTED).replaceAll("%z", target.getName());
			if(all.getPlayer().hasPermission("eban.checkinfo"))
				all.sendHoverMessage(msg, mute.getInfo(GeneralLanguage.MUTE_INFO, all.getLanguage()));
			else
				all.getPlayer().sendMessage(msg);
		}
	}
	
	public static void reload() {
		mutes.clear();
		tempmutes.clear();
		pastmutes.clear();

		Database db = General.getDB();
		try {
			ResultSet muteRes = db.select(General.DB_MUTES);
			while(muteRes.next()) {
				String muter = muteRes.getString("muter");
				UUID target = UUID.fromString(muteRes.getString("target"));
				PunishReason reason = PunishReason.valueOf(muteRes.getString("reason").toUpperCase());
				String details = muteRes.getString("details");
				long time = muteRes.getLong("time");
				long date = muteRes.getLong("date");
				UUID id = UUID.fromString(muteRes.getString("id"));
				boolean isTemp = muteRes.getBoolean("tempmute");
				if(isTemp) {
					TempMute tempMute = new TempMute(muter, target, reason, details, time, date, id);
					tempmutes.put(target, tempMute);
				} else {
					Mute mute = new Mute(muter, target, reason, details, date, id);
					mutes.put(target, mute);
				}
			}
			ResultSet pastMuteRes = db.select(General.DB_MUTEHISTORY);
			while(pastMuteRes.next()) {
				String muter = pastMuteRes.getString("muter");
				UUID target = UUID.fromString(pastMuteRes.getString("target"));
				PunishReason reason = PunishReason.valueOf(pastMuteRes.getString("reason").toUpperCase());
				String details = pastMuteRes.getString("details");
				long time = pastMuteRes.getLong("time");
				long date = pastMuteRes.getLong("date");
				UUID id = UUID.fromString(pastMuteRes.getString("id"));
				String unmuter = pastMuteRes.getString("unmuter");
				long unmutedate = pastMuteRes.getLong("unmutedate");

				PastMute pastMute = new PastMute(id, target, reason, details, date, time, muter, unmuter, unmutedate);
				addPastMute(target, pastMute);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
    public static void unmutePlayer(String unmuter, OfflinePlayer z) {

		Mute mute = null;
		if(mutes.containsKey(z.getUniqueId()))
			mute = mutes.get(z.getUniqueId());
		else if(tempmutes.containsKey(z.getUniqueId()))
			mute = tempmutes.get(z.getUniqueId());

		if(mute != null) {
			final long currentTime = System.currentTimeMillis();
			final long time = (mute instanceof Temporary ? ((Temporary) mute).getTime() : 0);
			Database db = General.getDB();

			db.execute("INSERT INTO " + General.DB_MUTEHISTORY + " (tempmute, time, muter, id, reason, details, date, target, unmuter, unmutedate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
					(mute instanceof Temporary ? 1 : 0), time, mute.getPunisher(), mute.getUniqueId(), mute.getReason().toString(), mute.getDetails(), mute.getDate(), mute.getTarget(), unmuter, currentTime);

			PastMute pastMute = new PastMute(mute.getUniqueId(), mute.getTarget(), mute.getReason(), mute.getDetails(), mute.getDate(), time, mute.getPunisher(), unmuter, currentTime);
			addPastMute(z.getUniqueId(), pastMute);

			db.delete(General.DB_MUTES, "target", z.getUniqueId());

			if(mutes.containsKey(z.getUniqueId()))
				mutes.remove(z.getUniqueId());
			if(tempmutes.containsKey(z.getUniqueId()))
				tempmutes.remove(z.getUniqueId());

			System.out.println(Language.ENGLISH.get(GeneralLanguage.UNMUTE_UNMUTED).replaceAll("%z", z.getName()).replaceAll("%p", unmuter));
			for(Player players : Bukkit.getOnlinePlayers()) {
				ePlayer all = ePlayer.get(players);
				all.sendClickMessage(all.getLanguage().get(GeneralLanguage.UNMUTE_UNMUTED).replaceAll("%z", z.getName()).replaceAll("%p", unmuter), "/checkinfo " + z.getName(), true);
			}
		}
	}

	public static void addPastMute(UUID uuid, PastMute pastMute) {
		Collection<PastMute> pastMuteList = getPastMutes(uuid);
		Collection<PastMute> tempPastMutes = new ArrayList<>();
		if(pastMuteList != null && !pastMuteList.isEmpty())
			tempPastMutes.addAll(pastMuteList);
		tempPastMutes.add(pastMute);
		if(pastmutes.containsKey(uuid))
			pastmutes.remove(uuid);
		pastmutes.put(uuid, tempPastMutes);
	}
}
