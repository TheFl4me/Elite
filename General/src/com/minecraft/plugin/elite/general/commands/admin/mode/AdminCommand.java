package com.minecraft.plugin.elite.general.commands.admin.mode;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommand extends eCommand {

    public AdminCommand() {
        super("admin", "egeneral.admin", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        ePlayer p = ePlayer.get((Player) cs);
        if (!p.isWatching()) {
            p.setAdminMode(!p.isAdminMode());
            String msg = p.getLanguage().get(GeneralLanguage.GAMEMODE_SET_YOU);
            if (p.isAdminMode())
                msg = msg.replaceAll("%gm", "ADMIN");
            else
                msg = msg.replaceAll("%gm", "PLAY");
            p.getPlayer().sendMessage(msg);
            return true;
        } else {
            p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.MODE_STILL_WATCH));
            return true;
        }
    }
}