package com.minecraft.plugin.elite.general.listeners;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.special.supportchat.SupportChat;
import com.minecraft.plugin.elite.general.api.special.supportchat.SupportChatManager;
import com.minecraft.plugin.elite.general.database.Database;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SupportEventListener implements Listener {
	
	@EventHandler
	public void onSupportChat(AsyncPlayerChatEvent e) {
		ePlayer p = ePlayer.get(e.getPlayer());
		SupportChat chat = SupportChatManager.get(p);
		if(chat != null) {
			e.setCancelled(true);
			Database db = General.getDB();
			db.execute("INSERT INTO " + General.DB_CHATLOGS + " (date, uuid, name, message, type) VALUES (?, ?, ?, ?, ?);", System.currentTimeMillis(), p.getUniqueId(), p.getName(), e.getMessage(), "support");
			chat.sendMessage(ChatColor.GRAY + "[" + ChatColor.YELLOW + "Support" + ChatColor.GRAY + "] " + ChatColor.RESET + p.getChatName() + " > " + ChatColor.YELLOW + e.getMessage());
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		ePlayer p = ePlayer.get(e.getPlayer());
		SupportChat chat = SupportChatManager.get(p);
		if(chat != null)
			chat.end();
		if(SupportChatManager.hasSentRequest(p))
			SupportChatManager.removeRequest(p);
	}
}