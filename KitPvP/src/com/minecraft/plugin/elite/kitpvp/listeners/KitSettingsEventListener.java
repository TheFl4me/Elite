package com.minecraft.plugin.elite.kitpvp.listeners;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.kitpvp.manager.KitPlayer;
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
        KitPlayer kp = KitPlayer.get((Player) e.getPlayer());
        if(kp.isEditing()) {
            for(int i = 0; i < e.getInventory().getSize(); i++) {
                ItemStack item = e.getInventory().getItem(i);
                if (item == null || item.getType() == Material.AIR)
                    continue;
                kp.setSlot(item.getType(), i);
            }
            kp.getPlayer().setItemOnCursor(null);
            kp.setEditing(false);
            GeneralPlayer.get(kp.getUniqueId()).clear();
        }
    }

    @EventHandler
    public void onDropClick(InventoryClickEvent e) {
        KitPlayer kp = KitPlayer.get((Player) e.getWhoClicked());
        if(kp.isEditing() && (e.getClick() == ClickType.DROP || e.getClick() == ClickType.CONTROL_DROP)) {
            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
                return;
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        KitPlayer kp = KitPlayer.get(e.getPlayer());
        if(kp.isEditing()) {
            e.setCancelled(true);
        }
    }
}
