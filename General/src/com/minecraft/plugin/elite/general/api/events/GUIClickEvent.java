package com.minecraft.plugin.elite.general.api.events;

import com.minecraft.plugin.elite.general.api.abstracts.GUI;
import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GUIClickEvent extends Event {

    private ePlayer player;
    private GUI gui;
    private ItemStack item;
    private int slot;

    private static final HandlerList handlers = new HandlerList();

    public GUIClickEvent(InventoryClickEvent event) {
        this.player = ePlayer.get((Player) event.getWhoClicked());
        this.gui = this.getPlayer().getGUI();
        this.item = event.getCurrentItem();
        this.slot = event.getSlot();
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ePlayer getPlayer() {
        return this.player;
    }

    public GUI getGUI() {
        return this.gui;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public int getSlot() {
        return this.slot;
    }
}
