package com.minecraft.plugin.elite.general.api.enums;

import org.bukkit.ChatColor;

public enum Prefix {

    //http://minecraft.gamepedia.com/Glass_Pane
    ADMIN(ChatColor.RED.toString(), (short) 14),
    MODPLUS(ChatColor.DARK_PURPLE.toString() + ChatColor.ITALIC.toString(), (short) 10),
    MOD(ChatColor.DARK_PURPLE.toString(), (short) 10),
    SUPPORTER(ChatColor.LIGHT_PURPLE.toString(), (short) 6),
    BUILDER(ChatColor.GREEN.toString(), (short) 5),
    MEDIA(ChatColor.AQUA.toString(), (short) 3),
    PREMIUM(ChatColor.GOLD.toString(), (short) 1),
    NORMAL(ChatColor.GRAY.toString(), (short) 0);

    private final String color;
    private final short i;

    Prefix(String color, short s) {
        this.color = color;
        this.i = s;
    }

    public String getColor() {
        return this.color;
    }

    public short getHexadecimal() {
        return this.i;
    }
}