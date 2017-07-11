package com.minecraft.plugin.elite.survivalgames.listeners.players;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.events.GUIClickEvent;
import com.minecraft.plugin.elite.survivalgames.manager.alive.PlayerGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerGUIEventListener implements Listener {

    @EventHandler
    public void onClickPlayerHead(GUIClickEvent e) {
        GeneralPlayer p = e.getPlayer();
        ItemStack item = e.getItem();
        ItemMeta itemMeta = item.getItemMeta();
        if(e.getGUI() instanceof PlayerGUI) {
            if(itemMeta.hasDisplayName()) {
                PlayerGUI gui = (PlayerGUI) e.getGUI();
                if(item.getType() == Material.SKULL_ITEM) {
                    p.getPlayer().closeInventory();
                    GeneralPlayer z = GeneralPlayer.get(ChatColor.stripColor(itemMeta.getDisplayName()));
                    if(z != null)
                        p.getPlayer().teleport(z.getPlayer());
                    return;
                }

                if(item.getType() == Material.PAPER) {
                    int page = Integer.parseInt(ChatColor.stripColor(itemMeta.getDisplayName()));
                    p.getPlayer().closeInventory();
                    p.openGUI(gui, gui.main(page));
                }
            }
        }
    }
}
