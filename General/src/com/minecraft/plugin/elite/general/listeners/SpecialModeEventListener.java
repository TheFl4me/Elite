package com.minecraft.plugin.elite.general.listeners;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.events.mode.ModeChangeEvent;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Iterator;
import java.util.List;

public class SpecialModeEventListener implements Listener {

    @EventHandler
    public void clickOpenInv(PlayerInteractEntityEvent e) {
        ePlayer p = ePlayer.get(e.getPlayer());
        if (e.getRightClicked() instanceof Player) {
            if (p.isAdminMode() && p.getPlayer().getGameMode() != GameMode.SPECTATOR) {
                ePlayer z = ePlayer.get((Player) e.getRightClicked());
                p.getPlayer().openInventory(z.getPlayer().getInventory());
            }
        }
    }

    @EventHandler
    public void pickupItem(PlayerPickupItemEvent e) {
        ePlayer p = ePlayer.get(e.getPlayer());
        if (p.isAdminMode() || p.isWatching()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void invisToMob(EntityTargetEvent e) {
        if (e.getTarget() instanceof Player) {
            ePlayer p = ePlayer.get((Player) e.getTarget());
            if (p.isAdminMode() || p.isWatching()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHitPlayerWatching(EntityDamageEvent e) {

        //damage watcher
        if(e.getEntity() instanceof Player) {
            ePlayer p = ePlayer.get((Player)e.getEntity());
            if(p.isWatching()) {
                e.setCancelled(true);
                return;
            }
        }

        if(e instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent ee = (EntityDamageByEntityEvent) e;

            //watcher hit entity
            if(ee.getDamager() instanceof Player) {
                ePlayer p = ePlayer.get((Player) ee.getDamager());
                if(p.isWatching()) {
                    e.setCancelled(true);
                    return;
                }
            }

            //watcher shoot at player
            if(ee.getDamager() instanceof Projectile) {
                Projectile proj = (Projectile) ee.getDamager();
                if(proj.getShooter() instanceof Player) {
                    ePlayer p = ePlayer.get((Player) proj.getShooter());
                    if(p.isWatching()) {
                        e.setCancelled(true);
                        proj.setBounce(false);
                    }
                }
            }
        }
    }

    @EventHandler
    public void projHitMode(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Projectile && e.getEntity() instanceof Player) {
            ePlayer p = ePlayer.get((Player) e.getEntity());
            Projectile proj = (Projectile) e.getDamager();
            if(p.isWatching() || p.isAdminMode()) {
                final Vector v = proj.getVelocity();
                proj.setBounce(false);
                proj.remove();
                if(proj instanceof EnderPearl || proj instanceof FishHook || proj instanceof ThrownPotion)
                    return;
                Projectile newProj = p.getPlayer().launchProjectile(proj.getClass(), v);
            }
        }
    }

    @EventHandler
    public void hungerOnWatch(FoodLevelChangeEvent e) {
        ePlayer p = ePlayer.get((Player) e.getEntity());
        if(p.isWatching())
            e.setCancelled(true);
    }

    @EventHandler
    public void dontMoveGlass(InventoryClickEvent e) {
        ePlayer p = ePlayer.get((Player) e.getWhoClicked());
        if(e.getSlotType() == InventoryType.SlotType.ARMOR && (p.isWatching() || p.isAdminMode()))
            e.setCancelled(true);
    }

    @EventHandler
    public void dontDropGlass(PlayerDropItemEvent e) {
        ePlayer p = ePlayer.get(e.getPlayer());
        if((p.isAdminMode() || p.isWatching()) && e.getItemDrop().getItemStack().getType() == Material.GLASS) {
            e.getItemDrop().remove();
        }
    }

    @EventHandler
    public void clearGlassOnDeath(PlayerDeathEvent e) {
        ePlayer p = ePlayer.get(e.getEntity());
        if(p.isWatching() || p.isAdminMode()) {
            List<ItemStack> list = e.getDrops();
            Iterator<ItemStack> i = list.iterator();
            while(i.hasNext()) {
                ItemStack item = i.next();
                switch(item.getType()) {
                    case GLASS:
                        i.remove();
                }
            }
        }
    }
}