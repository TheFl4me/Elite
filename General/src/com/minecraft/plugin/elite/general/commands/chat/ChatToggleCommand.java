package com.minecraft.plugin.elite.general.commands.chat;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatToggleCommand extends eCommand {

    public ChatToggleCommand() {
        super("globalmute", "egeneral.gmute", true);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        Server server = Server.get();
        server.setChat(!server.chatIsEnabled());
        if (server.chatIsEnabled())
            for(Player players : Bukkit.getOnlinePlayers())
                ePlayer.get(players).sendMessage(GeneralLanguage.CHAT_ENABLED);
        else
            for(Player players : Bukkit.getOnlinePlayers())
                ePlayer.get(players).sendMessage(GeneralLanguage.CHAT_DISABLED);
        return true;
    }
}
