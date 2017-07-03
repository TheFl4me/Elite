package com.minecraft.plugin.elite.general.punish.ban;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.punish.PunishReason;
import com.minecraft.plugin.elite.general.punish.Punishment;

import java.util.UUID;

public class Ban extends Punishment {
	
	public Ban(String bannerName, UUID targetUuid, PunishReason banReason, String banDetails, long banDate, UUID id) {
		super(targetUuid, bannerName, banReason, banDetails, banDate, id);
	}
	
	public String getKickMessage(Language lang) {
		Server server = Server.get();
		return lang.get(GeneralLanguage.BAN_SCREEN)
				.replaceAll("%name", server.getName())
				.replaceAll("%reason", this.getReason().toString())
				.replaceAll("%duration", "permanent")
				.replaceAll("%remaining", "N/A")
				.replaceAll("%bandate", server.getDate(this.getDate()))
				.replaceAll("%id", this.getUniqueId().toString())
				.replaceAll("%currentdate", server.getDate())
				.replaceAll("%domain", server.getDomain());
	}
}
