package com.minecraft.plugin.elite.kitpvp.manager.kits.events;

import com.minecraft.plugin.elite.kitpvp.manager.KitPlayer;
import com.minecraft.plugin.elite.kitpvp.manager.kits.Kit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KitChangeEvent extends Event {

    private KitPlayer player;
    private Kit kit;

    private static final HandlerList handlers = new HandlerList();

    public KitChangeEvent(KitPlayer player, Kit kit) {
        this.player = player;
        this.kit = kit;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public KitPlayer getPlayer() {
        return this.player;
    }

    public Kit getKit() {
        return this.kit;
    }
}
