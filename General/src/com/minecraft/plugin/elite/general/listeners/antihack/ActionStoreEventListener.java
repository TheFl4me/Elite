package com.minecraft.plugin.elite.general.listeners.antihack;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.antihack.hacks.PlayerAttack;
import com.minecraft.plugin.elite.general.antihack.hacks.PlayerClick;
import com.minecraft.plugin.elite.general.antihack.hacks.PlayerMove;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ActionStoreEventListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void storeMoves(PlayerMoveEvent e) {
        PlayerMove.store(e);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void storeDamage(EntityDamageByEntityEvent e) {
        PlayerAttack.store(e);
    }

    @EventHandler
    public void cancelMoveDetectOnHit(EntityDamageByEntityEvent e) {
        if (e.isCancelled() || e.getDamage() == 0)
            return;
        if (e.getEntity() instanceof Player) {
            GeneralPlayer target = GeneralPlayer.get((Player) e.getEntity());
            if (!target.canBypassChecks()) {
                if(target.hasKnockback()) {
                    target.getKnockbackTask().cancel();
                    target.setKnockbackTask(null);
                }
                target.setKnockbackTask(new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(target == null)
                            cancel();
                        if(target.hasKnockback()) {
                            target.getKnockbackTask().cancel();
                            target.setKnockbackTask(null);
                        }
                    }
                });
                target.getKnockbackTask().runTaskLater(General.getPlugin(), 40);
            }
        }
    }

    @EventHandler
    public void onTp(PlayerTeleportEvent e) {
        GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
        p.setLastOnGround(null);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void storeHitClick(PlayerInteractEvent e) {
        PlayerClick.storeHitClick(e);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void storeInvClick(InventoryClickEvent e) {
        PlayerClick.storeInvClick(e);
    }
}
