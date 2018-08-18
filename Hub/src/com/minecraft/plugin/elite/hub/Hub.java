package com.minecraft.plugin.elite.hub;

import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.hub.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

public class Hub extends JavaPlugin {
	
	private static Hub plugin;
	
	public void onEnable() {
		
		plugin = this;

		loadEvents();

		Server server = new Server("Hub");
		server.initiate();
	}
	
	private void loadEvents() {	
		getServer().getPluginManager().registerEvents(new JoinQuitEventsListener(), this);
		getServer().getPluginManager().registerEvents(new ClearPlayerEventListener(), this);
		getServer().getPluginManager().registerEvents(new FoodChangeEventListener(), this);
		getServer().getPluginManager().registerEvents(new DamageEventListener(), this);
		getServer().getPluginManager().registerEvents(new ModeChangeEventListener(), this);
	}
	
	public static Hub getPlugin() {
		return plugin;
	}
}