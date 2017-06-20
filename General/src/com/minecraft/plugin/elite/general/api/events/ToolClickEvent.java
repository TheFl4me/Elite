package com.minecraft.plugin.elite.general.api.events;

import com.minecraft.plugin.elite.general.api.abstracts.Tool;
import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;

public class ToolClickEvent extends Event {

    private ePlayer player;
    private Tool tool;

    private static final HandlerList handlers = new HandlerList();

    public ToolClickEvent(PlayerInteractEvent e, Tool tool) {
        this.player = ePlayer.get(e.getPlayer());
        this.tool = tool;
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

    public Tool getTool() {
        return this.tool;
    }
}
