package com.minecraft.plugin.elite.nohax.listeners;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.punish.PunishManager;
import com.minecraft.plugin.elite.nohax.manager.HaxPlayer;
import com.minecraft.plugin.elite.nohax.manager.SpamCheck;
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
        HaxPlayer p = HaxPlayer.get(e.getPlayer());
        List<String> blacklist = new ArrayList<>();
        List<String> cmdNames = Arrays.asList("tell", "reply", "clanchat");

        for(String string : cmdNames) {
            Command cmd = General.getPlugin().getCommand(string);
            if(cmd != null) {
                blacklist.add(cmd.getName());
                blacklist.addAll(cmd.getAliases());
            }
        }

        for (String cmd : blacklist)
            if (e.getMessage().startsWith(cmd) && !PunishManager.isMuted(p.getUniqueId()))
                SpamCheck.onPlayerCommand(e);
    }
}
