package com.minecraft.plugin.elite.survivalgames.manager.alive;

import com.minecraft.plugin.elite.general.api.abstracts.Tool;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.survivalgames.SurvivalGamesLanguage;
import org.bukkit.Material;

public class PlayerTool extends Tool {

    public PlayerTool(Language lang) {
        super(lang.get(SurvivalGamesLanguage.PLAYERS_GUI_TITLE), Material.PAPER, 8);
    }
}
