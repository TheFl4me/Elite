package com.minecraft.plugin.elite.raid.listeners.basic;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.events.mode.ModeChangeEvent;
import com.minecraft.plugin.elite.raid.Raid;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ModeChangeEventListener implements Listener {
	
	@EventHandler
	public void onAdminChange(ModeChangeEvent e) {
		ePlayer p = e.getPlayer();
		if(!e.isToMode() && p.isInRegion(Raid.SPAWN)) {
			p.clear();
		}
	}
}
