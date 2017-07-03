package com.minecraft.plugin.elite.general.punish.mute;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.api.interfaces.LanguageNode;
import com.minecraft.plugin.elite.general.punish.PunishReason;
import com.minecraft.plugin.elite.general.punish.Temporary;
import org.bukkit.Bukkit;

import java.util.UUID;

public class TempMute extends Mute implements Temporary {
	
	private long time;
	private long expireDate;
	
	public TempMute(String muterName, UUID targetUuid, PunishReason muteReason, String muteDetails, long muteTime, long muteDate, UUID id) {
		super(muterName, targetUuid, muteReason, muteDetails, muteDate, id);
		this.time = muteTime;
		this.expireDate = muteTime + muteDate;
	}

	public String getMuteMessage() {
		return ePlayer.get(this.getTarget()).getLanguage().get(GeneralLanguage.MUTE_MUTED_ON_TALK);
	}

	public String getMuteDisplayMessage(Language lang) {
		Server server = Server.get();
		return lang.get(GeneralLanguage.MUTE_DISPLAY)
				.replaceAll("%id", this.getUniqueId().toString())
				.replaceAll("%reason", this.getReason().toString())
				.replaceAll("%duration", server.getTime(this.getTime(), lang))
				.replaceAll("%remaining", server.getTimeUntil(this.getExpireDate(), lang));
	}

	public String getInfo(LanguageNode node, Language lang) {
		Server server = Server.get();
		return lang.get(node)
				.replaceAll("%id", this.getUniqueId().toString())
				.replaceAll("%target", Bukkit.getOfflinePlayer(this.getTarget()).getName())
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