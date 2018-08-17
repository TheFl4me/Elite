package com.minecraft.plugin.elite.general.antihack.hacks.combat;

import com.minecraft.plugin.elite.general.antihack.alert.AlertManager;
import com.minecraft.plugin.elite.general.antihack.alert.AlertType;
import com.minecraft.plugin.elite.general.antihack.hacks.PlayerAttack;
import com.minecraft.plugin.elite.general.antihack.hacks.PlayerMove;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class AimbotHack {

    public static void check(PlayerAttack attack) {
        GeneralPlayer attacker = attack.getAttacker();
        Entity target = attack.getTarget();

        final float angle = attacker.getLineOfSightAngleTo(target);
        final double min = Math.toRadians(100);
        final double max = Math.PI;

        if(attacker.getPlayer().getLocation().distance(target.getLocation()) > 1) {
            if(angle >= min && angle <= max) {
                AlertManager.set(attacker, AlertType.FORCEFIELD, (angle - min) / (max - min));
            } else {
                PlayerMove moveBefore = attacker.getMovesAgo(2);
                if (moveBefore != null) {
                    Vector before = moveBefore.getEyeLocation().getDirection().normalize();
                    float difference = before.angle(attacker.getPlayer().getEyeLocation().getDirection().normalize());
                    if (difference >= min && difference <= max) {
                        AlertManager.set(attacker, AlertType.AIMBOT, (difference - min) / (max - min));
                    }
                }
            }
        }
    }
}
