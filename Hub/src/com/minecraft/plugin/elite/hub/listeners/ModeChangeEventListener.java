package com.minecraft.plugin.elite.hub.listeners;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.events.mode.ModeChangeEvent;
import com.minecraft.plugin.elite.general.api.special.menu.MenuTool;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ModeChangeEventListener implements Listener {
	
	@EventHandler
	public void modeChange(ModeChangeEvent e) {
		ePlayer p = e.getPlayer();
		if(p.hasTool())
			p.clearTools();
		p.giveTool(new MenuTool(p.getLanguage()));
	}
}
