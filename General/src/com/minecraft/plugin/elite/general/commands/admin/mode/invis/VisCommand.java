package com.minecraft.plugin.elite.general.commands.admin.mode.invis;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VisCommand extends eCommand {

    public VisCommand() {
        super("visible", "egeneral.invis", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        ePlayer p = ePlayer.get((Player) cs);
        if (!p.isInvis()) {
            p.sendMessage(GeneralLanguage.INVIS_VIS_ALREADY);
            return true;
        }
        p.setInvis(false);
        return true;
    }
}