package com.minecraft.plugin.elite.general.listeners.punish;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.events.GUIClickEvent;
import com.minecraft.plugin.elite.general.punish.PunishManager;
import com.minecraft.plugin.elite.general.punish.report.Report;
import com.minecraft.plugin.elite.general.punish.report.ReportGUI;
import com.minecraft.plugin.elite.general.punish.report.ReportManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReportEventListener implements Listener {
	
	public static Map<UUID, UUID> reportedPlayer = new HashMap<>();
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent e) {
		final GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
		if(p.isMod()) {
			String list = ReportManager.getReportList(false, p.getLanguage());
			if(list != null)
				p.getPlayer().sendMessage(list);
		}
	}

	@EventHandler
	public void onReportItemClick(GUIClickEvent e) {
		if(e.getGUI() instanceof ReportGUI) {
			final GeneralPlayer p = e.getPlayer();
			ItemStack item = e.getItem();
			ItemMeta itemMeta = item.getItemMeta();
			if(itemMeta.hasDisplayName()) {
				String name = e.getGUI().getInventory().getName().substring(9);
				String name_final = name.substring(0, name.length() - 5);
				GeneralPlayer z = GeneralPlayer.get(name_final);
				if(z != null) {
					Report report = new Report(z.getUniqueId(), p.getUniqueId(), p.getLanguage().getNode(itemMeta.getDisplayName(), true), System.currentTimeMillis());
					report.saveToDB();
					PunishManager.addSentReport(p.getUniqueId());
					Bukkit.getOnlinePlayers().stream().filter(players -> players.hasPermission(GeneralPermission.PUNISH_REPORT_LIST.toString())).forEach(players -> players.sendMessage(GeneralPlayer.get(players).getLanguage().get(GeneralLanguage.REPORT_STAFF)
                            .replaceAll("%hacker", z.getName())
                            .replaceAll("%reason", itemMeta.getDisplayName().substring(2))
                            .replaceAll("%reporter", p.getName())));
					p.sendMessage(GeneralLanguage.REPORT_CONFIRMED);
					p.getPlayer().closeInventory();
					reportedPlayer.put(p.getUniqueId(), z.getUniqueId());
					Bukkit.getScheduler().runTaskLater(General.getPlugin(), () -> reportedPlayer.remove(p.getUniqueId()), 6000);
				}
			}
		}
	}
}
