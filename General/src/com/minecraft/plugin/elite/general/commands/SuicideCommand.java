package com.minecraft.plugin.elite.general.commands;

import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SuicideCommand extends eCommand {

    public SuicideCommand() {
        super("suicide", "egeneral.suicide", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        ePlayer p = ePlayer.get((Player) cs);
        p.getPlayer().setHealth(0);
        return true;
    }
}
