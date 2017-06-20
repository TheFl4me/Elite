package com.minecraft.plugin.elite.kitpvp.manager.duel;

import com.minecraft.plugin.elite.general.api.abstracts.Tool;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import org.bukkit.Material;

public class DuelSelectorTool extends Tool {

	public DuelSelectorTool(Language lang) {
		super(lang.get(KitPvPLanguage.DUEL_TOOL), Material.BLAZE_ROD, 8);
	}
}