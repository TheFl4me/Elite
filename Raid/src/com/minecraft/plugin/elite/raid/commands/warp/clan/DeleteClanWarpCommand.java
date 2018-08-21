package com.minecraft.plugin.elite.raid.commands.warp.clan;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.api.special.clan.Clan;
import com.minecraft.plugin.elite.raid.RaidLanguage;
import com.minecraft.plugin.elite.raid.RaidPermission;
import com.minecraft.plugin.elite.raid.manager.Warp;
import com.minecraft.plugin.elite.raid.manager.WarpManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteClanWarpCommand extends GeneralCommand {
	
	public DeleteClanWarpCommand() {
		super("deleteclanwarp", RaidPermission.WARP, false);
	}

	public boolean execute(CommandSender cs, Command cmd, String[] args) {

		GeneralPlayer p = GeneralPlayer.get((Player)cs);
		if(args.length > 0) {
			Clan clan = p.getClan();
			if(clan != null) {
				Warp warp = WarpManager.getClanWarp(args[0], clan);
				if(warp != null) {
					if(clan.isCreator(p.getUniqueId())) {
						p.getPlayer().sendMessage(p.getLanguage().get(RaidLanguage.WARP_DELETED)
								.replaceAll("%warp", warp.getName()));
						warp.delete();
						return true;
					} else {
						p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.CLAN_RANK_ERROR);
						return true;
					}
				} else {
					p.sendMessage(RaidLanguage.WARP_NULL);
					return true;
				}
			} else {
				p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.CLAN_NONE);
				return true;
			}
		} else {
			p.sendMessage(RaidLanguage.WARP_CLAN_DELETE_USAGE);
			return true;
		}
	}
}