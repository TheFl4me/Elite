package com.minecraft.plugin.elite.raid.commands.warp.personal;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.raid.RaidLanguage;
import com.minecraft.plugin.elite.raid.RaidPermission;
import com.minecraft.plugin.elite.raid.manager.Warp;
import com.minecraft.plugin.elite.raid.manager.WarpManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCommand extends GeneralCommand {

	public SetWarpCommand() {
		super("setwarp", RaidPermission.WARP, false);
	}

	public boolean execute(CommandSender cs, Command cmd, String[] args) {

		GeneralPlayer p = GeneralPlayer.get((Player)cs);
		if(args.length > 0) {
			Warp warp = WarpManager.get(args[0], p);
			if(warp == null) {
				if(WarpManager.getPlayerWarps(p).length < WarpManager.getMaxWarps(p)) {
					Location loc = p.getPlayer().getLocation();
					Warp warp_new = new Warp(args[0], loc, p.getUniqueId().toString(), Warp.WarpType.PLAYER);
					warp_new.saveToDB();
					p.getPlayer().sendMessage(p.getLanguage().get(RaidLanguage.WARP_CREATED)
							.replaceAll("%warp", args[0]));
					return true;
				} else {
					Server server = Server.get();
					p.getPlayer().sendMessage(p.getLanguage().get(RaidLanguage.WARP_PLAYER_MAX).replaceAll("%domain", server.getDomain()));
					return true;
				}
			} else {
				p.sendMessage(RaidLanguage.WARP_ALREADY_EXIST);
				return true;
			}
		} else {
			p.sendMessage(RaidLanguage.WARP_CREATE_USAGE);
			return true;
		}
	}
}