package com.minecraft.plugin.elite.general.punish.ban;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.ePlayer;
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
		ePlayer p = ePlayer.get(this.getTarget());
		return p.getLanguage().get(GeneralLanguage.BAN_SCREEN)
				.replaceAll("%name", server.getName())
				.replaceAll("%reason", this.getReason())
				.replaceAll("%duration", server.getTime(this.getTime(), p.getLanguage()))
				.replaceAll("%remaining", server.getTimeUntil(this.getExpireDate(), p.getLanguage()))
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
