package com.minecraft.plugin.elite.survivalgames.commands;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.survivalgames.SurvivalGamesLanguage;
import com.minecraft.plugin.elite.survivalgames.SurvivalGamesPermission;
import com.minecraft.plugin.elite.survivalgames.manager.Lobby;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand extends GeneralCommand {

	public StartCommand() {
		super("start", SurvivalGamesPermission.START, false);
	}

	public boolean execute(CommandSender cs, Command cmd, String[] args) {


		GeneralPlayer p = GeneralPlayer.get((Player) cs);
		Lobby lobby = Lobby.get();
		if(lobby.isActive()) {
			if(lobby.hasStartedCountdown())
				lobby.setCountdownTime(10);
			else
				lobby.startCountDown(10);
        } else {
			p.sendMessage(SurvivalGamesLanguage.START_ALREADY);
        }
        return true;
    }
}