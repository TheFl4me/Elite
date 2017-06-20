package com.minecraft.plugin.elite.general.listeners;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.enums.Rank;
import com.minecraft.plugin.elite.general.api.events.stats.RankChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RankChangeEventListener implements Listener {

    @EventHandler
    public void onRankChange(RankChangeEvent e) {
        if (e.getTarget().isOnline()) {
            ePlayer p = ePlayer.get(e.getTarget().getName());
            p.setTag();
            if(p.isAdminMode()) {
                if (!e.getNewRank().hasPermission("egeneral.admin")) {
                    p.setAdminMode(false);
                }
            } else if(p.isWatching()) {
                if (!e.getNewRank().hasPermission("egeneral.watch")) {
                    p.setWatching(false);
                }
            }
            if(p.isInvis())
                p.setInvis(Rank.valueOf(e.getNewRank().ordinal() - 1));

            if(p.isBuilding() && !e.getNewRank().hasPermission("egeneral.build"))
                p.setBuilding(false);

            for (Player players : Bukkit.getOnlinePlayers()) {
                ePlayer all = ePlayer.get(players);
                if (all.isInvis()) {
                    if (e.getNewRank().ordinal() <= all.getInvisTo().ordinal())
                        p.getPlayer().hidePlayer(all.getPlayer());
                    if (e.getNewRank().ordinal() >= all.getInvisTo().ordinal())
                        p.getPlayer().showPlayer(all.getPlayer());
                }
            }

        }
    }
}