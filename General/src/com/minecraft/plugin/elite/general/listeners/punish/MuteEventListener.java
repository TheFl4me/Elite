package com.minecraft.plugin.elite.general.listeners.punish;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.punish.mute.Mute;
import com.minecraft.plugin.elite.general.punish.mute.MuteManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class MuteEventListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onMuteTalk(AsyncPlayerChatEvent e) {
		ePlayer p = ePlayer.get(e.getPlayer());
		Mute mute = MuteManager.getMute(p.getUniqueId());
		if(mute != null) {
			e.setCancelled(true);
			p.sendHoverMessage(mute.getMuteMessage(), mute.getMuteDisplayMessage());
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void checkSpamOnCommand(PlayerCommandPreprocessEvent e) {
		ePlayer p = ePlayer.get(e.getPlayer());
		String[] blacklist = new String[]{"/msg", "/tell", "/r", "/reply", "/message", "/answer"};
		boolean msg = false;
		for (String command : blacklist) {
			if (e.getMessage().startsWith(command)) {
				msg = true;
				break;
			}
		}
		if(msg) {
			Mute mute = MuteManager.getMute(p.getUniqueId());
			if(mute != null) {
				e.setCancelled(true);
				p.sendHoverMessage(mute.getMuteMessage(), mute.getMuteDisplayMessage());
			}
		}
	}
}