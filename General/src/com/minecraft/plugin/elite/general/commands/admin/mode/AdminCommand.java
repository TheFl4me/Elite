package com.minecraft.plugin.elite.general.commands.admin.mode;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommand extends GeneralCommand {

    public AdminCommand() {
        super("admin", GeneralPermission.MODE_ADMIN, false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        if (!p.isWatching()) {
            p.setAdminMode(!p.isAdminMode());
            String msg = p.getLanguage().get(GeneralLanguage.GAMEMODE_SET_YOU);
            if (p.isAdminMode())
                msg = msg.replaceAll("%gm", "ADMIN");
            else
                msg = msg.replaceAll("%gm", "PLAY");
            p.getPlayer().sendMessage(msg);
        } else {
            p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.MODE_STILL_WATCH));
        }
        return true;
    }
}