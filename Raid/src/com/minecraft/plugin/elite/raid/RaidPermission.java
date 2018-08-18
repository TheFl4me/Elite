package com.minecraft.plugin.elite.raid;

import com.minecraft.plugin.elite.general.api.interfaces.PermissionNode;

public enum RaidPermission implements PermissionNode {

    WARP("warp"),
    SPAWN("spawn"),
    TRACK("track");

    private String string;

    RaidPermission(String string) {
        this.string = "raid." + string;
    }

    public String toString() {
        return this.string;
    }
}
