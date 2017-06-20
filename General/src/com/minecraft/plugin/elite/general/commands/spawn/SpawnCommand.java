package com.minecraft.plugin.elite.general.commands.spawn;

import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends eCommand {

    public SpawnCommand() {
        super("spawn", "egeneral.spawn", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        ePlayer p = ePlayer.get((Player) cs);
        p.getPlayer().teleport(p.getPlayer().getWorld().getSpawnLocation());
        if (!p.isAdminMode() && !p.isBuilding())
            p.clear();
        return true;
    }
}