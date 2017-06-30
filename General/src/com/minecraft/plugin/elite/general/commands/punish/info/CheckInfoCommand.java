package com.minecraft.plugin.elite.general.commands.punish.info;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.general.punish.info.InfoGUI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class CheckInfoCommand extends eCommand {
	
	public CheckInfoCommand() {
		super("checkinfo", "egeneral.checkinfo", false);
	}
	
	@SuppressWarnings("deprecation")
	public boolean execute(CommandSender cs, Command cmd, String[] args) {

		ePlayer p = ePlayer.get((Player) cs);
		if(args.length == 1) {
			p.sendMessage(GeneralLanguage.DB_CHECK);
			Database db = General.getDB();
			OfflinePlayer z =  Bukkit.getOfflinePlayer(args[0]);
			if(z != null && db.containsValue(General.DB_PLAYERS, "uuid", z.getUniqueId().toString())) {
				try {
					InfoGUI gui = new InfoGUI(p.getLanguage(), z.getUniqueId());
					p.openGUI(gui, gui.info());
					return true;
				} catch(SQLException e) {
					p.sendMessage(GeneralLanguage.DB_CHECK_FAIL);
					e.printStackTrace();
					return true;
				}
			} else {
				p.sendMessage(GeneralLanguage.NEVER_JOINED);
				return true;
			}
		} else {
			p.sendMessage(GeneralLanguage.INFO_USAGE);
			return true;
		}
	}
}