package com.minecraft.plugin.elite.general.punish.mute;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
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
import java.util.*;

public class MuteManager {
	
	private static Map<UUID, Mute> mutes = new HashMap<>();
	private static Map<UUID, TempMute> temporaryMutes = new HashMap<>();
	private static Map<UUID, Collection<PastMute>> pastMutes = new HashMap<>();

	public static Collection<PastMute> getPastMutes(UUID uuid) {
		return pastMutes.get(uuid);
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
		for(UUID uuid : pastMutes.keySet())
			for(PastMute pastMute : getPastMutes(uuid))
				if(pastMute.getUniqueId().equals(id))
					return pastMute;
		return null;
	}
	
	public static Mute getMute(UUID uuid) {
		Mute mute = mutes.get(uuid);
		if(mute != null)
			return mute;
		TempMute tempMute = temporaryMutes.get(uuid);
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
		long time = 0;
		Mute mute;
		if(reason.isTemp()) {
			time = PunishManager.computeTime((MuteManager.getPastMutes(target.getUniqueId(), reason).size() + 1D), reason.getModifier(), reason.getUnit());
			mute = new TempMute(muterName, target, reason, muteDetails, time, System.currentTimeMillis(), id);
			temporaryMutes.put(target.getUniqueId(), (TempMute) mute);
			mutes.remove(target.getUniqueId());
		} else {
			mute = new Mute(muterName, target, reason, muteDetails, System.currentTimeMillis(), id);
			mutes.put(target.getUniqueId(), mute);
			temporaryMutes.remove(target.getUniqueId());
		}

		Database db = General.getDB();
		if(db.containsValue(General.DB_MUTES, "target", target.getUniqueId().toString()))
			db.delete(General.DB_MUTES, "target", target.getUniqueId());

		db.execute("INSERT INTO " + General.DB_MUTES + " (target, id, tempmute, time, muter, reason, details, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
				target.getUniqueId(), id, (reason.isTemp() ? 1: 0), time, muterName, reason, muteDetails.trim(), System.currentTimeMillis());

		System.out.println(Language.ENGLISH.get(com.minecraft.plugin.elite.general.GeneralLanguage.MUTE_MUTED).replaceAll("%z", target.getName()));
		for(Player players : Bukkit.getOnlinePlayers()) {
			GeneralPlayer all = GeneralPlayer.get(players);
			String msg = all.getLanguage().get(com.minecraft.plugin.elite.general.GeneralLanguage.MUTE_MUTED).replaceAll("%z", target.getName());
			if(all.hasPermission(GeneralPermission.PUNISH_INFO_CHECK))
				all.sendHoverMessage(msg, mute.getInfo(com.minecraft.plugin.elite.general.GeneralLanguage.MUTE_INFO, all.getLanguage()));
			else
				all.getPlayer().sendMessage(msg);
		}
	}
	
	public static void reload() {
		mutes.clear();
		temporaryMutes.clear();
		pastMutes.clear();

		Database db = General.getDB();
		try {
			ResultSet muteRes = db.select(General.DB_MUTES);
			while(muteRes.next()) {
				String muter = muteRes.getString("muter");
				OfflinePlayer target = Bukkit.getOfflinePlayer(UUID.fromString(muteRes.getString("target")));
				PunishReason reason = PunishReason.valueOf(muteRes.getString("reason").toUpperCase());
				String details = muteRes.getString("details");
				long time = muteRes.getLong("time");
				long date = muteRes.getLong("date");
				UUID id = UUID.fromString(muteRes.getString("id"));
				boolean isTemp = muteRes.getBoolean("tempmute");
				if(isTemp) {
					TempMute tempMute = new TempMute(muter, target, reason, details, time, date, id);
					temporaryMutes.put(target.getUniqueId(), tempMute);
				} else {
					Mute mute = new Mute(muter, target, reason, details, date, id);
					mutes.put(target.getUniqueId(), mute);
				}
			}
			ResultSet pastMuteRes = db.select(General.DB_MUTE_HISTORY);
			while(pastMuteRes.next()) {
				String muter = pastMuteRes.getString("muter");
				OfflinePlayer target = Bukkit.getOfflinePlayer(UUID.fromString(pastMuteRes.getString("target")));
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
		else if(temporaryMutes.containsKey(z.getUniqueId()))
			mute = temporaryMutes.get(z.getUniqueId());

		if(mute != null) {
			final long currentTime = System.currentTimeMillis();
			final long time = (mute instanceof Temporary ? ((Temporary) mute).getTime() : 0);
			Database db = General.getDB();

			db.execute("INSERT INTO " + General.DB_MUTE_HISTORY + " (tempmute, time, muter, id, reason, details, date, target, unmuter, unmutedate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
					(mute instanceof Temporary ? 1 : 0), time, mute.getPunisher(), mute.getUniqueId(), mute.getReason().toString(), mute.getDetails(), mute.getDate(), mute.getTarget(), unmuter, currentTime);

			PastMute pastMute = new PastMute(mute.getUniqueId(), mute.getTarget(), mute.getReason(), mute.getDetails(), mute.getDate(), time, mute.getPunisher(), unmuter, currentTime);
			addPastMute(z, pastMute);

			db.delete(General.DB_MUTES, "target", z.getUniqueId());

			mutes.remove(z.getUniqueId());
			temporaryMutes.remove(z.getUniqueId());

			System.out.println(Language.ENGLISH.get(com.minecraft.plugin.elite.general.GeneralLanguage.UNMUTE_UNMUTED).replaceAll("%z", z.getName()).replaceAll("%p", unmuter));
			for(Player players : Bukkit.getOnlinePlayers()) {
				GeneralPlayer all = GeneralPlayer.get(players);
				all.sendClickMessage(all.getLanguage().get(com.minecraft.plugin.elite.general.GeneralLanguage.UNMUTE_UNMUTED).replaceAll("%z", z.getName()).replaceAll("%p", unmuter), "/checkinfo " + z.getName(), true);
			}
		}
	}

	public static void addPastMute(OfflinePlayer target, PastMute pastMute) {
		Collection<PastMute> pastMuteList = getPastMutes(target.getUniqueId());
		Collection<PastMute> tempPastMutes = new ArrayList<>();
		if(pastMuteList != null && !pastMuteList.isEmpty())
			tempPastMutes.addAll(pastMuteList);
		tempPastMutes.add(pastMute);
		pastMutes.put(target.getUniqueId(), tempPastMutes);
	}
}
