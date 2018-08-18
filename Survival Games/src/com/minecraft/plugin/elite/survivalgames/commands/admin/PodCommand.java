package com.minecraft.plugin.elite.survivalgames.commands.admin;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.survivalgames.SurvivalGames;
import com.minecraft.plugin.elite.survivalgames.SurvivalGamesLanguage;
import com.minecraft.plugin.elite.survivalgames.SurvivalGamesPermission;
import com.minecraft.plugin.elite.survivalgames.manager.arena.Arena;
import com.minecraft.plugin.elite.survivalgames.manager.arena.ArenaManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PodCommand extends GeneralCommand {

	public PodCommand() {
		super("pod", SurvivalGamesPermission.MAP_EDIT, false);
	}

	public boolean execute(CommandSender cs, Command cmd, String[] args) {

		GeneralPlayer p = GeneralPlayer.get((Player) cs);
		Database db = SurvivalGames.getDB();
		if(args.length > 0) {
			Arena arena = ArenaManager.get(p.getPlayer().getWorld());
			if(arena != null) {
				try {
					int index = Integer.parseInt(args[0]);
					if(index > 0 && index < 25) {
						Location loc = p.getPlayer().getLocation();
						db.update(SurvivalGames.DB_ARENAS, "pod" + Integer.toString(index) + "x", loc.getX(), "name", arena.getName());
						db.update(SurvivalGames.DB_ARENAS, "pody", loc.getY(), "name", arena.getName());
						db.update(SurvivalGames.DB_ARENAS, "pod" + Integer.toString(index) + "z", loc.getZ(), "name", arena.getName());
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