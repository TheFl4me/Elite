package com.minecraft.plugin.elite.general.commands.admin.mode;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildCommand extends GeneralCommand {

    public BuildCommand() {
        super("build", GeneralPermission.MODE_BUILD, false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        p.setBuilding(!p.isBuilding());
        if (p.isBuilding())
            p.sendMessage(GeneralLanguage.BUILD_ENABLED);
        else
            p.sendMessage(GeneralLanguage.BUILD_DISABLED);
        return true;
    }
}