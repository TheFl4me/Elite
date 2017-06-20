package com.minecraft.plugin.elite.general.punish.mute;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.punish.Temporary;

import java.util.UUID;

public class TempMute extends Mute implements Temporary {
	
	private long time;
	private long expireDate;
	
	public TempMute(String muterName, UUID targetUuid, String muteReason, String muteDetails, long muteTime, long muteDate, UUID id) {
		super(muterName, targetUuid, muteReason, muteDetails, muteDate, id);
		this.time = muteTime;
		this.expireDate = muteTime + muteDate;
	}

	public String getMuteMessage() {
		return ePlayer.get(this.getTarget()).getLanguage().get(GeneralLanguage.MUTE_MUTED_ON_TALK);
	}

	public String getMuteDisplayMessage() {
		Server server = Server.get();
		ePlayer p = ePlayer.get(this.getTarget());
		return p.getLanguage().get(GeneralLanguage.MUTE_INFO)
				.replaceAll("%id", this.getUniqueId().toString())
				.replaceAll("%reason", this.getReason())
				.replaceAll("%duration", server.getTime(this.getTime(), p.getLanguage()))
				.replaceAll("%remaining", server.getTimeUntil(this.getExpireDate(), p.getLanguage()));
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