package com.minecraft.plugin.elite.nohax;

import com.minecraft.plugin.elite.nohax.commands.AlertsCommand;
import com.minecraft.plugin.elite.nohax.commands.SilentCommand;
import com.minecraft.plugin.elite.nohax.listeners.EventListener;
import com.minecraft.plugin.elite.nohax.listeners.LanguageEventListener;
import org.bukkit.plugin.java.JavaPlugin;

public class NoHax extends JavaPlugin {

    private static NoHax plugin;

    public void onEnable() {

        plugin = this;

        new SilentCommand();
        new AlertsCommand();

        getServer().getPluginManager().registerEvents(new EventListener(), this);
        getServer().getPluginManager().registerEvents(new LanguageEventListener(), this);
    }

    public static NoHax getPlugin() {
        return plugin;
    }
}