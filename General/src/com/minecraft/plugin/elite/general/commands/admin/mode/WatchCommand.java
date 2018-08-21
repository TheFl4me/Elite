package com.minecraft.plugin.elite.general.commands.admin.mode;

import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WatchCommand extends GeneralCommand {

    public WatchCommand() {
        super("watch", GeneralPermission.MODE_WATCH, false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        if (p.isAdminMode()) {
            p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.MODE_STILL_ADMIN);
            return true;
        }
        p.setWatching(!p.isWatching());
        return true;
    }
}