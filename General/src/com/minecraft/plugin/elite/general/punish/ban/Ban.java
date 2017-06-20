package com.minecraft.plugin.elite.general.punish.ban;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.punish.Punishment;

import java.util.UUID;

public class Ban extends Punishment {
	
	public Ban(String bannerName, UUID targetUuid, String banReason, String banDetails, long banDate, UUID id) {
		super(targetUuid, bannerName, banReason, banDetails, banDate, id);
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
				.replaceAll("%duration", "permanent")
				.replaceAll("%remaining", "N/A")
				.replaceAll("%bandate", server.getDate(this.getDate()))
				.replaceAll("%id", this.getUniqueId().toString())
				.replaceAll("%currentdate", server.getDate())
				.replaceAll("%domain", server.getDomain());
	}
}
