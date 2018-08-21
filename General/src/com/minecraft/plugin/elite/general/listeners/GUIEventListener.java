package com.minecraft.plugin.elite.general.listeners;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.abstracts.GUI;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.api.events.GUIClickEvent;
import com.minecraft.plugin.elite.general.api.special.kits.Kit;
import com.minecraft.plugin.elite.general.api.special.kits.KitGUI;
import com.minecraft.plugin.elite.general.api.special.kits.KitSelectorTool;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIEventListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void clearGUI(InventoryCloseEvent e) {
        GeneralPlayer p = GeneralPlayer.get((Player) e.getPlayer());
        if (p != null) {
            GUI gui = p.getGUI();
            if (gui != null)
                p.removeGUI();
        }
    }

    @EventHandler
    public void callGUIClickEvent(InventoryClickEvent e) {
        GeneralPlayer p = GeneralPlayer.get((Player) e.getWhoClicked());
        if (p.isInGUI()) {
            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
                return;
            e.setCancelled(true);
            GUIClickEvent event = new GUIClickEvent(e);
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    @EventHandler
    public void clickItemInKitGUI(GUIClickEvent e) {
        GeneralPlayer p = e.getPlayer();
        Server server = Server.get();
        ItemStack item = e.getItem();
        ItemMeta itemMeta = item.getItemMeta();
        if(e.getGUI() instanceof KitGUI) {
            KitGUI gui = (KitGUI) e.getGUI();
            if(itemMeta.hasDisplayName()) {
                KitGUI kitgui = (KitGUI) p.getGUI();

                if (itemMeta.getDisplayName().equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(GeneralLanguage.KIT_GUI_BACK))) {
                    if(item.getType() == Material.SUGAR) {
                        p.getPlayer().closeInventory();
                        p.openGUI(kitgui, kitgui.selector(p, 1));
                        return;
                    } else if(item.getType() == Material.REDSTONE) {
                        p.getPlayer().closeInventory();
                        p.openGUI(kitgui, kitgui.shop(p, 1));
                        return;
                    }
                }

                for(Kit kit : Kit.values()) {
                    if(itemMeta.getDisplayName().equalsIgnoreCase(ChatColor.GREEN + kit.getName())) {
                        p.getPlayer().closeInventory();
                        p.openGUI(kitgui, kitgui.selectorSecond(p, kit));
                        return;
                    }
                }
                for(Kit kit : Kit.values()) {
                    if (ChatColor.stripColor(gui.getInventory().getName()).equalsIgnoreCase(kit.getName())) {
                        if(itemMeta.getDisplayName().equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(GeneralLanguage.KIT_GUI_SELECTOR_SELECT))) {
                            p.giveKit(kit);
                            p.getPlayer().closeInventory();
                            return;
                        }
                    }
                }
                if(itemMeta.getDisplayName().equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(GeneralLanguage.KIT_GUI_SHOP_TITLE))) {
                    p.getPlayer().closeInventory();
                    p.openGUI(kitgui, kitgui.shop(p, 1));
                    return;
                }

                if(itemMeta.getDisplayName().equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(GeneralLanguage.KIT_GUI_SETTINGS_TITLE))) {
                    p.getPlayer().closeInventory();
                    Inventory inv = Bukkit.createInventory(null, 36, p.getLanguage().get(GeneralLanguage.KIT_GUI_SETTINGS_TITLE));
                    inv.clear();

                    ItemStack sword = new ItemStack(Material.STONE_SWORD);
                    ItemStack red = new ItemStack(Material.RED_MUSHROOM);
                    ItemStack brown = new ItemStack(Material.BROWN_MUSHROOM);
                    ItemStack bowl = new ItemStack(Material.BOWL);

                    ItemStack kitItem = new ItemStack(Material.NETHER_STAR);
                    server.rename(kitItem, p.getLanguage().get(GeneralLanguage.KIT_GUI_SETTINGS_ITEM_KIT));

                    ItemStack info = new ItemStack(Material.ENCHANTED_BOOK);
                    server.rename(info, p.getLanguage().get(GeneralLanguage.KIT_GUI_SETTINGS_ITEM_INFO));

                    inv.setItem(p.getSlot(GeneralPlayer.SlotType.SWORD), sword);
                    inv.setItem(p.getSlot(GeneralPlayer.SlotType.KIT_ITEM), kitItem);
                    inv.setItem(p.getSlot(GeneralPlayer.SlotType.RED_MUSHROOM), red);
                    inv.setItem(p.getSlot(GeneralPlayer.SlotType.BROWN_MUSHROOM), brown);
                    inv.setItem(p.getSlot(GeneralPlayer.SlotType.BOWL), bowl);
                    inv.addItem(info);

                    p.getPlayer().openInventory(inv);
                    p.setEditing(true);
                    return;
                }

                //shop related
                KitSelectorTool tool = new KitSelectorTool(p.getLanguage());
                if(itemMeta.getDisplayName().equalsIgnoreCase(tool.getName())) {
                    p.getPlayer().closeInventory();
                    p.openGUI(kitgui, kitgui.selector(p, 1));
                    return;
                }
                for(Kit kit : Kit.values()) {
                    if(itemMeta.getDisplayName().equalsIgnoreCase(ChatColor.GOLD + kit.getName())) {
                        p.getPlayer().closeInventory();
                        p.openGUI(kitgui, kitgui.shopSecond(p, kit));
                        return;
                    }
                    if (gui.getInventory().getName().contains(kit.getName())) {
                        if(itemMeta.getDisplayName().equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(GeneralLanguage.KIT_GUI_SHOP_BUY_NORMAL_TOKEN)) && p.getTokens() >= kit.getPrice()) {
                            kit.givePermission(p.getUniqueId(), 1);
                            p.setTokens(p.getTokens() - kit.getPrice());
                            p.getPlayer().getWorld().playSound(p.getPlayer().getLocation(), Sound.LEVEL_UP, 1, 1);
                            p.getPlayer().closeInventory();
                            p.openGUI(kitgui, kitgui.selector(p, 1));
                            return;
                        } else if(itemMeta.getDisplayName().equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(GeneralLanguage.KIT_GUI_SHOP_BUY_PRESTIGE_TOKEN)) && p.getPrestigeTokens() >= 1) {
                            kit.givePermission(p.getUniqueId(), 2);
                            p.setPrestigeTokens(p.getPrestigeTokens() - 1);
                            p.getPlayer().getWorld().playSound(p.getPlayer().getLocation(), Sound.LEVEL_UP, 1, 1);
                            p.getPlayer().closeInventory();
                            p.openGUI(kitgui, kitgui.selector(p, 1));
                            return;
                        }
                    }
                }

                if(item.getType() == Material.PAPER) {
                    String guiName = e.getGUI().getInventory().getName().toLowerCase();
                    int page = Integer.parseInt(ChatColor.stripColor(itemMeta.getDisplayName()));
                    Language lang = e.getGUI().getLanguage();
                    p.getPlayer().closeInventory();
                    if(guiName.equalsIgnoreCase(lang.get(GeneralLanguage.KIT_GUI_SELECTOR_TITLE))) {
                        p.openGUI(gui, gui.selector(p, page));
                        return;
                    }
                    if(guiName.equalsIgnoreCase(lang.get(GeneralLanguage.KIT_GUI_SHOP_TITLE))) {
                        p.openGUI(gui, gui.selector(p, page));
                    }
                }
            }
        }
    }
}