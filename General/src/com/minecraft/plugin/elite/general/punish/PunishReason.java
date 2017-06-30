package com.minecraft.plugin.elite.general.punish;

import com.minecraft.plugin.elite.general.api.enums.Unit;

public enum PunishReason {

    CHEATING(PunishType.BAN, null, 0),
    FRAUD(PunishType.BAN, null, 0),

    THREAT(PunishType.BAN, Unit.DAYS, 4.0),
    ABUSE_RANK(PunishType.BAN, Unit.DAYS, 3.0),
    ABUSE_BUG(PunishType.BAN, Unit.DAYS, 1.0),
    IMPERSONATION(PunishType.BAN, Unit.DAYS, 3.0),
    STATS_MANIPULATION(PunishType.BAN, Unit.DAYS, 3.0),
    TEAM(PunishType.BAN, Unit.DAYS, 1.0),

    ADVERTISING(PunishType.MUTE, Unit.DAYS, 5.0),
    HARASSMENT(PunishType.MUTE, Unit.DAYS, 2.0),
    NSFW(PunishType.MUTE, Unit.DAYS, 2.0),

    CHAT_SPAM(PunishType.MUTE, Unit.MINUTES, 1.0),
    CHAT_INSULT(PunishType.MUTE, Unit.HOURS, 0.1);

    private double modifier;
    private PunishType type;
    private Unit unit;
    private boolean temp;

    public static PunishReason get(String args) {
        for(PunishReason reason : values()) {
            if(reason.toString().equalsIgnoreCase(args))
                return reason;
        }
        return null;
    }

    PunishReason(PunishType type, Unit unit, double modifier) {
        this.modifier = modifier;
        this.type = type;
        this.unit = unit;
        this.temp = unit != null;
    }

    public double getModifier() {
        return this.modifier;
    }

    public boolean isTemp() {
        return this.temp;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public PunishType getType() {
        return this.type;
    }

    public String toDisplay() {
        return this.toString().toUpperCase();
    }

    public enum PunishType {

        BAN,
        MUTE
    }
}
