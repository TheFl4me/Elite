package com.minecraft.plugin.elite.general.commands;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.api.enums.Language;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LanguageCommand extends GeneralCommand implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {

        if(args.length == 0) {
            List<String> languages = new ArrayList<>();
            for(Language language : Language.values())
                languages.add(language.toString());
            return languages;
        }
        return null;
    }

    public LanguageCommand() {
        super("language", GeneralPermission.LANGUAGE, false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        if(args.length > 0) {
            try {
                p.setLanguage(Language.valueOf(args[0].toUpperCase()));
                p.sendMessage(GeneralLanguage.LANGUAGE);
                return true;
            } catch(IllegalArgumentException e) {
                p.sendMessage(GeneralLanguage.LANGUAGE_NULL);
                return true;
            }
        } else {
            p.sendMessage(GeneralLanguage.LANGUAGE_USAGE);
            return true;
        }
    }
}
