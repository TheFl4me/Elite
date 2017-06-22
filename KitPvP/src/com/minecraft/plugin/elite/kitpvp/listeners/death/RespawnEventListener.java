package com.minecraft.plugin.elite.kitpvp.listeners.death;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.kitpvp.manager.KitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnEventListener implements Listener {
		
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		final ePlayer p = ePlayer.get(e.getPlayer());
		p.clear();
		e.setRespawnLocation(Bukkit.getWorld("world").getSpawnLocation());
		KitPlayer kp = KitPlayer.get(p.getUniqueId());
		kp.giveKit(kp.getLastKit());
	}
}