package com.minecraft.plugin.elite.general.commands;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SuicideCommand extends GeneralCommand {

    public SuicideCommand() {
        super("suicide", "egeneral.suicide", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer
                .get((Player) cs);
        p.getPlayer().setHealth(0);
        return true;
    }
}
