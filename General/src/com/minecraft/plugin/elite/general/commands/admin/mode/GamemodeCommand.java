package com.minecraft.plugin.elite.general.commands.admin.mode;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class GamemodeCommand extends GeneralCommand implements TabCompleter {

    public GamemodeCommand() {
        super("gamemode", GeneralPermission.MODE_GAME, false);
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String lbl, String[] args) {
        if (args.length == 1)
            return Arrays.asList("survival", "creative", "adventure", "spectator");
        return null;
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        if (args.length == 1) {
            setGameMode(p, args[0]);
            return true;
        } else if(args.length > 1) {
            GeneralPlayer z = GeneralPlayer.get(args[1]);
            if (z != null) {
                if (p.hasPermission(GeneralPermission.MODE_GAME_OTHER) && p.getRank().ordinal() > z.getRank().ordinal()) {
                    setGameMode(z, args[0]);
                    p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.GAMEMODE_SET_OTHER)
                            .replaceAll("%p", z.getChatName())
                            .replaceAll("%gm", z.getPlayer().getGameMode().toString()));
                } else {
                    setGameMode(p, args[0]);
                }
            } else {
                p.sendMessage(GeneralLanguage.NO_TARGET);
            }
            return true;
        } else {
            p.sendMessage(GeneralLanguage.GAMEMODE_USAGE);
            return true;
        }
    }

    private void setGameMode(GeneralPlayer p, String gm) {
        switch (gm.toLowerCase()) {
            case "survival":
            case "0":
                p.getPlayer().setGameMode(GameMode.SURVIVAL);
                p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.GAMEMODE_SET_YOU)
                        .replaceAll("%gm", p.getPlayer().getGameMode().toString()));
                break;
            case "creative":
            case "1":
                p.getPlayer().setGameMode(GameMode.CREATIVE);
                p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.GAMEMODE_SET_YOU)
                        .replaceAll("%gm", p.getPlayer().getGameMode().toString()));
                break;
            case "adventure":
            case "2":
                p.getPlayer().setGameMode(GameMode.ADVENTURE);
                p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.GAMEMODE_SET_YOU)
                        .replaceAll("%gm", p.getPlayer().getGameMode().toString()));
                break;
            case "spectator":
            case "3":
                p.getPlayer().setGameMode(GameMode.SPECTATOR);
                p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.GAMEMODE_SET_YOU)
                        .replaceAll("%gm", p.getPlayer().getGameMode().toString()));
                break;
            default:
                p.sendMessage(GeneralLanguage.GAMEMODE_USAGE);
        }
    }
}