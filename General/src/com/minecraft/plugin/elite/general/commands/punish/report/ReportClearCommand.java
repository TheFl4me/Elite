package com.minecraft.plugin.elite.general.commands.punish.report;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.punish.report.ReportManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportClearCommand extends eCommand {

    public ReportClearCommand() {
        super("reportclear", "epunish.report.clear", false);
    }

    @SuppressWarnings("deprecation")
    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        ePlayer p = ePlayer.get((Player) cs);
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
