package com.minecraft.plugin.elite.general.listeners;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.special.PlayerHit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class DamageEventListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void storeDamageOnHit(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player && e.getDamage() > 0 && !e.isCancelled()) {
            ePlayer p = ePlayer.get((Player) e.getEntity());
            UUID uuid = null;
            if(e.getDamager() instanceof Player) {
                uuid = e.getDamager().getUniqueId();
            } else if(e.getDamager() instanceof Projectile && ((Projectile) e.getDamager()).getShooter() instanceof Player) {
                uuid = ((Player) ((Projectile) e.getDamager()).getShooter()).getUniqueId();
            }
            if(uuid != null)  {
                ePlayer z = ePlayer.get(uuid);
                if(!z.isAdminMode() && !z.isWatching()) {
                    PlayerHit hit = new PlayerHit(uuid, e.getDamage());
                    p.saveHit(hit);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void clearOnDeath(PlayerDeathEvent e) {
        ePlayer p = ePlayer.get(e.getEntity());
        p.clearHits();
    }
}
