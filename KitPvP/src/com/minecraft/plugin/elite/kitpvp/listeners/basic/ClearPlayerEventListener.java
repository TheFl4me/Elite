package com.minecraft.plugin.elite.kitpvp.listeners.basic;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.events.ClearPlayerEvent;
import com.minecraft.plugin.elite.general.api.special.kits.KitSelectorTool;
import com.minecraft.plugin.elite.general.api.special.menu.MenuTool;
import com.minecraft.plugin.elite.kitpvp.KitPvP;
import com.minecraft.plugin.elite.kitpvp.manager.custom.CustomDuelSelector;
import com.minecraft.plugin.elite.kitpvp.manager.normal.NormalDuelSelector;
import com.minecraft.plugin.elite.kitpvp.manager.queue.DuelQueueTool;
import com.minecraft.plugin.elite.kitpvp.manager.tools.DuelTool;
import com.minecraft.plugin.elite.kitpvp.manager.tools.SpawnTool;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ClearPlayerEventListener implements Listener {

	@EventHandler
	public void clearKit(ClearPlayerEvent e) {
		GeneralPlayer p = GeneralPlayer.get(e.getPlayer().getPlayer());
		if (p != null) {
			p.clearKit();
			if(p.hasTool())
				p.clearTools();
			if(p.isInRegion(KitPvP.REGION_SPAWN)) {
				p.giveTool(new MenuTool(p.getLanguage()));
				p.giveTool(new DuelTool(p.getLanguage()));
				if(!p.isAdminMode() && !p.isWatching()) {
					p.giveTool(new KitSelectorTool(p.getLanguage()));
				}
			}
			if(p.isInRegion(KitPvP.REGION_DUEL)) {
				p.giveTool(new MenuTool(p.getLanguage()));
				p.giveTool(new SpawnTool(p.getLanguage()));
				if(!p.isAdminMode() && !p.isWatching()) {
					p.giveTool(new CustomDuelSelector(p.getLanguage()));
					p.giveTool(new NormalDuelSelector(p.getLanguage()));
					p.giveTool(new DuelQueueTool(p.getLanguage()));
				}
			}
		}
	}
}