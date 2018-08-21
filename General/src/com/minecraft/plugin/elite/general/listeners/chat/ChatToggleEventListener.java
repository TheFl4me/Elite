package com.minecraft.plugin.elite.general.listeners.chat;

import com.minecraft.plugin.elite.general.GeneralPermission;
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
            if (!p.hasPermission(GeneralPermission.CHAT_TOGGLE_BYPASS)) {
                p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.CHAT_DISABLED_ON_TALK);
                e.setCancelled(true);
            }
        }
    }
}