package com.minecraft.plugin.elite.kitpvp.listeners;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.special.BossBar;
import com.minecraft.plugin.elite.kitpvp.manager.KitPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BossBarEventListener implements Listener {

    @EventHandler
    public void showBarOnViewPlayerWithKit(PlayerMoveEvent e) {
        ePlayer p = ePlayer.get(e.getPlayer());
        ePlayer nearest = null;
        List<ePlayer> possible = new ArrayList<>();
        for(Entity ent : p.getPlayer().getNearbyEntities(3,3,3)) {
            if(ent instanceof Player) {
                ePlayer z = ePlayer.get((Player) ent);

                Vector p2p = z.getPlayer().getLocation().toVector().subtract(p.getPlayer().getLocation().toVector()).normalize();
                Vector sight = p.getPlayer().getEyeLocation().getDirection().normalize();
                float angle = sight.angle(p2p);
                boolean lineOfSight = false;
                if(angle < Math.toRadians(20))
                    lineOfSight = true;

                if(lineOfSight) {
                    possible.add(z);
                }
            }
        }

        for(ePlayer near : possible)
            if(nearest == null || p.getPlayer().getLocation().distance(near.getPlayer().getLocation()) < p.getPlayer().getLocation().distance(nearest.getPlayer().getLocation()))
                nearest = near;

        if(nearest != null) {
            KitPlayer kz = KitPlayer.get(nearest.getUniqueId());
            if(kz.hasKit() && !nearest.isAdminMode() && !nearest.isWatching()) {
                BossBar bar = BossBar.get(p);
                if(bar == null) {
                    BossBar newBar = new BossBar(p);
                    newBar.show(nearest.getName() + " - " + kz.getKit().getName(), 10);
                } else {
                    bar.changeTo(nearest.getName() + " - " + kz.getKit().getName(), 10);
                }
            }
        }
    }
}
