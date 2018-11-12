package com.minecraft.plugin.elite.general.commands.party;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.api.special.party.Party;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class PartyChatCommand extends GeneralCommand {

    public PartyChatCommand() {
        super("partychat", GeneralPermission.PARTY, false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        if (args.length > 0) {
            Party party = p.getParty();
            if (party != null) {
                StringBuilder message = new StringBuilder();
                Arrays.stream(args).forEach((arg) -> message.append(arg).append(" "));
                party.sendMessage(message.toString(), p);
                return true;
            } else {
                p.sendMessage(GeneralLanguage.PARTY_NONE_YOU);
                return true;
            }
        } else {
            p.sendMessage(GeneralLanguage.PARTY_CHAT_USAGE);
            return true;
        }
    }


}
