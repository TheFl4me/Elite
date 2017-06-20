package com.minecraft.plugin.elite.general.commands.punish.mute;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.api.enums.Rank;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.general.punish.mute.MuteManager;
import com.minecraft.plugin.elite.general.punish.mute.MuteReason;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class MuteCommand extends eCommand implements TabCompleter {
	
	public MuteCommand() {
		super("mute", "eban.mute", true);
	}

	@Override
	public List<String> onTabComplete(CommandSender cs, Command cmd, String lbl, String[] args) {
		if(args.length == 2)
			return MuteReason.getAllReasons();
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public boolean execute(CommandSender cs, Command cmd, String[] args) {

		Language lang = Language.ENGLISH;
		if(cs instanceof Player)
			lang = ePlayer.get((Player) cs).getLanguage();

		if(args.length > 2) {
			OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
			boolean can = true;
			Database db = General.getDB();
			if(!db.containsValue(General.DB_PLAYERS, "uuid", target.getUniqueId().toString())) {
				cs.sendMessage(lang.get(GeneralLanguage.NEVER_JOINED));
				return true;
			}
			if(cs instanceof Player) {
				ePlayer p = ePlayer.get((Player) cs);
				if(p.getRank().ordinal() <= Rank.get(target).ordinal())
					can = false;
			} 
			if(can) {
				MuteReason reason = MuteReason.get(args[1]);
				if(reason != null) {
					StringBuilder details = new StringBuilder();
					for(int i = 2; i < args.length; i++)
						details.append(args[i]).append(" ");
					MuteManager.mute(cs.getName(), target, reason, details.toString());
					return true;
				} else {
					cs.sendMessage(lang.get(GeneralLanguage.ARG_INVALID).replaceAll("%arg", args[1]));
					return true;
				}
			} else {
				cs.sendMessage(lang.get(GeneralLanguage.MUTE_NOPERM));
				return true;
			}
		} else {
			cs.sendMessage(lang.get(GeneralLanguage.MUTE_USAGE));
			return true;
		}
	}
}