package com.minecraft.plugin.elite.kitpvp.manager.queue;

import com.minecraft.plugin.elite.general.api.abstracts.Tool;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import org.bukkit.Material;

public class CancelQueueTool extends Tool {

    public CancelQueueTool(Language lang) {
        super(lang.get(KitPvPLanguage.DUEL_TOOL_QUEUE_CANCEL), Material.BARRIER, 4);
    }
}
