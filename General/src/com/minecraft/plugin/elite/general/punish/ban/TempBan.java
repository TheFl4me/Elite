package com.minecraft.plugin.elite.general.punish.ban;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.api.interfaces.LanguageNode;
import com.minecraft.plugin.elite.general.punish.PunishReason;
import com.minecraft.plugin.elite.general.punish.Temporary;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class TempBan extends Ban implements Temporary {
	
	private long time;
	private long expireDate;
	
	
	public TempBan(String bannerName, OfflinePlayer target, PunishReason banReason, String banDetails, long banTime, long banDate, UUID id) {
		super(bannerName, target, banReason, banDetails, banDate, id);
		this.time = banTime;
		this.expireDate = banTime + banDate;
	}
	
	public String getKickMessage(Language lang) {
		Server server = Server.get();
		return lang.get(GeneralLanguage.BAN_SCREEN)
				.replaceAll("%name", server.getName())
				.replaceAll("%reason", this.getReason().toString())
				.replaceAll("%duration", server.getTime(this.getTime(), lang))
				.replaceAll("%remaining", server.getTimeUntil(this.getExpireDate(), lang))
				.replaceAll("%bandate", server.getDate(this.getDate()))
				.replaceAll("%id", this.getUniqueId().toString())
				.replaceAll("%currentdate", server.getDate())
				.replaceAll("%domain", server.getDomain());
	}

	public String getInfo(LanguageNode node, Language lang) {
		Server server = Server.get();
		return lang.get(node)
				.replaceAll("%id", this.getUniqueId().toString())
				.replaceAll("%target", this.getTarget().getName())
				.replaceAll("%reason", this.getReason().toString())
				.replaceAll("%details", this.getDetails())
				.replaceAll("%duration", server.getTime(this.getTime(), lang))
				.replaceAll("%remaining", server.getTimeUntil(this.getExpireDate(), lang))
				.replaceAll("%date", server.getDate(this.getDate()))
				.replaceAll("%punisher", this.getPunisher());
	}
	
	public long getTime() {
		return this.time;
	}
	
	public long getExpireDate() {
		return this.expireDate;
	}

	public boolean hasExpired() {
		return System.currentTimeMillis() > getExpireDate();
	}
}
