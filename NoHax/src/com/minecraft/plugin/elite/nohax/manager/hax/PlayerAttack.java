package com.minecraft.plugin.elite.nohax.manager.hax;

import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.nohax.manager.HaxPlayer;
import com.minecraft.plugin.elite.nohax.manager.hax.combat.ForceFieldHack;
import com.minecraft.plugin.elite.nohax.manager.hax.combat.ReachHack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerAttack {

    private HaxPlayer attacker;
    private Entity target;
    private double damage;
    private boolean isValid;

    public static void store(EntityDamageByEntityEvent e) {
        if (e.isCancelled())
            return;
        if (e.getDamager() instanceof Player) {
            PlayerAttack attack = new PlayerAttack(e);

            if (attack.isValid()) {
                ForceFieldHack.check(attack);
                ReachHack.check(attack);
            }
        }
    }

    public PlayerAttack(EntityDamageByEntityEvent e) {
        this.attacker = HaxPlayer.get((Player) e.getDamager());
        this.target = e.getEntity();
        this.getAttacker().getAttacks()[this.getAttacker().getAttackCount() % this.getAttacker().getAttacks().length] = this;
        this.getAttacker().setAttackCount(this.getAttacker().getAttackCount() + 1);
        this.damage = e.getDamage();
        this.isValid = true;

        Server server = Server.get();
        if(!this.getAttacker().isValid() || this.getAttacker().isLagging() || this.getAttacker().canBypassChecks() || server.isLagging())
            this.isValid = false;
    }

    public HaxPlayer getAttacker() {
        return this.attacker;
    }
    public Entity getTarget() {
        return this.target;
    }
    public double getDamage() {
        return this.damage;
    }
    public boolean isValid() {
        return this.isValid;
    }
}
