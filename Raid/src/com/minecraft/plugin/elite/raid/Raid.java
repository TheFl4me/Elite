package com.minecraft.plugin.elite.raid;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.general.database.DatabaseCore;
import com.minecraft.plugin.elite.general.database.MySQLCore;
import com.minecraft.plugin.elite.raid.commands.RandomSpawnCommand;
import com.minecraft.plugin.elite.raid.commands.TrackCommand;
import com.minecraft.plugin.elite.raid.commands.warp.WarpCommand;
import com.minecraft.plugin.elite.raid.commands.warp.WarpListCommand;
import com.minecraft.plugin.elite.raid.commands.warp.clan.DeleteClanWarpCommand;
import com.minecraft.plugin.elite.raid.commands.warp.clan.SetClanWarpCommand;
import com.minecraft.plugin.elite.raid.commands.warp.personal.DeleteWarpCommand;
import com.minecraft.plugin.elite.raid.commands.warp.personal.SetWarpCommand;
import com.minecraft.plugin.elite.raid.listeners.MobCaptureEventListener;
import com.minecraft.plugin.elite.raid.listeners.WarpEventListener;
import com.minecraft.plugin.elite.raid.listeners.basic.DeathEventListener;
import com.minecraft.plugin.elite.raid.listeners.basic.JoinQuitEventListener;
import com.minecraft.plugin.elite.raid.listeners.basic.LanguageEventListener;
import com.minecraft.plugin.elite.raid.listeners.basic.ModeChangeEventListener;
import com.minecraft.plugin.elite.raid.listeners.basic.SpawnEventListener;
import com.minecraft.plugin.elite.raid.manager.WarpManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;

public class Raid extends JavaPlugin {
	
	private static Raid plugin;
	private static Database db;
	
	public static final String SPAWN = "spawn";
	public static final String DB_WARP = "warps";
	
	public void onEnable() {
		plugin = this;
		
		Server server = new Server("Raid");
		server.initiate();

		loadDatabase();
		WarpManager.load();
		loadCmds();
		loadEvents();
		
		World world = Bukkit.getWorld("world");
		WorldBorder border = world.getWorldBorder();
		border.setCenter(world.getSpawnLocation());
		border.setSize(1000);
	}
	
	private void loadCmds() {
		new WarpCommand();
		new WarpListCommand();
		new SetWarpCommand();
		new DeleteWarpCommand();
		new SetClanWarpCommand();
		new DeleteClanWarpCommand();
		new TrackCommand();
		new RandomSpawnCommand();
	}
	
	private void loadEvents() {
		
		getServer().getPluginManager().registerEvents(new WarpEventListener(), this);
		getServer().getPluginManager().registerEvents(new ModeChangeEventListener(), this);
		getServer().getPluginManager().registerEvents(new SpawnEventListener(), this);
		getServer().getPluginManager().registerEvents(new JoinQuitEventListener(), this);
		getServer().getPluginManager().registerEvents(new MobCaptureEventListener(), this);
		getServer().getPluginManager().registerEvents(new DeathEventListener(), this);
		getServer().getPluginManager().registerEvents(new LanguageEventListener(), this);
	}

	private void loadDatabase() {
		File database = new File(General.DIRECTORY_DATABASE);
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(database);

		String prefix = "[Raid - Database]";
		String host = cfg.getString("host");
		String user = cfg.getString("user");
		String pass = cfg.getString("pass");
		String name = "raid";
		String port = cfg.getString("port");

		DatabaseCore core = new MySQLCore(host, user, pass, name, port);

		System.out.println(prefix + " Connecting...");
		try {
			db = new Database(core);
		} catch (Database.ConnectionException e) {
			System.out.println(prefix + " Connection failed! Shutting down server...");
			e.printStackTrace();
			Bukkit.getServer().shutdown();
			return;
		}
		System.out.println(prefix + " Connected!");
		System.out.println(prefix + " Setting up...");
		try {
			if(!db.hasTable(DB_WARP)) {
				String query = "CREATE TABLE " + DB_WARP + " ("
						+ " name TEXT(100) NOT NULL,"
						+ " type TEXT(10) NOT NULL,"
						+ " owner TEXT(50) NOT NULL,"
						+ " x BIGINT NOT NULL,"
						+ " y BIGINT NOT NULL,"
						+ " z BIGINT NOT NULL);";
				db.createTable(query, DB_WARP);
			}
			System.out.println(prefix + " Set up done!");
		} catch (SQLException e) {
			System.out.println(prefix + " Setup failed!");
			e.printStackTrace();
		}
	}

	public static Raid getPlugin() {
		return plugin;
	}
	public static Database getDB() { return db; }
}
