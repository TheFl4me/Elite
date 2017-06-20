package com.minecraft.plugin.elite.general.commands.admin;

import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class LagCommand extends eCommand {

    public LagCommand() {
        super("lag", "egeneral.lag", true);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        Server server = Server.get();
        cs.sendMessage(ChatColor.GRAY + "Server Lag: " + ChatColor.RESET + Double.toString(server.getLagPercentage()) + "%");
        return true;
    }
}
