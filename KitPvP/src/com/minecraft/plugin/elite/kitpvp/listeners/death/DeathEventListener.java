package com.minecraft.plugin.elite.kitpvp.listeners.death;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.kitpvp.KitPvP;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;

import java.text.DecimalFormat;

public class DeathEventListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void sendInvStatus(PlayerDeathEvent e) {
		e.setDeathMessage(null);
		GeneralPlayer p = GeneralPlayer.get(e.getEntity().getPlayer());
		if (p != null) {
			Player ent = e.getEntity().getKiller();
			if(ent !=  null && ent.getUniqueId().equals(e.getEntity().getPlayer().getUniqueId())) //Checks if player killed himself
				return;
			if(!p.isAdminMode()) {
				DecimalFormat df = new DecimalFormat("0.0");
				p.addDeath();
				p.setKillStreak(0);
				if(ent != null) {
					GeneralPlayer killer = GeneralPlayer.get(ent);
					if (killer != null) {
						killer.addKill();
						p.addExpToAttackers();
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
								.replaceAll("%kit", (killer.getKit() == null ? "NONE" : killer.getKit().getName().toUpperCase()))
								.replaceAll("%health", df.format(killer.getPlayer().getHealth() / 2.0)).replaceAll("%soups", Integer.toString(soups)));

						int streak = killer.getKillStreak();
						if(streak == 5 || streak == 10 || streak == 20 || streak == 30 || streak == 50 || streak == 75 || streak == 100 || streak == 150 || streak == 200) {
							for(Player all : Bukkit.getOnlinePlayers()) {
								all.sendMessage(GeneralPlayer.get(all).getLanguage().get(KitPvPLanguage.KILL_STREAK)
										.replaceAll("%p", killer.getName())
										.replaceAll("%streak", Integer.toString(streak)));
							}
						}
					}
				}
			}
		}
		KitPvP.updateScoreboard();
	}
}