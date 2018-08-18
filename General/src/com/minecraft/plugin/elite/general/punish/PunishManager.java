package com.minecraft.plugin.elite.general.punish;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.api.enums.Unit;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.general.punish.ban.Ban;
import com.minecraft.plugin.elite.general.punish.ban.BanManager;
import com.minecraft.plugin.elite.general.punish.mute.Mute;
import com.minecraft.plugin.elite.general.punish.mute.MuteManager;
import org.bukkit.OfflinePlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PunishManager {
	
	public static boolean isMuted(UUID target) {
		Mute mute = MuteManager.getMute(target);
		return mute != null;
	}
	
	public static boolean isBanned(UUID uuid) {
		Ban ban = BanManager.getBan(uuid);
		return ban != null;
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

	public static long computeTime(double x, double modifier, Unit unit) {

		final double temp_z = -3D - modifier;
		final double temp_a = 0.5D + modifier;
		final double temp_c = 0D - modifier;
		final double temp_d = modifier - 0.5D;

		final double z = (temp_z < -10D ? -10D : temp_z);
		final double a = (temp_a > 10D ? 10D : temp_a);
		final double b = 0.9D;
		final double c = (temp_c < -10D ? -10D : temp_c);
		final double d = (temp_d > 10D ? 10D : temp_d);

		final double y = a * Math.pow(b, z * x + c) + d;

		return Math.round(y * unit.toMS());
	}

	public static void punish(String punisherName, OfflinePlayer target, PunishReason reason, String punishDetails) {
		UUID uuid = UUID.randomUUID();
		boolean exists = true;
		Database db = General.getDB();
		while(exists)
			if(!db.containsValue(General.DB_BANS, "id", uuid.toString()) && !db.containsValue(General.DB_BAN_HISTORY, "id", uuid.toString())&& !db.containsValue(General.DB_MUTES, "id", uuid.toString())&& !db.containsValue(General.DB_MUTE_HISTORY, "id", uuid.toString()))
				exists = false;
			else
				uuid = UUID.randomUUID();

		switch(reason.getType()) {
			case BAN:
				BanManager.ban(uuid, punisherName, target, reason, punishDetails);
				break;
			case MUTE:
				MuteManager.mute(uuid, punisherName, target, reason, punishDetails);
				break;
		}
	}
}
