package com.minecraft.plugin.elite.nohax.manager;

import com.minecraft.plugin.elite.general.api.abstracts.Tool;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.nohax.NoHaxLanguage;
import org.bukkit.Material;

public class ForceFieldCheckTool extends Tool {

    public ForceFieldCheckTool(Language lang) {
        super(lang.get(NoHaxLanguage.CHECK_FF_TOOL), Material.BLAZE_POWDER, 0);
    }
}
