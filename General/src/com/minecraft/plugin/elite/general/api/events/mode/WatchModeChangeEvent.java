package com.minecraft.plugin.elite.general.api.events.mode;

import com.minecraft.plugin.elite.general.api.ePlayer;

public class WatchModeChangeEvent extends ModeChangeEvent {

    public WatchModeChangeEvent(ePlayer player, boolean toMode) {
        super(player, toMode);
    }
}