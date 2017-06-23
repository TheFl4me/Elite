package com.minecraft.plugin.elite.kitpvp.listeners.basic;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.events.ClearPlayerEvent;
import com.minecraft.plugin.elite.general.api.special.menu.MenuTool;
import com.minecraft.plugin.elite.kitpvp.KitPvP;
import com.minecraft.plugin.elite.kitpvp.manager.KitPlayer;
import com.minecraft.plugin.elite.kitpvp.manager.duel.custom.CustomDuelSelector;
import com.minecraft.plugin.elite.kitpvp.manager.duel.normal.NormalDuelSelector;
import com.minecraft.plugin.elite.kitpvp.manager.duel.tools.DuelTool;
import com.minecraft.plugin.elite.kitpvp.manager.duel.tools.SpawnTool;
import com.minecraft.plugin.elite.kitpvp.manager.kits.KitSelectorTool;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ClearPlayerEventListener implements Listener {

	@EventHandler
	public void clearKit(ClearPlayerEvent e) {
		ePlayer p = ePlayer.get(e.getPlayer().getPlayer());
		KitPlayer.get(p.getUniqueId()).clearKit();
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
			}
		}
	}
}