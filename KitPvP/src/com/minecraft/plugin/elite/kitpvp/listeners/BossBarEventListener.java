package com.minecraft.plugin.elite.kitpvp.listeners;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.special.BossBar;
import com.minecraft.plugin.elite.kitpvp.manager.KitPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class BossBarEventListener implements Listener {

    @EventHandler
    public void showBarOnViewPlayerWithKit(PlayerMoveEvent e) {
        ePlayer p = ePlayer.get(e.getPlayer());
        for(Entity ent : p.getPlayer().getNearbyEntities(5,5,5)) {
            if(ent instanceof Player) {
                ePlayer z = ePlayer.get((Player) ent);
                KitPlayer kz = KitPlayer.get((Player) ent);
                if(p.getPlayer().hasLineOfSight(ent) && kz.hasKit() && !z.isAdminMode() && !z.isWatching()) {
                    BossBar bar = BossBar.get(p);
                    if(bar == null) {
                        BossBar newBar = new BossBar(p);
                        newBar.show(z.getName() + " - " + kz.getKit().getName(), 40);
                    } else {
                        bar.changeTo(z.getName() + " - " + kz.getKit().getName(), 40);
                    }
                }
            }
        }
    }
}
