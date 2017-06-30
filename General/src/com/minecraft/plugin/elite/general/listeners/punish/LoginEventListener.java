package com.minecraft.plugin.elite.general.listeners.punish;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class LoginEventListener implements Listener {
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		ePlayer p = ePlayer.get(e.getPlayer());
		Server server = Server.get();
		if(Bukkit.getServer().hasWhitelist())
			if(!p.getPlayer().isWhitelisted())
				e.disallow(Result.KICK_WHITELIST, p.getLanguage().get(GeneralLanguage.JOIN_WHITELIST));
		if(Bukkit.getServer().getMaxPlayers() <= Bukkit.getOnlinePlayers().size() && !p.getPlayer().hasPermission("egeneral.joinfull"))
			e.disallow(Result.KICK_FULL, p.getLanguage().get(GeneralLanguage.JOIN_FULL)
					.replaceAll("%domain", server.getDomain()));
	}
}