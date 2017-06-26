package com.minecraft.plugin.elite.kitpvp.commands.duel;

import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.kitpvp.KitPvP;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetDuelLocationCommand extends eCommand {

    public SetDuelLocationCommand() {
        super("setduellocation", "ekitpvp.setduel.location", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {
        ePlayer p = ePlayer.get((Player) cs);
        Database db = KitPvP.getDB();
        if(args.length > 0) {
            try {
                int index = Integer.parseInt(args[0]);
                if(index == 1 || index == 2) {
                    db.update(KitPvP.DB_DUEL, "loc-x", p.getPlayer().getLocation().getX(), "location", "loc" + args[0]);
                    db.update(KitPvP.DB_DUEL, "loc-y", p.getPlayer().getLocation().getY(), "location", "loc" + args[0]);
                    db.update(KitPvP.DB_DUEL, "loc-z", p.getPlayer().getLocation().getZ(), "location", "loc" + args[0]);
                    p.getPlayer().sendMessage(p.getLanguage().get(KitPvPLanguage.DUEL_SET_LOCATION_SET)
                            .replaceAll("%loc", args[0]));
                    return true;
                } else {
                    p.sendMessage(KitPvPLanguage.DUEL_SET_LOCATION_USAGE);
                    return true;
                }
            } catch (IllegalArgumentException e) {
                p.sendMessage(KitPvPLanguage.DUEL_SET_LOCATION_USAGE);
                return true;
            }
        } else {
            p.sendMessage(KitPvPLanguage.DUEL_SET_LOCATION_USAGE);
            return true;
        }
    }
}
