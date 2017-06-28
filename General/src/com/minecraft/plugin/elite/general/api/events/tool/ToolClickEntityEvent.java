package com.minecraft.plugin.elite.general.api.events.tool;

import com.minecraft.plugin.elite.general.api.abstracts.Tool;
import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.entity.Entity;

public class ToolClickEntityEvent extends ToolClickEvent {

    private Entity ent;

    public ToolClickEntityEvent(ePlayer p, Tool tool, Entity ent) {
        super(p, tool);
        this.ent = ent;
    }

    public Entity getClickedEntity() {
        return this.ent;
    }
}
