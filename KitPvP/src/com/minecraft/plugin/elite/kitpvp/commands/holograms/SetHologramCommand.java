package com.minecraft.plugin.elite.kitpvp.commands.holograms;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.kitpvp.KitPvP;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import com.minecraft.plugin.elite.kitpvp.KitPvPPermission;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHologramCommand extends GeneralCommand {

    public SetHologramCommand() {
        super("sethologram", KitPvPPermission.HOLOGRAM_SET, false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {
        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        Database db = KitPvP.getDB();
        Location loc = p.getPlayer().getLocation();
        if(args.length > 0) {
            String name = args[0].toLowerCase();
            if(name.equalsIgnoreCase("feast") || name.equalsIgnoreCase("ehg")) {
                if(db.containsValue(KitPvP.DB_HOLOGRAMS, "name", name)) {
                    db.update(KitPvP.DB_HOLOGRAMS, "locx", loc.getX(), "name", name);
                    db.update(KitPvP.DB_HOLOGRAMS, "locy", loc.getY(), "name", name);
                    db.update(KitPvP.DB_HOLOGRAMS, "locz", loc.getZ(), "name", name);
                } else {
                    db.execute("INSERT INTO " + KitPvP.DB_HOLOGRAMS + " (name, locx, locy, locz) VALUES (?, ?, ?, ?);", name, loc.getX(), loc.getY(), loc.getZ());
                }
                p.getPlayer().sendMessage(p.getLanguage().get(KitPvPLanguage.HOLOGRAM_SET)
                        .replaceAll("%holo", name));
                KitPvP.reloadHolograms();
                return true;
            } else {
                p.sendMessage(KitPvPLanguage.HOLOGRAM_USAGE);
                return true;
            }
        } else {
            p.sendMessage(KitPvPLanguage.HOLOGRAM_USAGE);
            return true;
        }
    }
}
