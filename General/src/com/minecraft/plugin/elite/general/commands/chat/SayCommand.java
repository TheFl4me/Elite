package com.minecraft.plugin.elite.general.commands.chat;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SayCommand extends GeneralCommand {

    public SayCommand() {
        super("say", "egeneral.say", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        if (args.length > 0) {
            StringBuilder message = new StringBuilder();
            Arrays.stream(args).forEach((arg) -> message.append(arg).append(" "));
            Bukkit.broadcastMessage("\n  \n  " + p.getChatName() + " > " + ChatColor.RESET + message.toString() + "\n  \n  ");
            return true;
        } else {
            p.sendMessage(GeneralLanguage.SAY_USAGE);
            return true;
        }
    }
}