package com.minecraft.plugin.elite.general.commands.admin;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InvseeCommand extends eCommand {

    public InvseeCommand() {
        super("invsee", "egeneral.invsee", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        ePlayer p = ePlayer.get((Player) cs);
        if (p.isAdminMode()) {
            if (args.length > 0) {
                ePlayer z = ePlayer.get(args[0]);
                if (z != null) {
                    Inventory inv = z.getPlayer().getInventory();
                    p.getPlayer().openInventory(inv);
                    return true;
                } else {
                    p.sendMessage(GeneralLanguage.NO_TARGET);
                    return true;
                }
            } else {
                p.sendMessage(GeneralLanguage.INVSEE_USAGE);
                return true;
            }
        } else {
            p.sendMessage(GeneralLanguage.NOT_ADMIN_MODE);
            return true;
        }
    }
}