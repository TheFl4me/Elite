package com.minecraft.plugin.elite.raid.listeners;

import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.material.SpawnEgg;

import java.util.Random;

public class MobCaptureEventListener implements Listener {

	@EventHandler
	public void onMobCatch(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Egg) {
		    if(e.getEntity() instanceof Player)
		        return;
		    Egg egg = (Egg) e.getDamager();
		    if(egg.getShooter() instanceof Player) {
				Entity shooter = (Player) egg.getShooter();
				e.setCancelled(true);
				e.getEntity().remove();
				Random r = new Random();
				int per = r.nextInt(100) + 1;
				boolean chance;
				if(e.getEntityType() == EntityType.MUSHROOM_COW)
					chance = per <= 10;
				else
					chance = per <= 50;
				if (chance) {
					SpawnEgg spawnEgg = new SpawnEgg();
					spawnEgg.setSpawnedType(e.getEntityType());
					shooter.getWorld().dropItemNaturally(e.getEntity().getLocation(), spawnEgg.toItemStack());
				}
		    }	
		}
	}
}