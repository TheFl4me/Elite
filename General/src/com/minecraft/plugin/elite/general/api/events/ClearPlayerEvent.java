package com.minecraft.plugin.elite.general.api.events;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClearPlayerEvent extends Event {

    private GeneralPlayer player;

    private static final HandlerList handlers = new HandlerList();

    public ClearPlayerEvent(GeneralPlayer player) {
        this.player = player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public GeneralPlayer getPlayer() {
        return this.player;
    }
}