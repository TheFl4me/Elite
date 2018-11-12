package com.minecraft.plugin.elite.general.listeners.punish;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class LoginEventListener implements Listener {
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
		Server server = Server.get();
		if(Bukkit.getServer().hasWhitelist())
			if(!p.getPlayer().isWhitelisted())
				e.disallow(Result.KICK_WHITELIST, p.getLanguage().get(GeneralLanguage.JOIN_WHITELIST));
		if(Bukkit.getServer().getMaxPlayers() <= Bukkit.getOnlinePlayers().size() && !p.hasPermission(GeneralPermission.JOIN_FULL))
			e.disallow(Result.KICK_FULL, p.getLanguage().get(GeneralLanguage.JOIN_FULL)
					.replaceAll("%domain", server.getDomain()));
	}
}