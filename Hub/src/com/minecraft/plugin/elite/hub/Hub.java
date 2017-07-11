package com.minecraft.plugin.elite.hub;

import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.hub.listeners.ClearPlayerEventListener;
import com.minecraft.plugin.elite.hub.listeners.DamageEventListener;
import com.minecraft.plugin.elite.hub.listeners.FoodChangeEventListener;
import com.minecraft.plugin.elite.hub.listeners.JoinQuitEventsListener;
import com.minecraft.plugin.elite.hub.listeners.LanguageEventListener;
import com.minecraft.plugin.elite.hub.listeners.ModeChangeEventListener;
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
		getServer().getPluginManager().registerEvents(new LanguageEventListener(), this);
	}
	
	public static Hub getPlugin() {
		return plugin;
	}
}