package com.minecraft.plugin.elite.general.api.events.stats;

import com.minecraft.plugin.elite.general.api.enums.Rank;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RankChangeEvent extends Event {

    private OfflinePlayer targetPlayer;
    private Rank newRank;
    private Rank oldRank;
    private boolean cancelled;

    private static final HandlerList handlers = new HandlerList();

    public RankChangeEvent(OfflinePlayer target, Rank newRank, Rank oldRank) {
        this.targetPlayer = target;
        this.newRank = newRank;
        this.oldRank = oldRank;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public OfflinePlayer getTarget() {
        return this.targetPlayer;
    }

    public Rank getNewRank() {
        return this.newRank;
    }

    public Rank getOldRank() {
        return this.oldRank;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
