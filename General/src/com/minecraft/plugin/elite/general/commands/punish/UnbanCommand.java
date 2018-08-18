package com.minecraft.plugin.elite.general.commands.punish;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.punish.PunishManager;
import com.minecraft.plugin.elite.general.punish.ban.BanManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class UnbanCommand extends GeneralCommand {

	public UnbanCommand() {
		super("unban", GeneralPermission.PUNISH_UNBAN, true);
	}

	@SuppressWarnings("deprecation")
	public boolean execute(CommandSender cs, Command cmd, String[] args) {

		if(args.length == 1) {
			OfflinePlayer z = Bukkit.getOfflinePlayer(args[0]);
			if(PunishManager.isBanned(z.getUniqueId())) {
				BanManager.unbanPlayer(cs.getName(), z);
				return true;
			} else {
				cs.sendMessage(this.getLanguage().get(GeneralLanguage.UNBAN_NOT_BANNED).replaceAll("%z", z.getName()));
				return true;
			}
		} else {
			cs.sendMessage(this.getLanguage().get(GeneralLanguage.UNBAN_USAGE));
			return true;
		}
	}
}