package com.minecraft.plugin.elite.general.commands.admin.mode;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WatchCommand extends eCommand {

    public WatchCommand() {
        super("watch", "egeneral.watch", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        ePlayer p = ePlayer.get((Player) cs);
        if (p.isAdminMode()) {
            p.sendMessage(GeneralLanguage.MODE_STILL_ADMIN);
            return true;
        }
        p.setWatching(!p.isWatching());
        return true;
    }
}