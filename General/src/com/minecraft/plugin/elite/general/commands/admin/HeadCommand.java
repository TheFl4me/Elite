package com.minecraft.plugin.elite.general.commands.admin;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class HeadCommand extends GeneralCommand {

    public HeadCommand() {
        super("head", GeneralPermission.ADMIN_HEAD, false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
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