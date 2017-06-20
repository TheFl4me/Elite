package com.minecraft.plugin.elite.general.commands.admin.staff;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.database.Database;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class ClearStaffCommand extends eCommand implements TabCompleter {

    public ClearStaffCommand() {
        super("clearstaff", "egeneral.setrank", true);
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String lbl, String[] args) {
        if (args.length == 1)
            return General.STAFF_SLOTS;
        return null;
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        Language lang = Language.ENGLISH;
        if(cs instanceof Player)
            lang = ePlayer.get((Player) cs).getLanguage();
        if (args.length > 0) {
            String slot = args[0].toLowerCase();
            if(General.STAFF_SLOTS.contains(slot)) {
                Database db = General.getDB();
                db.update(General.DB_STAFF, "uuid", "You?", "rank", slot);
                db.update(General.DB_STAFF, "role", "staff", "rank", slot);
                cs.sendMessage(lang.get(GeneralLanguage.STAFF_UPDATED));
                return true;
            } else {
                cs.sendMessage(lang.get(GeneralLanguage.STAFF_NULL));
                return true;
            }
        } else {
            cs.sendMessage(lang.get(GeneralLanguage.STAFF_CLEAR_USAGE));
            return true;
        }
    }
}
