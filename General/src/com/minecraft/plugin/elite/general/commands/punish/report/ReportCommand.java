package com.minecraft.plugin.elite.general.commands.punish.report;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.listeners.punish.ReportEventListener;
import com.minecraft.plugin.elite.general.punish.report.ReportGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCommand extends eCommand {

	public ReportCommand() {
		super("report", "eban.report", false);
	}

	public boolean execute(CommandSender cs, Command cmd, String[] args) {
		
		ePlayer p = ePlayer.get((Player) cs);
		if(args.length > 0) {
			ePlayer z = ePlayer.get(args[0]);
			if(z != null) {
				if(!p.getUniqueId().equals(z.getUniqueId())) {
					if(!z.isMod()) {
						if(ReportEventListener.reportedPlayer.containsKey(p.getUniqueId()) && ReportEventListener.reportedPlayer.get(p.getUniqueId()).equalsIgnoreCase(z.getName())) {
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