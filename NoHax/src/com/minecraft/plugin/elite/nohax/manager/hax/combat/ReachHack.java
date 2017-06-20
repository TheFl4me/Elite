package com.minecraft.plugin.elite.nohax.manager.hax.combat;

import com.minecraft.plugin.elite.nohax.manager.HaxPlayer;
import com.minecraft.plugin.elite.nohax.manager.alert.AlertManager;
import com.minecraft.plugin.elite.nohax.manager.alert.AlertType;
import com.minecraft.plugin.elite.nohax.manager.hax.PlayerDamage;
import org.bukkit.Location;

public class ReachHack {

    public static void check(PlayerDamage e) {
        HaxPlayer attacker = e.getAttacker();
        HaxPlayer target = e.getTarget();

        Location aLoc = attacker.getPlayer().getLocation();
        Location tLoc = target.getPlayer().getLocation();
        double dist = aLoc.distance(tLoc);
        final double index = 4.5;
        if(dist > index && e.getDamage() > 0)
            AlertManager.set(attacker, AlertType.REACH, (dist - index) / 10D);
    }
}
