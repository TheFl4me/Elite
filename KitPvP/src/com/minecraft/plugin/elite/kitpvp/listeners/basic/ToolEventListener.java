package com.minecraft.plugin.elite.kitpvp.listeners.basic;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.events.tool.ToolClickEvent;
import com.minecraft.plugin.elite.kitpvp.manager.DuelManager;
import com.minecraft.plugin.elite.kitpvp.manager.tools.DuelTool;
import com.minecraft.plugin.elite.kitpvp.manager.tools.SpawnTool;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ToolEventListener implements Listener {

	@EventHandler
	public void toolClick(ToolClickEvent e) {
		GeneralPlayer p = e.getPlayer();
		if(e.getTool() instanceof SpawnTool) {
			p.getPlayer().teleport(p.getPlayer().getWorld().getSpawnLocation());
			return;
		}
		if(e.getTool() instanceof DuelTool) {
			Location loc = DuelManager.getDuelSpawn();
			if(loc != null)
				p.getPlayer().teleport(loc);
		}
	}
}
