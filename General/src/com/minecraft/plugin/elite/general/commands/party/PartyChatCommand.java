package com.minecraft.plugin.elite.general.commands.party;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.special.party.Party;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class PartyChatCommand extends eCommand {

    public PartyChatCommand() {
        super("partychat", "egeneral.partychat", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        ePlayer p = ePlayer.get((Player) cs);
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
