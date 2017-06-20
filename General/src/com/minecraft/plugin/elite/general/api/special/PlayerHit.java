package com.minecraft.plugin.elite.general.api.special;

import java.util.UUID;

public class PlayerHit {

    private UUID damager;
    private double damage;
    private long timeStamp;

    public PlayerHit(UUID hitter, double damage) {
        this.damager = hitter;
        this.damage = damage;
        this.timeStamp = System.currentTimeMillis();
    }

    public UUID getDamager() {
        return this.damager;
    }

    public double getDamage() {
        return this.damage;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }
}
