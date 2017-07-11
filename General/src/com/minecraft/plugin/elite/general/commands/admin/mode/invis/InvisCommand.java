package com.minecraft.plugin.elite.general.commands.admin.mode.invis;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.api.enums.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class InvisCommand extends GeneralCommand implements TabCompleter {

    public InvisCommand() {
        super("invisible", "egeneral.invis", false);
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String lbl, String[] args) {

        if (args.length == 1 && this.hasPermission(cs)) {
            GeneralPlayer p = GeneralPlayer.get((Player) cs);
            List<String> ranks = new ArrayList<>();
            ranks.add(Rank.MEDIA.getName());
            ranks.add(Rank.BUILDER.getName());
            ranks.add(Rank.SUPPORTER.getName());
            if(p.isModPlus())
                ranks.add(Rank.MOD.getName());
            if(p.isAdmin())
                ranks.add(Rank.MODPLUS .getName());
            return ranks;
        }
        return null;
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        if (args.length == 0) {
            p.setInvis(true);
            return true;
        } else if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "modplus": setInvis(p, Rank.MODPLUS);
                    break;
                case "mod": setInvis(p, Rank.MOD);
                    break;
                case "supporter": setInvis(p, Rank.SUPPORTER);
                    break;
                case "builder": setInvis(p, Rank.BUILDER);
                    break;
                case "media": setInvis(p, Rank.MEDIA);
                    break;
                default: p.sendMessage(GeneralLanguage.INVIS_USAGE);
            }
        }
        return true;
    }

    private void setInvis(GeneralPlayer p, Rank rank) {
        if (p.getRank().ordinal() > rank.ordinal())
            p.setInvis(rank);
        else {
            p.sendMessage(GeneralLanguage.INVIS_NOPERM);
            p.sendMessage(GeneralLanguage.INVIS_USAGE);
        }
    }
}