package com.minecraft.plugin.elite.general.commands.admin;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class KillAllCommand extends GeneralCommand {

    public KillAllCommand() {
        super("killall", "egeneral.killall", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        Server server = Server.get();
        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        p.sendMessage(GeneralLanguage.KILL_ALL);
        for(Entity ent : p.getPlayer().getWorld().getEntities()) {
            if(!(ent instanceof HumanEntity)) {
                ent.remove();
            }
        }
        return true;
    }
}
