package com.minecraft.plugin.elite.general.commands.admin;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class KillAllCommand extends eCommand {

    public KillAllCommand() {
        super("killall", "egeneral.killall", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        Server server = Server.get();
        ePlayer p = ePlayer.get((Player) cs);
        p.sendMessage(GeneralLanguage.KILL_ALL);
        for(Entity ent : p.getPlayer().getWorld().getEntities()) {
            if(!(ent instanceof HumanEntity)) {
                ent.remove();
            }
        }
        return true;
    }
}
