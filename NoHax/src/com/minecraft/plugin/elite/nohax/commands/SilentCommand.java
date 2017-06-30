package com.minecraft.plugin.elite.nohax.commands;

import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.nohax.NoHax;
import com.minecraft.plugin.elite.nohax.NoHaxLanguage;
import com.minecraft.plugin.elite.nohax.manager.HaxPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SilentCommand extends eCommand {

    public SilentCommand() {
        super("silent", NoHax.ALERTS_PERM, false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        HaxPlayer p = HaxPlayer.get((Player) cs);
        p.showAlerts(!p.canViewAlerts());
        if (p.canViewAlerts())
            p.sendMessage(NoHaxLanguage.SILENT_VIS);
        else
            p.sendMessage(NoHaxLanguage.SILENT_HIDDEN);
        return true;
    }
}