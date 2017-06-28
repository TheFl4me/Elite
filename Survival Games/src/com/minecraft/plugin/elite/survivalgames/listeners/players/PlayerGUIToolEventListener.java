package com.minecraft.plugin.elite.survivalgames.listeners.players;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.events.tool.ToolClickEvent;
import com.minecraft.plugin.elite.survivalgames.manager.alive.PlayerGUI;
import com.minecraft.plugin.elite.survivalgames.manager.alive.PlayerTool;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerGUIToolEventListener implements Listener {

    @EventHandler
    public void toolClick(ToolClickEvent e) {
        ePlayer p = e.getPlayer();
        if(e.getTool() instanceof PlayerTool) {
            PlayerGUI gui = new PlayerGUI(p.getLanguage());
            p.openGUI(gui, gui.main(1));
        }
    }
}
