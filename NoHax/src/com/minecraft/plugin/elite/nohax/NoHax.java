package com.minecraft.plugin.elite.nohax;

import com.minecraft.plugin.elite.nohax.commands.AlertsCommand;
import com.minecraft.plugin.elite.nohax.commands.SilentCommand;
import com.minecraft.plugin.elite.nohax.listeners.ActionStoreEventListener;
import com.minecraft.plugin.elite.nohax.listeners.CombatLogEventListener;
import com.minecraft.plugin.elite.nohax.listeners.SpamCheckEventListener;
import com.minecraft.plugin.elite.nohax.listeners.basic.EventListener;
import com.minecraft.plugin.elite.nohax.listeners.basic.JoinQuitEventListener;
import com.minecraft.plugin.elite.nohax.listeners.basic.LanguageEventListener;
import org.bukkit.plugin.java.JavaPlugin;

public class NoHax extends JavaPlugin {

    private static NoHax plugin;

    public static final String ALERTS_PERM = "enohax.alerts";

    public void onEnable() {

        plugin = this;

        loadEvents();
        loadCmds();
    }

    private void loadCmds() {
        new SilentCommand();
        new AlertsCommand();
    }

    private void loadEvents() {
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        getServer().getPluginManager().registerEvents(new LanguageEventListener(), this);
        getServer().getPluginManager().registerEvents(new JoinQuitEventListener(), this);
        getServer().getPluginManager().registerEvents(new ActionStoreEventListener(), this);
        getServer().getPluginManager().registerEvents(new CombatLogEventListener(), this);
        getServer().getPluginManager().registerEvents(new SpamCheckEventListener(), this);
    }

    public static NoHax getPlugin() {
        return plugin;
    }
}