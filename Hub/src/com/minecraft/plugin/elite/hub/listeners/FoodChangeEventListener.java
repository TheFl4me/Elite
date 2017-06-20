package com.minecraft.plugin.elite.hub.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodChangeEventListener implements Listener {
	
	@EventHandler
	public void foodChange(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}
}
