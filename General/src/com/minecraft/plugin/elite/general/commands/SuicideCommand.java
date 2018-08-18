package com.minecraft.plugin.elite.general.commands;

import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SuicideCommand extends GeneralCommand {

    public SuicideCommand() {
        super("suicide", GeneralPermission.SUICIDE, false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {
        ((Player) cs).setHealth(0);
        return true;
    }
}
