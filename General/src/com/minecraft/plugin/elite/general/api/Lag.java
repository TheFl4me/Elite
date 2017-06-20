package com.minecraft.plugin.elite.general.api;

public class Lag implements Runnable {

    public static int TICK_COUNT = 0;
    public static long[] TICKS = new long[600];
    public static long LAST_TICK = 0L;

    public static double getTPS() {
        return getTPS(100);
    }

    public static double getTPS(int ticks) {
        if (TICK_COUNT < ticks)
            return 20.0D;

        int target = (TICK_COUNT - 1 - ticks) % TICKS.length;
        long elapsed = System.currentTimeMillis() - TICKS[(target < 0 ? 0 : target)];

        return ticks / (elapsed / 1000.0D);
    }

    public void run() {
        int target = (TICK_COUNT % TICKS.length);
        TICKS[(target < 0 ? 0 : target)] = System.currentTimeMillis();
        TICK_COUNT += 1;
    }
}
