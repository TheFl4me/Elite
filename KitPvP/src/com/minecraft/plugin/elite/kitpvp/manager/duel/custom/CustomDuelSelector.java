package com.minecraft.plugin.elite.kitpvp.manager.duel.custom;

import com.minecraft.plugin.elite.general.api.abstracts.Tool;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import org.bukkit.Material;

public class CustomDuelSelector extends Tool {

    public CustomDuelSelector(Language lang) {
        super(lang.get(KitPvPLanguage.DUEL_TOOL_CUSTOM), Material.GOLD_AXE, 1);
    }
}
