package com.minecraft.plugin.elite.raid.listeners.basic;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.raid.Raid;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;

public class SpawnEventListener implements Listener {
	
	@EventHandler
	public void cancelDamageInSpawn(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			GeneralPlayer p = GeneralPlayer.get((Player) e.getEntity());
			if(p.isInRegion(Raid.SPAWN)) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void invisToMobInSpawn(EntityTargetEvent e) {
		if (e.getTarget() instanceof Player) {
			GeneralPlayer p = GeneralPlayer.get((Player) e.getTarget());
			if(p.isInRegion(Raid.SPAWN)) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void spawnBuild(BlockPlaceEvent e) {
		GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
		if(p.isInRegion(Raid.SPAWN) && !p.isAdmin() && !p.isBuilding())
			e.setCancelled(true);
	}
	
	@EventHandler
	public void spawnBreak(BlockBreakEvent e) {
		GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
		if(p.isInRegion(Raid.SPAWN) && !p.isAdmin() && !p.isBuilding())
			e.setCancelled(true);
	}
}
