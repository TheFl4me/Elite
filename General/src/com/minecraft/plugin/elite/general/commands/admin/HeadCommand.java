package com.minecraft.plugin.elite.general.commands.admin;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class HeadCommand extends eCommand {

    public HeadCommand() {
        super("head", "egeneral.head", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        ePlayer p = ePlayer.get((Player) cs);
        if (args.length > 0) {
            Server server = Server.get();
            Inventory inv = p.getPlayer().getInventory();
            inv.addItem(server.playerHead(args[0]));
            return true;
        } else {
            p.sendMessage(GeneralLanguage.HEAD_USAGE);
            return true;
        }
    }
}