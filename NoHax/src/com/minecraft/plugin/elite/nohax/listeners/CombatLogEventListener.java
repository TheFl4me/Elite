package com.minecraft.plugin.elite.nohax.listeners;

import com.minecraft.plugin.elite.nohax.manager.HaxPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CombatLogEventListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerQuit(PlayerQuitEvent e) {
        HaxPlayer p = HaxPlayer.get(e.getPlayer());
        if (p.isCombatLog())
            p.getPlayer().setHealth(0);
        if(p.isCombatLog()) {
            p.getCombatLogTask().cancel();
            p.setCombatLogTask(null);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerFightCheckCombatLog(EntityDamageByEntityEvent e) {
        if (e.isCancelled() || e.getDamage() == 0)
            return;
        if ((e.getDamager() instanceof Player) && (e.getEntity() instanceof Player)) {
            HaxPlayer hitter = HaxPlayer.get((Player) e.getDamager());
            HaxPlayer target = HaxPlayer.get((Player) e.getEntity());
            if (!hitter.isAdminMode() && !hitter.isWatching() && !target.isAdminMode() && !target.isWatching()) {
                hitter.setCombatLog();
                target.setCombatLog();
            }
        }
    }
}
