package com.minecraft.plugin.elite.general.punish.mute;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.punish.PunishReason;
import com.minecraft.plugin.elite.general.punish.Punishment;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class Mute extends Punishment {
	
	public Mute(String muterName, OfflinePlayer target, PunishReason muteReason, String muteDetails, long muteDate, UUID id) {
		super(target, muterName, muteReason, muteDetails, muteDate, id);
	}
	
	public String getMuteMessage(Language lang) {
		return lang.get(GeneralLanguage.MUTE_MUTED_ON_TALK);
	}

	public String getMuteDisplayMessage(Language lang) {
		return lang.get(GeneralLanguage.MUTE_DISPLAY)
				.replaceAll("%id", this.getUniqueId().toString())
				.replaceAll("%reason", this.getReason().toString())
				.replaceAll("%duration", "permanent")
				.replaceAll("%remaining", "N/A");
	}
}