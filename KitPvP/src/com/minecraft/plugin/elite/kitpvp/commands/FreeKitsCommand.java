package com.minecraft.plugin.elite.kitpvp.commands;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.kitpvp.KitPvP;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import com.minecraft.plugin.elite.kitpvp.KitPvPPermission;
import com.minecraft.plugin.elite.kitpvp.manager.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreeKitsCommand extends GeneralCommand {

    public FreeKitsCommand() {
        super("freekits", KitPvPPermission.KIT_FREE, true);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {
        boolean free = Kit.values().length != KitPvP.getFreeKits().size();
        KitPvP.setAllKitsFree(free);
        System.out.println(this.getLanguage().get((free ? KitPvPLanguage.KIT_FREE_TRUE : KitPvPLanguage.KIT_FREE_FALSE)));
        for(Player all : Bukkit.getOnlinePlayers()) {
            GeneralPlayer p = GeneralPlayer.get(all);
            if(free)
                p.sendMessage(KitPvPLanguage.KIT_FREE_TRUE);
            else
                p.sendMessage(KitPvPLanguage.KIT_FREE_FALSE);
        }
        return true;
    }
}
