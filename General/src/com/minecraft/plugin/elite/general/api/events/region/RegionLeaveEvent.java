package com.minecraft.plugin.elite.general.api.events.region;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RegionLeaveEvent extends Event {

    private ePlayer player;
    private ProtectedRegion oldRegion;

    private static final HandlerList handlers = new HandlerList();

    public RegionLeaveEvent(ePlayer p, ProtectedRegion oldRegion) {
        this.player = p;
        this.oldRegion = oldRegion;
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

    public ProtectedRegion getRegion() {
        return this.oldRegion;
    }
}
