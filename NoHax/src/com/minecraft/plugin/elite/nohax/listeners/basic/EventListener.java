package com.minecraft.plugin.elite.nohax.listeners.basic;

import com.minecraft.plugin.elite.nohax.manager.HaxPlayer;
import com.minecraft.plugin.elite.nohax.manager.alert.AlertManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent e) {
        HaxPlayer.login(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLoginCanceled(PlayerLoginEvent e) {
        if (e.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            HaxPlayer p = HaxPlayer.get(e.getPlayer());
            if (p != null)
                p.logout();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void cleanUpPlayerHashMap(PlayerQuitEvent e) {
        HaxPlayer p = HaxPlayer.get(e.getPlayer());
        AlertManager.clear(p);
        if (p != null)
            p.leave();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e) {
        HaxPlayer p = HaxPlayer.get(e.getPlayer());
        if (p != null)
            p.join();
    }
}