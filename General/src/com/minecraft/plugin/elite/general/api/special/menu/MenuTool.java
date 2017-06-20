package com.minecraft.plugin.elite.general.api.special.menu;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.Tool;
import com.minecraft.plugin.elite.general.api.enums.Language;
import org.bukkit.Material;

public class MenuTool extends Tool {

	public MenuTool(Language lang) {
		super(lang.get(GeneralLanguage.MENU_GUI_TITLE), Material.BOOK, 4);
	}
}