package com.minecraft.plugin.elite.survivalgames.listeners.basic;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.events.ClearPlayerEvent;
import com.minecraft.plugin.elite.general.api.special.menu.MenuTool;
import com.minecraft.plugin.elite.survivalgames.manager.Lobby;
import com.minecraft.plugin.elite.survivalgames.manager.alive.PlayerTool;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ClearPlayerEventListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onClear(ClearPlayerEvent e) {
        GeneralPlayer p = e.getPlayer();
        Lobby lobby = Lobby.get();
        if(p.hasTool())
            p.clearTools();
        if(lobby.isActive())
            p.giveTool(new MenuTool(p.getLanguage()));
        if(p.isAdminMode() || p.isWatching()) {
            p.giveTool(new PlayerTool(p.getLanguage()));
        }
    }
}
