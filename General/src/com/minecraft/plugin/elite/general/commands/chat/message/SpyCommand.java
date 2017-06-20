package com.minecraft.plugin.elite.general.commands.chat.message;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpyCommand extends eCommand {

    public SpyCommand() {
        super("spy", "egeneral.spy", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        ePlayer p = ePlayer.get((Player) cs);
        p.setSpy(!p.isSpy());
        if(p.isSpy())
            p.sendMessage(GeneralLanguage.SPY_ENABLED);
        else
            p.sendMessage(GeneralLanguage.SPY_DISABLED);
        return true;
    }
}
