package com.minecraft.plugin.elite.general.listeners.punish;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.api.events.GUIClickEvent;
import com.minecraft.plugin.elite.general.punish.info.InfoGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;

public class InfoEventListener implements Listener {

    @EventHandler
    public void onClickBack(GUIClickEvent e) {
        GeneralPlayer p = e.getPlayer();
        ItemStack item = e.getItem();
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta.hasDisplayName() && e.getGUI() instanceof InfoGUI) {
            if (itemMeta.getDisplayName().equalsIgnoreCase(p.getLanguage().get(com.minecraft.plugin.elite.general.GeneralLanguage.GUI_BACK))) {
                InfoGUI gui = new InfoGUI(p.getLanguage(), ((InfoGUI) e.getGUI()).getPlayer().getUniqueId());
                if(item.getType() == Material.SUGAR) {
                    p.getPlayer().closeInventory();
                    try {
                        p.openGUI(gui, gui.info());
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                        p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.DB_CHECK_FAIL);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClick(GUIClickEvent e) {
        GeneralPlayer p = e.getPlayer();
        ItemStack item = e.getItem();
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta.hasDisplayName() && e.getGUI() instanceof InfoGUI) {

            InfoGUI gui = new InfoGUI(p.getLanguage(), ((InfoGUI) e.getGUI()).getPlayer().getUniqueId());
            String itemName = itemMeta.getDisplayName();

            if(itemName.equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(com.minecraft.plugin.elite.general.GeneralLanguage.INFO_GUI_MUTES))) {
                p.getPlayer().closeInventory();
                p.openGUI(gui, gui.mutes(1));
                return;
            }

            if(itemName.equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(com.minecraft.plugin.elite.general.GeneralLanguage.INFO_GUI_BANS))) {
                p.getPlayer().closeInventory();
                p.openGUI(gui, gui.bans(1));
                return;
            }

            if(itemName.equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(com.minecraft.plugin.elite.general.GeneralLanguage.INFO_GUI_REPORTS))) {
                p.getPlayer().closeInventory();
                p.openGUI(gui, gui.reports(1));
                return;
            }

            if(itemName.equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(com.minecraft.plugin.elite.general.GeneralLanguage.INFO_GUI_ALTS))) {
                p.getPlayer().closeInventory();
                p.openGUI(gui, gui.alts(1));
                return;
            }

            if(item.getType() == Material.SKULL_ITEM && !itemName.equalsIgnoreCase(ChatColor.GREEN + "ONLINE") && !itemName.equalsIgnoreCase(ChatColor.RED + "OFFLINE") && !itemName.equalsIgnoreCase(p.getLanguage().get(com.minecraft.plugin.elite.general.GeneralLanguage.INFO_GUI_ALTS))) {
                p.getPlayer().closeInventory();
                p.getPlayer().performCommand("checkinfo " + ChatColor.stripColor(itemName));
            }

            if(item.getType() == Material.PAPER) {
                String guiName = e.getGUI().getInventory().getName().toLowerCase();
                int page = Integer.parseInt(ChatColor.stripColor(itemName));
                Language lang = e.getGUI().getLanguage();
                p.getPlayer().closeInventory();
                if(guiName.contains(lang.get(com.minecraft.plugin.elite.general.GeneralLanguage.INFO_GUI_REPORTS).toLowerCase()))
                    p.openGUI(gui, gui.reports(page));

                else if(guiName.contains(lang.get(com.minecraft.plugin.elite.general.GeneralLanguage.INFO_GUI_ALTS).toLowerCase()))
                    p.openGUI(gui, gui.alts(page));

                else if(guiName.contains(lang.get(com.minecraft.plugin.elite.general.GeneralLanguage.INFO_GUI_BANS).toLowerCase()))
                    p.openGUI(gui, gui.bans(page));

                else if(guiName.contains(lang.get(com.minecraft.plugin.elite.general.GeneralLanguage.INFO_GUI_MUTES).toLowerCase()))
                    p.openGUI(gui, gui.mutes(page));
            }
        }
    }
}
