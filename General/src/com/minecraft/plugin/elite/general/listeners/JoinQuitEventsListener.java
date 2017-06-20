package com.minecraft.plugin.elite.general.listeners;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.special.clan.Clan;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitEventsListener implements Listener {

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        final ePlayer p = ePlayer.get(e.getPlayer());
        Server server = Server.get();
        p.clear();
        p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.WELCOME)
                .replaceAll("%server", server.getType() + " - " + server.getName()));
        p.getPlayer().setGameMode(GameMode.SURVIVAL);
        e.setJoinMessage(null);
        if (p.canAdminMode()) {
            p.setAdminMode(true);
            p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.GAMEMODE_SET_YOU)
                    .replaceAll("%gm", "ADMIN"));
        }
        for (Player players : Bukkit.getOnlinePlayers()) {
            ePlayer all = ePlayer.get(players);
            if (all.isInvis())
                if (p.getRank().ordinal() <= all.getInvisTo().ordinal())
                    p.getPlayer().hidePlayer(all.getPlayer());
        }
        for (Player players : Bukkit.getOnlinePlayers()) {
            ePlayer all = ePlayer.get(players);
            all.setHeaderFooter();
            Clan allClan = all.getClan();
            Clan clan = p.getClan();
            if (allClan != null && clan != null && clan.equals(allClan) && !p.isInvis())
                all.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.JOINED)
                        .replaceAll("%p", p.getName()));
        }
        if (!p.hasAgreed()) {
            p.pendingAgree(true);
            p.sendTitle(GeneralLanguage.RULES_TITLE, 1, 20, 1);
            p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.RULES_AGREED_NOT)
                    .replaceAll("%domain", server.getDomain()));
        }

        if (p.canSpy())
            p.setSpy(true);

        Bukkit.getScheduler().runTaskTimer(General.getPlugin(), () -> {
            if (!p.isAFK() && !p.isPendingAgree())
                p.setPlayTime(p.getPlayTime() + 1000);
        }, 0, 20);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        ePlayer p = ePlayer.get(e.getPlayer());
        p.getPlayer().setFlySpeed((float) 0.1);
        p.getPlayer().setWalkSpeed((float) 0.2);
        e.setQuitMessage(null);

        for (Player players : Bukkit.getOnlinePlayers()) {
            ePlayer all = ePlayer.get(players);
            Clan allClan = all.getClan();
            Clan clan = p.getClan();
            if (allClan != null && clan != null && clan.equals(allClan) && !p.isInvis()) {
                all.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.LEFT)
                        .replaceAll("%p", p.getName()));
            }
        }
    }
}