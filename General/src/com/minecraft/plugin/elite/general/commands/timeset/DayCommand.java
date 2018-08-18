package com.minecraft.plugin.elite.general.commands.timeset;

import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class DayCommand extends GeneralCommand {

    public DayCommand() {
        super("day", GeneralPermission.TIME_SET, true);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        for (World world : Bukkit.getWorlds())
            world.setTime(6000);
        return true;
    }
}
