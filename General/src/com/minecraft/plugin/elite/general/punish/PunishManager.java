package com.minecraft.plugin.elite.general.punish;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.general.punish.ban.Ban;
import com.minecraft.plugin.elite.general.punish.ban.BanManager;
import com.minecraft.plugin.elite.general.punish.mute.Mute;
import com.minecraft.plugin.elite.general.punish.mute.MuteManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PunishManager {

	public static UUID generateUUID() {
		UUID uuid = UUID.randomUUID();
		boolean exists = true;
		Database db = General.getDB();
		while(exists)
			if(!db.containsValue(General.DB_BANS, "id", uuid.toString()) && !db.containsValue(General.DB_BANHISTORY, "id", uuid.toString())&& !db.containsValue(General.DB_MUTES, "id", uuid.toString())&& !db.containsValue(General.DB_MUTEHISTORY, "id", uuid.toString()))
				exists = false;
			else
				uuid = UUID.randomUUID();
		return uuid;
	}
	
	public static boolean isMuted(UUID target) {
		Mute mute = MuteManager.getMute(target);
		return mute != null;
	}
	
	public static boolean isBanned(UUID uuid) {
		Ban ban = BanManager.getBan(uuid);
		return ban != null;
	}
    
    public static List<UUID> getActiveBanIDs(UUID uuid) {
		List<UUID> finalList = new ArrayList<>();
    	Database db = General.getDB();
		ResultSet active = db.select(General.DB_BANS, "target", uuid.toString());
		try {
			while(active.next())
				finalList.add(UUID.fromString(active.getString("id")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return finalList;
    }
    
    public static List<UUID> getPastBanIDs(UUID uuid) {
    	List<UUID> finalList = new ArrayList<>();
    	Database db = General.getDB();
		ResultSet past = db.select(General.DB_BANHISTORY, "target", uuid.toString());
		try {
			while(past.next())
				finalList.add(UUID.fromString(past.getString("id")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return finalList;
    }
    
    public static List<UUID> getActiveMuteIDs(UUID uuid) {
		List<UUID> finalList = new ArrayList<>();
    	Database db = General.getDB();
		ResultSet active = db.select(General.DB_MUTES, "target", uuid.toString());
		try {
			while(active.next())
				finalList.add(UUID.fromString(active.getString("id")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return finalList;
    }
    
    public static List<UUID> getPastMuteIDs(UUID uuid) {
		List<UUID> finalList = new ArrayList<>();
    	Database db = General.getDB();
		ResultSet past = db.select(General.DB_MUTEHISTORY, "target", uuid.toString());
		try {
			while(past.next())
				finalList.add(UUID.fromString(past.getString("id")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return finalList;
    }
    
    public static long getSentReports(UUID uuid) {
		Database db = General.getDB();
		try {
			ResultSet res = db.select(General.DB_PLAYERS, "uuid", uuid.toString());
			if(res.next())
				return res.getLong("sentreports");
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static long getTrueSentReports(UUID uuid) {
		Database db = General.getDB();
		try {
			ResultSet res = db.select(General.DB_PLAYERS, "uuid", uuid.toString());
			if(res.next())
				return res.getLong("truereports");
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static void addSentReport(UUID uuid) {
		Database db = General.getDB();
		try {
			ResultSet res = db.select(General.DB_PLAYERS, "uuid", uuid.toString());
			if(res.next())
				db.update(General.DB_PLAYERS, "sentreports", getSentReports(uuid) + 1, "uuid", uuid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static String banIDInfo(UUID id, Language lang) throws SQLException {
		ChatColor mainColor =  ChatColor.DARK_GRAY;
		ChatColor secondColor = ChatColor.WHITE;
		Database db = General.getDB();
		boolean exists = false;
		boolean active = false;
		StringBuilder info = new StringBuilder();
		OfflinePlayer hacker = null;
		String reason = null;
		String details = null;
		String time = null;
		String remaining = null;
		String date = null;
		String banner = null;
		String unbanner = null;
		String unbandate = null;
		Server server = Server.get();
		ResultSet ban = db.select(General.DB_BANS);
		while(ban.next()) {
			if(ban.getString("id").equalsIgnoreCase(id.toString())) {
				exists = true;
				active = true;
				hacker = Bukkit.getOfflinePlayer(UUID.fromString(ban.getString("target")));
				reason = ban.getString("reason").trim();
				details = ban.getString("details").trim();
				time = (ban.getLong("time") == 0 ? "permanent" : server.getTime(ban.getLong("time"), lang));
				if(!time.equalsIgnoreCase("permanent"))
					remaining = server.getTimeUntil(ban.getLong("date") + ban.getLong("time"), lang);
				date = server.getDate(ban.getLong("date"));
				banner = ban.getString("banner");
				break;
			}
		}
		ResultSet pastban = db.select(General.DB_BANHISTORY);
		while(pastban.next()) {
			if(pastban.getString("id").equalsIgnoreCase(id.toString())) {
				exists = true;
				hacker = Bukkit.getOfflinePlayer(UUID.fromString(pastban.getString("target")));
				reason = pastban.getString("reason").trim();
				details = pastban.getString("details").trim();
				time = (pastban.getLong("time") == 0 ? "permanent" : server.getTime(pastban.getLong("time"), lang));
				date = server.getDate(pastban.getLong("date"));
				banner = pastban.getString("banner");
				unbanner = pastban.getString("unbanner");
				unbandate = server.getDate(pastban.getLong("unbandate"));
				break;
			}
		}
		if(exists) {
			info.append(mainColor + "ID: " + secondColor + id.toString());
			info.append("\n" + mainColor + "Target: " + secondColor + hacker.getName());
			info.append("\n" + mainColor + "Reason: " + secondColor + reason);
			info.append("\n" + mainColor + "Details: " + secondColor + details);
			info.append("\n" + mainColor + "Duration: " + secondColor + time);
			if(remaining != null)
				info.append("\n" + mainColor + "Remaining: " + secondColor + remaining);
			info.append("\n" + mainColor + "Active: " + secondColor + Boolean.toString(active));
			info.append("\n" + mainColor + "Ban-Date: " + secondColor + date);
			info.append("\n" + mainColor + "Banner: " + secondColor + banner);
			if (!active) {
				info.append("\n" + mainColor + "Unbanner: " + secondColor + unbanner);
				info.append("\n" + mainColor + "Unban-Date: " + secondColor + unbandate);
			}
			return info.toString();
		} else {
			return null;
		}
	}

	public static String muteIDInfo(UUID id, Language lang) throws SQLException {
		ChatColor mainColor =  ChatColor.DARK_GRAY;
		ChatColor secondColor = ChatColor.WHITE;
		Database db = General.getDB();
		boolean exists = false;
		boolean active = false;
		StringBuilder info = new StringBuilder();
		OfflinePlayer target = null;
		String reason = null;
		String details = null;
		String time = null;
		String remaining = null;
		String date = null;
		String muter = null;
		String unmuter = null;
		String unmutedate = null;
		Server server = Server.get();
		ResultSet mute = db.select(General.DB_MUTES);
		while(mute.next()) {
			if(mute.getString("id").equalsIgnoreCase(id.toString())) {
				exists = true;
				active = true;
				target = Bukkit.getOfflinePlayer(UUID.fromString(mute.getString("target")));
				reason = mute.getString("reason").trim();
				details = mute.getString("details").trim();
				time = (mute.getLong("time") == 0 ? "permanent" : server.getTime(mute.getLong("time"), lang));
				if(!time.equalsIgnoreCase("permanent"))
					remaining = server.getTimeUntil(mute.getLong("date") + mute.getLong("time"), lang);
				date = server.getDate(mute.getLong("date"));
				muter = mute.getString("muter");
				break;
			}
		}
		ResultSet pastmute = db.select(General.DB_MUTEHISTORY);
		while(pastmute.next()) {
			if(pastmute.getString("id").equalsIgnoreCase(id.toString())) {
				exists = true;
				target = Bukkit.getOfflinePlayer(UUID.fromString(pastmute.getString("target")));
				reason = pastmute.getString("reason").trim();
				details = pastmute.getString("details").trim();
				time = (pastmute.getLong("time") == 0 ? "permanent" : server.getTime(pastmute.getLong("time"), lang));
				date = server.getDate(pastmute.getLong("date"));
				muter = pastmute.getString("muter");
				unmuter = pastmute.getString("unmuter");
				unmutedate = server.getDate(pastmute.getLong("unmutedate"));
				break;
			}
		}
		if(exists) {
			info.append(mainColor + "ID: " + secondColor + id.toString());
			info.append("\n" + mainColor + "Target: " + secondColor + target.getName());
			info.append("\n" + mainColor + "Reason: " + secondColor + reason);
			info.append("\n" + mainColor + "Details: " + secondColor + details);
			info.append("\n" + mainColor + "Duration: " + secondColor + time);
			if(remaining != null)
				info.append("\n" + mainColor + "Remaining: " + secondColor + remaining);
			info.append("\n" + mainColor + "Active: " + secondColor + Boolean.toString(active));
			info.append("\n" + mainColor + "Mute-Date: " + secondColor + date);
			info.append("\n" + mainColor + "Muter: " + secondColor + muter);
			if(!active) {
				info.append("\n" + mainColor + "Unmuter: " + secondColor + unmuter);
				info.append("\n" + mainColor + "Unmute-Date: " + secondColor + unmutedate);
			}

			return info.toString();
		} else {
			return null;
		}
	}

	public static long computeTime(double x, double modifier, boolean hours) {

		final double a = 0.5D + modifier;
		final double b = 0.9D;
		final double c = 0D - modifier;
		final double d = -0.5D + modifier;
		final double z = -3D - modifier;
		final double y = a * Math.pow(b, z * x + c) + d;

		//y = days
		return Math.round(y * (1000L * 60L * 60L * (hours ? 1L : 24L)));
	}
}
