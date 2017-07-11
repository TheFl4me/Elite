package com.minecraft.plugin.elite.general.commands.admin;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand extends GeneralCommand {

    public PingCommand() {
        super("ping", "egeneral.ping", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        if (args.length == 0) {
            sendPingInfo(p, p);
            return true;
        } else if (args.length > 0) {
            GeneralPlayer z = GeneralPlayer.get(args[0]);
            if (z != null) {
                sendPingInfo(p, z);
                return true;
            } else {
                p.sendMessage(GeneralLanguage.NO_TARGET);
                return true;
            }
        }
        return true;
    }

    private void sendPingInfo(GeneralPlayer p, GeneralPlayer z) {
        int ping = z.getPing();
        ChatColor color;
        if (!z.isLagging())
            color = ChatColor.GREEN;
        else if (ping <= 300)
            color = ChatColor.GOLD;
        else
            color = ChatColor.RED;
        p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.PING)
                .replaceAll("%z", z.getName())
                .replaceAll("%ping", color + Integer.toString(ping)));
    }
}