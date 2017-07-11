package com.minecraft.plugin.elite.general.listeners.chat;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatToggleEventListener implements Listener {

    @EventHandler
    public void onDisabledChatTalk(AsyncPlayerChatEvent e) {
        GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
        Server server = Server.get();
        if (!server.chatIsEnabled()) {
            if (!p.getPlayer().hasPermission("egeneral.gmute.bypass")) {
                p.sendMessage(GeneralLanguage.CHAT_DISABLED_ON_TALK);
                e.setCancelled(true);
            }
        }
    }
}