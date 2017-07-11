package com.minecraft.plugin.elite.general.api.events.stats;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PrestigeChangeEvent extends Event {

    private GeneralPlayer player;
    private int newPrestige;
    private int oldPrestige;

    private static final HandlerList handlers = new HandlerList();

    public PrestigeChangeEvent(GeneralPlayer player, int oldPrestige, int newPrestige) {
        this.player = player;
        this.newPrestige = newPrestige;
        this.oldPrestige = oldPrestige;
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

    public int getNewPrestige() {
        return this.newPrestige;
    }

    public int getOldPrestige() {
        return this.oldPrestige;
    }
}
