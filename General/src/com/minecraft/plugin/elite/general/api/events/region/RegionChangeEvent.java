package com.minecraft.plugin.elite.general.api.events.region;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class RegionChangeEvent extends Event {

    private ePlayer player;
    private ProtectedRegion region;

    private static final HandlerList handlers = new HandlerList();

    public RegionChangeEvent(ePlayer p, ProtectedRegion region) {
        this.player = p;
        this.region = region;
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
        return this.region;
    }

    public boolean isRegion(String name) {
        return this.getRegion().getId().equalsIgnoreCase(name);
    }
}
