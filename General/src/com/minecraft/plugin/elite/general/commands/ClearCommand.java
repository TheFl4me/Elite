package com.minecraft.plugin.elite.general.commands;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearCommand extends GeneralCommand {

    public ClearCommand() {
        super("clear", GeneralPermission.CLEAR, false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        if (args.length == 0) {
            p.clear();
            p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.CLEAR)
                    .replaceAll("%p", p.getChatName()));
            return true;
        } else {
            GeneralPlayer z = GeneralPlayer.get(args[0]);
            if (z != null) {
                if (p.hasPermission(GeneralPermission.CLEAR_OTHER)) {
                    if (p.getRank().ordinal() > z.getRank().ordinal()) {
                        z.clear();
                        p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.CLEAR)
                                .replaceAll("%p", z.getChatName()));
                        return true;
                    } else {
                        p.sendMessage(GeneralLanguage.CLEAR_NO_PERMISSION);
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
    }
}