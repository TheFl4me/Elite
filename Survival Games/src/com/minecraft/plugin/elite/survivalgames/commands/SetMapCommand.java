package com.minecraft.plugin.elite.survivalgames.commands;

import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.survivalgames.SurvivalGamesLanguage;
import com.minecraft.plugin.elite.survivalgames.manager.Lobby;
import com.minecraft.plugin.elite.survivalgames.manager.arena.Arena;
import com.minecraft.plugin.elite.survivalgames.manager.arena.ArenaManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class SetMapCommand extends eCommand implements TabCompleter {

	public SetMapCommand() {
		super("setmap","esurvivalgames.setmap", false);
	}
	
	@Override
	public List<String> onTabComplete(CommandSender cs, Command cmd, String lbl, String[] args) {	
		if(args.length == 1)
			return ArenaManager.getAll().stream().map(Arena::getName).collect(Collectors.toList());
		return null;
	}

	public boolean execute(CommandSender cs, Command cmd, String[] args) {

		ePlayer p = ePlayer.get((Player) cs);
		if(args.length > 0) {
			Lobby lobby = Lobby.get();
			if(lobby.isActive()) {
				Arena arena = ArenaManager.get(args[0]);
				if(arena != null) {
					lobby.setArena(arena);
					lobby.updateScoreboard();
					for(Player players : Bukkit.getOnlinePlayers()) {
						ePlayer all = ePlayer.get(players);
						all.getPlayer().sendMessage(all.getLanguage().get(SurvivalGamesLanguage.MAP_SET)
								.replaceAll("%arena", arena.getName()));
					}
					return true;
				} else {
					p.sendMessage(SurvivalGamesLanguage.ARENA_NULL);
					return true;
				}
			} else {
				p.sendMessage(SurvivalGamesLanguage.START_ALREADY);
				return true;
			}
		} else {
			p.sendMessage(SurvivalGamesLanguage.MAP_SET_USAGE);
			return true;
		}
	}
}