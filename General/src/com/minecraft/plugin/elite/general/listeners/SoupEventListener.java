package com.minecraft.plugin.elite.general.listeners;

import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SoupEventListener implements Listener {

	@EventHandler
	public void onSoupGiveHealth(PlayerInteractEvent e){
		Server server = Server.get();
		if(server.hasSoups()) {
			GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
			ItemStack item = p.getPlayer().getItemInHand();
			if(item != null) {
				if(item.getType() == Material.MUSHROOM_SOUP) {
					if ((e.getAction() != Action.RIGHT_CLICK_AIR) && (e.getAction() != Action.RIGHT_CLICK_BLOCK))
						return;
					double health = p.getPlayer().getHealth();
					double maxHealth = p.getPlayer().getMaxHealth();
					int food = p.getPlayer().getFoodLevel();
					e.setCancelled(true);
					if (health < maxHealth) {
						if(health + 7.0D > maxHealth)
							health = maxHealth;
						else
							health += 7.0D;
						p.getPlayer().setHealth(health);
						item.setType(Material.BOWL);
					} else if(food < 20) {
						food += 7;
						p.getPlayer().setFoodLevel(food);
						item.setType(Material.BOWL);
					}
				}
			}
		}
	}
}