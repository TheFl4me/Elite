package com.minecraft.plugin.elite.kitpvp.listeners.basic;

import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class BuildEventListener implements Listener {

    @EventHandler
    public void buildBlockPlace(BlockPlaceEvent e) {
        ePlayer p = ePlayer.get(e.getPlayer());
        if (!p.isBuilding())
            e.setCancelled(true);
    }

    @EventHandler
    public void buildBlockBreak(BlockBreakEvent e) {
        ePlayer p = ePlayer.get(e.getPlayer());
        if (!p.isBuilding())
            e.setCancelled(true);
    }

    @EventHandler
    public void onItemFrameInteract(PlayerInteractEntityEvent e) {
        ePlayer p = ePlayer.get(e.getPlayer());
        if(e.getRightClicked().getType() == EntityType.ITEM_FRAME)
            if(!p.isBuilding())
                e.setCancelled(true);
    }

    @EventHandler
    public void onItemFrameBreak(HangingBreakByEntityEvent e) {
        if(e.getRemover() instanceof Player) {
            ePlayer p = ePlayer.get((Player) e.getRemover());
            if(e.getEntity().getType() == EntityType.ITEM_FRAME)
                if(!p.isBuilding())
                    e.setCancelled(true);
        }
    }

    @EventHandler
    public void itemFrameItemRemoval(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof ItemFrame && e.getDamager() instanceof Player) {
            ePlayer p = ePlayer.get((Player) e.getDamager());
            if(!p.isBuilding())
                e.setCancelled(true);
        }
    }
}
