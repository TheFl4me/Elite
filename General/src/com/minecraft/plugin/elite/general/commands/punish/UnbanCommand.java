package com.minecraft.plugin.elite.general.commands.punish;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.punish.PunishManager;
import com.minecraft.plugin.elite.general.punish.ban.BanManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnbanCommand extends eCommand {

	public UnbanCommand() {
		super("unban", "egeneral.unban", true);
	}

	@SuppressWarnings("deprecation")
	public boolean execute(CommandSender cs, Command cmd, String[] args) {

		Language lang = Language.ENGLISH;
		if(cs instanceof Player)
			lang = ePlayer.get((Player) cs).getLanguage();

		if(args.length == 1) {
			OfflinePlayer z = Bukkit.getOfflinePlayer(args[0]);
			if(PunishManager.isBanned(z.getUniqueId())) {
				BanManager.unbanPlayer(cs, z);
				return true;
			} else {
				cs.sendMessage(lang.get(GeneralLanguage.UNBAN_NOT_BANNED).replaceAll("%z", z.getName()));
				return true;
			}
		} else {
			cs.sendMessage(lang.get(GeneralLanguage.UNBAN_USAGE));
			return true;
		}
	}
}