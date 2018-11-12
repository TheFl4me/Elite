package com.minecraft.plugin.elite.general.commands.punish.report;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.punish.report.ReportManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportClearCommand extends GeneralCommand {

    public ReportClearCommand() {
        super("reportclear", GeneralPermission.PUNISH_REPORT_CLEAR, false);
    }

    @SuppressWarnings("deprecation")
    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        if(args.length > 0) {
            OfflinePlayer z = Bukkit.getOfflinePlayer(args[0]);
            if(!ReportManager.getReports(z.getUniqueId()).isEmpty()) {
                ReportManager.clearReports(z.getUniqueId());
                p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.REPORT_CLEAR).replaceAll("%p", z.getName()));
                return true;
            } else {
                p.sendMessage(GeneralLanguage.REPORT_CLEAR_EMPTY);
                return true;
            }
        } else {
            p.sendMessage(GeneralLanguage.REPORT_CLEAR_USAGE);
            return true;
        }
    }
}
