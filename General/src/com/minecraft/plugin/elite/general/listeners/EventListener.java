package com.minecraft.plugin.elite.general.listeners;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.antihack.alert.AlertManager;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.events.stats.LevelChangeEvent;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent e) {
        GeneralPlayer.login(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLoginCanceled(PlayerLoginEvent e) {
        if (e.getResult() != Result.ALLOWED) {
            GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
            if (p != null)
                p.logout();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void cleanUpPlayerHashMap(PlayerQuitEvent e) {
        GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
        AlertManager.clear(p);
        if (p != null)
            p.leave();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e) {
        GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
        if (p != null)
            p.join();
    }

    @EventHandler
    public void onLevelUp(LevelChangeEvent e) {
        GeneralPlayer p = e.getPlayer();
        if (e.isLevelUp()) {
            p.setTokens(p.getTokens() + 1);
            p.sendTitle(p.getLanguage().get(GeneralLanguage.LEVEL_UP), 1, 20, 3);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void cancelDeathScreen(PlayerDeathEvent e) {
        Bukkit.getScheduler().runTaskLater(General.getPlugin(), () -> {
            CraftPlayer craftplayer = (CraftPlayer) e.getEntity();
            PlayerConnection connection = craftplayer.getHandle().playerConnection;
            PacketPlayInClientCommand packet = new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN);
            connection.a(packet);
        }, 10);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        p.getPlayer().getWorld().playEffect(p.getPlayer().getLocation(), Effect.SMOKE, 0);
    }

    @EventHandler
    public void onItemDrop(ItemSpawnEvent e) {
        Entity ent = e.getEntity();
        if(ent instanceof Item) {
            Bukkit.getScheduler().runTaskLater(General.getPlugin(), () -> {
                if(ent != null)
                    ent.remove();
            }, 600);
        }
    }

    @EventHandler
    public void manipulate(PlayerArmorStandManipulateEvent e) {
        if(!e.getRightClicked().isVisible())
            e.setCancelled(true);
    }
}
