package com.minecraft.plugin.elite.general.commands.admin;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class StatusCommand extends eCommand {

    public StatusCommand() {
        super("status", "egeneral.status", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        ChatColor mainColor = ChatColor.GRAY;
        ChatColor secColor = ChatColor.WHITE;
        ePlayer p = ePlayer.get((Player) cs);
        World world = p.getPlayer().getWorld();
        long maxMemory = Runtime.getRuntime().maxMemory();
        Server server = Server.get();
        String status = mainColor + General.SPACER + "\n" +
                "" + mainColor + "Online players: " + secColor + Bukkit.getOnlinePlayers().size() + "\n" +
                "" + mainColor + "Pending tasks: " + secColor + Bukkit.getScheduler().getPendingTasks().size() + "\n" +
                "" + mainColor + "TPS: " + secColor + Double.toString(server.getTPS()) + "\n" +
                "" + mainColor + "Entities (World = " + world.getName() + ": " + secColor + world.getEntities().size() + "\n" +
                "" + mainColor + "Mobs (World = " + world.getName() + ": " + secColor + world.getEntitiesByClass(LivingEntity.class).size() + "\n" +
                "" + mainColor + "Free memory: " + secColor + Runtime.getRuntime().freeMemory() / 1048576 + "MB" + "\n" +
                "" + mainColor + "Used memory: " + secColor + ((Runtime.getRuntime().totalMemory() / 1048576) - (Runtime.getRuntime().freeMemory() / 1048576)) + "MB" + "\n" +
                "" + mainColor + "Maximum memory: " + secColor + (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory / 1048576 + "MB") + "\n" +
                "" + mainColor + "Total memory: " + secColor + Runtime.getRuntime().totalMemory() / 1048576 + "MB\n" +
                "" + mainColor + General.SPACER;
        cs.sendMessage(status);
        return true;
    }
}