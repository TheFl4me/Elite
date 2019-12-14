package com.minecraft.plugin.elite.general.commands.admin;

import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class LagCommand extends GeneralCommand {

    public LagCommand() {
        super("lag", GeneralPermission.ADMIN_LAG, true);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        Server server = Server.get();
        cs.sendMessage(ChatColor.GRAY + "Server Lag: " + ChatColor.RESET + server.getLagPercentage() + "%");
        return true;
    }
}
