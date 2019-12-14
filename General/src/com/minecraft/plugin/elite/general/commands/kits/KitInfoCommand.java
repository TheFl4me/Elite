package com.minecraft.plugin.elite.general.commands.kits;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.api.special.kits.Kit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitInfoCommand extends GeneralCommand {

	public KitInfoCommand() {
		super("kitinfo", GeneralPermission.KIT_INFO, false);
	}

	public boolean execute(CommandSender cs, Command cmd, String[] args) {

		GeneralPlayer p = GeneralPlayer.get((Player)cs);
		if(p.isAdminMode()) {
			if(args.length > 0) {
				GeneralPlayer z = GeneralPlayer.get(args[0]);
				if(z != null) {
					Kit kit = z.getKit();
					if(kit != null) {
						p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.KIT_INFO)
								.replaceAll("%z", z.getName())
								.replaceAll("%kit", kit.getName()));
                    } else {
						p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.KIT_INFO_NONE)
								.replaceAll("%z", z.getName()));
                    }
				} else {
					p.sendMessage(GeneralLanguage.NO_TARGET);
				}
			} else {
				p.sendMessage(GeneralLanguage.KIT_INFO_USAGE);
			}
		} else {
			p.sendMessage(GeneralLanguage.NOT_ADMIN_MODE);
		}
		return true;
	}
}