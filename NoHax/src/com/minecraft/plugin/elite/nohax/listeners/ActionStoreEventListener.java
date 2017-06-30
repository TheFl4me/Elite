package com.minecraft.plugin.elite.nohax.listeners;

import com.minecraft.plugin.elite.nohax.manager.HaxPlayer;
import com.minecraft.plugin.elite.nohax.manager.hax.PlayerClick;
import com.minecraft.plugin.elite.nohax.manager.hax.PlayerDamage;
import com.minecraft.plugin.elite.nohax.manager.hax.PlayerMove;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class ActionStoreEventListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void storeMoves(PlayerMoveEvent e) {
        PlayerMove.store(e);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void storeDamage(EntityDamageEvent e) {
        PlayerDamage.store(e);
        if(e.getEntity() instanceof Player) {
            HaxPlayer p = HaxPlayer.get((Player) e.getEntity());
            p.invalidate(40);
        }
    }

    @EventHandler
    public void onTp(PlayerTeleportEvent e) {
        HaxPlayer p = HaxPlayer.get(e.getPlayer());
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
