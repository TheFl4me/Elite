package com.minecraft.plugin.elite.kitpvp.commands;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import com.minecraft.plugin.elite.kitpvp.manager.KitPlayer;
import com.minecraft.plugin.elite.kitpvp.manager.kits.Kit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitInfoCommand extends GeneralCommand {

	public KitInfoCommand() {
		super("kitinfo", "ekitpvp.kitinfo", false);
	}

	public boolean execute(CommandSender cs, Command cmd, String[] args) {

		GeneralPlayer p = GeneralPlayer.get((Player)cs);
		if(p.isAdminMode()) {
			if(args.length > 0) {
				KitPlayer z = KitPlayer.get(args[0]);
				if(z != null) {
					Kit kit = z.getKit();
					if(kit != null) {
						p.getPlayer().sendMessage(p.getLanguage().get(KitPvPLanguage.KIT_INFO)
								.replaceAll("%z", z.getName())
								.replaceAll("%kit", kit.getName()));
						return true;
					} else {
						p.getPlayer().sendMessage(p.getLanguage().get(KitPvPLanguage.KIT_INFO_NONE)
								.replaceAll("%z", z.getName()));
						return true;
					}
				} else {
					p.sendMessage(GeneralLanguage.NO_TARGET);
					return true;
				}
			} else {
				p.sendMessage(KitPvPLanguage.KIT_INFO_USAGE);
				return true;
			}
		} else {
			p.sendMessage(GeneralLanguage.NOT_ADMIN_MODE);
			return true;
		}
	}
}