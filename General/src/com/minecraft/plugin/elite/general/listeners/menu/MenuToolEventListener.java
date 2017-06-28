package com.minecraft.plugin.elite.general.listeners.menu;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.events.tool.ToolClickEvent;
import com.minecraft.plugin.elite.general.api.special.menu.MenuGUI;
import com.minecraft.plugin.elite.general.api.special.menu.MenuTool;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MenuToolEventListener implements Listener {

    @EventHandler
    public void toolClick(ToolClickEvent e) {
        ePlayer p = e.getPlayer();
        if(e.getTool() instanceof MenuTool) {
            MenuGUI menu = new MenuGUI(p.getLanguage());
            p.openGUI(menu, menu.main());
        }
    }
}
