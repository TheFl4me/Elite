package com.minecraft.plugin.elite.kitpvp.manager.duel.normal;

import com.minecraft.plugin.elite.general.api.abstracts.Tool;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import org.bukkit.Material;

public class NormalDuelSelectorTool extends Tool {

	public NormalDuelSelectorTool(Language lang) {
		super(lang.get(KitPvPLanguage.DUEL_TOOL_DEFAULT), Material.DIAMOND_AXE, 0);
	}
}