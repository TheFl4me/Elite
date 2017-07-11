package com.minecraft.plugin.elite.raid.commands.warp;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.raid.Raid;
import com.minecraft.plugin.elite.raid.RaidLanguage;
import com.minecraft.plugin.elite.raid.manager.Warp;
import com.minecraft.plugin.elite.raid.manager.WarpManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand extends GeneralCommand {
	
	public WarpCommand() {
		super("warp", "eraid.warp", false);
	}

	public boolean execute(CommandSender cs, Command cmd, String[] args) {

		GeneralPlayer p = GeneralPlayer.get((Player)cs);
		if(args.length > 0) {
			if(WarpManager.getPlayerWarps(p).length > WarpManager.getMaxWarps(p)) {
				p.getPlayer().sendMessage(p.getLanguage().get(RaidLanguage.WARP_ERROR_TOO_MANY)
						.replaceAll("%amount", Integer.toString(WarpManager.getPlayerWarps(p).length - WarpManager.getMaxWarps(p))));
				return true;
			}
			if(p.isInRegion(Raid.SPAWN)) {
				p.sendMessage(RaidLanguage.WARP_ERROR_SPAWN);
				return true;
			}
			Warp warp = WarpManager.get(args[0], p);
			if(warp == null) {
				p.sendMessage(RaidLanguage.WARP_NULL);
				return true;
			} else {
				p.getPlayer().sendMessage(p.getLanguage().get(RaidLanguage.WARP_ANNOUNCE).replaceAll("%warp", warp.getName()));
				p.sendMessage(RaidLanguage.WARP_NOT_MOVE);
				warp.teleport(p);
				return true;
			}
		} else {
			p.sendMessage(RaidLanguage.WARP_USAGE);
			return true;
		}
	}
}