package com.minecraft.plugin.elite.general.antihack.hacks.combat;

import com.minecraft.plugin.elite.general.antihack.alert.AlertManager;
import com.minecraft.plugin.elite.general.antihack.alert.AlertType;
import com.minecraft.plugin.elite.general.antihack.hacks.PlayerAttack;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class ReachHack {

    public static void check(PlayerAttack e) {
        GeneralPlayer attacker = e.getAttacker();
        Entity target = e.getTarget();

        Location aLoc = attacker.getPlayer().getLocation();
        Location tLoc = target.getLocation();
        double dist = aLoc.distance(tLoc);
        final double index = 4.5;
        if(dist > index && e.getDamage() > 0)
            AlertManager.set(attacker, AlertType.REACH, (dist - index) / 10D);
    }
}
