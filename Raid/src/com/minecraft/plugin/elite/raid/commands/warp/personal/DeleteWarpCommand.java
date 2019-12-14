package com.minecraft.plugin.elite.raid.commands.warp.personal;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.raid.RaidLanguage;
import com.minecraft.plugin.elite.raid.RaidPermission;
import com.minecraft.plugin.elite.raid.manager.Warp;
import com.minecraft.plugin.elite.raid.manager.WarpManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteWarpCommand extends GeneralCommand {
	
	public DeleteWarpCommand() {
		super("deletewarp", RaidPermission.WARP, false);
	}

	public boolean execute(CommandSender cs, Command cmd, String[] args) {

		GeneralPlayer p = GeneralPlayer.get((Player)cs);
		if(args.length > 0) {
			Warp warp = WarpManager.get(args[0], p);
			if(warp != null) {
				p.getPlayer().sendMessage(p.getLanguage().get(RaidLanguage.WARP_DELETED)
						.replaceAll("%warp", warp.getName()));
				warp.delete();
            } else {
				p.sendMessage(RaidLanguage.WARP_NULL);
            }
		} else {
			p.sendMessage(RaidLanguage.WARP_DELETE_USAGE);
		}
		return true;
	}
}