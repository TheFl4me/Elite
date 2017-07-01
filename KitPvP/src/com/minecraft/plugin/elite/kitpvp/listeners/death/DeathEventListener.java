package com.minecraft.plugin.elite.kitpvp.listeners.death;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.kitpvp.KitPvP;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import com.minecraft.plugin.elite.kitpvp.manager.KitPlayer;
import com.minecraft.plugin.elite.kitpvp.manager.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.List;

public class DeathEventListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void sendInvStatus(PlayerDeathEvent e) {
		ePlayer p = ePlayer.get(e.getEntity().getPlayer());
		KitPlayer kp = KitPlayer.get(p.getUniqueId());
		e.setDeathMessage(null);
		
		if(kp.hasKit()) {
			for(Kit kit : Kit.values()) {
				if(kp.hasKit(kit)) {
					List<ItemStack> list = e.getDrops();
					list.removeIf(drop -> kit.getItem().getType() == drop.getType());
					break;
				}
			}
		}

		Entity ent = e.getEntity().getKiller();
		if(ent !=  null && ent instanceof Player && ent.getUniqueId().equals(e.getEntity().getPlayer().getUniqueId()))
			return;
		if(!p.isAdminMode()) {
			DecimalFormat df = new DecimalFormat("0.0");
			p.addDeath();
			p.setKillStreak(0);
			if(e.getEntity().getKiller() instanceof Player) {
				ePlayer killer = ePlayer.get(e.getEntity().getKiller());
				KitPlayer kitKiller = KitPlayer.get(killer.getUniqueId());
				killer.addKill();
				p.addExpToDamagers();
				Inventory inv = killer.getPlayer().getInventory();
				int soups = 0;
				for (int i = 0; i < inv.getSize(); i++) {
					if(inv.getItem(i) != null) {
						if(inv.getItem(i).getType() == Material.MUSHROOM_SOUP) {
			    			soups++;
			    		}
					}
				}
				p.getPlayer().sendMessage(p.getLanguage().get(KitPvPLanguage.DEATH)
						.replaceAll("%z", killer.getName())
						.replaceAll("%kit", (kitKiller.getKit() == null ? "NONE" : kitKiller.getKit().getName().toUpperCase()))
						.replaceAll("%health", df.format(killer.getPlayer().getHealth() / 2.0)).replaceAll("%soups", Integer.toString(soups)));
				
				int streak = killer.getKillStreak();
				if(streak == 5 || streak == 10 || streak == 20 || streak == 30 || streak == 50 || streak == 75 || streak == 100 || streak == 150 || streak == 200) {
					for(Player all : Bukkit.getOnlinePlayers()) {
						all.sendMessage(ePlayer.get(all).getLanguage().get(KitPvPLanguage.KILL_STREAK)
								.replaceAll("%p", killer.getName())
								.replaceAll("%streak", Integer.toString(streak)));
					}
				}
			}
		}
		KitPvP.updateScoreboard();
	}
}