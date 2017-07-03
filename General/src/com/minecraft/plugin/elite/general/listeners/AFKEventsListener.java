package com.minecraft.plugin.elite.general.listeners;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class AFKEventsListener implements Listener {

    @EventHandler
    public void onPlayerMoveRemoveAFK(PlayerMoveEvent e) {
        ePlayer p = ePlayer.get(e.getPlayer());
        if (p.isAFK()) {
            p.setAFK(false);
            p.sendMessage(GeneralLanguage.AFK_FALSE);
        }
        if (p.isPendingAFK()) {
            p.stopAFKPendingTimer();
            p.startAFKPendingTimer();
        }
    }

    @EventHandler
    public void onChatRemoveAFK(AsyncPlayerChatEvent e) {
        ePlayer p = ePlayer.get(e.getPlayer());
        if (p.isAFK()) {
            p.setAFK(false);
            p.sendMessage(GeneralLanguage.AFK_FALSE);
        }
        if (p.isPendingAFK()) {
            p.stopAFKPendingTimer();
            p.startAFKPendingTimer();
        }
    }

    @EventHandler
    public void onCommandRemoveAFK(PlayerCommandPreprocessEvent e) {
        ePlayer p = ePlayer.get(e.getPlayer());
        String message = e.getMessage().toLowerCase();
        if (p.isAFK()) {
            p.setAFK(false);
            p.sendMessage(GeneralLanguage.AFK_FALSE);
        }
        if (p.isPendingAFK()) {
            p.stopAFKPendingTimer();
            p.startAFKPendingTimer();
        }
    }

    @EventHandler
    public void onLeaveRemoveAFK(PlayerQuitEvent e) {
        ePlayer p = ePlayer.get(e.getPlayer());
        if (p.isPendingAFK())
            p.stopAFKPendingTimer();
    }

    @EventHandler
    public void onJoinStartAFKTimer(PlayerJoinEvent e) {
        ePlayer p = ePlayer.get(e.getPlayer());
        if (p.getPlayer().hasPermission("egeneral.afk"))
            p.startAFKPendingTimer();
    }
}