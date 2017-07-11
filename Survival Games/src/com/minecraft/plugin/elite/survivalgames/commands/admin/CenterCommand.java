package com.minecraft.plugin.elite.survivalgames.commands.admin;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.survivalgames.SurvivalGames;
import com.minecraft.plugin.elite.survivalgames.SurvivalGamesLanguage;
import com.minecraft.plugin.elite.survivalgames.manager.arena.Arena;
import com.minecraft.plugin.elite.survivalgames.manager.arena.ArenaManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CenterCommand extends GeneralCommand {
	
	public CenterCommand() {
		super("center", "esurvivalgames.center", false);
	}

	public boolean execute(CommandSender cs, Command cmd, String[] args) {

		GeneralPlayer p = GeneralPlayer.get((Player) cs);
		Database db = SurvivalGames.getDB();
		Arena arena = ArenaManager.get(p.getPlayer().getWorld());
		Location loc = p.getPlayer().getLocation();
		if(arena != null) {
			db.update(SurvivalGames.DB_ARENAS, "centerx", loc.getX(), "name", arena.getName());
			db.update(SurvivalGames.DB_ARENAS, "centery", loc.getY(), "name", arena.getName());
			db.update(SurvivalGames.DB_ARENAS, "centerz", loc.getZ(), "name", arena.getName());
			p.sendMessage(SurvivalGamesLanguage.CENTER_SET);
			return true;
		} else {
			p.sendMessage(SurvivalGamesLanguage.ARENA_NULL);
			return true;
		}
	}
}