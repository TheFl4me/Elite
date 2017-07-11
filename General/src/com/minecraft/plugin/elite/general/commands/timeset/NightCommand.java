package com.minecraft.plugin.elite.general.commands.timeset;

import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class NightCommand extends GeneralCommand {

    public NightCommand() {
        super("night", "egeneral.timeset", true);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        for (World world : Bukkit.getWorlds())
            world.setTime(18000);
        return true;
    }
}