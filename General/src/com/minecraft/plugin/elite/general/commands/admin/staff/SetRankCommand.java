package com.minecraft.plugin.elite.general.commands.admin.staff;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.api.enums.Rank;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetRankCommand extends GeneralCommand implements TabCompleter {

    public SetRankCommand() {
        super("setrank", "egeneral.setrank", true);
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String lbl, String[] args) {
        if (args.length == 2) {
            List<String> list = new ArrayList<>();
            Arrays.stream(Rank.values()).forEach((group) -> list.add(group.getName()));
            return list;
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        if (args.length > 1) {
            try {
                OfflinePlayer z = Bukkit.getOfflinePlayer(args[0]);
                Rank rank = Rank.valueOf(args[1].toUpperCase());
                Rank.set(z, rank);
                cs.sendMessage(this.getLanguage().get(GeneralLanguage.RANK_SET)
                        .replaceAll("%z", z.getName())
                        .replaceAll("%rank", rank.getDisplayName()));
                if(z.isOnline()) {
                    GeneralPlayer zp = GeneralPlayer.get(z.getUniqueId());
                    zp.getPlayer().sendMessage(zp.getLanguage().get(GeneralLanguage.RANK_SET_YOU)
                            .replaceAll("%rank", zp.getRank().getDisplayName()));
                }
                return true;
            } catch (IllegalArgumentException e) {
                cs.sendMessage(this.getLanguage().get(GeneralLanguage.RANK_INVALID));
                return true;
            }
        } else {
            cs.sendMessage(this.getLanguage().get(GeneralLanguage.RANK_USAGE));
            return true;
        }
    }
}