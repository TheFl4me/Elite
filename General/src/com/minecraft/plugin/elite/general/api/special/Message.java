package com.minecraft.plugin.elite.general.api.special;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Message {

    private UUID sender;
    private UUID target;
    private String msg;
    private long date;

    private static Map<UUID, UUID> reply = new HashMap<>();

    public static void remove(ePlayer p) {
        reply.remove(p.getUniqueId());
    }

    public static boolean hasSentMessage(ePlayer p) {
        return reply.containsKey(p.getUniqueId());
    }

    public static ePlayer getTargetFrom(ePlayer p) {
        return ePlayer.get(reply.get(p.getUniqueId()));
    }

    public Message(ePlayer sender, ePlayer target, String msg) {
        this.sender = sender.getUniqueId();
        this.target = target.getUniqueId();
        this.msg = msg;
        this.date = System.currentTimeMillis();
    }

    public ePlayer getSender() {
        return ePlayer.get(this.sender);
    }

    public ePlayer getTarget() {
        return ePlayer.get(this.target);
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

        if (reply.containsKey(this.getSender().getUniqueId()))
            reply.remove(this.getSender().getUniqueId());
        if (reply.containsKey(this.getSender().getUniqueId()))
            reply.remove(this.getTarget().getUniqueId());

        reply.put(this.getSender().getUniqueId(), this.getTarget().getUniqueId());
        reply.put(this.getTarget().getUniqueId(), this.getSender().getUniqueId());

        String format = ChatColor.GRAY + "<" + this.getSender().getName() + " -> " + this.getTarget().getName() + "> " + this.getMessage();
        this.getTarget().getPlayer().sendMessage(format);
        this.getSender().getPlayer().sendMessage(format);

        for (Player staffs : Bukkit.getOnlinePlayers()) {
            ePlayer staff = ePlayer.get(staffs);
            if (!this.getSender().getUniqueId().equals(staff.getUniqueId()) && !this.getTarget().getUniqueId().equals(staff.getUniqueId()) && staff.isSpy()) {
                staff.getPlayer().sendMessage(ChatColor.GRAY + "[" + ChatColor.RED + "SPY" + ChatColor.GRAY + "] <" + this.getSender().getName() + " -> " + this.getTarget().getName() + "> " + this.getMessage());
            }
        }

        Database db = General.getDB();
        db.execute("INSERT INTO " + General.DB_CHATLOGS + " (date, uuid, name, message, type) VALUES (?, ?, ?, ?, ?);", this.getDate(), this.getSender().getUniqueId(), this.getSender().getName(), format, "private");
    }
}