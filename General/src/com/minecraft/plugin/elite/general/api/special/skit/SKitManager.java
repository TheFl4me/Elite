package com.minecraft.plugin.elite.general.api.special.skit;

import java.util.HashSet;

public class SKitManager {

    private static HashSet<SKit> sKits = new HashSet<>();

    public static SKit[] getAll() {
        return sKits.toArray(new SKit[sKits.size()]);
    }

    public static SKit get(String name) {
        for (SKit skit : getAll())
            if (skit.getName().equalsIgnoreCase(name))
                return skit;
        return null;
    }

    public static void add(SKit skit) {
        sKits.add(skit);
    }

    public static void remove(SKit skit) {
        sKits.remove(skit);
    }
}
