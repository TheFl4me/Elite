package com.minecraft.plugin.elite.survivalgames.commands;

import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.survivalgames.SurvivalGamesLanguage;
import com.minecraft.plugin.elite.survivalgames.manager.Lobby;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand extends eCommand {

	public StartCommand() {
		super("start", "esurvivalgames.start", false);
	}

	public boolean execute(CommandSender cs, Command cmd, String[] args) {


		ePlayer p = ePlayer.get((Player) cs);
		Lobby lobby = Lobby.get();
		if(lobby.isActive()) {
			if(lobby.hasStartedCountdown())
				lobby.setCountdownTime(10);
			else
				lobby.startCountDown(10);
			return true;
		} else {
			p.sendMessage(SurvivalGamesLanguage.START_ALREADY);
			return true;
		}
	}
}