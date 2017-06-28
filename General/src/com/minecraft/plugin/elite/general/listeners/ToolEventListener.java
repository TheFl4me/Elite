package com.minecraft.plugin.elite.general.listeners;

import com.minecraft.plugin.elite.general.api.abstracts.Tool;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.events.tool.ToolClickEntityEvent;
import com.minecraft.plugin.elite.general.api.events.tool.ToolClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Iterator;
import java.util.List;

public class ToolEventListener implements Listener {

    @EventHandler
    public void callToolClickEvent(PlayerInteractEvent e) {
        ePlayer p = ePlayer.get(e.getPlayer());
        ItemStack item = p.getPlayer().getItemInHand();
        if(item != null) {
            if(item.getType() != Material.AIR) {
                if((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && p.hasTool()) {
                    for (Tool tool : p.getTools()) {
                        if (tool.getItem().getItemMeta().hasDisplayName() && item.getItemMeta().hasDisplayName()) {
                            if (tool.getName().equalsIgnoreCase(item.getItemMeta().getDisplayName())) {
                                ToolClickEvent event = new ToolClickEvent(p, tool);
                                Bukkit.getPluginManager().callEvent(event);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void callToolClickEntityEvent(PlayerInteractEntityEvent e) {
        ePlayer p = ePlayer.get(e.getPlayer());
        ItemStack item = p.getPlayer().getItemInHand();
        if(item != null) {
            if(item.getType() != Material.AIR) {
                if(p.hasTool()) {
                    for (Tool tool : p.getTools()) {
                        if (tool.getItem().getItemMeta().hasDisplayName() && item.getItemMeta().hasDisplayName()) {
                            if (tool.getName().equalsIgnoreCase(item.getItemMeta().getDisplayName())) {
                                ToolClickEntityEvent event = new ToolClickEntityEvent(p, tool, e.getRightClicked());
                                Bukkit.getPluginManager().callEvent(event);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onToolDrop(PlayerDropItemEvent e) {
        ePlayer p = ePlayer.get(e.getPlayer());
        if (p.hasTool()) {
            for (Tool tool : p.getTools()) {
                ItemMeta meta = e.getItemDrop().getItemStack().getItemMeta();
                if (meta.hasDisplayName()) {
                    if (meta.getDisplayName().equalsIgnoreCase(tool.getName())) {
                        e.setCancelled(true);
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void dontMoveTool(InventoryClickEvent e) {
        ePlayer p = ePlayer.get((Player) e.getWhoClicked());
        if(e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            if (p.hasTool()) {
                for (Tool tool : p.getTools()) {
                    ItemMeta meta = e.getCurrentItem().getItemMeta();
                    if (meta.hasDisplayName()) {
                        if (meta.getDisplayName().equalsIgnoreCase(tool.getName())) {
                            e.setCancelled(true);
                            break;
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent e) {
        ePlayer p = ePlayer.get(e.getEntity());
        if (p.hasTool()) {
            for (Tool tool : p.getTools()) {
                List<ItemStack> list = e.getDrops();
                Iterator<ItemStack> i = list.iterator();
                while (i.hasNext()) {
                    ItemStack drop = i.next();
                    if (drop.getItemMeta().hasDisplayName()) {
                        if (drop.getItemMeta().getDisplayName().equalsIgnoreCase(tool.getName()))
                            i.remove();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBuild(BlockPlaceEvent e) {
        ePlayer p = ePlayer.get(e.getPlayer());
        if (p.hasTool()) {
            for (Tool tool : p.getTools()) {
                if (e.getItemInHand().getItemMeta().hasDisplayName() && tool.getItem().getType().isBlock()) {
                    if (e.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(tool.getName())) {
                        e.setCancelled(true);
                        break;
                    }
                }
            }
        }
    }

}
