package com.minecraft.plugin.elite.general.punish.report;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.abstracts.GUI;
import com.minecraft.plugin.elite.general.api.enums.Language;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ReportGUI extends GUI {

	public ReportGUI(Language lang) {
		super(lang);
	}

	public Inventory main(String target) {
		this.setInventory(Bukkit.createInventory(null, 27, this.getLanguage().get(GeneralLanguage.REPORT_GUI_TITLE).replaceAll("%z", target)));
		Server server = Server.get();
		ItemStack glass = new ItemStack(Material.THIN_GLASS, 1);
		server.rename(glass, "");
		
		ItemStack hack = new ItemStack(Material.DIAMOND_SWORD);
		server.rename(hack, this.getLanguage().get(GeneralLanguage.REPORT_GUI_HACKING));
		
		ItemStack abuse = new ItemStack(Material.TNT);
		server.rename(abuse, this.getLanguage().get(GeneralLanguage.REPORT_GUI_BUG));
		
		ItemStack team = new ItemStack(Material.IRON_CHESTPLATE);
		server.rename(team, this.getLanguage().get(GeneralLanguage.REPORT_GUI_TEAM));
		
		ItemStack boost = new ItemStack(Material.LAVA_BUCKET);
		server.rename(boost, this.getLanguage().get(GeneralLanguage.REPORT_GUI_STATS));
		
		this.fill(glass);
		
		this.getInventory().setItem(10, hack);
		this.getInventory().setItem(11, abuse);
		this.getInventory().setItem(12, team);
		this.getInventory().setItem(13, boost);
		return this.getInventory();
	}
}
