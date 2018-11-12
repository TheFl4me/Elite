package com.minecraft.plugin.elite.general.commands.chat.message;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpyCommand extends GeneralCommand {

    public SpyCommand() {
        super("spy", GeneralPermission.MODE_SPY, false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        p.setSpy(!p.isSpy());
        if(p.isSpy())
            p.sendMessage(GeneralLanguage.SPY_ENABLED);
        else
            p.sendMessage(GeneralLanguage.SPY_DISABLED);
        return true;
    }
}
