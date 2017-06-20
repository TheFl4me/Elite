package com.minecraft.plugin.elite.survivalgames.listeners;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.survivalgames.manager.GamePhase;
import com.minecraft.plugin.elite.survivalgames.manager.Lobby;
import com.minecraft.plugin.elite.survivalgames.manager.arena.Arena;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class GamePhaseEventsListener implements Listener {
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Lobby lobby = Lobby.get();
		ePlayer p = ePlayer.get(e.getPlayer());
		if(!lobby.isActive()) {
			Arena arena = lobby.getArena();
			if(arena != null && arena.getGamePhase() == GamePhase.WAITING && arena.getPlayers().contains(p)) {
				if(e.getFrom().getX() != e.getTo().getX() && e.getFrom().getZ() != e.getTo().getZ()) {
					Location loc = new Location(arena.getWorld(), e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getZ());
					loc.setDirection(arena.getCenter().toVector().subtract(loc.toVector()));
					p.getPlayer().teleport(loc);
				}				
			}
		}
	}
	
	@EventHandler
	public void onHit(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Lobby lobby = Lobby.get();
			Arena arena = lobby.getArena();
			if(lobby.isActive() || arena.getGamePhase() == GamePhase.WAITING || arena.getGamePhase() == GamePhase.END)
				e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent e) {
		Lobby lobby = Lobby.get();
		if(lobby.isActive())
			e.setCancelled(true);
	}
	
	@EventHandler
	public void checkSpamOnCommand(PlayerCommandPreprocessEvent e) {
		if(e.getMessage().startsWith("spawn"))
			e.setCancelled(true);
	}
}