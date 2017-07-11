package com.minecraft.plugin.elite.hub.listeners;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.special.menu.MenuTool;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinQuitEventsListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
		p.clear();
		p.giveTool(new MenuTool(p.getLanguage()));
		p.getPlayer().setFoodLevel(20);
		p.getPlayer().setHealth(20);
	}
}