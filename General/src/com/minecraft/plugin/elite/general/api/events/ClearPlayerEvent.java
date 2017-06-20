package com.minecraft.plugin.elite.general.api.events;

import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClearPlayerEvent extends Event {

    private ePlayer player;

    private static final HandlerList handlers = new HandlerList();

    public ClearPlayerEvent(ePlayer player) {
        this.player = player;
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
}