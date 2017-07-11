package com.minecraft.plugin.elite.general.api.events;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InvisChangeEvent extends Event {

    private GeneralPlayer player;
    private boolean toInvis;

    private static final HandlerList handlers = new HandlerList();

    public InvisChangeEvent(GeneralPlayer player, boolean toInvis) {
        this.player = player;
        this.toInvis = toInvis;
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

    public boolean isToInvis() {
        return this.toInvis;
    }
}