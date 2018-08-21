package com.minecraft.plugin.elite.survivalgames.commands.admin;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.survivalgames.SurvivalGamesLanguage;
import com.minecraft.plugin.elite.survivalgames.SurvivalGamesPermission;
import com.minecraft.plugin.elite.survivalgames.manager.arena.Arena;
import com.minecraft.plugin.elite.survivalgames.manager.arena.ArenaManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class MapTeleportCommand extends GeneralCommand implements TabCompleter{

    public MapTeleportCommand() {
        super("mapteleport", SurvivalGamesPermission.MAP_EDIT, false);
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String lbl, String[] args) {
        if(args.length == 1)
            return ArenaManager.getAll().stream().map(Arena::getName).collect(Collectors.toList());
        return null;
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {
        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        if(args.length > 0) {
            Arena arena = ArenaManager.get(args[0]);
            if(arena != null) {
                p.getPlayer().teleport(arena.getCenter());
                p.getPlayer().sendMessage(p.getLanguage().get(SurvivalGamesLanguage.MAP_TELEPORT_CONFIRM)
                        .replaceAll("%arena", arena.getName()));
                return true;
            } else {
                p.sendMessage(SurvivalGamesLanguage.ARENA_NULL);
                return true;
            }
        } else {
            p.sendMessage(SurvivalGamesLanguage.MAP_TELEPORT_USAGE);
            return true;
        }
    }
}
