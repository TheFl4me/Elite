package com.minecraft.plugin.elite.general.commands.timeset;

import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class DayCommand extends eCommand {

    public DayCommand() {
        super("day", "egeneral.timeset", true);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        for (World world : Bukkit.getWorlds())
            world.setTime(6000);
        return true;
    }
}
