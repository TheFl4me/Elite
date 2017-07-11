package com.minecraft.plugin.elite.survivalgames.commands.admin;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.survivalgames.SurvivalGames;
import com.minecraft.plugin.elite.survivalgames.SurvivalGamesLanguage;
import com.minecraft.plugin.elite.survivalgames.manager.arena.Arena;
import com.minecraft.plugin.elite.survivalgames.manager.arena.ArenaManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BorderCommand extends GeneralCommand {

    public BorderCommand() {
        super("border", "esurvivalgames.border", false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        Database db = SurvivalGames.getDB();
        Arena arena = ArenaManager.get(p.getPlayer().getWorld());
        if(arena != null) {
            if(args.length > 1) {
                try {
                    int minsize = Integer.parseInt(args[0]);
                    int maxsize = Integer.parseInt(args[1]);
                    db.update(SurvivalGames.DB_ARENAS, "minsize", minsize, "name", arena.getName());
                    db.update(SurvivalGames.DB_ARENAS, "maxsize", maxsize, "name", arena.getName());
                    p.sendMessage(SurvivalGamesLanguage.SIZE_SET);
                    return true;
                } catch (IllegalArgumentException e) {
                    p.sendMessage(SurvivalGamesLanguage.SIZE_INVALID);
                    return true;
                }
            } else {
                p.sendMessage(SurvivalGamesLanguage.SIZE_USAGE);
                return true;
            }
        } else {
            p.sendMessage(SurvivalGamesLanguage.ARENA_NULL);
            return true;
        }
    }
}
