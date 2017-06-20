package com.minecraft.plugin.elite.hub.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageEventListener implements Listener {
	
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player)
			e.setCancelled(true);
	}
}