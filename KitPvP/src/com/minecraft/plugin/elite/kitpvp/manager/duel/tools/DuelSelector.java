package com.minecraft.plugin.elite.kitpvp.manager.duel.tools;

import com.minecraft.plugin.elite.general.api.abstracts.Tool;
import org.bukkit.Material;

public abstract class DuelSelector extends Tool {

    public DuelSelector(String name, Material mat, int slot) {
        super(name, mat, slot);
    }
}
