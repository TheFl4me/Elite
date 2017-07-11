package com.minecraft.plugin.elite.raid.commands.warp;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.api.special.clan.Clan;
import com.minecraft.plugin.elite.raid.RaidLanguage;
import com.minecraft.plugin.elite.raid.manager.Warp;
import com.minecraft.plugin.elite.raid.manager.WarpManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class WarpListCommand extends GeneralCommand {

	public WarpListCommand() {
		super("warplist", "eraid.warp", false);
	}

	public boolean execute(CommandSender cs, Command cmd, String[] args) {

		GeneralPlayer p = GeneralPlayer.get((Player)cs);
		if(args.length == 0) {
			Collection<String> list = new ArrayList<>();
			for(Warp warp : WarpManager.getAvailableWarps(p))
				list.add(warp.getName());
			String warps = ChatColor.GOLD + General.SPACER + "\n" +
					"" + ChatColor.GOLD + "Warp list:\n" +
					"" + ChatColor.GRAY + list.toString() + "\n" +
					"" + ChatColor.GOLD + General.SPACER;
			p.getPlayer().sendMessage(warps);
			return true;
		} else {
			if(args[0].equalsIgnoreCase("clan")) {
				Clan clan = p.getClan();
				if(clan != null) {
					Collection<String> list = new ArrayList<>();
					for(Warp warp : WarpManager.getClanWarps(clan))
						list.add(warp.getName());
					String warps = ChatColor.GOLD + General.SPACER + "\n" +
							"" + ChatColor.GOLD + "Clan Warps:\n" +
							"" + ChatColor.GRAY + list.toString() + "\n" +
							"" + ChatColor.GOLD + General.SPACER;
					p.getPlayer().sendMessage(warps);
					return true;
				} else {
					p.sendMessage(GeneralLanguage.CLAN_NONE);
					return true;
				}
			} else if(args[0].equalsIgnoreCase("private")) {
				Collection<String> list = new ArrayList<>();
				for(Warp warp : WarpManager.getPlayerWarps(p))
					list.add(warp.getName());
				String warps = ChatColor.GOLD + General.SPACER + "\n" +
						"" + ChatColor.GOLD + "Clan Warps:\n" +
						"" + ChatColor.GRAY + list.toString() + "\n" +
						"" + ChatColor.GOLD + General.SPACER;
				p.getPlayer().sendMessage(warps);
				return true;
			} else {
				p.sendMessage(RaidLanguage.WARP_LIST_USAGE);
				return true;
			}
		}
	}
}