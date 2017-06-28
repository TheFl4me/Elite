package com.minecraft.plugin.elite.general.api;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.api.enums.Unit;
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
        long sec = ms / Unit.SECONDS.toSeconds();

        final long unitMin = Unit.MINUTES.toSeconds();
        final long unitHours = Unit.HOURS.toSeconds();
        final long unitDays = Unit.DAYS.toSeconds();
        final long unitWeeks = Unit.WEEKS.toSeconds();
        final long unitMonths = Unit.MONTHS.toSeconds();
        final long unitYears = Unit.YEARS.toSeconds();

        StringBuilder sb = new StringBuilder(40);
        long years = sec / unitYears;
        if (years > 0) {
            sb.append(years + (years == 1 ? lang.get(GeneralLanguage.UNIT_YEAR) : lang.get(GeneralLanguage.UNIT_YEAR_PLURAL)));
            sec -= years * unitYears;
        }
        long months = sec / unitMonths;
        if (months > 0) {
            sb.append(months + (months == 1 ? lang.get(GeneralLanguage.UNIT_MONTH) : lang.get(GeneralLanguage.UNIT_MONTH_PLURAL)));
            sec -= months * unitMonths;
        }
        long weeks = sec / unitWeeks;
        if (weeks > 0) {
            sb.append(weeks + (weeks == 1 ? lang.get(GeneralLanguage.UNIT_WEEK) : lang.get(GeneralLanguage.UNIT_WEEK_PLURAL)));
            sec -= weeks * unitWeeks;
        }
        long days = sec / unitDays;
        if (days > 0) {
            sb.append(days + (days == 1 ? lang.get(GeneralLanguage.UNIT_DAY) : lang.get(GeneralLanguage.UNIT_DAY_PLURAL)));
            sec -= days * unitDays;
        }
        long hours = sec / unitHours;
        if (hours > 0) {
            sb.append(hours + (hours == 1 ? lang.get(GeneralLanguage.UNIT_HOUR) : lang.get(GeneralLanguage.UNIT_HOUR_PLURAL)));
            sec -= hours * unitHours;
        }
        long minutes = sec / unitMin;
        if (minutes > 0) {
            sb.append(minutes + (minutes == 1 ? lang.get(GeneralLanguage.UNIT_MINUTE) : lang.get(GeneralLanguage.UNIT_MINUTE_PLURAL)));
            sec -= minutes * unitMin;
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
        long sec = ms / Unit.SECONDS.toSeconds();

        final long unitMin = Unit.MINUTES.toSeconds();

        long minutes = sec / unitMin;
        if (minutes > 0) {
            sb.append(minutes < 10 ? "0" + minutes + ":" : minutes + ":");
            sec -= minutes * unitMin;
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

    public void computeELO(ePlayer p1, ePlayer p2, double s1, double s2, double k) {
        //K = 32 chess, 20 Basketball

        final double r1 = p1.getELO();
        final double r2 = p2.getELO();

        final double e1 = 1 / (1 + Math.pow(10, (r2 - r1) / 400));
        final double e2 = 1 / (1 + Math.pow(10, (r1 - r2) / 400));

        final long f1 = Math.round(r1 + k * (s1 - e1));
        final long f2 = Math.round(r2 + k * (s2 - e2));

        p1.setELO(f1);
        p2.setELO(f2);
    }
}