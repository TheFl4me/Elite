package com.minecraft.plugin.elite.general.antihack.hacks.movement;

import com.minecraft.plugin.elite.general.antihack.alert.AlertManager;
import com.minecraft.plugin.elite.general.antihack.alert.AlertType;
import com.minecraft.plugin.elite.general.antihack.hacks.PlayerMove;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;

public class FastLadderHack {

    public static void check(PlayerMove move) {
        GeneralPlayer p = move.getPlayer();
        double index = 0.2;
        if ((move.isInLadder() || move.isInVine()) && !move.isCanSpeed())
            if (move.getSpeed() >= index && move.getJumpHeight() > index)
                AlertManager.set(p, AlertType.FAST_LADDER, 5, ((move.getSpeed() - index) + (move.getJumpHeight() - index)) / 2D);
    }
}
