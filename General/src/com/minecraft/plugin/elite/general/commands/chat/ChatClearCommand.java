package com.minecraft.plugin.elite.general.commands.chat;

import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatClearCommand extends GeneralCommand {

    public ChatClearCommand() {
        super("clearchat", GeneralPermission.CHAT_CLEAR, false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        for (Player all : Bukkit.getOnlinePlayers()) {
            GeneralPlayer z = GeneralPlayer.get(all.getUniqueId());
            if (!z.hasPermission(GeneralPermission.CHAT_CLEAR_BYPASS))
                for (int i = 0; i < 100; i++)
                    z.getPlayer().sendMessage(" ");
            z.getPlayer().sendMessage(z.getLanguage().get(com.minecraft.plugin.elite.general.GeneralLanguage.CHAT_CLEAR).replaceAll("%p", p.getChatName()));
        }
        return true;
    }
}