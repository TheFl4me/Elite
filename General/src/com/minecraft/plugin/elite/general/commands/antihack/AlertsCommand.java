package com.minecraft.plugin.elite.general.commands.antihack;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.antihack.alert.Alert;
import com.minecraft.plugin.elite.general.antihack.alert.AlertManager;
import com.minecraft.plugin.elite.general.antihack.alert.AlertType;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AlertsCommand extends GeneralCommand {

    public AlertsCommand() {
        super("alerts", GeneralPermission.ANTI_HACK_ALERTS, false);
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        if (args.length > 0) {
            GeneralPlayer z = GeneralPlayer.get(args[0]);
            if (z != null) {
                List<Alert> list = AlertManager.getAll(z);
                if (list.isEmpty()) {
                    p.sendMessage(GeneralLanguage.ALERT_NONE);
                    return true;
                } else {
                    Collection<AlertType> typeList = new ArrayList<>();
                    Collection<Alert> cleanList = new ArrayList<>();
                    list.stream().filter(alert -> !typeList.contains(alert.getType())).forEach(alert -> {
                        typeList.add(alert.getType());
                        cleanList.add(alert);
                    });
                    List<String> finalList = cleanList.stream().map(alert -> alert.toString() + "(" + Integer.toString(alert.getDetections()) + ")").collect(Collectors.toList());
                    p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.ALERT_LIST)
                            .replaceAll("%alerts", finalList.toString()));
                    return true;
                }
            } else {
                p.sendMessage(GeneralLanguage.NO_TARGET);
                return true;
            }
        } else {
            p.sendMessage(GeneralLanguage.ALERT_USAGE);
            return true;
        }
    }
}
