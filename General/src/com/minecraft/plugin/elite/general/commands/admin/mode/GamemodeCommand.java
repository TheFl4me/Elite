package com.minecraft.plugin.elite.general.commands.admin.mode;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class GamemodeCommand extends eCommand implements TabCompleter {

    public GamemodeCommand() {
        super("gamemode", "egeneral.gamemode", false);
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String lbl, String[] args) {
        if (args.length == 1)
            return Arrays.asList("survival", "creative", "adventure", "spectator");
        return null;
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        ePlayer p = ePlayer.get((Player) cs);
        if (args.length == 0) {
            switch (p.getPlayer().getGameMode()) {
                case SURVIVAL:
                    p.getPlayer().setGameMode(GameMode.CREATIVE);
                    break;
                case CREATIVE:
                    p.getPlayer().setGameMode(GameMode.SURVIVAL);
                    break;
                case ADVENTURE:
                    p.getPlayer().setGameMode(GameMode.SURVIVAL);
                    break;
                case SPECTATOR:
                    p.getPlayer().setGameMode(GameMode.CREATIVE);
            }
            p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.GAMEMODE_SET_YOU)
                    .replaceAll("%gm", p.getPlayer().getGameMode().toString()));
            return true;
        } else if (args.length == 1) {
            setGameMode(p, args[0]);
            return true;
        } else if (args.length > 1) {
            ePlayer z = ePlayer.get(args[1]);
            if (z != null) {
                if (p.getPlayer().hasPermission("egeneral.gamemode.other") && p.getRank().ordinal() > z.getRank().ordinal()) {
                    setGameMode(z, args[0]);
                    p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.GAMEMODE_SET_OTHER)
                            .replaceAll("%p", z.getChatName())
                            .replaceAll("%gm", z.getPlayer().getGameMode().toString()));
                    return true;
                } else {
                    setGameMode(p, args[0]);
                    return true;
                }
            } else {
                p.sendMessage(GeneralLanguage.NO_TARGET);
                return true;
            }
        }
        return true;
    }

    private void setGameMode(ePlayer p, String gm) {
        switch (gm.toLowerCase()) {
            case "survival":
                p.getPlayer().setGameMode(GameMode.SURVIVAL);
                break;
            case "creative":
                p.getPlayer().setGameMode(GameMode.CREATIVE);
                break;
            case "adventure":
                p.getPlayer().setGameMode(GameMode.ADVENTURE);
                break;
            case "spectator":
                p.getPlayer().setGameMode(GameMode.SPECTATOR);
                break;
            case "0":
                p.getPlayer().setGameMode(GameMode.SURVIVAL);
                break;
            case "1":
                p.getPlayer().setGameMode(GameMode.CREATIVE);
                break;
            case "2":
                p.getPlayer().setGameMode(GameMode.ADVENTURE);
                break;
            case "3":
                p.getPlayer().setGameMode(GameMode.SPECTATOR);
                break;
            default:
                p.sendMessage(GeneralLanguage.GAMEMODE_USAGE);
        }
        switch (gm.toLowerCase()) {
            case "survival":
            case "creative":
            case "adventure":
            case "spectator":
            case "0":
            case "1":
            case "2":
            case "3":
                p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.GAMEMODE_SET_YOU)
                        .replaceAll("%gm", p.getPlayer().getGameMode().toString()));
        }
    }
}