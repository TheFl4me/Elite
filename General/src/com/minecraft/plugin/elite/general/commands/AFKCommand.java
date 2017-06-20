package com.minecraft.plugin.elite.general.commands;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AFKCommand extends eCommand {

    public AFKCommand() {
        super("afk", "egeneral.afk", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        ePlayer p = ePlayer.get((Player) cs);
        p.setAFK(!p.isAFK());
        if (p.isAFK())
            p.sendMessage(GeneralLanguage.AFK_TRUE);
        else
            p.sendMessage(GeneralLanguage.AFK_FALSE);
        return true;
    }
}