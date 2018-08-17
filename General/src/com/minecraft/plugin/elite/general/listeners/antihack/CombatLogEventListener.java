package com.minecraft.plugin.elite.general.listeners.antihack;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CombatLogEventListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerQuit(PlayerQuitEvent e) {
        GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
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
            GeneralPlayer hitter = GeneralPlayer.get((Player) e.getDamager());
            GeneralPlayer target = GeneralPlayer.get((Player) e.getEntity());
            if (!hitter.isAdminMode() && !hitter.isWatching() && !target.isAdminMode() && !target.isWatching()) {
                hitter.setCombatLog();
                target.setCombatLog();
            }
        }
    }
}
