package com.minecraft.plugin.elite.general.api.events.tool;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.Tool;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ToolClickEvent extends Event {

    private GeneralPlayer player;
    private Tool tool;

    private static final HandlerList handlers = new HandlerList();

    public ToolClickEvent(GeneralPlayer p, Tool tool) {
        this.player = p;
        this.tool = tool;
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

    public Tool getTool() {
        return this.tool;
    }
}
