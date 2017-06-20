package com.minecraft.plugin.elite.kitpvp.commands;

import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.kitpvp.KitPvP;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import com.minecraft.plugin.elite.kitpvp.manager.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreeKitsCommand extends eCommand {

    public FreeKitsCommand() {
        super("freekits", "ekitpvp.freekits", true);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        Language lang = Language.ENGLISH;
        if(cs instanceof Player)
            lang = ePlayer.get((Player) cs).getLanguage();

        boolean free = Kit.values().length != KitPvP.getFreeKits().size();
        KitPvP.setAllKitsFree(free);
        System.out.println(lang.get((free ? KitPvPLanguage.KIT_FREE_TRUE : KitPvPLanguage.KIT_FREE_FALSE)));
        for(Player all : Bukkit.getOnlinePlayers()) {
            ePlayer p = ePlayer.get(all);
            if(free)
                p.sendMessage(KitPvPLanguage.KIT_FREE_TRUE);
            else
                p.sendMessage(KitPvPLanguage.KIT_FREE_FALSE);
        }
        return true;
    }
}
