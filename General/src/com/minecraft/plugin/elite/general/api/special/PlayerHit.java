package com.minecraft.plugin.elite.general.api.special;

import java.util.UUID;

public class PlayerHit {

    private UUID attacker;
    private double damage;
    private long timeStamp;

    public PlayerHit(UUID hitter, double damage) {
        this.attacker = hitter;
        this.damage = damage;
        this.timeStamp = System.currentTimeMillis();
    }

    public UUID getAttacker() {
        return this.attacker;
    }

    public double getDamage() {
        return this.damage;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }
}
