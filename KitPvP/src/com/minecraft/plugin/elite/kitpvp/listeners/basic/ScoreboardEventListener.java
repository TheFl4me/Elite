package com.minecraft.plugin.elite.kitpvp.listeners.basic;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ScoreboardEventListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void setTagOnJoin(PlayerJoinEvent e) {
        GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
        p.setupRanks();
        p.setTag();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void clearTagOnQuit(PlayerQuitEvent e) {
        GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
        p.clearTag();
    }
}
