package com.minecraft.plugin.elite.nohax.listeners.basic;

import com.minecraft.plugin.elite.general.api.events.stats.RankChangeEvent;
import com.minecraft.plugin.elite.nohax.NoHax;
import com.minecraft.plugin.elite.nohax.manager.HaxPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinQuitEventListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent e) {
        HaxPlayer p = HaxPlayer.get(e.getPlayer());
        if (p.getPlayer().hasPermission(NoHax.ALERTS_PERM))
            p.showAlerts(true);
    }

    @EventHandler
    public void onRankChange(RankChangeEvent e) {
        if (e.getTarget().isOnline()) {
            HaxPlayer p = HaxPlayer.get(e.getTarget().getUniqueId());
            if (!e.getNewRank().hasPermission(NoHax.ALERTS_PERM))
                p.showAlerts(false);
            else
                p.showAlerts(true);
        }
    }
}
