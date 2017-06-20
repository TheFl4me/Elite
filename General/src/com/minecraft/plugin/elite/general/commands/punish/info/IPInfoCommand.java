package com.minecraft.plugin.elite.general.commands.punish.info;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IPInfoCommand extends eCommand {
	
	public IPInfoCommand() {
		super("ipinfo", "eban.ipinfo", false);
	}
	
	public boolean execute(CommandSender cs, Command cmd, String[] args) {
		
		ChatColor mainColor =  ChatColor.GRAY;
		ChatColor secondColor = ChatColor.GOLD;
		ePlayer p = ePlayer.get((Player) cs);
		if(args.length > 0) {
			String info = mainColor + General.SPACER + "\n" +
					"" + mainColor + "IP: " + secondColor + args[0] + "\n" +
					"" + mainColor + "Location: " + secondColor + "https://geoiptool.com/en/?ip=" + args[0] + "\n" +
					"" + mainColor + General.SPACER;
			p.getPlayer().sendMessage(info);
			return true;
		} else {
			p.sendMessage(GeneralLanguage.IP_USAGE);
			return true;
		}
	}	
}
