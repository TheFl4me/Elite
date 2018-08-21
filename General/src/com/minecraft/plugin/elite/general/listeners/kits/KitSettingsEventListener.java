package com.minecraft.plugin.elite.general.listeners.kits;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class KitSettingsEventListener implements Listener {

    @EventHandler
    public void saveSettings(InventoryCloseEvent e) {
        GeneralPlayer p = GeneralPlayer.get((Player) e.getPlayer());
        if(p.isEditing()) {
            for(int i = 0; i < e.getInventory().getSize(); i++) {
                ItemStack item = e.getInventory().getItem(i);
                if (item == null || item.getType() == Material.AIR)
                    continue;
                p.setSlot(item.getType(), i);
            }
            p.getPlayer().setItemOnCursor(null);
            p.setEditing(false);
            GeneralPlayer.get(p.getUniqueId()).clear();
        }
    }

    @EventHandler
    public void onDropClick(InventoryClickEvent e) {
        GeneralPlayer p = GeneralPlayer.get((Player) e.getWhoClicked());
        if(p.isEditing() && (e.getClick() == ClickType.DROP || e.getClick() == ClickType.CONTROL_DROP)) {
            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
                return;
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
        if(p.isEditing()) {
            e.setCancelled(true);
        }
    }
}
