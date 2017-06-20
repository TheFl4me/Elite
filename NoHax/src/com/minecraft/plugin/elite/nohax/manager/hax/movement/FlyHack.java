package com.minecraft.plugin.elite.nohax.manager.hax.movement;

import com.minecraft.plugin.elite.nohax.manager.HaxPlayer;
import com.minecraft.plugin.elite.nohax.manager.alert.AlertManager;
import com.minecraft.plugin.elite.nohax.manager.alert.AlertType;
import com.minecraft.plugin.elite.nohax.manager.hax.PlayerMove;

public class FlyHack {

    public static void check(PlayerMove move) {
        HaxPlayer p = move.getPlayer();
        int chestplate = 102;
        if (!move.isInLadder() && !move.isNextToBlock()) {
            double index = 1.4;
            if (move.getJumpHeight() > index && move.getVerticalDistance() >= 0) {
                AlertManager.set(p, AlertType.FLY, 10, (move.getJumpHeight() - index) / 10D);
            } else if (move.getJumpHeight() > index) {
                boolean isValid = true;
                for (int i = 1; i < p.getLastOnGroundMovesAgo(); i++) {
                    PlayerMove other = p.getMovesAgo(i);
                    if (other != null) {
                        if (other.isInLadder() || !other.isValid()) {
                            isValid = false;
                            break;
                        }
                    }
                }
                if (isValid)
                    AlertManager.set(p, AlertType.HIGHJUMP, 5, (move.getJumpHeight() - index) / 10D);
            } else if (move.getHeightAboveGround() > 3) {
                boolean isGliding = true;
                for (int i = 2; i < 20; i++) {
                    PlayerMove other = p.getMovesAgo(i);
                    if (other == null)
                        return;
                    if (Math.abs(other.getSpeed()) != Math.abs(move.getSpeed()) || other.isOnGround() || other.isInLadder() || other.getHeightAboveGround() <= 3 || move.getLocation().getY() == other.getLocation().getY() || !other.isValid()) {
                        isGliding = false;
                        break;
                    }
                }
                boolean isHovering = true;
                for (int i = 2; i < 20; i++) {
                    PlayerMove other = p.getMovesAgo(i);
                    if (other == null)
                        return;
                    if (Math.abs(other.getHeightAboveGround() - move.getHeightAboveGround()) > 0.01 || other.isOnGround() || other.isInLadder() || !other.isValid()) {
                        isHovering = false;
                        break;
                    }
                }
                if (isGliding) {
                    AlertManager.set(p, AlertType.GLIDE, 2);
                } else if (isHovering) {
                    AlertManager.set(p, AlertType.HOVER, 3);
                }
            }
        }
    }
}