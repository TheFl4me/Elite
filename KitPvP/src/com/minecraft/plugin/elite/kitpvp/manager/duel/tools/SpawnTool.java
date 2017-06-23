package com.minecraft.plugin.elite.kitpvp.manager.duel.tools;

import com.minecraft.plugin.elite.general.api.abstracts.Tool;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import org.bukkit.Material;

public class SpawnTool extends Tool {

    public SpawnTool(Language lang) {
        super(lang.get(KitPvPLanguage.SPAWN_TOOL), Material.NETHER_BRICK_ITEM, 8);
    }
}
