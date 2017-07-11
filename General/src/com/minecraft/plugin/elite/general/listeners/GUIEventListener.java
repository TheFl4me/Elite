package com.minecraft.plugin.elite.general.listeners;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GUI;
import com.minecraft.plugin.elite.general.api.events.GUIClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GUIEventListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void clearGUI(InventoryCloseEvent e) {
        GeneralPlayer p = GeneralPlayer.get((Player) e.getPlayer());
        if (p != null) {
            GUI gui = p.getGUI();
            if (gui != null)
                p.removeGUI();
        }
    }

    @EventHandler
    public void callGUIClickEvent(InventoryClickEvent e) {
        GeneralPlayer p = GeneralPlayer.get((Player) e.getWhoClicked());
        if (p.isInGUI()) {
            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
                return;
            e.setCancelled(true);
            GUIClickEvent event = new GUIClickEvent(e);
            Bukkit.getPluginManager().callEvent(event);
        }
    }
}