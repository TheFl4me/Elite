package com.minecraft.plugin.elite.general.commands.admin.staff;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.database.Database;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class ClearStaffCommand extends GeneralCommand implements TabCompleter {

    public ClearStaffCommand() {
        super("clearstaff", GeneralPermission.ADMIN_SET_RANK, true);
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String lbl, String[] args) {
        if (args.length == 1)
            return General.STAFF_SLOTS;
        return null;
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        if (args.length > 0) {
            String slot = args[0].toLowerCase();
            if(General.STAFF_SLOTS.contains(slot)) {
                Database db = General.getDB();
                db.update(General.DB_STAFF, "uuid", "You?", "rank", slot);
                db.update(General.DB_STAFF, "role", "staff", "rank", slot);
                cs.sendMessage(this.getLanguage().get(GeneralLanguage.STAFF_UPDATED));
            } else {
                cs.sendMessage(this.getLanguage().get(GeneralLanguage.STAFF_NULL));
            }
        } else {
            cs.sendMessage(this.getLanguage().get(GeneralLanguage.STAFF_CLEAR_USAGE));
        }
        return true;
    }
}
