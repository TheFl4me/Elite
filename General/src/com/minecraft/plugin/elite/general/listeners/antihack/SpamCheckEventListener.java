package com.minecraft.plugin.elite.general.listeners.antihack;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.punish.PunishManager;
import com.minecraft.plugin.elite.general.antihack.SpamCheck;
import org.bukkit.command.Command;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpamCheckEventListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (!PunishManager.isMuted(e.getPlayer().getUniqueId()))
            SpamCheck.onPlayerChat(e);
        while (e.getMessage().contains("  "))
            e.setMessage(e.getMessage().replaceAll(" {2}", " "));
    }

    @EventHandler
    public void checkSpamOnCommand(PlayerCommandPreprocessEvent e) {
        GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
        List<String> blacklist = new ArrayList<>();
        List<String> cmdNames = Arrays.asList("tell", "reply", "clanchat");

        for(String string : cmdNames) {
            Command cmd = General.getPlugin().getCommand(string);
            if(cmd != null) {
                blacklist.add("/" + cmd.getName());
                for(String alias : cmd.getAliases())
                    blacklist.add("/" + alias);
            }
        }

        String message = e.getMessage().split(" ")[0];
        for (String cmd : blacklist)
            if (message.toLowerCase().equalsIgnoreCase(cmd.toLowerCase()) && !PunishManager.isMuted(p.getUniqueId()))
                SpamCheck.onPlayerCommand(e);
    }
}
