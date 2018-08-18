package com.minecraft.plugin.elite.general.commands.spawn;

import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends GeneralCommand {

    public SpawnCommand() {
        super("spawn", GeneralPermission.SPAWN, false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        p.getPlayer().teleport(p.getPlayer().getWorld().getSpawnLocation());
        if (!p.isAdminMode() && !p.isBuilding())
            p.clear();
        return true;
    }
}