package com.minecraft.plugin.elite.general.commands.admin;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class SpeedCommand extends GeneralCommand implements TabCompleter {

    public SpeedCommand() {
        super("speed", GeneralPermission.ADMIN_SPEED, false);
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String lbl, String[] args) {
        if (args.length == 1)
            return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        return null;
    }

    @SuppressWarnings("deprecation")
    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        if (args.length > 0) {
            try {
                int speed = Integer.parseInt(args[0]);
                String speedMsg;
                if (p.getPlayer().isOnGround()) {
                    speedMsg = p.getLanguage().get(GeneralLanguage.SPEED_WALK)
                            .replaceAll("%speed", Integer.toString(speed));
                    p.getPlayer().setWalkSpeed((float) speed / 10.0f);
                } else {
                    speedMsg = p.getLanguage().get(GeneralLanguage.SPEED_FLY)
                            .replaceAll("%speed", Integer.toString(speed));
                    p.getPlayer().setFlySpeed((float) speed / 10.0f);
                }
                p.getPlayer().setAllowFlight((float) speed / 10.0f < 3);
                p.getPlayer().sendMessage(speedMsg);
            } catch (IllegalArgumentException e) {
                p.sendMessage(GeneralLanguage.SPEED_USAGE);
                return true;
            }
        } else {
            p.sendMessage(GeneralLanguage.SPEED_USAGE);
            return true;
        }
        return true;
    }
}