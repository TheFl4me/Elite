package com.minecraft.plugin.elite.general.commands.punish.report;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.listeners.punish.ReportEventListener;
import com.minecraft.plugin.elite.general.punish.report.ReportGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCommand extends GeneralCommand {

	public ReportCommand() {
		super("report", "egeneral.report", false);
	}

	public boolean execute(CommandSender cs, Command cmd, String[] args) {

		GeneralPlayer p = GeneralPlayer.get((Player) cs);
		if(args.length > 0) {
			GeneralPlayer z = GeneralPlayer.get(args[0]);
			if(z != null) {
				if(!p.getUniqueId().equals(z.getUniqueId())) {
					if(!z.isSupporter()) {
						if(ReportEventListener.reportedPlayer.containsKey(p.getUniqueId()) && ReportEventListener.reportedPlayer.get(p.getUniqueId()).equals(z.getUniqueId())) {
							p.sendMessage(GeneralLanguage.REPORT_COOLDOWN);
							return true;
						} else {								
							ReportGUI report = new ReportGUI(p.getLanguage());
							p.openGUI(report, report.main(z.getName()));
							return true;
						}
					} else {
						p.sendMessage(GeneralLanguage.REPORT_ERROR_STAFF);
						return true;
					}
				} else {
					p.sendMessage(GeneralLanguage.REPORT_ERROR_SELF);
					return true;
				}
			} else {
				p.sendMessage(GeneralLanguage.REPORT_NOT_ONLINE);
				return true;
			}
		} else {
			p.sendMessage(GeneralLanguage.REPORT_USAGE);
			return true;
		}
	}
}