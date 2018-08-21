package com.minecraft.plugin.elite.general.commands.chat.message;

import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.api.special.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ReplyCommand extends GeneralCommand {

    public ReplyCommand() {
        super("reply", GeneralPermission.CHAT_TELL, false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        if (args.length >= 1) {
            if (Message.hasSentMessage(p)) {
                GeneralPlayer z = Message.getTargetFrom(p);
                if (z != null) {
                    StringBuilder message = new StringBuilder();
                    Arrays.stream(args).forEach((arg) -> message.append(arg).append(" "));
                    Message msg = new Message(p, z, message.toString());
                    msg.send();
                    return true;
                } else {
                    Message.remove(p);
                    p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.MSG_PARTNER_LEFT);
                    return true;
                }
            } else {
                p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.MSG_REPLY_NULL);
                return true;
            }
        } else {
            p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.MSG_USAGE_REPLY);
            return true;
        }
    }
}