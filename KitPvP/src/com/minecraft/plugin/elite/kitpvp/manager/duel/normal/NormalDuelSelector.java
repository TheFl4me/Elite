package com.minecraft.plugin.elite.kitpvp.manager.duel.normal;

import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import com.minecraft.plugin.elite.kitpvp.manager.duel.tools.DuelSelector;
import org.bukkit.Material;

public class NormalDuelSelector extends DuelSelector {

	public NormalDuelSelector(Language lang) {
		super(lang.get(KitPvPLanguage.DUEL_TOOL_DEFAULT), Material.DIAMOND_SWORD, 0);
	}
}