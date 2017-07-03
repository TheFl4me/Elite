package com.minecraft.plugin.elite.general.punish;

import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.api.interfaces.LanguageNode;
import org.bukkit.Bukkit;

import java.util.UUID;

public abstract class Punishment {
	
	protected UUID target;
	protected String punisher;
	protected PunishReason reason;
	protected String details;
	protected long date;
	protected UUID id;
	
	public Punishment(UUID targetUuid, String pName, PunishReason pReason, String pDetails, long pDate, UUID id) {
		this.target = targetUuid;
		this.punisher = pName;
		this.reason = pReason;
		this.details = pDetails;
		this.date = pDate;
		this.id = id;
	}
	
	public UUID getTarget() {
		return this.target;
	}
	public String getPunisher() {
		return this.punisher;
	}
	public PunishReason getReason() {
		return this.reason;
	}
	public String getDetails() {
		return this.details;
	}
	public long getDate() {
		return this.date;
	}
	public UUID getUniqueId() {
		return this.id;
	}

	public String getInfo(LanguageNode node, Language lang) {
		Server server = Server.get();
		return lang.get(node)
				.replaceAll("%id", this.getUniqueId().toString())
				.replaceAll("%target", Bukkit.getOfflinePlayer(this.getTarget()).getName())
				.replaceAll("%reason", this.getReason().toString())
				.replaceAll("%details", this.getDetails())
				.replaceAll("%duration", "permanent")
				.replaceAll("%remaining", "N/A")
				.replaceAll("%date", server.getDate(this.getDate()))
				.replaceAll("%punisher", this.getPunisher());
	}
}

