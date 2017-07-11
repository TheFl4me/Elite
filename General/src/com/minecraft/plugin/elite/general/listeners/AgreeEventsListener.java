package com.minecraft.plugin.elite.general.listeners;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class AgreeEventsListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMove(PlayerMoveEvent e) {
        Server server = Server.get();
        GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
        if (p.isPendingAgree()) {
            p.sendTitle(GeneralLanguage.RULES_TITLE, 1, 20, 1);
            p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.RULES_AGREED_NOT)
                    .replaceAll("%domain", server.getDomain()));
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelInteractOnPendingAgree(PlayerInteractEvent e) {
        GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
        if (p.isPendingAgree())
            e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelChatOnPendingAgree(AsyncPlayerChatEvent e) {
        GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
        if (p.isPendingAgree())
            e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelCmdOnPendingAgree(PlayerCommandPreprocessEvent e) {
        GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
        if (p.isPendingAgree() && !e.getMessage().startsWith("/agree"))
            e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelDamageOnPendingAgree(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            GeneralPlayer p = GeneralPlayer.get((Player) e.getEntity());
            if (p.isPendingAgree())
                e.setCancelled(true);
        }
    }
}
