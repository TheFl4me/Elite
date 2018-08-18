package com.minecraft.plugin.elite.kitpvp;

import com.minecraft.plugin.elite.general.api.interfaces.PermissionNode;

public enum KitPvPPermission implements PermissionNode {

    DUEL_SET_LOCATION("duel.set.location"),
    DUEL_SET_SPAWN("duel.set.spawn"),

    HOLOGRAM_SET("hologram"),

    KIT_FREE("kit.free"),
    KIT("kit"),
    KIT_INFO("kit.info");

    private String string;

    KitPvPPermission(String string) {
        this.string = "kitpvp." + string;
    }

    public String toString() {
        return this.string;
    }
}
