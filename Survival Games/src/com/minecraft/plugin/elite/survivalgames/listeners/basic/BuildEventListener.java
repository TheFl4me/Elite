package com.minecraft.plugin.elite.survivalgames.listeners.basic;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.survivalgames.manager.Lobby;
import org.bukkit.Material;
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

import java.util.Arrays;
import java.util.List;

public class BuildEventListener implements Listener {

    private static final List<Material> blocks = Arrays.asList(
            Material.LEAVES, Material.LEAVES_2,
            Material.LONG_GRASS,
            Material.YELLOW_FLOWER, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM,
            Material.WEB, Material.CAKE_BLOCK,
            Material.CROPS);

    @EventHandler
    public void buildBlockPlace(BlockPlaceEvent e) {
        GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
        Lobby lobby = Lobby.get();
        if (!p.isBuilding())
            if(lobby.isActive() || !blocks.contains(e.getBlock().getType()))
                e.setCancelled(true);
    }

    @EventHandler
    public void buildBlockBreak(BlockBreakEvent e) {
        GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
        Lobby lobby = Lobby.get();
        if (!p.isBuilding())
            if(lobby.isActive() || !blocks.contains(e.getBlock().getType()))
                e.setCancelled(true);
    }

    @EventHandler
    public void onItemFrameInteract(PlayerInteractEntityEvent e) {
        GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
        if(e.getRightClicked().getType() == EntityType.ITEM_FRAME)
            if(!p.isBuilding())
                e.setCancelled(true);
    }

    @EventHandler
    public void onItemFrameBreak(HangingBreakByEntityEvent e) {
        if(e.getRemover() instanceof Player) {
            GeneralPlayer p = GeneralPlayer.get((Player) e.getRemover());
            Lobby lobby = Lobby.get();
            if(e.getEntity().getType() == EntityType.ITEM_FRAME)
                if(!p.isBuilding())
                    e.setCancelled(true);
        }
    }

    @EventHandler
    public void itemFrameItemRemoval(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof ItemFrame && e.getDamager() instanceof Player) {
            GeneralPlayer p = GeneralPlayer.get((Player) e.getDamager());
            if(!p.isBuilding())
                e.setCancelled(true);
        }
    }
}
