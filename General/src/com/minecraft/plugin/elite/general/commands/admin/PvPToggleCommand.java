package com.minecraft.plugin.elite.general.commands.admin;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvPToggleCommand extends eCommand {

    public PvPToggleCommand() {
        super("pvp", "egeneral.pvptoggle", true);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        Server server = Server.get();
        String msg;
        server.setPvP(!server.pvpIsEnabled());
        if (server.pvpIsEnabled())
            for(Player all : Bukkit.getOnlinePlayers())
                ePlayer.get(all).sendMessage(GeneralLanguage.PVP_ENABLED);
        else
            for(Player all : Bukkit.getOnlinePlayers())
                ePlayer.get(all).sendMessage(GeneralLanguage.PVP_DISABLED);
        return true;
    }
}	