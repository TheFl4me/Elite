package com.minecraft.plugin.elite.general.listeners.chat;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.special.Message;
import com.minecraft.plugin.elite.general.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ChatEventListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void generalChatEvents(AsyncPlayerChatEvent e) {
        ePlayer p = ePlayer.get(e.getPlayer());

        if (p.getPlayer().hasPermission("egeneral.chat.color"))
            e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));

        e.setFormat(p.getClanPrefix() + p.getLevelPrefix() + p.getChatName() + " > " + ChatColor.RESET + e.getMessage());
        String type = "public";
        if (e.getMessage().startsWith("@") && p.getPlayer().hasPermission("egeneral.chat.staff")) {
            type = "staff";
            String msg = ChatColor.GRAY + "[" + ChatColor.GOLD + "STAFF" + ChatColor.GRAY + "] " + ChatColor.RESET + p.getChatName() + " > " + ChatColor.AQUA + e.getMessage().substring(1);
            System.out.println(msg);
            Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("egeneral.chat.staff")).forEach(staff -> staff.sendMessage(msg));
            e.setCancelled(true);
        }

        Database db = General.getDB();
        db.execute("INSERT INTO " + General.DB_CHATLOGS + " (date, uuid, name, message, type) VALUES (?, ?, ?, ?, ?);", System.currentTimeMillis(), p.getUniqueId(), p.getName(), e.getMessage(), type);
    }

    @EventHandler
    public void removePlayerFromHashMap(PlayerQuitEvent e) {
        ePlayer p = ePlayer.get(e.getPlayer());
        if (Message.hasSentMessage(p))
            Message.remove(p);
    }
}