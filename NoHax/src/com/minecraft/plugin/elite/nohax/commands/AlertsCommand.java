package com.minecraft.plugin.elite.nohax.commands;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.nohax.NoHax;
import com.minecraft.plugin.elite.nohax.NoHaxLanguage;
import com.minecraft.plugin.elite.nohax.manager.alert.Alert;
import com.minecraft.plugin.elite.nohax.manager.alert.AlertManager;
import com.minecraft.plugin.elite.nohax.manager.alert.AlertType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AlertsCommand extends eCommand {

    public AlertsCommand() {
        super("alerts", NoHax.ALERTS_PERM, false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        ePlayer p = ePlayer.get((Player) cs);
        if (args.length > 0) {
            ePlayer z = ePlayer.get(args[0]);
            if (z != null) {
                List<Alert> list = AlertManager.getAll(z);
                if (list.isEmpty()) {
                    p.sendMessage(NoHaxLanguage.ALERT_NONE);
                    return true;
                } else {
                    Collection<AlertType> typeList = new ArrayList<>();
                    Collection<Alert> cleanList = new ArrayList<>();
                    list.stream().filter(alert -> !typeList.contains(alert.getType())).forEach(alert -> {
                        typeList.add(alert.getType());
                        cleanList.add(alert);
                    });
                    List<String> finalList = cleanList.stream().map(alert -> alert.toString() + "(" + Integer.toString(alert.getDetections()) + ")").collect(Collectors.toList());
                    p.getPlayer().sendMessage(p.getLanguage().get(NoHaxLanguage.ALERT_LIST)
                            .replaceAll("%alerts", finalList.toString()));
                    return true;
                }
            } else {
                p.sendMessage(GeneralLanguage.NO_TARGET);
                return true;
            }
        } else {
            p.sendMessage(NoHaxLanguage.ALERT_USAGE);
            return true;
        }
    }
}
