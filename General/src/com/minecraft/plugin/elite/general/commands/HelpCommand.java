package com.minecraft.plugin.elite.general.commands;

import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand extends GeneralCommand {

    public HelpCommand() {
        super("help", GeneralPermission.HELP, false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.HELP);
        return true;
    }
}