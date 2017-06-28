package com.minecraft.plugin.elite.kitpvp.listeners;

import com.minecraft.plugin.elite.general.api.abstracts.Tool;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.events.ToolClickEvent;
import com.minecraft.plugin.elite.general.api.events.stats.ELOChangeEvent;
import com.minecraft.plugin.elite.general.api.events.tool.ToolClickEntityEvent;
import com.minecraft.plugin.elite.kitpvp.KitPvP;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import com.minecraft.plugin.elite.kitpvp.manager.duel.Duel;
import com.minecraft.plugin.elite.kitpvp.manager.duel.DuelRequest;
import com.minecraft.plugin.elite.kitpvp.manager.duel.queue.CancelQueueTool;
import com.minecraft.plugin.elite.kitpvp.manager.duel.queue.DuelQueueTool;
import com.minecraft.plugin.elite.kitpvp.manager.duel.tools.DuelSelector;
import com.minecraft.plugin.elite.kitpvp.manager.duel.custom.CustomDuelSelector;
import com.minecraft.plugin.elite.kitpvp.manager.duel.custom.DuelGUI;
import com.minecraft.plugin.elite.kitpvp.manager.duel.DuelManager;
import com.minecraft.plugin.elite.kitpvp.manager.duel.custom.DuelSetup;
import com.minecraft.plugin.elite.kitpvp.manager.duel.normal.NormalDuelSelector;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DuelEventListener implements Listener {

	@EventHandler
	public void onQueueClick(ToolClickEvent e) {
		ePlayer p = e.getPlayer();
		if(e.getTool() instanceof DuelQueueTool) {
			if(DuelManager.getQueue().isEmpty()) {
				DuelManager.addToQueue(p.getUniqueId());
				if(p.hasTool())
					p.clearTools();
				p.giveTool(new CancelQueueTool(p.getLanguage()));
			} else {
				ePlayer z = ePlayer.get(DuelManager.getQueue().get(0));
				Duel duel = new Duel(z, p, Duel.DuelType.NORMAL);
				duel.queueStart();
			}
		}
	}

	@EventHandler
	public void onQueueCancelClick(ToolClickEvent e) {
		ePlayer p = e.getPlayer();
		if(e.getTool() instanceof CancelQueueTool) {
			DuelManager.removeFromQueue(p.getUniqueId());
			p.clear();
		}
	}

	@EventHandler
	public void onDuelSelectorClick(ToolClickEntityEvent e) {
		if(e.getClickedEntity() instanceof Player) {
			ePlayer p = e.getPlayer();
			ePlayer z = ePlayer.get((Player) e.getClickedEntity());
			Tool tool = e.getTool();
			if(tool instanceof DuelSelector) {
				if(!p.isAdminMode() && !p.isWatching() && !z.isAdminMode() && !z.isWatching() && p.isInRegion(KitPvP.REGION_DUEL) && z.isInRegion(KitPvP.REGION_DUEL)) {
					Duel duel = DuelManager.get(z);
					if(duel != null) {
						p.getPlayer().sendMessage(p.getLanguage().get(KitPvPLanguage.DUEL_ALREADY)
								.replaceAll("%z1", z.getName())
								.replaceAll("%z2", duel.getOpponent(z).getName()));
						return;
					}
					Duel.DuelType type = null;
					if(tool instanceof CustomDuelSelector)
						type = Duel.DuelType.CUSTOM;
					else if(tool instanceof NormalDuelSelector)
						type = Duel.DuelType.NORMAL;
					if(type != null) {
						DuelRequest request_by_z = DuelManager.getRequest(z, p);
						if(request_by_z != null && request_by_z.getType() == type) {
							Duel new_duel = new Duel(request_by_z);
							new_duel.accepted();
							return;
						}
						DuelRequest request_by_p = DuelManager.getRequest(p, z);
						if(request_by_p == null) {
							DuelRequest other_request_by_p = DuelManager.getRequest(p);
							if(other_request_by_p != null)
								other_request_by_p.delete();
							final DuelRequest new_request_by_p = new DuelRequest(p, z, type);
							z.getPlayer().sendMessage(z.getLanguage().get(KitPvPLanguage.DUEL_REQUEST_RECEIVED)
									.replaceAll("%p", p.getName())
									.replaceAll("%type", type.toString()));
							p.getPlayer().sendMessage(p.getLanguage().get(KitPvPLanguage.DUEL_REQUEST_SENT)
									.replaceAll("%z", z.getName())
									.replaceAll("%type", type.toString()));
							Bukkit.getScheduler().runTaskLater(KitPvP.getPlugin(), new_request_by_p::delete, 200);
						} else {
							p.sendMessage(KitPvPLanguage.DUEL_REQUEST_COOLDOWN);
						}
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

			if(p.isInRegion(KitPvP.REGION_DUEL) && z.isInRegion(KitPvP.REGION_DUEL)) {
				//fighter hits spec
				Duel duel = DuelManager.get(p);
				if(duel == null)
					e.setCancelled(true);
				if(duel != null)
					if(!duel.getOpponent(p).getUniqueId().equals(z.getUniqueId()))
						e.setCancelled(true);

				//spec hits fighter
				Duel duel2 = DuelManager.get(z);
				if(duel2 == null)
					e.setCancelled(true);
				if(duel2 != null)
					if(!duel2.getOpponent(z).getUniqueId().equals(p.getUniqueId()))
						e.setCancelled(true);
			}
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
	public void onOtherJoinHideFromDuel(PlayerJoinEvent e) {
		ePlayer p = ePlayer.get(e.getPlayer());
		for(Player players : Bukkit.getOnlinePlayers()) {
			ePlayer z = ePlayer.get(players);
			Duel duel = DuelManager.get(z);
			if(duel != null && duel.hasStarted())
				z.getPlayer().hidePlayer(p.getPlayer());
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
	public void invClose(InventoryCloseEvent e) {
		ePlayer p = ePlayer.get((Player) e.getPlayer());
		if(p.isInGUI() && p.getGUI() instanceof DuelGUI) {
			final DuelSetup setup = ((DuelGUI) p.getGUI()).getSetup();
			p.removeGUI();
			setup.abort();
		}
	}

	@EventHandler
	public void foodChange(FoodLevelChangeEvent e) {
		ePlayer p = ePlayer.get(e.getEntity().getUniqueId());
		if(DuelManager.get(p) == null && p.isInRegion(KitPvP.REGION_DUEL))
			e.setCancelled(true);
	}

	@EventHandler
	public void ELOChange(ELOChangeEvent e) {
		KitPvP.updateScoreboard();
	}
}
