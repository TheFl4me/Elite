package com.minecraft.plugin.elite.raid.listeners.basic;

import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinQuitEventListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		ePlayer p = ePlayer.get(e.getPlayer());
		p.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());
		p.setBuilding(true);
	}
}
