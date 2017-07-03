package com.minecraft.plugin.elite.general.listeners.punish;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.punish.ban.Ban;
import com.minecraft.plugin.elite.general.punish.ban.BanManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class BanEventListener implements Listener {

	@EventHandler
	public void onBannedPlayerLogin(PlayerLoginEvent e) {
		ePlayer p = ePlayer.get(e.getPlayer());
		Ban ban = BanManager.getBan(p.getUniqueId());
		if(ban != null) {
			e.disallow(Result.KICK_BANNED, ban.getKickMessage(p.getLanguage()));
		}
	}
}