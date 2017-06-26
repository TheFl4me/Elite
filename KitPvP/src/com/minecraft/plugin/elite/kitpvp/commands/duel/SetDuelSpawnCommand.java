package com.minecraft.plugin.elite.kitpvp.commands.duel;

import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.kitpvp.KitPvP;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetDuelSpawnCommand extends eCommand {

    public SetDuelSpawnCommand() {
        super("setduelspawn", "ekitpvp.setduel.spawn", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        ePlayer p = ePlayer.get((Player) cs);
        Database db = KitPvP.getDB();
        db.update(KitPvP.DB_DUEL, "loc-x", p.getPlayer().getLocation().getX(), "location", "duelspawn");
        db.update(KitPvP.DB_DUEL, "loc-y", p.getPlayer().getLocation().getY(), "location", "duelspawn");
        db.update(KitPvP.DB_DUEL, "loc-z", p.getPlayer().getLocation().getZ(), "location", "duelspawn");
        p.sendMessage(KitPvPLanguage.DUEL_SET_SPAWN);
        return true;
    }
}
