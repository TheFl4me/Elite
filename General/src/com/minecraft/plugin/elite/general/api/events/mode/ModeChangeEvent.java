package com.minecraft.plugin.elite.general.api.events.mode;

import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class ModeChangeEvent extends Event {

    private ePlayer player;
    private boolean toMode;

    private static final HandlerList handlers = new HandlerList();

    public ModeChangeEvent(ePlayer player, boolean toMode) {
        this.player = player;
        this.toMode = toMode;
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

    public boolean isToMode() {
        return this.toMode;
    }

}
