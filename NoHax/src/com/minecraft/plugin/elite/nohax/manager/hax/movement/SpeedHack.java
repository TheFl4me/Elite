package com.minecraft.plugin.elite.nohax.manager.hax.movement;

import com.minecraft.plugin.elite.nohax.manager.HaxPlayer;
import com.minecraft.plugin.elite.nohax.manager.alert.AlertManager;
import com.minecraft.plugin.elite.nohax.manager.alert.AlertType;
import com.minecraft.plugin.elite.nohax.manager.hax.PlayerMove;
import org.bukkit.potion.PotionEffectType;

public class SpeedHack {

    public static void check(PlayerMove move) {
        HaxPlayer p = move.getPlayer();
        double index = 0.7;
        if (!p.getPlayer().hasPotionEffect(PotionEffectType.SPEED) && !p.isCanSpeed() && !move.isOnIce())
            if (move.getHorizontalDistance() > index)
                AlertManager.set(p, AlertType.SPEED, 5, move.getHorizontalDistance() - index);
    }
}