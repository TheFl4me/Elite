package com.minecraft.plugin.elite.general.api.events.mode;

import com.minecraft.plugin.elite.general.api.ePlayer;

public class AdminModeChangeEvent extends ModeChangeEvent {

    public AdminModeChangeEvent(ePlayer player, boolean toMode) {
        super(player, toMode);
    }
}