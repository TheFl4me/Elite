package com.minecraft.plugin.elite.general.commands.chat.message;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.special.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ReplyCommand extends eCommand {

    public ReplyCommand() {
        super("reply", "egeneral.reply", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        ePlayer p = ePlayer.get((Player) cs);
        if (args.length >= 1) {
            if (Message.hasSentMessage(p)) {
                ePlayer z = Message.getTargetFrom(p);
                if (z != null) {
                    StringBuilder message = new StringBuilder();
                    Arrays.stream(args).forEach((arg) -> message.append(arg).append(" "));
                    Message msg = new Message(p, z, message.toString());
                    msg.send();
                    return true;
                } else {
                    Message.remove(p);
                    p.sendMessage(GeneralLanguage.MSG_PARTNER_LEFT);
                    return true;
                }
            } else {
                p.sendMessage(GeneralLanguage.MSG_REPLY_NULL);
                return true;
            }
        } else {
            p.sendMessage(GeneralLanguage.MSG_USAGE_REPLY);
            return true;
        }
    }
}