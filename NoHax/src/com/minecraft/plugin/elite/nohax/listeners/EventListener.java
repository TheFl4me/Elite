package com.minecraft.plugin.elite.nohax.listeners;

import com.minecraft.plugin.elite.general.api.events.stats.RankChangeEvent;
import com.minecraft.plugin.elite.general.punish.PunishManager;
import com.minecraft.plugin.elite.nohax.NoHax;
import com.minecraft.plugin.elite.nohax.manager.HaxPlayer;
import com.minecraft.plugin.elite.nohax.manager.alert.AlertManager;
import com.minecraft.plugin.elite.nohax.manager.SpamCheck;
import com.minecraft.plugin.elite.nohax.manager.hax.PlayerClick;
import com.minecraft.plugin.elite.nohax.manager.hax.PlayerDamage;
import com.minecraft.plugin.elite.nohax.manager.hax.PlayerMove;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

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
        if (p != null)
            p.leave();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e) {
        HaxPlayer p = HaxPlayer.get(e.getPlayer());
        if (p != null)
            p.join();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent e) {
        HaxPlayer p = HaxPlayer.get(e.getPlayer());
        if (p.getPlayer().hasPermission("enohax.alerts"))
            p.showAlerts(true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        HaxPlayer p = HaxPlayer.get(e.getPlayer());
        if (p.isCombatLog())
            p.getPlayer().setHealth(0);
        p.getPlayer().getInventory().setArmorContents(null);
        p.getPlayer().getInventory().clear();
        if(p.isCombatLog()) {
            p.getCombatLogTask().cancel();
            p.setCombatLogTask(null);
        }
        AlertManager.clear(p);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void storeMoves(PlayerMoveEvent e) {
        PlayerMove.store(e);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void storeDamage(EntityDamageEvent e) {
        PlayerDamage.store(e);
        if(e.getEntity() instanceof Player) {
            HaxPlayer p = HaxPlayer.get((Player) e.getEntity());
            p.invalidate(40);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (!PunishManager.isMuted(e.getPlayer().getUniqueId()))
            SpamCheck.onPlayerChat(e);
        while (e.getMessage().contains("  "))
            e.setMessage(e.getMessage().replaceAll(" {2}", " "));
    }

    @EventHandler
    public void checkSpamOnCommand(PlayerCommandPreprocessEvent e) {
        HaxPlayer p = HaxPlayer.get(e.getPlayer());
        String[] blacklist = new String[]{"/msg", "/tell", "/r", "/reply", "/message", "/answer"};
        for (String command : blacklist) {
            if (e.getMessage().startsWith(command) && !PunishManager.isMuted(p.getUniqueId()))
                SpamCheck.onPlayerCommand(e);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerFightCheckCombatLog(EntityDamageByEntityEvent e) {
        if (e.isCancelled() || e.getDamage() == 0)
            return;
        if ((e.getDamager() instanceof Player) && (e.getEntity() instanceof Player)) {
            HaxPlayer hitter = HaxPlayer.get((Player) e.getDamager());
            HaxPlayer target = HaxPlayer.get((Player) e.getEntity());
            if (!hitter.isAdminMode() && !hitter.isWatching() && !target.isAdminMode() && !target.isWatching()) {
                hitter.setCombatLog();
                hitter.setCombatLog();
            }
        }
    }

    @EventHandler
    public void onRankChange(RankChangeEvent e) {
        if (e.getTarget().isOnline()) {
            HaxPlayer p = HaxPlayer.get(e.getTarget().getUniqueId());
            if (!e.getNewRank().hasPermission("enohax.alerts"))
                p.showAlerts(false);
            else
                p.showAlerts(true);
        }
    }

    @EventHandler
    public void onTp(PlayerTeleportEvent e) {
        HaxPlayer p = HaxPlayer.get(e.getPlayer());
        p.setLastOnGround(null);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void storeHitClick(PlayerInteractEvent e) {
        PlayerClick.storeHitClick(e);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void storeInvClick(InventoryClickEvent e) {
        PlayerClick.storeInvClick(e);
    }
}