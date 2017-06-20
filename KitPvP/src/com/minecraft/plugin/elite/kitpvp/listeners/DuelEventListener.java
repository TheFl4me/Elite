package com.minecraft.plugin.elite.kitpvp.listeners;

import com.minecraft.plugin.elite.general.api.abstracts.Tool;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.kitpvp.KitPvP;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import com.minecraft.plugin.elite.kitpvp.manager.duel.Duel;
import com.minecraft.plugin.elite.kitpvp.manager.duel.DuelGUI;
import com.minecraft.plugin.elite.kitpvp.manager.duel.DuelManager;
import com.minecraft.plugin.elite.kitpvp.manager.duel.DuelRequest;
import com.minecraft.plugin.elite.kitpvp.manager.duel.DuelSelectorTool;
import com.minecraft.plugin.elite.kitpvp.manager.duel.DuelSetup;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class DuelEventListener implements Listener {

	@EventHandler
	public void on1v1ItemClick(PlayerInteractEntityEvent e) {
		if(e.getRightClicked() instanceof Player) {
			ePlayer p = ePlayer.get(e.getPlayer());
			ePlayer z = ePlayer.get((Player) e.getRightClicked());
			ItemStack item = p.getPlayer().getItemInHand();
			if(item.getType() == Material.AIR || item.getType() == null)
				return;
			if(!p.isAdminMode() && !p.isWatching() && !z.isAdminMode() && !z.isWatching()) {
				if(p.hasTool()) {
					for(Tool tool : p.getTools())
						if (item.getItemMeta().hasDisplayName() && tool instanceof DuelSelectorTool) {
							if (item.getItemMeta().getDisplayName().equalsIgnoreCase(tool.getName())) {
								Duel duel = DuelManager.get(z);
								if (duel != null)
									p.getPlayer().sendMessage(p.getLanguage().get(KitPvPLanguage.DUEL_ALREADY)
											.replaceAll("%z1", z.getName())
											.replaceAll("%z2", duel.getOpponent(z).getName()));
								DuelRequest valid_request = DuelManager.getRequest(z, p);
								if (valid_request != null) {
									Duel new_duel = new Duel(valid_request);
									new_duel.openGUI();
									return;
								}
								if (DuelManager.getRequest(p, z) == null) {
									DuelRequest reqOld = DuelManager.getRequest(p);
									if (reqOld != null)
										reqOld.delete();
									final DuelRequest request = new DuelRequest(p, z);
									z.getPlayer().sendMessage(z.getLanguage().get(KitPvPLanguage.DUEL_REQUEST_RECEIVED)
											.replaceAll("%p", p.getName()));
									p.getPlayer().sendMessage(p.getLanguage().get(KitPvPLanguage.DUEL_REQUEST_SENT)
											.replaceAll("%z", z.getName()));
									Bukkit.getScheduler().scheduleSyncDelayedTask(KitPvP.getPlugin(), request::delete, 200);
								} else
									p.sendMessage(KitPvPLanguage.DUEL_REQUEST_COOLDOWN);
							}
							return;
						}
				}
			}
		}
	}
	
	@EventHandler
	public void checkIfOneVsOne(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			
			ePlayer p = ePlayer.get((Player) e.getDamager());
			ePlayer z = ePlayer.get((Player) e.getEntity());
			
			//fighter hits spec
			Duel duel = DuelManager.get(p);
			if(duel != null)
				if(!duel.getOpponent(p).getUniqueId().equals(z.getUniqueId()))
					e.setCancelled(true);
			
			//spec hits fighter
			Duel duel2 = DuelManager.get(z);
			if(duel != null)
				if(!duel2.getOpponent(z).getUniqueId().equals(p.getUniqueId()))
					e.setCancelled(true);
		}
	} 
	
	@EventHandler
	public void on1v1Quit(PlayerQuitEvent e) {
		ePlayer p = ePlayer.get(e.getPlayer());
		Duel duel = DuelManager.get(p);
		if(duel != null) {
			ePlayer z = duel.getOpponent(p);
			duel.end(z, p);
		}
	}
	
	@EventHandler
	public void on1v1Death(PlayerDeathEvent e) {
		if(e.getEntity().getKiller() instanceof Player) {
			ePlayer z = ePlayer.get(e.getEntity().getKiller());
			ePlayer p = ePlayer.get(e.getEntity().getPlayer());
			Duel duel = DuelManager.get(p);
			if(duel != null && duel.getOpponent(p).getUniqueId().equals(z.getUniqueId())) {
				e.getDrops().clear();
				z.clear();
				duel.end(z, p);
			}
		}
	}
	
	@EventHandler
	public void remove1v1Drops(PlayerDropItemEvent e) {
		ePlayer p = ePlayer.get(e.getPlayer());
		Duel duel = DuelManager.get(p);
		if(duel != null)
			e.getItemDrop().remove();
	}
	
	@EventHandler
	public void invClose(InventoryCloseEvent e) {
		ePlayer p = ePlayer.get((Player) e.getPlayer());
		if(p.isInGUI() && p.getGUI() instanceof DuelGUI) {
			final DuelSetup setup = ((DuelGUI) p.getGUI()).getSetup();
			p.removeGUI();
			setup.abort();
		}
	}
}
