package com.minecraft.plugin.elite.general.commands.clan;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.special.clan.Clan;
import com.minecraft.plugin.elite.general.database.Database;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ClanChatCommand extends eCommand {

    public ClanChatCommand() {
        super("clanchat", "egeneral.clan", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        ePlayer p = ePlayer.get((Player) cs);
        if (args.length > 0) {
            Clan clan = p.getClan();
            if (clan != null) {
                StringBuilder message = new StringBuilder();
                Arrays.stream(args).forEach((arg) -> message.append(arg).append(" "));
                clan.sendMessage(message.toString(), p);
                Database db = General.getDB();
                db.execute("INSERT INTO " + General.DB_CHATLOGS + " (date, uuid, name, message, type) VALUES (?, ?, ?, ?, ?);", System.currentTimeMillis(), p.getUniqueId(), p.getName(), message.toString(), "clan");
                return true;
            } else {
                p.sendMessage(GeneralLanguage.CLAN_NONE);
                return true;
            }
        } else {
            p.sendMessage(GeneralLanguage.CLAN_CHAT_USAGE);
            return true;
        }
    }
}