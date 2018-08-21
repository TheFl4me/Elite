package com.minecraft.plugin.elite.survivalgames;

import com.minecraft.plugin.elite.general.api.interfaces.PermissionNode;

public enum SurvivalGamesPermission implements PermissionNode {

    MAP_EDIT("map.edit"),
    MAP_SET("map.set"),
    START("start");

    private String string;

    SurvivalGamesPermission(String string) {
        this.string = "sg." + string;
    }

    public String toString() {
        return this.string;
    }
}
