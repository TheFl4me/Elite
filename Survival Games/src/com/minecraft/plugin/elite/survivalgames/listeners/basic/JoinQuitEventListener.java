package com.minecraft.plugin.elite.survivalgames.listeners.basic;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.survivalgames.SurvivalGamesLanguage;
import com.minecraft.plugin.elite.survivalgames.manager.Lobby;
import com.minecraft.plugin.elite.survivalgames.manager.arena.Arena;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitEventListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onLogin(PlayerLoginEvent e) {
		GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
		Lobby lobby = Lobby.get();
		Server server = Server.get();
		if(lobby.isActive()) {
			if(lobby.isFull()) {
				if(!p.isPremium())
					e.disallow(Result.KICK_FULL, p.getLanguage().get(SurvivalGamesLanguage.KICK_FULL)
							.replaceAll("%domain", server.getDomain()));
				else if(lobby.hasNonPremiumPlayer()) {
					lobby.kickNormalPlayer();
					lobby.addPlayer(p);
				} else
					e.disallow(Result.KICK_FULL, p.getLanguage().get(SurvivalGamesLanguage.KICK_FULL_VIP));
			}
		} else if(!p.isMod())
			e.disallow(Result.KICK_OTHER, p.getLanguage().get(SurvivalGamesLanguage.KICK_ALREADY));
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
		Lobby lobby = Lobby.get();
		Server server = Server.get();
		p.getPlayer().teleport(lobby.getWorld().getSpawnLocation());

		if(lobby.isActive()) {
			if(!p.isInvis()) {
				lobby.addPlayer(p);
			}
		} else {
			p.getPlayer().teleport(lobby.getArena().getCenter());
		}
		lobby.updateScoreboard();

	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
		Lobby lobby = Lobby.get();
		Arena arena = lobby.getArena();
		if(lobby.isActive() && lobby.getPlayers().contains(p))
		    lobby.removePlayer(p);
		else if(arena.getPlayers().contains(p))
			arena.removePlayer(p, SurvivalGamesLanguage.DISCONNECT);
		p.clear();
	}
}
