package com.minecraft.plugin.elite.general.antihack.hacks.movement;

import com.minecraft.plugin.elite.general.antihack.alert.AlertManager;
import com.minecraft.plugin.elite.general.antihack.alert.AlertType;
import com.minecraft.plugin.elite.general.antihack.hacks.PlayerMove;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import org.bukkit.potion.PotionEffectType;

public class SpeedHack {

    public static void check(PlayerMove move) {
        GeneralPlayer p = move.getPlayer();
        double index = 0.7;
        if (!p.getPlayer().hasPotionEffect(PotionEffectType.SPEED) && !p.isCanSpeed() && !move.isOnIce())
            if (move.getHorizontalDistance() > index)
                AlertManager.set(p, AlertType.SPEED, 5, move.getHorizontalDistance() - index);
    }
}