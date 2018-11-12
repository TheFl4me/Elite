package com.minecraft.plugin.elite.raid.commands.warp.clan;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.api.special.clan.Clan;
import com.minecraft.plugin.elite.raid.RaidLanguage;
import com.minecraft.plugin.elite.raid.RaidPermission;
import com.minecraft.plugin.elite.raid.manager.Warp;
import com.minecraft.plugin.elite.raid.manager.WarpManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetClanWarpCommand extends GeneralCommand {

	public SetClanWarpCommand() {
		super("setclanwarp", RaidPermission.WARP, false);
	}

	public boolean execute(CommandSender cs, Command cmd, String[] args) {

		GeneralPlayer p = GeneralPlayer.get((Player)cs);
		if(args.length > 0) {
			Clan clan = p.getClan();
			if(clan != null) {
				Warp warp = WarpManager.get(args[0], p);
				if(warp == null) {
					if(WarpManager.getClanWarps(clan).length < 4) {
						Location loc = p.getPlayer().getLocation();
						Warp warp_new = new Warp(args[0], loc, p.getClan().getName(), Warp.WarpType.CLAN);
						warp_new.saveToDB();
						p.getPlayer().sendMessage(p.getLanguage().get(RaidLanguage.WARP_CREATED)
								.replaceAll("%warp", args[0]));
						return true;
					} else {
						p.sendMessage(RaidLanguage.WARP_CLAN_MAX);
						return true;
					}
				} else {
					p.sendMessage(RaidLanguage.WARP_ALREADY_EXIST);
					return true;
				}
			} else {
				p.sendMessage(GeneralLanguage.CLAN_NONE);
				return true;
			}
		} else {
			p.sendMessage(RaidLanguage.WARP_CLAN_CREATE_USAGE);
			return true;
		}
	}
}