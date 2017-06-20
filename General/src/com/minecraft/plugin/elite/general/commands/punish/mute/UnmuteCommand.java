package com.minecraft.plugin.elite.general.commands.punish.mute;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.punish.PunishManager;
import com.minecraft.plugin.elite.general.punish.mute.MuteManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnmuteCommand extends eCommand {
	
	public UnmuteCommand() {
		super("unmute", "eban.unmute", true);
	}

	@SuppressWarnings("deprecation")
	public boolean execute(CommandSender cs, Command cmd, String[] args) {

		Language lang = Language.ENGLISH;
		if(cs instanceof Player)
			lang = ePlayer.get((Player) cs).getLanguage();

		if(args.length == 1) {
			OfflinePlayer z = Bukkit.getOfflinePlayer(args[0]);
			if(PunishManager.isMuted(z.getUniqueId())) {
				MuteManager.unmutePlayer(cs, z);
				if(z.isOnline())
					ePlayer.get(z.getUniqueId()).sendMessage(GeneralLanguage.UNMUTE_UNMUTED_YOU);
				return true;
			} else {
				cs.sendMessage(lang.get(GeneralLanguage.UNMUTE_NOT_MUTED).replaceAll("%z", z.getName()));
				return true;
			}
		}  else {
			cs.sendMessage(lang.get(GeneralLanguage.UNMUTE_USAGE));
			return true;
		}	
	}
}