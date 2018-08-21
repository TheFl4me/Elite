package com.minecraft.plugin.elite.kitpvp.manager.queue;

import com.minecraft.plugin.elite.general.api.abstracts.Tool;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import org.bukkit.Material;

public class DuelQueueTool extends Tool {


    public DuelQueueTool(Language lang) {
        super(lang.get(KitPvPLanguage.DUEL_TOOL_QUEUE), Material.DIAMOND, 6);
    }
}
