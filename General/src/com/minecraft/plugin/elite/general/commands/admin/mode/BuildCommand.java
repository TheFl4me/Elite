package com.minecraft.plugin.elite.general.commands.admin.mode;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildCommand extends eCommand {

    public BuildCommand() {
        super("build", "egeneral.build", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        ePlayer p = ePlayer.get((Player) cs);
        p.setBuilding(!p.isBuilding());
        if (p.isBuilding())
            p.sendMessage(GeneralLanguage.BUILD_ENABLED);
        else
            p.sendMessage(GeneralLanguage.BUILD_DISABLED);
        return true;
    }
}