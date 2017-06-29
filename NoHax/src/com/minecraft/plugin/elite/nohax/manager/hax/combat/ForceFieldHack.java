package com.minecraft.plugin.elite.nohax.manager.hax.combat;

import com.minecraft.plugin.elite.nohax.manager.HaxPlayer;
import com.minecraft.plugin.elite.nohax.manager.alert.AlertManager;
import com.minecraft.plugin.elite.nohax.manager.alert.AlertType;
import com.minecraft.plugin.elite.nohax.manager.hax.PlayerDamage;
import com.minecraft.plugin.elite.nohax.manager.hax.PlayerMove;
import org.bukkit.util.Vector;

public class ForceFieldHack {

    public static void check(PlayerDamage e) {
        HaxPlayer attacker = e.getAttacker();
        HaxPlayer target = e.getTarget();

        final float angle = attacker.getLineOfSightAngleTo(target.getPlayer());
        final double min = Math.toRadians(100);
        final double max = Math.PI;

        if(attacker.getPlayer().getLocation().distance(target.getPlayer().getLocation()) > 1) {
            if(angle >= min && angle <= max) {
                AlertManager.set(attacker, AlertType.KILLAURA, (angle - min) / (max - min));
            } else {
                PlayerMove moveBefore = attacker.getMovesAgo(2);
                if (moveBefore != null) {
                    Vector before = moveBefore.getEyeLocation().getDirection().normalize();
                    float difference = before.angle(attacker.getPlayer().getEyeLocation().getDirection().normalize());
                    if (difference >= min && difference <= max) {
                        AlertManager.set(attacker, AlertType.FORCEFIELD, (difference - min) / (max - min));
                    }
                }
            }
        }
    }
}
