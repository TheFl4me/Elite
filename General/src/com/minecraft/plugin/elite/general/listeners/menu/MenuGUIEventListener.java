package com.minecraft.plugin.elite.general.listeners.menu;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.api.events.GUIClickEvent;
import com.minecraft.plugin.elite.general.api.special.menu.MenuGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuGUIEventListener implements Listener {

    @EventHandler
    public void clickBackInGUI(GUIClickEvent e) {
        GeneralPlayer p = e.getPlayer();
        ItemStack item = e.getItem();
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta.hasDisplayName() && p.getGUI() instanceof MenuGUI) {
            if (itemMeta.getDisplayName().equalsIgnoreCase(p.getLanguage().get(GeneralLanguage.MENU_GUI_BACK))) {
                MenuGUI menu = new MenuGUI(p.getLanguage());
                if(item.getType() == Material.SUGAR) {
                    p.getPlayer().closeInventory();
                    p.openGUI(menu, menu.main());
                } else if(item.getType() == Material.REDSTONE) {
                    p.getPlayer().closeInventory();
                    p.openGUI(menu, menu.faqMain());
                }
            }
        }
    }

    @EventHandler
    public void clickItemInGUI(GUIClickEvent e) {
        GeneralPlayer p = e.getPlayer();
        ItemStack item = e.getItem();
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta.hasDisplayName() && p.getGUI() instanceof MenuGUI) {
            MenuGUI menu = new MenuGUI(p.getLanguage());
            String itemName = itemMeta.getDisplayName();
            Server server = Server.get();

            //Staff list related
            if(itemName.equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(GeneralLanguage.MENU_GUI_STAFF))) {
                p.getPlayer().closeInventory();
                p.openGUI(menu, menu.staffMain(p.getName()));
                return;
            }
            if (ChatColor.stripColor(itemName).equalsIgnoreCase(p.getLanguage().get(GeneralLanguage.MENU_GUI_STAFF_YOU)) || itemName.equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(GeneralLanguage.MENU_GUI_APPLICATION))) {
                p.getPlayer().closeInventory();
                p.openGUI(menu, menu.application());
                return;
            }
            if(itemName.equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(GeneralLanguage.MENU_GUI_APPLICATION_APPLY))) {
                p.getPlayer().closeInventory();
                p.removeGUI();
                p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.APPLICATION_LINK).replaceAll("%domain", server.getDomain()));
                return;
            }
            if(itemName.equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(GeneralLanguage.MENU_GUI_STAFF_ADMINS))) {
                p.getPlayer().closeInventory();
                p.openGUI(menu, menu.staffAdmin());
                return;
            }
            if(itemName.equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(GeneralLanguage.MENU_GUI_STAFF_MODS))) {
                p.getPlayer().closeInventory();
                p.openGUI(menu, menu.staffMod());
                return;
            }
            if(itemName.equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(GeneralLanguage.MENU_GUI_STAFF_SUPPORTERS))) {
                p.getPlayer().closeInventory();
                p.openGUI(menu, menu.staffSupporter());
                return;
            }
            if(itemName.equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(GeneralLanguage.MENU_GUI_STAFF_BUILDERS))) {
                p.getPlayer().closeInventory();
                p.openGUI(menu, menu.staffBuilder());
                return;
            }

            //FAQ related
            if(itemName.equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(GeneralLanguage.MENU_GUI_FAQ))) {
                p.getPlayer().closeInventory();
                p.openGUI(menu, menu.faqMain());
                return;
            }
            if(itemName.equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(GeneralLanguage.MENU_GUI_FAQ_RULES))) {
                p.getPlayer().closeInventory();
                p.openGUI(menu, menu.faqRules());
                return;
            }
            if(itemName.equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(GeneralLanguage.MENU_GUI_FAQ_LEVELS))) {
                p.getPlayer().closeInventory();
                p.openGUI(menu, menu.faqLevels());
                return;
            }
            if(itemName.equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(GeneralLanguage.MENU_GUI_FAQ_STORE))) {
                p.getPlayer().closeInventory();
                p.openGUI(menu, menu.faqStore());
                return;
            }
            if(itemName.equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(GeneralLanguage.MENU_GUI_FAQ_TEAMSPEAK))) {
                p.getPlayer().closeInventory();
                p.openGUI(menu, menu.faqTeamspeak());
                return;
            }
            if(itemName.equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(GeneralLanguage.MENU_GUI_FAQ_UNBAN))) {
                p.getPlayer().closeInventory();
                p.openGUI(menu, menu.faqUnban());
                return;
            }
            if(itemName.equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(GeneralLanguage.MENU_GUI_FAQ_SUPPORT))) {
                p.getPlayer().closeInventory();
                p.getPlayer().performCommand("support");
                return;
            }

            //stats related
            if(itemName.equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(GeneralLanguage.MENU_GUI_STATS))) {
                p.getPlayer().closeInventory();
                p.openGUI(menu, menu.stats(p));
                return;
            }
            if(itemName.equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(GeneralLanguage.MENU_GUI_STATS_PRESTIGE_SET))) {
                p.getPlayer().closeInventory();
                p.prestige();
                return;
            }

            //UpdateLog related
            if(itemName.equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(GeneralLanguage.MENU_GUI_UPDATE))) {
                p.getPlayer().closeInventory();
                p.openGUI(menu, menu.updateLog());
            }

            //Achievements related
            if(itemName.equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(GeneralLanguage.MENU_GUI_ACHIEVEMENTS))) {
                p.getPlayer().closeInventory();
                p.openGUI(menu, menu.achievements(p, 1));
            }

            if(item.getType() == Material.PAPER) {
                try {
                    String guiName = e.getGUI().getInventory().getName().toLowerCase();
                    int page = Integer.parseInt(ChatColor.stripColor(itemMeta.getDisplayName()));
                    Language lang = e.getGUI().getLanguage();
                    p.getPlayer().closeInventory();
                    if(guiName.equalsIgnoreCase(lang.get(GeneralLanguage.MENU_GUI_ACHIEVEMENTS))) {
                        p.openGUI(menu, menu.achievements(p, page));
                    }
                } catch (NumberFormatException e1) {
                    //this exist so that it does not active when player click a page item which does not act as a gui page
                }
            }
        }
    }
}
