package com.minecraft.plugin.elite.kitpvp.listeners.death;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnEventListener implements Listener {
		
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		ePlayer p = ePlayer.get(e.getPlayer());
		e.setRespawnLocation(Bukkit.getWorld("world").getSpawnLocation());
		Bukkit.getScheduler().runTaskLater(General.getPlugin(), () -> {
            if(p != null) {
                p.clear();
            }
        }, 5);
	}
}