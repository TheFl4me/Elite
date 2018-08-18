package com.minecraft.plugin.elite.kitpvp.manager.kits;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.abstracts.GUI;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.kitpvp.KitPvP;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import com.minecraft.plugin.elite.kitpvp.manager.KitPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KitGUI extends GUI {

	public KitGUI(Language lang) {
		super(lang);
	}

	private ItemStack glass() {
		Server server = Server.get();
		ItemStack not = new ItemStack(Material.STAINED_GLASS_PANE);
		server.rename(not, " ");
		return not;
	}

	public Inventory selector(KitPlayer p, int page) {
		List<Kit> kits = new ArrayList<>();
		for(Kit kit : Kit.values())
			if(p.hasKitPermission(kit) && kit.getIcon(ChatColor.RED) != null)
				kits.add(kit);


		this.buildPageType(KitPvPLanguage.KIT_GUI_SELECTOR_TITLE, page, kits.size(), this.glass());
		Server server = Server.get();
		
		ItemStack shop = new ItemStack(Material.GOLD_INGOT);
		server.rename(shop, this.getLanguage().get(KitPvPLanguage.KIT_GUI_SHOP_TITLE));

		ItemStack settings = new ItemStack(Material.REDSTONE_COMPARATOR);
		server.rename(settings, this.getLanguage().get(KitPvPLanguage.KIT_GUI_SETTINGS_TITLE));

		for(int i = this.getLastSlot(page); i < this.getNextSlot(page); i++) {
			if(i >= kits.size())
				break;
			Kit kit = kits.get(i);
			ItemStack item = kit.getIcon(ChatColor.GREEN);
			if(kit.getPermissionType(p.getUniqueId()) == 1 && p.getLevel() < kit.getLevel() && !p.isMasterPrestige() && !KitPvP.getFreeKits().contains(kit))
				server.rename(item, item.getItemMeta().getDisplayName() + "\n" + this.getLanguage().get(KitPvPLanguage.KIT_GUI_SELECTOR_LOCKED).replaceAll("%level", Integer.toString(kit.getLevel())));
			this.getInventory().addItem(item);
		}
		this.getInventory().setItem(3, settings);
		this.getInventory().setItem(4, this.glass());
		this.getInventory().setItem(5, shop);
		return this.getInventory();
	}
	
	public Inventory selectorSecond(KitPlayer p, Kit kit) {
		this.build(54, ChatColor.RED + kit.getName());
		Server server = Server.get();
		
		ItemStack back = new ItemStack(Material.SUGAR);
		server.rename(back, this.getLanguage().get(KitPvPLanguage.KIT_GUI_BACK));
    	
    	ItemStack description = new ItemStack(Material.BOOK);
    	server.rename(description, this.getLanguage().get(KitPvPLanguage.KIT_GUI_DESCRIPTION).replaceAll("%description", kit.getDescription(this.getLanguage())));
		
		ItemStack ability = new ItemStack(Material.COMPASS);
		server.rename(ability, this.getLanguage().get(KitPvPLanguage.KIT_GUI_ABILITY).replaceAll("%ability", kit.getAbility(this.getLanguage())));
		
		ItemStack start = new ItemStack(Material.SIGN);
		server.rename(start, this.getLanguage().get(KitPvPLanguage.KIT_GUI_ITEMS));

		ItemStack play;

		if(kit.getPermissionType(p.getUniqueId()) == 1 && p.getLevel() < kit.getLevel() && !p.isMasterPrestige() && !KitPvP.getFreeKits().contains(kit)) {
			if(p.getPrestigeTokens() > 0) {
				play = new ItemStack(Material.DIAMOND);
				server.rename(play, this.getLanguage().get(KitPvPLanguage.KIT_GUI_SHOP_BUY_PRESTIGE_TOKEN)
						.replaceAll("%prestigetokens", Long.toString(p.getPrestigeTokens())));
			} else {
				play = this.glass();
			}
		} else {
			play = new ItemStack(Material.FLINT_AND_STEEL);
			server.rename(play, this.getLanguage().get(KitPvPLanguage.KIT_GUI_SELECTOR_SELECT));
		}

		this.fill(this.glass());
		this.getInventory().setItem(4, back);
		this.getInventory().setItem(21, description);
		this.getInventory().setItem(23, ability);
		this.getInventory().setItem(36, start);
		this.getInventory().setItem(44, play);
		this.getInventory().setItem(45, kit.getItem());
		return this.getInventory();
	}
	
	public Inventory shop(KitPlayer p, int page) {
		List<Kit> kits = new ArrayList<>();
		for(Kit kit : Kit.values())
			if(!p.hasKitPermission(kit) && kit.getIcon(ChatColor.GOLD) != null)
				kits.add(kit);

		this.buildPageType(KitPvPLanguage.KIT_GUI_SHOP_TITLE, page, kits.size(), this.glass());

		Server server = Server.get();
		
		ItemStack kitSelector = new ItemStack(Material.CHEST);
		server.rename(kitSelector, this.getLanguage().get(KitPvPLanguage.KIT_GUI_SELECTOR_TITLE));

		for(int i = this.getLastSlot(page); i < this.getNextSlot(page); i++) {
			if(i >= kits.size())
				break;
			Kit kit = kits.get(i);
			if(p.getLevel() < kit.getLevel()) {
				ItemStack item = kit.getIcon(ChatColor.GOLD);
				server.rename(item, item.getItemMeta().getDisplayName() + "\n" + this.getLanguage().get(KitPvPLanguage.KIT_GUI_SELECTOR_LOCKED).replaceAll("%level", Integer.toString(kit.getLevel())));
				this.getInventory().addItem(item);
			} else
				this.getInventory().addItem(kit.getIcon(ChatColor.GOLD));
		}

		this.getInventory().setItem(4, kitSelector);
		return this.getInventory();
	}
	
	public Inventory shopSecond(GeneralPlayer p, Kit kit) {
		this.build(54, ChatColor.GOLD + kit.getName());
		Server server = Server.get();
		
		ItemStack back = new ItemStack(Material.REDSTONE);
		server.rename(back, this.getLanguage().get(KitPvPLanguage.KIT_GUI_BACK));

		ItemStack description = new ItemStack(Material.BOOK);
		server.rename(description, this.getLanguage().get(KitPvPLanguage.KIT_GUI_DESCRIPTION).replaceAll("%description", kit.getDescription(this.getLanguage())));

		ItemStack ability = new ItemStack(Material.COMPASS);
		server.rename(ability, this.getLanguage().get(KitPvPLanguage.KIT_GUI_ABILITY).replaceAll("%ability", kit.getAbility(this.getLanguage())));

		ItemStack start = new ItemStack(Material.SIGN);
		server.rename(start, this.getLanguage().get(KitPvPLanguage.KIT_GUI_ITEMS));
		
		ItemStack buy = new ItemStack(Material.GOLD_INGOT);

		if(p.getLevel() < kit.getLevel())
			server.rename(buy, this.getLanguage().get(KitPvPLanguage.KIT_GUI_SHOP_INFERIOR_LEVEL)
					.replaceAll("%level", Integer.toString(kit.getLevel())));
		else if(p.getTokens() >= (long) kit.getPrice())
			server.rename(buy, this.getLanguage().get(KitPvPLanguage.KIT_GUI_SHOP_BUY_NORMAL_TOKEN)
					.replaceAll("%price", Integer.toString(kit.getPrice()))
					.replaceAll("%tokens", Long.toString(p.getTokens())));
		else
			server.rename(buy, this.getLanguage().get(KitPvPLanguage.KIT_GUI_SHOP_INFERIOR_TOKENS)
					.replaceAll("%price", Integer.toString(kit.getPrice()))
					.replaceAll("%tokens", Long.toString(p.getTokens())));
		
		ItemStack prestige = new ItemStack(Material.DIAMOND);
		server.rename(prestige, this.getLanguage().get(KitPvPLanguage.KIT_GUI_SHOP_BUY_PRESTIGE_TOKEN)
				.replaceAll("%prestigetokens", Long.toString(p.getPrestigeTokens())));
		
		this.fill(this.glass());
		this.getInventory().setItem(4, back);
		this.getInventory().setItem(21, description);
		this.getInventory().setItem(23, ability);
		this.getInventory().setItem(36, start);
		this.getInventory().setItem(44, buy);
		
		if(p.getPrestigeTokens() >= 1)
			this.getInventory().setItem(43, prestige);
		
		this.getInventory().setItem(45, kit.getItem());
		return this.getInventory();
	}
}