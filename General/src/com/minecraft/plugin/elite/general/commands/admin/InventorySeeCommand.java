package com.minecraft.plugin.elite.general.commands.admin;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventorySeeCommand extends GeneralCommand {

    public InventorySeeCommand() {
        super("invsee", GeneralPermission.ADMIN_INVENTORY_SEE, false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        if (p.isAdminMode()) {
            if (args.length > 0) {
                GeneralPlayer z = GeneralPlayer.get(args[0]);
                if (z != null) {
                    Inventory inv = z.getPlayer().getInventory();
                    p.getPlayer().openInventory(inv);
                } else {
                    p.sendMessage(GeneralLanguage.NO_TARGET);
                }
            } else {
                p.sendMessage(GeneralLanguage.INVENTORY_SEE_USAGE);
            }
        } else {
            p.sendMessage(GeneralLanguage.NOT_ADMIN_MODE);
        }
        return true;
    }
}