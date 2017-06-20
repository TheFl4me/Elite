package com.minecraft.plugin.elite.general.commands.punish;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.enums.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand extends eCommand {
	
	public KickCommand() {
		super("kick", "eban.kick", true);
	}

	public boolean execute(CommandSender cs, Command cmd, String[] args) {

		Language lang = Language.ENGLISH;
		if(cs instanceof Player)
			lang = ePlayer.get((Player) cs).getLanguage();

		if(args.length >= 2) {
			ePlayer z = ePlayer.get(args[0]);
			if(z != null) {
				boolean can = true;
				if(cs instanceof Player) {
					ePlayer p = ePlayer.get((Player) cs);
					if(p.getRank().ordinal() <= z.getRank().ordinal())
						can = false;
				} 
				if(can) {
					StringBuilder reason = new StringBuilder();
					for(int i = 1; i < args.length; i++)
						 reason.append(args[i]).append(" ");
					final String kick = z.getLanguage().get(GeneralLanguage.KICK_SCREEN)
							.replaceAll("%reason", reason.toString());
					z.getPlayer().kickPlayer(kick);
					for(Player all : Bukkit.getOnlinePlayers())
						ePlayer.get(all).getLanguage().get(GeneralLanguage.KICK_KICKED)
								.replaceAll("%z", z.getName())
								.replaceAll("%p", cs.getName());
					return true;
				} else {
					cs.sendMessage(lang.get(GeneralLanguage.KICK_NOPERM));
					return true;
				}
			} else {
				cs.sendMessage(lang.get(GeneralLanguage.NO_TARGET));
				return true;
			}
		} else {
			cs.sendMessage(lang.get(GeneralLanguage.KICK_USAGE));
			return true;
		}
	}
}