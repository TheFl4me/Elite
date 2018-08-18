package com.minecraft.plugin.elite.general.listeners;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.enums.Achievement;
import com.minecraft.plugin.elite.general.api.enums.Rank;
import com.minecraft.plugin.elite.general.api.events.stats.PrestigeChangeEvent;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AchievementEventListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFirstMsg(AsyncPlayerChatEvent e) {
        GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
        if(!p.hasAchievement(Achievement.FIRST_MSG) && !e.isCancelled())
            p.giveAchievement(Achievement.FIRST_MSG);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFirstKill(PlayerDeathEvent e) {
        if(e.getEntity().getKiller() instanceof Player) {
            GeneralPlayer p = GeneralPlayer.get(e.getEntity().getKiller());
            if(!p.getUniqueId().equals(e.getEntity().getUniqueId()))
                if(!p.hasAchievement(Achievement.KILLER))
                    p.giveAchievement(Achievement.KILLER);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFirstPrestige(PrestigeChangeEvent e) {
        GeneralPlayer p = e.getPlayer();
        if(!p.hasAchievement(Achievement.PRESTIGE) && e.getNewPrestige() == 1 && e.getOldPrestige() == 0)
            p.giveAchievement(Achievement.PRESTIGE);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMaxPrestige(PrestigeChangeEvent e) {
        GeneralPlayer p = e.getPlayer();
        if(!p.hasAchievement(Achievement.MAX_PRESTIGE) && e.getNewPrestige() == 10)
            p.giveAchievement(Achievement.MAX_PRESTIGE);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onStaffKill(PlayerDeathEvent e) {
        if(e.getEntity().getKiller() instanceof Player) {
            GeneralPlayer killer = GeneralPlayer.get(e.getEntity().getKiller());
            GeneralPlayer target = GeneralPlayer.get(e.getEntity().getPlayer());
            if(target.isBuilder() && !killer.hasAchievement(Achievement.STAFF_KILL) && !killer.getUniqueId().equals(target.getUniqueId()))
                killer.giveAchievement(Achievement.STAFF_KILL);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAdminKill(PlayerDeathEvent e) {
        if(e.getEntity().getKiller() instanceof Player) {
            GeneralPlayer killer = GeneralPlayer.get(e.getEntity().getKiller());
            GeneralPlayer target = GeneralPlayer.get(e.getEntity().getPlayer());
            if(target.isAdmin() && !killer.hasAchievement(Achievement.ADMIN_KIll) && !killer.getUniqueId().equals(target.getUniqueId()))
                killer.giveAchievement(Achievement.ADMIN_KIll);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMediaKill(PlayerDeathEvent e) {
        if(e.getEntity().getKiller() instanceof Player) {
            GeneralPlayer killer = GeneralPlayer.get(e.getEntity().getKiller());
            GeneralPlayer target = GeneralPlayer.get(e.getEntity().getPlayer());
            if(target.getRank() == Rank.MEDIA && !killer.hasAchievement(Achievement.MEDIA_KILL) && !killer.getUniqueId().equals(target.getUniqueId()))
                killer.giveAchievement(Achievement.MEDIA_KILL);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMaxPrestigeKill(PlayerDeathEvent e) {
        if(e.getEntity().getKiller() instanceof Player) {
            GeneralPlayer killer = GeneralPlayer.get(e.getEntity().getKiller());
            GeneralPlayer target = GeneralPlayer.get(e.getEntity().getPlayer());
            if(target.isMasterPrestige() && !killer.hasAchievement(Achievement.MAX_PRESTIGE_KILL) && !killer.getUniqueId().equals(target.getUniqueId()))
                killer.giveAchievement(Achievement.MAX_PRESTIGE_KILL);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBloodthirsty(PlayerDeathEvent e) {
        if(e.getEntity().getKiller() instanceof Player) {
            GeneralPlayer p = GeneralPlayer.get(e.getEntity().getKiller());
            if(p.getKillStreak() == 5 && !p.hasAchievement(Achievement.BLOODTHIRSTY))
                p.giveAchievement(Achievement.BLOODTHIRSTY);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInvincible(PlayerDeathEvent e) {
        if(e.getEntity().getKiller() instanceof Player) {
            GeneralPlayer p = GeneralPlayer.get(e.getEntity().getKiller());
            if(p.getKillStreak() == 10 && !p.hasAchievement(Achievement.INVINCIBLE))
                p.giveAchievement(Achievement.INVINCIBLE);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onNuclear(PlayerDeathEvent e) {
        if(e.getEntity().getKiller() instanceof Player) {
            GeneralPlayer p = GeneralPlayer.get(e.getEntity().getKiller());
            if(p.getKillStreak() == 25 && !p.hasAchievement(Achievement.NUCLEAR))
                p.giveAchievement(Achievement.NUCLEAR);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGod(PlayerDeathEvent e) {
        if(e.getEntity().getKiller() instanceof Player) {
            GeneralPlayer p = GeneralPlayer.get(e.getEntity().getKiller());
            if(p.getKillStreak() == 50 && !p.hasAchievement(Achievement.GOD))
                p.giveAchievement(Achievement.GOD);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLastResort(PlayerDeathEvent e) {
        if(e.getEntity().getKiller() instanceof Player) {
            GeneralPlayer p = GeneralPlayer.get(e.getEntity().getKiller());
            if(p.getPlayer().getHealth() <= 1  && p.getPlayer().getHealth() > 0 && !p.getPlayer().getInventory().contains(Material.MUSHROOM_SOUP) && !p.hasAchievement(Achievement.LAST_RESORT) && !p.getUniqueId().equals(e.getEntity().getUniqueId()))
                p.giveAchievement(Achievement.LAST_RESORT);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLongShot(PlayerDeathEvent e) {
        GeneralPlayer target = GeneralPlayer.get(e.getEntity().getPlayer());
        EntityDamageEvent dmg = target.getPlayer().getLastDamageCause();
        if(dmg instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) dmg;
            if(event.getDamager() instanceof Arrow) {
                Arrow a = (Arrow)event.getDamager();
                if(a.getShooter() instanceof Player) {
                    GeneralPlayer killer = GeneralPlayer.get((Player) a.getShooter());
                    if(!killer.hasAchievement(Achievement.LONG_SHOT) && !killer.getUniqueId().equals(target.getUniqueId()))
                        killer.giveAchievement(Achievement.LONG_SHOT);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBackStab(PlayerDeathEvent e) {
        if(e.getEntity().getKiller() instanceof Player) {
            GeneralPlayer killer = GeneralPlayer.get(e.getEntity().getKiller());
            GeneralPlayer target = GeneralPlayer.get(e.getEntity().getPlayer());
            if(killer.getPlayer().getLocation().getDirection().normalize().equals(target.getPlayer().getLocation().getDirection()) && killer.hasAchievement(Achievement.BACK_STAB) && !killer.getUniqueId().equals(target.getUniqueId()))
                killer.giveAchievement(Achievement.BACK_STAB);
        }
    }
}
