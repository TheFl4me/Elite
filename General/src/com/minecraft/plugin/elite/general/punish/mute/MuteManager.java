package com.minecraft.plugin.elite.general.punish.mute;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.api.enums.Unit;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.general.punish.PunishManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MuteManager {
	
	private static Map<UUID, Mute> mutes = new HashMap<>();
	private static Map<UUID, TempMute> tempmutes = new HashMap<>();
	
	public static Mute getMute(UUID uuid) {
		Mute mute = mutes.get(uuid);
		if(mute != null)
			return mute;
		TempMute tempMute = tempmutes.get(uuid);
		if(tempMute != null) {
			if(!tempMute.hasExpired()) {
				return tempMute;
			} else {
				tempmutes.remove(uuid);
				try {
					saveUnmute(uuid, "Expired");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static void mute(String muterName, OfflinePlayer target, MuteReason reason, String muteDetails) {
		if(mutes.containsKey(target.getUniqueId())) 
			mutes.remove(target.getUniqueId());
		if(tempmutes.containsKey(target.getUniqueId()))
			tempmutes.remove(target.getUniqueId());
		UUID id = PunishManager.generateUUID();
		long time = 0;
		if(reason.isTemp()) {
			time = PunishManager.computeTime((PunishManager.getPastMuteIDs(target.getUniqueId()).size() + 1D), reason.getModifier(), Unit.HOURS);
			TempMute mute = new TempMute(muterName, target.getUniqueId(), reason.toDisplayString(), muteDetails, time, System.currentTimeMillis(), id);
			tempmutes.put(target.getUniqueId(), mute);
		} else {
			Mute mute = new Mute(muterName, target.getUniqueId(), reason.toDisplayString(), muteDetails, System.currentTimeMillis(), id);
			mutes.put(target.getUniqueId(), mute);
		}

		Database db = General.getDB();
		if(db.containsValue(General.DB_MUTES, "target", target.getUniqueId().toString()))
			db.delete(General.DB_MUTES, "target", target.getUniqueId());

		db.execute("INSERT INTO " + General.DB_MUTES + " (target, id, tempmute, time, muter, reason, details, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
				target.getUniqueId(), id, (reason.isTemp() ? 1: 0), time, muterName, reason, muteDetails.substring(0, muteDetails.length() -1), System.currentTimeMillis());

		System.out.println(Language.ENGLISH.get(GeneralLanguage.MUTE_MUTED).replaceAll("%z", target.getName()));
		for(Player players : Bukkit.getOnlinePlayers()) {
			ePlayer all = ePlayer.get(players);
			String msg = all.getLanguage().get(GeneralLanguage.MUTE_MUTED).replaceAll("%z", target.getName());
			if(all.getPlayer().hasPermission("eban.checkinfo"))
				try {
					all.sendHoverMessage(msg, PunishManager.muteIDInfo(id, all.getLanguage()));
				} catch (SQLException e) {
					e.printStackTrace();
					all.sendHoverMessage(msg, all.getLanguage().get(GeneralLanguage.INFO_GUI_ERROR));
				}
			else
				all.getPlayer().sendMessage(msg);
		}
	}
	
	public static void reload() {
		mutes.clear();
		tempmutes.clear();
		Database db = General.getDB();
		try {
			ResultSet muteRes = db.select(General.DB_MUTES);
			while(muteRes.next()) {
				String muter = muteRes.getString("muter");
				UUID target = UUID.fromString(muteRes.getString("target"));
				String reason = muteRes.getString("reason");
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
    public static void unmutePlayer(CommandSender unmuter, OfflinePlayer z) {
		
		Mute mute = mutes.get(z.getUniqueId());
		TempMute tempMute = tempmutes.get(z.getUniqueId());
		
		if(mute != null)
			mutes.remove(z.getUniqueId());
		if(tempMute != null)
			tempmutes.remove(z.getUniqueId());
	    try {
			saveUnmute(z.getUniqueId(), unmuter.getName());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println(Language.ENGLISH.get(GeneralLanguage.UNMUTE_UNMUTED).replaceAll("%z", z.getName()).replaceAll("%p", unmuter.getName()));
		for(Player players : Bukkit.getOnlinePlayers()) {
			ePlayer all = ePlayer.get(players);
			all.sendClickMessage(all.getLanguage().get(GeneralLanguage.UNMUTE_UNMUTED).replaceAll("%z", z.getName()).replaceAll("%p", unmuter.getName()), "/checkinfo " + z.getName(), true);
		}
				
	}
	
	private static void saveUnmute(UUID uuid, String unmuter) throws SQLException {
		Database db = General.getDB();
		ResultSet res = db.select(General.DB_MUTES, "target", uuid.toString());
		if(res.next()) {
			db.execute("INSERT INTO " + General.DB_MUTEHISTORY + " (tempmute, time, muter, id, reason, details, date, target, unmuter, unmutedate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
					(res.getBoolean("tempmute") ? 1: 0), res.getLong("time"), res.getString("muter"), res.getString("id"), res.getString("reason"), res.getString("details"), res.getLong("date"), res.getString("target"), unmuter, System.currentTimeMillis());
			db.delete(General.DB_MUTES, "target", uuid);
		}
	}
}
