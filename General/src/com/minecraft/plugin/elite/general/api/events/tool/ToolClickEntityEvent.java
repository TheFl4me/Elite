package com.minecraft.plugin.elite.general.api.events.tool;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.Tool;
import org.bukkit.entity.Entity;

public class ToolClickEntityEvent extends ToolClickEvent {

    private Entity ent;

    public ToolClickEntityEvent(GeneralPlayer p, Tool tool, Entity ent) {
        super(p, tool);
        this.ent = ent;
    }

    public Entity getClickedEntity() {
        return this.ent;
    }
}
