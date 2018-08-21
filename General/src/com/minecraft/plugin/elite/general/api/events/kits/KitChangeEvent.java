package com.minecraft.plugin.elite.general.api.events.kits;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.special.kits.Kit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KitChangeEvent extends Event {

    private GeneralPlayer player;
    private Kit oldKit;
    private Kit newKit;

    private static final HandlerList handlers = new HandlerList();

    public KitChangeEvent(GeneralPlayer player, Kit oldKit, Kit newKit) {
        this.player = player;
        this.oldKit = oldKit;
        this.newKit = newKit;
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

    public Kit getOldKit() {
        return this.oldKit;
    }

    public Kit getNewKit() {
        return this.newKit;
    }
}
