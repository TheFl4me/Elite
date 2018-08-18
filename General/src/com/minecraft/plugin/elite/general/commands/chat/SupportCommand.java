package com.minecraft.plugin.elite.general.commands.chat;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.api.special.supportchat.SupportChat;
import com.minecraft.plugin.elite.general.api.special.supportchat.SupportChatManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SupportCommand extends GeneralCommand implements TabCompleter {

	public SupportCommand() {
		super("support", GeneralPermission.CHAT_SUPPORT, false);
	}

	@Override
	public List<String> onTabComplete(CommandSender cs, Command cmd, String lbl, String[] args) {
	
		if(args.length == 1)
			return Arrays.asList("end", "add");
		if(args.length == 2 && args[0].equalsIgnoreCase("add"))
			return SupportChatManager.getRequests().stream().map(GeneralPlayer::getName).collect(Collectors.toList());
		return null;
	}
	
	public boolean execute(CommandSender cs, Command cmd, String[] args) {
		
		final GeneralPlayer p = GeneralPlayer.get((Player) cs);
		if(args.length == 0) {
			if(!SupportChatManager.hasSentRequest(p)) {
				if(SupportChatManager.get(p) == null) {
						SupportChatManager.sendRequest(p);
						return true;
				} else {
					p.sendMessage(GeneralLanguage.SUPPORT_ALREADY);
					return true;
				}
			} else {
				p.sendMessage(GeneralLanguage.SUPPORT_COOLDOWN);
				return true;
			}
		} else if(args[0].equalsIgnoreCase("end") && args.length > 0) {
			SupportChat chat = SupportChatManager.get(p);
			if(chat != null) {
				chat.end();
				return true;
			} else {
				p.sendMessage(GeneralLanguage.SUPPORT_CHAT_NULL);
				return true;
			}
		} else if(p.hasPermission(GeneralPermission.CHAT_SUPPORT_EXTRA) && args[0].equalsIgnoreCase("add") && args.length > 1) {
			GeneralPlayer z = GeneralPlayer.get(args[1]);
			if(z != null) {
				if(SupportChatManager.hasSentRequest(z)) {
					if(!p.getUniqueId().equals(z.getUniqueId())) {
						if(SupportChatManager.get(z) == null) {
							SupportChat chat = new SupportChat(p, z);
							chat.start();
							return true;
						} else {
							p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.SUPPORT_ALREADY).replaceAll("%z", z.getName()));
							return true;
						}
					} else {
						p.sendMessage(GeneralLanguage.SUPPORT_SELF);
						return true;
					}
				} else {
					p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.SUPPORT_NO_REQUEST).replaceAll("%z", z.getName()));
					return true;
				}
			} else {
				p.sendMessage(GeneralLanguage.NO_TARGET);
				return true;
			}
		} else {
			p.sendMessage(GeneralLanguage.SUPPORT_USAGE);
			return true;
		}
	}
}