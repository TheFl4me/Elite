package com.minecraft.plugin.elite.kitpvp.manager.kits;

import com.minecraft.plugin.elite.general.api.abstracts.Tool;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import org.bukkit.Material;

public class KitSelectorTool extends Tool {

	public KitSelectorTool(Language lang) {
		super(lang.get(KitPvPLanguage.KIT_GUI_SELECTOR_TITLE), Material.CHEST, 0);
	}
}
