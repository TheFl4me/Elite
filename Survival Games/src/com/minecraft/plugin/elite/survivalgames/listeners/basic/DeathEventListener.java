package com.minecraft.plugin.elite.survivalgames.listeners.basic;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.survivalgames.SurvivalGamesLanguage;
import com.minecraft.plugin.elite.survivalgames.manager.Lobby;
import com.minecraft.plugin.elite.survivalgames.manager.arena.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DeathEventListener implements Listener {
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		ePlayer p = ePlayer.get(e.getEntity().getPlayer());
		Lobby lobby = Lobby.get();
		Arena arena = lobby.getArena();
		e.setDeathMessage(null);
		if(lobby.isActive())
			return;
		if(arena.getPlayers().contains(p)) {
			arena.removePlayer(p, SurvivalGamesLanguage.DEATH);
			if(!p.canAdminMode() && !p.canWatch())
				p.getPlayer().kickPlayer(p.getLanguage().get(SurvivalGamesLanguage.KICK_DEATH)
						.replaceAll("%remaining", Integer.toString(arena.getPlayers().size())));
			if(e.getEntity().getKiller() instanceof Player) {
				ePlayer z = ePlayer.get(e.getEntity().getKiller());
				if(arena.getPlayers().contains(z)) {
					z.addExp((p.getPrestige() + 1) * 100);
					arena.addKill(z);
				}
			}
		}
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		ePlayer p = ePlayer.get(e.getPlayer());
		Lobby lobby = Lobby.get();
		p.clear();
		if(!lobby.isActive())
			if(p.canAdminMode()) {
				p.setAdminMode(true);
				e.setRespawnLocation(p.getPlayer().getLocation());
			} else if(p.canWatch()) {
				p.setWatching(true);
				e.setRespawnLocation(p.getPlayer().getLocation());
			}
	}
}