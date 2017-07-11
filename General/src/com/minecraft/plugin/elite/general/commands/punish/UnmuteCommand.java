package com.minecraft.plugin.elite.general.commands.punish;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.punish.PunishManager;
import com.minecraft.plugin.elite.general.punish.mute.MuteManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class UnmuteCommand extends GeneralCommand {
	
	public UnmuteCommand() {
		super("unmute", "egeneral.unmute", true);
	}

	@SuppressWarnings("deprecation")
	public boolean execute(CommandSender cs, Command cmd, String[] args) {

		if(args.length == 1) {
			OfflinePlayer z = Bukkit.getOfflinePlayer(args[0]);
			if(PunishManager.isMuted(z.getUniqueId())) {
				MuteManager.unmutePlayer(cs.getName(), z);
				if(z.isOnline())
					GeneralPlayer.get(z.getUniqueId()).sendMessage(GeneralLanguage.UNMUTE_UNMUTED_YOU);
				return true;
			} else {
				cs.sendMessage(this.getLanguage().get(GeneralLanguage.UNMUTE_NOT_MUTED).replaceAll("%z", z.getName()));
				return true;
			}
		}  else {
			cs.sendMessage(this.getLanguage().get(GeneralLanguage.UNMUTE_USAGE));
			return true;
		}	
	}
}