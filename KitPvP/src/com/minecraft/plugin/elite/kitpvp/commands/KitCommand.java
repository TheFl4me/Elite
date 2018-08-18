package com.minecraft.plugin.elite.kitpvp.commands;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.kitpvp.KitPvP;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import com.minecraft.plugin.elite.kitpvp.KitPvPPermission;
import com.minecraft.plugin.elite.kitpvp.manager.KitPlayer;
import com.minecraft.plugin.elite.kitpvp.manager.kits.Kit;
import com.minecraft.plugin.elite.kitpvp.manager.kits.KitGUI;
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
		KitPlayer kp = KitPlayer.get((Player) cs);
		for(Kit kit : Kit.values()) {
			if(kp.hasKitPermission(kit)) {
				if((kit.getPermissionType(kp.getUniqueId()) >= 1 && kp.getLevel() >= kit.getLevel()) || KitPvP.getFreeKits().contains(kit) || kp.isMasterPrestige()) {
					list.add(kit.getName());
				}
			}
		}
		return list;
	}
	
	public KitCommand() {
		super("kit", KitPvPPermission.KIT, false);
	}

	public boolean execute(CommandSender cs, Command cmd, String[] args) {

		GeneralPlayer p = GeneralPlayer.get((Player)cs);
		if(p.isInvis() || p.isAdminMode()) {
			p.sendMessage(KitPvPLanguage.KIT_ERROR_MODE);
			return true;
		}
		KitPlayer kp = KitPlayer.get(p.getUniqueId());
		if(kp.hasKit() && !kp.isInRegion(KitPvP.REGION_SPAWN)) {
			p.sendMessage(KitPvPLanguage.KIT_ERROR_ALREADY);
			return true;
		}
		if(args.length == 0) {
			KitGUI gui = new KitGUI(p.getLanguage());
			p.openGUI(gui, gui.selector(kp, 1));
			return true;
		} else  {
			for(Kit kit : Kit.values()) {
				if(args[0].equalsIgnoreCase(kit.getName())) {
					if(kp.hasKitPermission(kit)) {
						if(kit.getPermissionType(p.getUniqueId()) == 1 && p.getLevel() < kit.getLevel()) {
							p.getPlayer().sendMessage(p.getLanguage().get(KitPvPLanguage.KIT_ERROR_LOCKED)
									.replaceAll("%level", Integer.toString(kit.getLevel())));
						} else {
							kp.giveKit(kit);
						}
					} else {
						p.sendMessage(KitPvPLanguage.KIT_NO_PERMISSION);
					}
					return true;
		    	}
			}
			p.sendMessage(KitPvPLanguage.KIT_ERROR_NULL);
			return true;
		}
	}
}