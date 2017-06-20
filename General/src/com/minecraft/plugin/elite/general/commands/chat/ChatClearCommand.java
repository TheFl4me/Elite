package com.minecraft.plugin.elite.general.commands.chat;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatClearCommand extends eCommand {

    public ChatClearCommand() {
        super("clearchat", "egeneral.chat.clear", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        ePlayer p = ePlayer.get((Player) cs);
        String chatclear = p.getLanguage().get(GeneralLanguage.CHAT_CLEAR)
                .replaceAll("%p", p.getChatName());
        for (Player all : Bukkit.getOnlinePlayers()) {
            if (!all.hasPermission("egeneral.chat.clear.bypass"))
                for (int i = 0; i < 100; i++)
                    all.sendMessage(" ");
            all.sendMessage(chatclear);
        }
        return true;
    }
}