package com.minecraft.plugin.elite.general.api.events.stats;

import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ELOChangeEvent extends Event {

    private ePlayer player;
    private long amount;
    private boolean isPositive;

    private static final HandlerList handlers = new HandlerList();

    public ELOChangeEvent(ePlayer player, long amount) {
        this.player = player;
        this.amount = amount;
        this.isPositive = amount >= 0;
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

    public long getAmount() {
        return this.amount;
    }

    public boolean isPositive() {
        return this.isPositive;
    }
}
