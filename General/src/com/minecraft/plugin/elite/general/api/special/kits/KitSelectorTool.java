package com.minecraft.plugin.elite.general.api.special.kits;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.Tool;
import com.minecraft.plugin.elite.general.api.enums.Language;
import org.bukkit.Material;

public class KitSelectorTool extends Tool {

	public KitSelectorTool(Language lang) {
		super(lang.get(GeneralLanguage.KIT_GUI_SELECTOR_TITLE), Material.CHEST, 0);
	}
}
