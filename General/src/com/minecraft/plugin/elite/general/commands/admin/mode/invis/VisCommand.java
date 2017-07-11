package com.minecraft.plugin.elite.general.commands.admin.mode.invis;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VisCommand extends GeneralCommand {

    public VisCommand() {
        super("visible", "egeneral.invis", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        if (!p.isInvis()) {
            p.sendMessage(GeneralLanguage.INVIS_VIS_ALREADY);
            return true;
        }
        p.setInvis(false);
        return true;
    }
}