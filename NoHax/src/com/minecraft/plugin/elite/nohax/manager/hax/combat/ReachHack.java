package com.minecraft.plugin.elite.nohax.manager.hax.combat;

import com.minecraft.plugin.elite.nohax.manager.HaxPlayer;
import com.minecraft.plugin.elite.nohax.manager.alert.AlertManager;
import com.minecraft.plugin.elite.nohax.manager.alert.AlertType;
import com.minecraft.plugin.elite.nohax.manager.hax.PlayerAttack;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class ReachHack {

    public static void check(PlayerAttack e) {
        HaxPlayer attacker = e.getAttacker();
        Entity target = e.getTarget();

        Location aLoc = attacker.getPlayer().getLocation();
        Location tLoc = target.getLocation();
        double dist = aLoc.distance(tLoc);
        final double index = 4.5;
        if(dist > index && e.getDamage() > 0)
            AlertManager.set(attacker, AlertType.REACH, (dist - index) / 10D);
    }
}
