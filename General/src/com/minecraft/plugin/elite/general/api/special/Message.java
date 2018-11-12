package com.minecraft.plugin.elite.general.api.special;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Message {

    private GeneralPlayer sender;
    private GeneralPlayer target;
    private String msg;
    private long date;

    private static Map<UUID, UUID> reply = new HashMap<>();

    public static void remove(GeneralPlayer p) {
        reply.remove(p.getUniqueId());
    }

    public static boolean hasSentMessage(GeneralPlayer p) {
        return reply.containsKey(p.getUniqueId());
    }

    public static GeneralPlayer getTargetFrom(GeneralPlayer p) {
        return GeneralPlayer.get(reply.get(p.getUniqueId()));
    }

    public Message(GeneralPlayer sender, GeneralPlayer target, String msg) {
        this.sender = sender;
        this.target = target;
        this.msg = msg;
        this.date = System.currentTimeMillis();
    }

    public GeneralPlayer getSender() {
        return this.sender;
    }

    public GeneralPlayer getTarget() {
        return this.target;
    }

    public String getMessage() {
        return this.msg;
    }

    public long getDate() {
        return this.date;
    }

    public void send() {
        if (this.getTarget().isAFK())
            this.getSender().getPlayer().sendMessage(this.getSender().getLanguage().get(GeneralLanguage.MSG_AFK)
                    .replaceAll("%z", this.getTarget().getName()));

        reply.put(this.getSender().getUniqueId(), this.getTarget().getUniqueId());
        reply.put(this.getTarget().getUniqueId(), this.getSender().getUniqueId());

        String format = ChatColor.GRAY + "<" + this.getSender().getName() + " -> " + this.getTarget().getName() + "> " + this.getMessage();
        this.getTarget().getPlayer().sendMessage(format);
        this.getSender().getPlayer().sendMessage(format);

        for (Player staffs : Bukkit.getOnlinePlayers()) {
            GeneralPlayer staff = GeneralPlayer.get(staffs);
            if (!this.getSender().getUniqueId().equals(staff.getUniqueId()) && !this.getTarget().getUniqueId().equals(staff.getUniqueId()) && staff.isSpy()) {
                staff.getPlayer().sendMessage(ChatColor.GRAY + "[" + ChatColor.RED + "SPY" + ChatColor.GRAY + "] <" + this.getSender().getName() + " -> " + this.getTarget().getName() + "> " + this.getMessage());
            }
        }

        Database db = General.getDB();
        db.execute("INSERT INTO " + General.DB_CHAT_LOGS + " (date, uuid, name, message, type) VALUES (?, ?, ?, ?, ?);", this.getDate(), this.getSender().getUniqueId(), this.getSender().getName(), format, "private");
    }
}