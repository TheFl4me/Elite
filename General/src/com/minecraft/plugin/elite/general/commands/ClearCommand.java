package com.minecraft.plugin.elite.general.commands;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearCommand extends eCommand {

    public ClearCommand() {
        super("clear", "egeneral.clear", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        ePlayer p = ePlayer.get((Player) cs);
        if (args.length == 0) {
            p.clear();
            p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.CLEAR)
                    .replaceAll("%p", p.getChatName()));
            return true;
        } else if (args.length > 0) {
            ePlayer z = ePlayer.get(args[0]);
            if (z != null) {
                if (p.getPlayer().hasPermission("egeneral.clear.other")) {
                    if (p.getRank().ordinal() > z.getRank().ordinal()) {
                        z.clear();
                        p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.CLEAR)
                                .replaceAll("%p", z.getChatName()));
                        return true;
                    } else {
                        p.sendMessage(GeneralLanguage.CLEAR_NOPERM);
                        return true;
                    }
                } else {
                    p.clear();
                    p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.CLEAR)
                            .replaceAll("%p", p.getChatName()));
                    return true;
                }
            } else {
                p.sendMessage(GeneralLanguage.NO_TARGET);
                return true;
            }
        }
        return true;
    }
}