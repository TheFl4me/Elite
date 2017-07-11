package com.minecraft.plugin.elite.general.api.events.stats;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LevelChangeEvent extends Event {

    private GeneralPlayer player;
    private int newLevel;
    private int oldLevel;
    private boolean isPositive;

    private static final HandlerList handlers = new HandlerList();

    public LevelChangeEvent(GeneralPlayer player, int oldLevel, int newLevel) {
        this.player = player;
        this.newLevel = newLevel;
        this.oldLevel = oldLevel;
        this.isPositive = newLevel > oldLevel;
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

    public int getNewLevel() {
        return this.newLevel;
    }

    public int getOldLevel() {
        return this.oldLevel;
    }

    public boolean isLevelUp() {
        return this.isPositive;
    }
}
