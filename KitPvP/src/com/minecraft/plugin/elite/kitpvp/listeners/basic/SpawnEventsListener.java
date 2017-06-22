package com.minecraft.plugin.elite.kitpvp.listeners.basic;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.kitpvp.KitPvP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;

public class SpawnEventsListener implements Listener {
	
	@EventHandler
	public void cancelDamageInSpawn(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			ePlayer p = ePlayer.get((Player) e.getEntity());
			if(p.isInRegion(KitPvP.REGION_SPAWN)) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void invisToMobInSpawn(EntityTargetEvent e) {
		if (e.getTarget() instanceof Player) {
			ePlayer p = ePlayer.get((Player) e.getTarget());
			if(p.isInRegion(KitPvP.REGION_SPAWN)) {
				e.setCancelled(true);
			}
		}
	}
}