package com.minecraft.plugin.elite.general.punish.ban;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.punish.Temporary;

import java.util.UUID;

public class TempBan extends Ban implements Temporary {
	
	private long time;
	private long expireDate;
	
	
	public TempBan(String bannerName, UUID targetUuid, String banReason, String banDetails, long banTime, long banDate, UUID id) {
		super(bannerName, targetUuid, banReason, banDetails, banDate, id);
		this.time = banTime;
		this.expireDate = banTime + banDate;
	}
	
	public String getKickMessage() {
		Server server = Server.get();
		Language lang;
		ePlayer target = ePlayer.get(this.getTarget());
		if(target != null)
			lang = target.getLanguage();
		else
			lang = Language.ENGLISH;
		return lang.get(GeneralLanguage.BAN_SCREEN)
				.replaceAll("%name", server.getName())
				.replaceAll("%reason", this.getReason())
				.replaceAll("%duration", server.getTime(this.getTime(), lang))
				.replaceAll("%remaining", server.getTimeUntil(this.getExpireDate(), lang))
				.replaceAll("%bandate", server.getDate(this.getDate()))
				.replaceAll("%id", this.getUniqueId().toString())
				.replaceAll("%currentdate", server.getDate())
				.replaceAll("%domain", server.getDomain());
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
