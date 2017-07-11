package com.minecraft.plugin.elite.general.commands.chat.message;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.api.special.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TellCommand extends GeneralCommand {

    public TellCommand() {
        super("tell", "egeneral.tell", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        if (args.length >= 2) {
            GeneralPlayer z = GeneralPlayer.get(args[0]);
            if (z != null) {
                StringBuilder message = new StringBuilder();
                for(int i = 1; i < args.length; i++)
                    message.append(args[i]).append(" ");
                Message msg = new Message(p, z, message.toString());
                msg.send();
                return true;
            } else {
                p.sendMessage(GeneralLanguage.NO_TARGET);
                return true;
            }
        } else {
            p.sendMessage(GeneralLanguage.MSG_USAGE_TELL);
            return true;
        }
    }
}