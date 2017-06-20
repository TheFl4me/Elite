package com.minecraft.plugin.elite.general.api;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.enums.Language;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Server {

    private String name;
    private String domain;
    private String ip;
    private String type;
    private boolean chat;
    private boolean pvp;
    private boolean soup;

    private static Server server = null;
    private int number = 1;

    public static Server get() {
        return server;
    }

    public Server(String type) {
        this.name = "RetroPvP";
        this.type = type;
        this.domain = this.getName().toLowerCase() + ".com";
        this.ip = type.toLowerCase() + "." + this.getDomain();
        server = this;
        this.chat = true;
        this.pvp = true;
        this.soup = true;
    }

    public void initiate() {

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(General.getPlugin(), new Lag(), 100L, 1L);

        for(Language lang : Language.values())
            lang.loadStrings();
    }

    public String getName() {
        return this.name;
    }

    public String getIP() {
        return this.ip;
    }

    public String getDomain() {
        return this.domain;
    }

    public String getType() {
        return this.type;
    }

    public boolean isLagging() {
        return this.getTPS() < 19;
    }

    public double getTPS() {
        return Lag.getTPS();
    }

    public double getLagPercentage() {
        return Math.round((1.0D - this.getTPS() / 20.0D) * 100.0D);
    }

    public void setChat(boolean chat) {
        this.chat = chat;
    }

    public boolean chatIsEnabled() {
        return this.chat;
    }

    public void setPvP(boolean pvp) {
        this.pvp = pvp;
    }

    public boolean pvpIsEnabled() {
        return this.pvp;
    }

    public void setSoups(boolean soups) {
        this.soup = soups;
    }

    public boolean hasSoups() {
        return this.soup;
    }

    public String getDate() {
        SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        return date.format(new Date());
    }

    public String getDate(long ms) {
        SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        return date.format(new Date(ms));
    }

    public String getTimeUntil(long epoch, Language lang) {
        epoch -= System.currentTimeMillis();
        return this.getTime(epoch, lang);
    }

    public String getTime(long ms, Language lang) {
        long sec = ms / 1000;
        StringBuilder sb = new StringBuilder(40);
        if (sec / 31449600 > 0) {
            long years = sec / 31449600;
            sb.append(years + (years == 1 ? lang.get(GeneralLanguage.UNIT_YEAR) : lang.get(GeneralLanguage.UNIT_YEAR_PLURAL)));
            sec -= years * 31449600;
        }
        if (sec / 2620800 > 0) {
            long months = sec / 2620800;
            sb.append(months + (months == 1 ? lang.get(GeneralLanguage.UNIT_MONTH) : lang.get(GeneralLanguage.UNIT_MONTH_PLURAL)));
            sec -= months * 2620800;
        }
        if (sec / 604800 > 0) {
            long weeks = sec / 604800;
            sb.append(weeks + (weeks == 1 ? lang.get(GeneralLanguage.UNIT_WEEK) : lang.get(GeneralLanguage.UNIT_WEEK_PLURAL)));
            sec -= weeks * 604800;
        }
        if (sec / 86400 > 0) {
            long days = sec / 86400;
            sb.append(days + (days == 1 ? lang.get(GeneralLanguage.UNIT_DAY) : lang.get(GeneralLanguage.UNIT_DAY_PLURAL)));
            sec -= days * 86400;
        }
        if (sec / 3600 > 0) {
            long hours = sec / 3600;
            sb.append(hours + (hours == 1 ? lang.get(GeneralLanguage.UNIT_HOUR) : lang.get(GeneralLanguage.UNIT_HOUR_PLURAL)));
            sec -= hours * 3600;
        }
        if (sec / 60 > 0) {
            long minutes = sec / 60;
            sb.append(minutes + (minutes == 1 ? lang.get(GeneralLanguage.UNIT_MINUTE) : lang.get(GeneralLanguage.UNIT_MINUTE_PLURAL)));
            sec -= minutes * 60;
        }
        if (sec > 0) {
            sb.append(sec + (sec == 1 ? lang.get(GeneralLanguage.UNIT_SECOND) : lang.get(GeneralLanguage.UNIT_SECOND_PLURAL)));
        }
        if (sb.length() > 1) {
            sb.replace(sb.length() - 1, sb.length(), "");
        } else {
            sb = new StringBuilder("N/A");
        }
        return sb.toString();
    }

    public String getTimeDigitalUntil(long epoch) {
        epoch -= System.currentTimeMillis();
        return this.getTimeDigital(epoch);
    }

    public String getTimeDigital(long ms) {
        StringBuilder sb = new StringBuilder(40);
        long sec = ms / 1000;

        if (sec / 60 > 0) {
            long minutes = sec / 60;
            sb.append(minutes < 10 ? "0" + minutes + ":" : minutes + ":");
            sec -= minutes * 60;
        } else {
            sb.append("00:");
        }

        if (sec > 0) {
            sb.append(sec < 10 ? "0" + sec : sec);
        } else
            sb.append("00");

        if (sb.length() < 1)
            sb = new StringBuilder("--:--");
        return sb.toString();
    }

    public void rename(ItemStack stack, String name) {
        ItemMeta im = stack.getItemMeta();
        String color = ChatColor.translateAlternateColorCodes('&', name);
        String[] strings = color.split("\n");
        im.setDisplayName(strings[0]);
        List<String> lore = new ArrayList<>();
        for(String string : strings)
            if (!string.equals(strings[0]))
                lore.add(string);
        im.setLore(lore);
        stack.setItemMeta(im);
    }

    public ItemStack playerHead(String owner) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwner(owner);
        head.setItemMeta(meta);
        return head;
    }
}