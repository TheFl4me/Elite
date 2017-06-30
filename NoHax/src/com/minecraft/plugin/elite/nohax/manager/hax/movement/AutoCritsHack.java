package com.minecraft.plugin.elite.nohax.manager.hax.movement;

import com.minecraft.plugin.elite.nohax.manager.HaxPlayer;
import com.minecraft.plugin.elite.nohax.manager.alert.AlertManager;
import com.minecraft.plugin.elite.nohax.manager.alert.AlertType;
import com.minecraft.plugin.elite.nohax.manager.hax.PlayerMove;

public class AutoCritsHack {

    public static void check(PlayerMove move) {
        HaxPlayer p = move.getPlayer();
        double jumpHeight = move.getJumpHeight();
        final double min = 0.1;
        final double max = 0.21;

        if (!move.isUnderBlock() && !move.isInLadder() && !move.isNextToBlock()  && !move.isOnBlockEdge() && move.isOnGround() && !move.isOnSlab() && p.getLastOnGround().getLocation().getY() == move.getLocation().getY()) {
            for (int i = 1; i < p.getLastOnGroundMovesAgo(); i++) {
                PlayerMove other = p.getMovesAgo(i);
                if (!other.isValid() || other.isNextToBlock() || other.isOnBlockEdge() || other.isInLadder() || other.isUnderBlock() || other.isOnSlab())
                    return;
                if (other.getJumpHeight() > jumpHeight) {
                    jumpHeight = other.getJumpHeight();
                }
            }
            if (jumpHeight <= max && jumpHeight >= min) {
                AlertManager.set(p, AlertType.AUTOCRITS, 3, (jumpHeight - min) / (max - min));
            }
        }
    }
}
