package com.minecraft.plugin.elite.general.listeners.chat;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.File;

public class ChatBlacklistListener implements Listener {

    @EventHandler
    public void onChatInsult(AsyncPlayerChatEvent e) {
        GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
        File list = new File(General.DIRECTORY_BLACKLIST);
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(list);
        String msg = e.getMessage().toLowerCase();

        cfg.getStringList("blacklist").forEach((insult) -> {
            if(msg.contains(insult.toLowerCase())) {
                e.setCancelled(true);
                p.sendMessage(GeneralLanguage.CHAT_SWEAR);
            }
        });

        cfg.getStringList("links").forEach((link) -> {
            if(msg.contains(link.toLowerCase())) {
                e.setCancelled(true);
                p.sendMessage(GeneralLanguage.CHAT_LINKS);
            }
        });
    }
}