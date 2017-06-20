package com.minecraft.plugin.elite.general.punish.mute;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.punish.Punishment;

import java.util.UUID;

public class Mute extends Punishment {
	
	public Mute(String muterName, UUID targetUuid, String muteReason, String muteDetails, long muteDate, UUID id) {		
		super(targetUuid, muterName, muteReason, muteDetails, muteDate, id);
	}
	
	public String getMuteMessage() {
		return ePlayer.get(this.getTarget()).getLanguage().get(GeneralLanguage.MUTE_MUTED_ON_TALK);
	}

	public String getMuteDisplayMessage() {
		Language lang;
		ePlayer target = ePlayer.get(this.getTarget());
		if(target != null)
			lang = target.getLanguage();
		else
			lang = Language.ENGLISH;
		return lang.get(GeneralLanguage.MUTE_INFO)
				.replaceAll("%id", this.getUniqueId().toString())
				.replaceAll("%reason", this.getReason())
				.replaceAll("%duration", "permanent")
				.replaceAll("%remaining", "N/A");
	}
}