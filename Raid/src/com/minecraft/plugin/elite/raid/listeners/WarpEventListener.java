package com.minecraft.plugin.elite.raid.listeners;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.raid.RaidLanguage;
import com.minecraft.plugin.elite.raid.manager.WarpManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class WarpEventListener implements Listener {
    
    @EventHandler
	public void onPlayerMoveCancelWarp(PlayerMoveEvent e) {
		GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
    	WarpManager.cancel(p);
    	p.sendMessage(RaidLanguage.WARP_CANCELLED);
	}
}