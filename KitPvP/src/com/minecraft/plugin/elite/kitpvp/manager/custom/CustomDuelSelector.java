package com.minecraft.plugin.elite.kitpvp.manager.custom;

import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import com.minecraft.plugin.elite.kitpvp.manager.tools.DuelSelector;
import org.bukkit.Material;

public class CustomDuelSelector extends DuelSelector {

    public CustomDuelSelector(Language lang) {
        super(lang.get(KitPvPLanguage.DUEL_TOOL_CUSTOM), Material.GOLD_SWORD, 1);
    }
}
