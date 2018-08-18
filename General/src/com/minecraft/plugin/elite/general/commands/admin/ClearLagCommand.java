package com.minecraft.plugin.elite.general.commands.admin;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.List;

public class ClearLagCommand extends GeneralCommand {

    public ClearLagCommand() {
        super("clearlag", GeneralPermission.ADMIN_LAG_CLEAR, false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
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