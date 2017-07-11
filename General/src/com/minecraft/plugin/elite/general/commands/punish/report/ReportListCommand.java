package com.minecraft.plugin.elite.general.commands.punish.report;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.punish.report.ReportManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportListCommand extends GeneralCommand {

	public ReportListCommand() {
		super("reportlist", "egeneral.report.list", false);
	}

	@SuppressWarnings("deprecation")
	public boolean execute(CommandSender cs, Command cmd, String[] args) {

		GeneralPlayer p = GeneralPlayer.get((Player) cs);
		String list = ReportManager.getReportList(true, p.getLanguage());
		if(list != null) {
			p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.REPORT_LIST)
					.replaceAll("%list", list));
			return true;
		} else {
			p.sendMessage(GeneralLanguage.REPORT_ERROR_NONE);
			return true;
		}
	}
}
