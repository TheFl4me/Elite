package com.minecraft.plugin.elite.general.commands.admin;

import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvPToggleCommand extends GeneralCommand {

    public PvPToggleCommand() {
        super("pvp", GeneralPermission.ADMIN_PVP_TOGGLE, true);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        Server server = Server.get();
        String msg;
        server.setPvP(!server.pvpIsEnabled());
        for(Player all : Bukkit.getOnlinePlayers()) {
            if (server.pvpIsEnabled())
                GeneralPlayer.get(all).sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PVP_ENABLED);
            else
                GeneralPlayer.get(all).sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PVP_DISABLED);
        }
        return true;
    }
}	