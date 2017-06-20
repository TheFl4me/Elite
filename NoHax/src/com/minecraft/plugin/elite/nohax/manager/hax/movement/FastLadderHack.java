package com.minecraft.plugin.elite.nohax.manager.hax.movement;

import com.minecraft.plugin.elite.nohax.manager.HaxPlayer;
import com.minecraft.plugin.elite.nohax.manager.alert.AlertManager;
import com.minecraft.plugin.elite.nohax.manager.alert.AlertType;
import com.minecraft.plugin.elite.nohax.manager.hax.PlayerMove;

public class FastLadderHack {

    public static void check(PlayerMove move) {
        HaxPlayer p = move.getPlayer();
        double index = 0.2;
        if ((move.isInLadder() || move.isInVine()) && !move.isCanSpeed())
            if (move.getSpeed() >= index && move.getJumpHeight() > index)
                AlertManager.set(p, AlertType.FASTLADDER, 5, ((move.getSpeed() - index) + (move.getJumpHeight() - index)) / 2D);
    }
}
