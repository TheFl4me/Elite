package com.minecraft.plugin.elite.general.api.events.kits;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.special.kits.Kit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KitEnableEvent extends Event {

	private GeneralPlayer player;
	private Kit kit;

	private static final HandlerList handlers = new HandlerList();

	public KitEnableEvent(GeneralPlayer player, Kit kit) {
		this.player = player;
		this.kit = kit;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public GeneralPlayer getPlayer() {
		return this.player;
	}

	public Kit getKit() {
		return this.kit;
	}
}
