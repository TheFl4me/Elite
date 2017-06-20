package com.minecraft.plugin.elite.kitpvp.listeners.basic;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.events.mode.ModeChangeEvent;
import com.minecraft.plugin.elite.kitpvp.manager.KitPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ModeChangeEventListener implements Listener {
	
	@EventHandler
	public void onModeChange(ModeChangeEvent e) {
		ePlayer p = ePlayer.get(e.getPlayer().getPlayer());
		if(e.isToMode())
			p.clear();
		if(!e.isToMode() && !KitPlayer.get(p.getUniqueId()).hasKit())
			p.clear();
	}
}
