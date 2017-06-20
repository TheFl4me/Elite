package com.minecraft.plugin.elite.kitpvp.listeners;

import com.minecraft.plugin.elite.kitpvp.manager.KitPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class EventListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLogin(PlayerLoginEvent e) {
		KitPlayer.login(e.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerLoginCanceled(PlayerLoginEvent e) {
		if(e.getResult() != PlayerLoginEvent.Result.ALLOWED) {
			KitPlayer p = KitPlayer.get(e.getPlayer());
			if(p != null)
				p.logout();
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void cleanUpPlayerHashMap(PlayerQuitEvent e) {
		KitPlayer p = KitPlayer.get(e.getPlayer());
		if(p != null)
			p.leave();
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onJoin(PlayerJoinEvent e) {		
		KitPlayer p = KitPlayer.get(e.getPlayer());
		if(p != null)
			p.join();
	}
}
