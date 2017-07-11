package com.minecraft.plugin.elite.general.listeners.punish;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.punish.mute.Mute;
import com.minecraft.plugin.elite.general.punish.mute.MuteManager;
import org.bukkit.command.Command;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MuteEventListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onMuteTalk(AsyncPlayerChatEvent e) {
		GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
		Mute mute = MuteManager.getMute(p.getUniqueId());
		if(mute != null) {
			e.setCancelled(true);
			p.sendHoverMessage(mute.getMuteMessage(p.getLanguage()), mute.getMuteDisplayMessage(p.getLanguage()));
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void checkSpamOnCommand(PlayerCommandPreprocessEvent e) {
		GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
		List<String> blacklist = new ArrayList<>();
		List<String> cmdNames = Arrays.asList("tell", "reply", "clanchat");

		for(String string : cmdNames) {
			Command cmd = General.getPlugin().getCommand(string);
			if(cmd != null) {
				blacklist.add("/" + cmd.getName());
				for(String alias : cmd.getAliases())
					blacklist.add("/" + alias);
			}
		}

		boolean msg = false;
		String message = e.getMessage().split(" ")[0];
		for(String string : blacklist) {
			if(message.toLowerCase().equalsIgnoreCase(string.toLowerCase())) {
				msg = true;
				break;
			}
		}

		if(msg) {
			Mute mute = MuteManager.getMute(p.getUniqueId());
			if(mute != null) {
				e.setCancelled(true);
				p.sendHoverMessage(mute.getMuteMessage(p.getLanguage()), mute.getMuteDisplayMessage(p.getLanguage()));
			}
		}
	}
}