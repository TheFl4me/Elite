package com.minecraft.plugin.elite.kitpvp.listeners.basic;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.api.events.GUIClickEvent;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import com.minecraft.plugin.elite.kitpvp.manager.KitPlayer;
import com.minecraft.plugin.elite.kitpvp.manager.duel.custom.DuelGUI;
import com.minecraft.plugin.elite.kitpvp.manager.duel.custom.DuelSetup;
import com.minecraft.plugin.elite.kitpvp.manager.kits.Kit;
import com.minecraft.plugin.elite.kitpvp.manager.kits.KitGUI;
import com.minecraft.plugin.elite.kitpvp.manager.kits.KitSelectorTool;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIEventListener implements Listener {
	
	@EventHandler
	public void clickItemInGUI(GUIClickEvent e) {
		GeneralPlayer p = e.getPlayer();
		Server server = Server.get();
		KitPlayer kp = KitPlayer.get(p.getUniqueId());
		ItemStack item = e.getItem();
		ItemMeta itemMeta = item.getItemMeta();
		if(e.getGUI() instanceof KitGUI) {
			KitGUI gui = (KitGUI) e.getGUI();
			if(itemMeta.hasDisplayName()) {
				KitGUI kitgui = (KitGUI) p.getGUI();

				if (itemMeta.getDisplayName().equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(KitPvPLanguage.KIT_GUI_BACK))) {
					if(item.getType() == Material.SUGAR) {
						p.getPlayer().closeInventory();
						p.openGUI(kitgui, kitgui.selector(kp, 1));
						return;
					} else if(item.getType() == Material.REDSTONE) {
						p.getPlayer().closeInventory();
						p.openGUI(kitgui, kitgui.shop(kp, 1));
						return;
					}
				}

				for(Kit kit : Kit.values()) {
					if(itemMeta.getDisplayName().equalsIgnoreCase(ChatColor.GREEN + kit.getName())) {
						p.getPlayer().closeInventory();
						p.openGUI(kitgui, kitgui.selectorSecond(kp, kit));
						return;
					}
				}
				for(Kit kit : Kit.values()) {
					if (ChatColor.stripColor(gui.getInventory().getName()).equalsIgnoreCase(kit.getName())) {
						if(itemMeta.getDisplayName().equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(KitPvPLanguage.KIT_GUI_SELECTOR_SELECT))) {
							kp.giveKit(kit);
							p.getPlayer().closeInventory();
							return;
						}
					}
				}
				if(itemMeta.getDisplayName().equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(KitPvPLanguage.KIT_GUI_SHOP_TITLE))) {
					p.getPlayer().closeInventory();
					p.openGUI(kitgui, kitgui.shop(kp, 1));
					return;
				}

				if(itemMeta.getDisplayName().equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(KitPvPLanguage.KIT_GUI_SETTINGS_TITLE))) {
					p.getPlayer().closeInventory();
					Inventory inv = Bukkit.createInventory(null, 36, p.getLanguage().get(KitPvPLanguage.KIT_GUI_SETTINGS_TITLE));
					inv.clear();

					ItemStack sword = new ItemStack(Material.STONE_SWORD);
					ItemStack red = new ItemStack(Material.RED_MUSHROOM);
					ItemStack brown = new ItemStack(Material.BROWN_MUSHROOM);
					ItemStack bowl = new ItemStack(Material.BOWL);

					ItemStack kitItem = new ItemStack(Material.NETHER_STAR);
					server.rename(kitItem, p.getLanguage().get(KitPvPLanguage.KIT_GUI_SETTINGS_ITEM_KIT));

					ItemStack info = new ItemStack(Material.ENCHANTED_BOOK);
					server.rename(info, p.getLanguage().get(KitPvPLanguage.KIT_GUI_SETTINGS_ITEM_INFO));

					inv.setItem(kp.getSlot(KitPlayer.SlotType.SWORD), sword);
					inv.setItem(kp.getSlot(KitPlayer.SlotType.KIT_ITEM), kitItem);
					inv.setItem(kp.getSlot(KitPlayer.SlotType.RED_MUSHROOM), red);
					inv.setItem(kp.getSlot(KitPlayer.SlotType.BROWN_MUSHROOM), brown);
					inv.setItem(kp.getSlot(KitPlayer.SlotType.BOWL), bowl);
					inv.addItem(info);

					p.getPlayer().openInventory(inv);
					kp.setEditing(true);
					return;
				}

				//shop related
				KitSelectorTool tool = new KitSelectorTool(p.getLanguage());
				if(itemMeta.getDisplayName().equalsIgnoreCase(tool.getName())) {
					p.getPlayer().closeInventory();
					p.openGUI(kitgui, kitgui.selector(kp, 1));
					return;
				}
				for(Kit kit : Kit.values()) {
					if(itemMeta.getDisplayName().equalsIgnoreCase(ChatColor.GOLD + kit.getName())) {
						p.getPlayer().closeInventory();
						p.openGUI(kitgui, kitgui.shopSecond(p, kit));
						return;
					}
					if (gui.getInventory().getName().contains(kit.getName())) {
						if(itemMeta.getDisplayName().equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(KitPvPLanguage.KIT_GUI_SHOP_BUY_NORMAL_TOKEN)) && p.getTokens() >= kit.getPrice()) {
							kit.givePermission(p.getUniqueId(), 1);
							p.setTokens(p.getTokens() - kit.getPrice());
							p.getPlayer().getWorld().playSound(p.getPlayer().getLocation(), Sound.LEVEL_UP, 1, 1);
							p.getPlayer().closeInventory();
							p.openGUI(kitgui, kitgui.selector(kp, 1));
							return;
						} else if(itemMeta.getDisplayName().equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(KitPvPLanguage.KIT_GUI_SHOP_BUY_PRESTIGE_TOKEN)) && p.getPrestigeTokens() >= 1) {
							kit.givePermission(p.getUniqueId(), 2);
							p.setPrestigeTokens(p.getPrestigeTokens() - 1);
							p.getPlayer().getWorld().playSound(p.getPlayer().getLocation(), Sound.LEVEL_UP, 1, 1);
							p.getPlayer().closeInventory();
							p.openGUI(kitgui, kitgui.selector(kp, 1));
							return;
						}
					}
				}

				if(item.getType() == Material.PAPER) {
					String guiName = e.getGUI().getInventory().getName().toLowerCase();
					int page = Integer.parseInt(ChatColor.stripColor(itemMeta.getDisplayName()));
					Language lang = e.getGUI().getLanguage();
					p.getPlayer().closeInventory();
					if(guiName.equalsIgnoreCase(lang.get(KitPvPLanguage.KIT_GUI_SELECTOR_TITLE))) {
						p.openGUI(gui, gui.selector(kp, page));
						return;
					}
					if(guiName.equalsIgnoreCase(lang.get(KitPvPLanguage.KIT_GUI_SHOP_TITLE))) {
						p.openGUI(gui, gui.selector(kp, page));
						return;
					}
				}
			}
		} else if(e.getGUI() instanceof DuelGUI) {
			DuelGUI duelgui = (DuelGUI) p.getGUI();
			DuelSetup setup = duelgui.getSetup();
			if(p.getPlayer().getUniqueId().equals(setup.getEditor().getUniqueId())) {
				if(itemMeta.hasDisplayName()) {
					if(itemMeta.getDisplayName().equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(KitPvPLanguage.DUEL_GUI_CANCEL))) {
						setup.setEditing(false);
						setup.abort();
					}
					else if(itemMeta.getDisplayName().equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(KitPvPLanguage.DUEL_GUI_DONE)))
						setup.switchRoles();
					else if(itemMeta.getDisplayName().equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(KitPvPLanguage.DUEL_GUI_EDIT)))
						setup.edit();
					else if(itemMeta.getDisplayName().equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(KitPvPLanguage.DUEL_GUI_ACCEPT)))
						setup.accept();
					if(setup.getPhase(kp) != DuelSetup.Phase.PENDING) {
						int i = e.getSlot();
						Material mat = setup.next(item, i);
						if(mat != null) {
							ItemStack nextItem = new ItemStack(mat);
							if (nextItem.getType() == Material.BROWN_MUSHROOM)
								Server.get().rename(nextItem, p.getLanguage().get(KitPvPLanguage.DUEL_GUI_RE_CRAFT));
							setup.change(i, nextItem);
						}
					}
					return;
				}
				if(setup.getPhase(kp) != DuelSetup.Phase.PENDING) {
					int i = e.getSlot();
					Material mat = setup.next(item, i);
					if(mat != null) {
						ItemStack nextItem = new ItemStack(mat);
						if (nextItem.getType() == Material.BROWN_MUSHROOM)
							Server.get().rename(nextItem, p.getLanguage().get(KitPvPLanguage.DUEL_GUI_RE_CRAFT));
						setup.change(i, nextItem);
					}
				}
			}
		}
	}
}
