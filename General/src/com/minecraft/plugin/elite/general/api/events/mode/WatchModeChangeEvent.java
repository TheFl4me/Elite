package com.minecraft.plugin.elite.general.api.events.mode;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;

public class WatchModeChangeEvent extends ModeChangeEvent {

    public WatchModeChangeEvent(GeneralPlayer player, boolean toMode) {
        super(player, toMode);
    }
}