package com.minecraft.plugin.elite.nohax.manager.hax.combat;

import com.minecraft.plugin.elite.nohax.manager.HaxPlayer;
import com.minecraft.plugin.elite.nohax.manager.alert.AlertManager;
import com.minecraft.plugin.elite.nohax.manager.alert.AlertType;
import com.minecraft.plugin.elite.nohax.manager.hax.PlayerClick;

public class AutoClickHack {

    public static void check(PlayerClick click) {
        HaxPlayer p = click.getPlayer();
        double index = 40;
        long cps = 0;

        for(int i  = 1; i < p.getClickCount(); i++) {
            PlayerClick pastClick = p.getClicksAgo(i);
            if(pastClick != null)
                if(click.getTime() - pastClick.getTime() < 1000)
                    cps++;
        }

        if(cps > index)
            AlertManager.set(p, AlertType.AUTOCLICK, 10, (cps - index) / 100D);
    }
}
