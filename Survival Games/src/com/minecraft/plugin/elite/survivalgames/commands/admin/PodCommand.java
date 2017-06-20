package com.minecraft.plugin.elite.survivalgames.commands.admin;

import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.survivalgames.SurvivalGames;
import com.minecraft.plugin.elite.survivalgames.SurvivalGamesLanguage;
import com.minecraft.plugin.elite.survivalgames.manager.arena.Arena;
import com.minecraft.plugin.elite.survivalgames.manager.arena.ArenaManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PodCommand extends eCommand {

	public PodCommand() {
		super("pod", "esurvivalgames.pod", false);
	}

	public boolean execute(CommandSender cs, Command cmd, String[] args) {
		
		ePlayer p = ePlayer.get((Player) cs);
		Database db = SurvivalGames.getDB();
		if(args.length > 0) {
			Arena arena = ArenaManager.get(p.getPlayer().getWorld());
			if(arena != null) {
				try {
					int index = Integer.parseInt(args[0]);
					if(index > 0 && index < 25) {
						db.update(SurvivalGames.DB_ARENAS, "pod" + Integer.toString(index) + "x", p.getPlayer().getLocation().getX(), "name", arena.getName());
						db.update(SurvivalGames.DB_ARENAS, "pody", p.getPlayer().getLocation().getY(), "name", arena.getName());
						db.update(SurvivalGames.DB_ARENAS, "pod" + Integer.toString(index) + "z", p.getPlayer().getLocation().getZ(), "name", arena.getName());
						p.getPlayer().sendMessage(p.getLanguage().get(SurvivalGamesLanguage.POD_SET)
								.replaceAll("%pod", args[0]));
						return true;
					} else {
						p.sendMessage(SurvivalGamesLanguage.POD_USAGE);
						return true;
					}
				} catch (IllegalArgumentException e) {
					p.sendMessage(SurvivalGamesLanguage.POD_USAGE);
					return true;
				}
			} else {
				p.sendMessage(SurvivalGamesLanguage.ARENA_NULL);
				return true;
			}
		} else {
			p.sendMessage(SurvivalGamesLanguage.POD_USAGE);
			return true;
		}
	}
}