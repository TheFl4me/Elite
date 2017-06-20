package com.minecraft.plugin.elite.general.commands.admin;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.List;

public class ClearLagCommand extends eCommand {

    public ClearLagCommand() {
        super("clearlag", "egeneral.clearlag", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        ePlayer p = ePlayer.get((Player) cs);
        p.sendMessage(GeneralLanguage.CLEAR_LAG);
        for (World world : Bukkit.getWorlds()) {
            List<Entity> entList = world.getEntities();
            entList.forEach((drops) -> {
                if (drops instanceof Item)
                    drops.remove();
            });
        }
        return true;
    }
}