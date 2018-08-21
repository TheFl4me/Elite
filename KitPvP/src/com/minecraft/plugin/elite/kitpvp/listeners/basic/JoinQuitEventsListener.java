package com.minecraft.plugin.elite.kitpvp.listeners.basic;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.kitpvp.KitPvP;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitEventsListener implements Listener {
	
	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent e) {
		GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
		p.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());
		KitPvP.updateScoreboard();
		KitPvP.loadHolograms(p);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerQuit(PlayerQuitEvent e) {
		GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
		p.clear();
	}
}