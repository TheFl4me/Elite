package com.minecraft.plugin.elite.raid.listeners.basic;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.raid.RaidLanguage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;

import java.text.DecimalFormat;

public class DeathEventListener implements Listener {
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		GeneralPlayer p = GeneralPlayer.get(e.getEntity().getPlayer());
		e.setDeathMessage(null);
    	
		if(e.getEntity().getKiller().getUniqueId().equals(e.getEntity().getPlayer().getUniqueId()))
			return;
		if(!p.isAdminMode()) {
			DecimalFormat df = new DecimalFormat("0.0");
			p.addDeath();
			p.setKillStreak(0);
			if(e.getEntity().getKiller() instanceof Player) {
				GeneralPlayer killer = GeneralPlayer.get(e.getEntity().getKiller());
				killer.addKill();
				killer.addExp((p.getPrestige() + 1) * 300);
				Inventory inv = killer.getPlayer().getInventory();
				int soups = 0;
				for (int i = 0; i < inv.getSize(); i++) {
					if(inv.getItem(i) != null) {
						if(inv.getItem(i).getType() == Material.MUSHROOM_SOUP) {
			    			soups++;
			    		}
					}
				}
				p.getPlayer().sendMessage(p.getLanguage().get(RaidLanguage.DEATH)
						.replaceAll("%z", killer.getName())
						.replaceAll("%health", df.format(killer.getPlayer().getHealth() / 2.0))
						.replaceAll("%soups", Integer.toString(soups)));
			}
		}
	}

}
