package com.minecraft.plugin.elite.general.listeners;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PvPToggleEventListener implements Listener {

    @EventHandler
    public void onPvPToggleDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            GeneralPlayer p = GeneralPlayer.get((Player) e.getEntity());
            Server server = Server.get();
            if (!server.pvpIsEnabled()) {
                p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PVP_DISABLED_ON_HIT);
                e.setCancelled(true);
            }
        }
    }
}