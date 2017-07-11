package com.minecraft.plugin.elite.survivalgames.listeners.basic;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.events.mode.ModeChangeEvent;
import com.minecraft.plugin.elite.survivalgames.SurvivalGamesLanguage;
import com.minecraft.plugin.elite.survivalgames.manager.Lobby;
import com.minecraft.plugin.elite.survivalgames.manager.arena.Arena;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ModeChangeEventListener implements Listener {
	
	@EventHandler
	public void onAdminChange(ModeChangeEvent e) {
		GeneralPlayer p = e.getPlayer();
		Lobby lobby = Lobby.get();
		if(!e.isToMode()) {
			p.clear();
			if(lobby.isActive()) {
				if(lobby.isFull()) {
					if(lobby.hasNonPremiumPlayer()) {
						lobby.kickNormalPlayer();
						lobby.addPlayer(p);
					} else
						p.getPlayer().sendMessage("full");
				} else
					lobby.addPlayer(p);
			}
		} else {
			p.clear();
			if(!lobby.isActive()) {
				Arena arena = lobby.getArena();
				if(arena.getPlayers().contains(p))
					arena.removePlayer(p, SurvivalGamesLanguage.DISCONNECT);
			} else if(lobby.getPlayers().contains(p))
				lobby.removePlayer(p);
		}
	}
	
	@EventHandler
	public void cancelWatchOpenChest(PlayerInteractEvent e) {
		GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
		Lobby lobby = Lobby.get();
		if(p.isWatching() && !lobby.isActive())
		   if (e.getAction() == Action.RIGHT_CLICK_BLOCK && (e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.TRAPPED_CHEST))
		      e.setCancelled(true);
	}
}
