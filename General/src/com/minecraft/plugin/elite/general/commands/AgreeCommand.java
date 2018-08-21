package com.minecraft.plugin.elite.general.commands;

import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AgreeCommand extends GeneralCommand {

    public AgreeCommand() {
        super("agree", GeneralPermission.AGREE, false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        if (!p.hasAgreed()) {
            p.setAgree(true);
            p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.RULES_AGREED);
            return true;
        } else {
            p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.RULES_AGREED_ALREADY);
            return true;
        }
    }
}