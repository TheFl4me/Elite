package com.minecraft.plugin.elite.general.listeners;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;

public class SpecialModeEventListener implements Listener {

    @EventHandler
    public void clickOpenInv(PlayerInteractEntityEvent e) {
        GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
        if (e.getRightClicked() instanceof Player) {
            if (p.isAdminMode() && p.getPlayer().getGameMode() != GameMode.SPECTATOR) {
                GeneralPlayer z = GeneralPlayer.get((Player) e.getRightClicked());
                p.getPlayer().openInventory(z.getPlayer().getInventory());
            }
        }
    }

    @EventHandler
    public void pickupItem(PlayerPickupItemEvent e) {
        GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
        if (p.isAdminMode() || p.isWatching()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void invisToMob(EntityTargetEvent e) {
        if (e.getTarget() instanceof Player) {
            GeneralPlayer p = GeneralPlayer.get((Player) e.getTarget());
            if (p.isAdminMode() || p.isWatching()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHitPlayerWatching(EntityDamageEvent e) {

        //damage watcher
        if(e.getEntity() instanceof Player) {
            GeneralPlayer p = GeneralPlayer.get((Player)e.getEntity());
            if(p.isWatching()) {
                e.setCancelled(true);
                return;
            }
        }

        if(e instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent ee = (EntityDamageByEntityEvent) e;

            //watcher hit entity
            if(ee.getDamager() instanceof Player) {
                GeneralPlayer p = GeneralPlayer.get((Player) ee.getDamager());
                if(p.isWatching()) {
                    e.setCancelled(true);
                    return;
                }
            }

            //watcher shoot at player
            if(ee.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) ee.getDamager();
                if(projectile.getShooter() instanceof Player) {
                    GeneralPlayer p = GeneralPlayer.get((Player) projectile.getShooter());
                    if(p.isWatching()) {
                        e.setCancelled(true);
                        projectile.setBounce(false);
                    }
                }
            }
        }
    }

    @EventHandler
    public void projectileHitMode(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Projectile && e.getEntity() instanceof Player) {
            GeneralPlayer p = GeneralPlayer.get((Player) e.getEntity());
            Projectile projectile = (Projectile) e.getDamager();
            if(p.isWatching() || p.isAdminMode()) {
                final Vector v = projectile.getVelocity();
                projectile.setBounce(false);
                projectile.remove();
                if(projectile instanceof EnderPearl || projectile instanceof FishHook || projectile instanceof ThrownPotion)
                    return;
                Projectile newProjectile = p.getPlayer().launchProjectile(projectile.getClass(), v);
            }
        }
    }

    @EventHandler
    public void hungerOnWatch(FoodLevelChangeEvent e) {
        GeneralPlayer p = GeneralPlayer.get((Player) e.getEntity());
        if(p.isWatching())
            e.setCancelled(true);
    }

    @EventHandler
    public void doNotMoveGlass(InventoryClickEvent e) {
        GeneralPlayer p = GeneralPlayer.get((Player) e.getWhoClicked());
        if(e.getSlotType() == InventoryType.SlotType.ARMOR && (p.isWatching() || p.isAdminMode()))
            e.setCancelled(true);
    }

    @EventHandler
    public void doNotDropGlass(PlayerDropItemEvent e) {
        GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
        if((p.isAdminMode() || p.isWatching()) && e.getItemDrop().getItemStack().getType() == Material.GLASS) {
            e.getItemDrop().remove();
        }
    }

    @EventHandler
    public void clearGlassOnDeath(PlayerDeathEvent e) {
        GeneralPlayer p = GeneralPlayer.get(e.getEntity());
        if(p.isWatching() || p.isAdminMode()) {
            List<ItemStack> list = e.getDrops();
            list.removeIf(item -> item.getType() == Material.GLASS);
        }
    }
}