package com.minecraft.plugin.elite.general.api.enums;

public enum Unit {

    MILLISECONDS(1),
    SECONDS(1000),
    MINUTES(60),
    HOURS(3600),
    DAYS(86400),
    WEEKS(604800),
    MONTHS(2620800),
    YEARS(31449600);

    private long modifier;

    Unit(long modifier) {
        this.modifier = modifier;
    }

    public long toSeconds() {
        return this.modifier;
    }

    public long toMS() {
        return this.toSeconds() * 1000L;
    }
}
