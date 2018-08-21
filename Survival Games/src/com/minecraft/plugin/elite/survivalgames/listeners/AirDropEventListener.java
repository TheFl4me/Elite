package com.minecraft.plugin.elite.survivalgames.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class AirDropEventListener implements Listener {

	@EventHandler
	public void cancelEggTeleport(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getMaterial() == Material.DRAGON_EGG) {
			e.setCancelled(true);
		}
	}
}
