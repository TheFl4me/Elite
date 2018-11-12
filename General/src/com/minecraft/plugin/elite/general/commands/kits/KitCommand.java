package com.minecraft.plugin.elite.general.commands.kits;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.api.special.kits.Kit;
import com.minecraft.plugin.elite.general.api.special.kits.KitGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KitCommand extends GeneralCommand implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender cs, Command cmd, String s, String[] args) {
		List<String> list = new ArrayList<>();
		GeneralPlayer p = GeneralPlayer.get((Player) cs);
		for(Kit kit : Kit.values()) {
			if(p.hasKitPermission(kit)) {
				if((kit.getPermissionType(p.getUniqueId()) >= 1 && p.getLevel() >= kit.getLevel()) || General.getFreeKits().contains(kit) || p.isMasterPrestige()) {
					list.add(kit.getName());
				}
			}
		}
		return list;
	}
	
	public KitCommand() {
		super("kit", GeneralPermission.KIT, false);
	}

	public boolean execute(CommandSender cs, Command cmd, String[] args) {

		GeneralPlayer p = GeneralPlayer.get((Player)cs);
		if(p.isInvis() || p.isAdminMode()) {
			p.sendMessage(GeneralLanguage.KIT_ERROR_MODE);
			return true;
		}
		if(p.hasKit() && p.canUseKit()) {
			p.sendMessage(GeneralLanguage.KIT_ERROR_ALREADY);
			return true;
		}
		if(args.length == 0) {
			KitGUI gui = new KitGUI(p.getLanguage());
			p.openGUI(gui, gui.selector(p, 1));
			return true;
		} else  {
			for(Kit kit : Kit.values()) {
				if(args[0].equalsIgnoreCase(kit.getName())) {
					if(p.hasKitPermission(kit)) {
						if(kit.getPermissionType(p.getUniqueId()) == 1 && p.getLevel() < kit.getLevel()) {
							p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.KIT_ERROR_LOCKED)
									.replaceAll("%level", Integer.toString(kit.getLevel())));
						} else {
							p.giveKit(kit);
						}
					} else {
						p.sendMessage(GeneralLanguage.KIT_NO_PERMISSION);
					}
					return true;
				}
			}
			p.sendMessage(GeneralLanguage.KIT_ERROR_NULL);
			return true;
		}
	}
}