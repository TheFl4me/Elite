package com.minecraft.plugin.elite.general.commands.chat;

import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatToggleCommand extends GeneralCommand {

    public ChatToggleCommand() {
        super("globalmute", GeneralPermission.CHAT_TOGGLE, true);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        Server server = Server.get();
        server.setChat(!server.chatIsEnabled());
        if (server.chatIsEnabled())
            for(Player players : Bukkit.getOnlinePlayers())
                GeneralPlayer.get(players).sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.CHAT_ENABLED);
        else
            for(Player players : Bukkit.getOnlinePlayers())
                GeneralPlayer.get(players).sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.CHAT_DISABLED);
        return true;
    }
}
