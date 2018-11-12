package com.minecraft.plugin.elite.general.commands.kits;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.api.special.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreeKitsCommand extends GeneralCommand {

    public FreeKitsCommand() {
        super("freekits", GeneralPermission.KIT_FREE, true);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {
        boolean free = Kit.values().length != General.getFreeKits().size();
        General.setAllKitsFree(free);
        System.out.println(this.getLanguage().get((free ? GeneralLanguage.KIT_FREE_TRUE : GeneralLanguage.KIT_FREE_FALSE)));
        for(Player all : Bukkit.getOnlinePlayers()) {
            GeneralPlayer p = GeneralPlayer.get(all);
            if(free)
                p.sendMessage(GeneralLanguage.KIT_FREE_TRUE);
            else
                p.sendMessage(GeneralLanguage.KIT_FREE_FALSE);
        }
        return true;
    }
}
