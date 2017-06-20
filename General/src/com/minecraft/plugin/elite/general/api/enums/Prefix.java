package com.minecraft.plugin.elite.general.api.enums;

import org.bukkit.ChatColor;

public enum Prefix {

    ADMIN(ChatColor.RED.toString()),
    MODPLUS(ChatColor.DARK_PURPLE.toString() + ChatColor.ITALIC.toString()),
    MOD(ChatColor.DARK_PURPLE.toString()),
    SUPPORTER(ChatColor.LIGHT_PURPLE.toString()),
    BUILDER(ChatColor.GREEN.toString()),
    MEDIA(ChatColor.AQUA.toString()),
    PREMIUM(ChatColor.GOLD.toString()),
    NORMAL(ChatColor.GRAY.toString());

    private final String color;

    Prefix(String color) {
        this.color = color;
    }

    public String getColor() {
        return this.color;
    }
}