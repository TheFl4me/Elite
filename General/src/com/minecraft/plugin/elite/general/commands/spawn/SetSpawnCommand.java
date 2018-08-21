package com.minecraft.plugin.elite.general.commands.spawn;

import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand extends GeneralCommand {

    public SetSpawnCommand() {
        super("setspawn", GeneralPermission.SPAWN_SET, false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        Location loc = p.getPlayer().getLocation();
        p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.SPAWN_SET);
        p.getPlayer().getWorld().setSpawnLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        return true;
    }
}