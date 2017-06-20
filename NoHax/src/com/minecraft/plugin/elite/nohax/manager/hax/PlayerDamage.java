package com.minecraft.plugin.elite.nohax.manager.hax;

import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.nohax.manager.HaxPlayer;
import com.minecraft.plugin.elite.nohax.manager.hax.combat.ForceFieldHack;
import com.minecraft.plugin.elite.nohax.manager.hax.combat.ReachHack;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PlayerDamage {

    private HaxPlayer target;
    private HaxPlayer attacker;
    private DamageCause cause;
    private double damage;
    private boolean isEntity;

    public static void store(EntityDamageEvent e) {
        if (e.isCancelled())
            return;
        if (e.getEntity() instanceof Player) {

            PlayerDamage damage = new PlayerDamage(e);
            Server server = Server.get();

            HaxPlayer attacker = damage.getAttacker();

            if (attacker == null)
                return;

            if (!attacker.isLagging() && !damage.getTarget().isLagging() && !attacker.canBypassChecks() && !server.isLagging()) {
                ForceFieldHack.check(damage);
                ReachHack.check(damage);
            }
        }
    }

    public PlayerDamage(EntityDamageEvent e) {
        if (e instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent ee = (EntityDamageByEntityEvent) e;
            if (ee.getDamager() instanceof Player)
                this.attacker = HaxPlayer.get((Player) ee.getDamager());
        }
        this.target = HaxPlayer.get((Player) e.getEntity());
        this.getTarget().getDamages()[this.getTarget().getDamageCount() % this.getTarget().getDamages().length] = this;
        this.getTarget().setDamageCount(this.getTarget().getDamageCount() + 1);
        this.cause = e.getCause();
        this.damage = e.getDamage();

        switch (this.getCause()) {
            case ENTITY_ATTACK:
            case PROJECTILE:
            case CONTACT:
            case BLOCK_EXPLOSION:
            case ENTITY_EXPLOSION:
                this.isEntity = true;
            default: this.isEntity = false;
        }
    }

    public HaxPlayer getTarget() {
        return this.target;
    }
    public HaxPlayer getAttacker() {
        return this.attacker;
    }
    public DamageCause getCause() {
        return this.cause;
    }
    public double getDamage() {
        return this.damage;
    }
    public boolean isEntity() {
        return this.isEntity;
    }
}
