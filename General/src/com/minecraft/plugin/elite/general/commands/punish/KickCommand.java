package com.minecraft.plugin.elite.general.commands.punish;

import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand extends GeneralCommand {
	
	public KickCommand() {
		super("kick", GeneralPermission.PUNISH_KICK, true);
	}

	public boolean execute(CommandSender cs, Command cmd, String[] args) {

		if(args.length >= 2) {
			GeneralPlayer z = GeneralPlayer.get(args[0]);
			if(z != null) {
				boolean can = true;
				if(cs instanceof Player) {
					GeneralPlayer p = GeneralPlayer.get((Player) cs);
					if(p.getRank().ordinal() <= z.getRank().ordinal())
						can = false;
				} 
				if(can) {
					StringBuilder reason = new StringBuilder();
					for(int i = 1; i < args.length; i++)
						 reason.append(args[i]).append(" ");
					final String kick = z.getLanguage().get(com.minecraft.plugin.elite.general.GeneralLanguage.KICK_SCREEN)
							.replaceAll("%reason", reason.toString());
					z.getPlayer().kickPlayer(kick);
					for(Player all : Bukkit.getOnlinePlayers())
						all.sendMessage(GeneralPlayer.get(all).getLanguage().get(com.minecraft.plugin.elite.general.GeneralLanguage.KICK_KICKED)
								.replaceAll("%z", z.getName())
								.replaceAll("%p", cs.getName()));
					return true;
				} else {
					cs.sendMessage(this.getLanguage().get(com.minecraft.plugin.elite.general.GeneralLanguage.KICK_NO_PERMISSION));
					return true;
				}
			} else {
				cs.sendMessage(this.getLanguage().get(com.minecraft.plugin.elite.general.GeneralLanguage.NO_TARGET));
				return true;
			}
		} else {
			cs.sendMessage(this.getLanguage().get(com.minecraft.plugin.elite.general.GeneralLanguage.KICK_USAGE));
			return true;
		}
	}
}