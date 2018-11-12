package com.minecraft.plugin.elite.general.commands.antihack;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SilentCommand extends GeneralCommand {

    public SilentCommand() {
        super("silent", GeneralPermission.ANTI_HACK_ALERTS, false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        p.showAlerts(!p.canViewAlerts());
        if (p.canViewAlerts())
            p.sendMessage(GeneralLanguage.SILENT_VIS);
        else
            p.sendMessage(GeneralLanguage.SILENT_HIDDEN);
        return true;
    }
}