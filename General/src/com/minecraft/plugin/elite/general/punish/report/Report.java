package com.minecraft.plugin.elite.general.punish.report;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.api.interfaces.LanguageNode;
import com.minecraft.plugin.elite.general.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class Report {
	
	private OfflinePlayer hacker;
	private OfflinePlayer reporter;
	private LanguageNode reason;
	private long date;
	
	public Report(UUID hacker, UUID reporter, LanguageNode report, long date) {
		this.hacker = Bukkit.getOfflinePlayer(hacker);
		this.reporter = Bukkit.getOfflinePlayer(reporter);
		this.reason = report;
		this.date = date;
		ReportManager.add(this);
	}
	
	public void saveToDB() {
		Database db = General.getDB();
		db.execute("INSERT INTO " + General.DB_REPORTS + " (target, reporter, reason, date) VALUES (?, ?, ?, ?);", this.getTarget().getUniqueId(), this.getReporter().getUniqueId(), this.getReason().toString(), System.currentTimeMillis());
	}
	
	public void remove() {
		Database db = General.getDB();
		db.execute("DELETE FROM " + General.DB_REPORTS + " WHERE target = ? AND date = ?;", this.getTarget().getUniqueId().toString(), this.getDate());
		ReportManager.remove(this);
	}
	
	public LanguageNode getReason() {
		return this.reason;
	}
	
	public OfflinePlayer getReporter() {
		return this.reporter;
	}
	
	public OfflinePlayer getTarget() {
		return this.hacker;
	}
	
	public long getDate() {
		return this.date;
	}
}
