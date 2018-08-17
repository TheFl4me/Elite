package com.minecraft.plugin.elite.general.antihack.hacks.combat;

import com.minecraft.plugin.elite.general.antihack.alert.AlertManager;
import com.minecraft.plugin.elite.general.antihack.alert.AlertType;
import com.minecraft.plugin.elite.general.antihack.hacks.PlayerClick;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;

public class AutoClickHack {

    public static void check(PlayerClick click) {
        GeneralPlayer p = click.getPlayer();
        final double index = 40;
        final long time = System.currentTimeMillis();
        long cps = 0;
        for(PlayerClick pastClick : p.getClicks())
            if(pastClick != null)
                if(time - pastClick.getTime() <= 1000 && pastClick.isValid())
                    cps++;

        if(cps > index)
            AlertManager.set(p, AlertType.AUTOCLICK, 10, (cps - index) / 100D);
    }
}
