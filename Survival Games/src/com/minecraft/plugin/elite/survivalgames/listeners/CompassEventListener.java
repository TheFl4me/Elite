package com.minecraft.plugin.elite.survivalgames.listeners;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.survivalgames.SurvivalGamesLanguage;
import com.minecraft.plugin.elite.survivalgames.manager.Lobby;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class CompassEventListener implements Listener {

	@EventHandler
	public void onCompassClick(PlayerInteractEvent e) {
		GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
		Lobby lobby = Lobby.get();
		if(e.getItem() == null || e.getItem().getType() != Material.COMPASS)
			return;
		if((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && !lobby.isActive()) {
			Location loc = p.getPlayer().getLocation();
			if(lobby.getArena().getPlayers().size() > 1) {
				GeneralPlayer nearest = null;
				for(GeneralPlayer all : lobby.getArena().getPlayers()) {
					if((nearest == null || all.getPlayer().getLocation().distance(loc) < nearest.getPlayer().getLocation().distance(loc)) && all.getPlayer().getLocation().distance(loc) > 20)
						nearest = all;
				}
				p.getPlayer().setCompassTarget(nearest.getPlayer().getLocation());
				p.getPlayer().sendMessage(p.getLanguage().get(SurvivalGamesLanguage.TRACKING)
						.replaceAll("%player", nearest.getName()));
			} else {
				p.sendMessage(SurvivalGamesLanguage.TRACKING_NONE);
			}
		}
	}
}
