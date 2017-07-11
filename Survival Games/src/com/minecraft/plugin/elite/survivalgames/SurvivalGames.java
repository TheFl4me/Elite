package com.minecraft.plugin.elite.survivalgames;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.general.database.DatabaseCore;
import com.minecraft.plugin.elite.general.database.MySQLCore;
import com.minecraft.plugin.elite.survivalgames.commands.SetMapCommand;
import com.minecraft.plugin.elite.survivalgames.commands.StartCommand;
import com.minecraft.plugin.elite.survivalgames.commands.admin.BorderCommand;
import com.minecraft.plugin.elite.survivalgames.commands.admin.CenterCommand;
import com.minecraft.plugin.elite.survivalgames.commands.admin.MapTeleportCommand;
import com.minecraft.plugin.elite.survivalgames.commands.admin.PodCommand;
import com.minecraft.plugin.elite.survivalgames.listeners.ChestFillEventListener;
import com.minecraft.plugin.elite.survivalgames.listeners.CompassEventListener;
import com.minecraft.plugin.elite.survivalgames.listeners.GamePhaseEventsListener;
import com.minecraft.plugin.elite.survivalgames.listeners.basic.*;
import com.minecraft.plugin.elite.survivalgames.listeners.players.PlayerGUIEventListener;
import com.minecraft.plugin.elite.survivalgames.listeners.players.PlayerGUIToolEventListener;
import com.minecraft.plugin.elite.survivalgames.manager.Lobby;
import com.minecraft.plugin.elite.survivalgames.manager.arena.ArenaManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;

public class SurvivalGames extends JavaPlugin {

	public static final String DB_ARENAS = "arenas";
	
	private static SurvivalGames plugin;
	private static Database db;
	
	public void onEnable() {
		plugin = this;

		loadDatabase();
		loadCmds();
		loadEvents();

		Server server = new Server("SurvivalGames");
		server.initiate();
		
		ArenaManager.loadAll();
		Lobby.create();
	}
	
	private void loadCmds() {
		new PodCommand();
		new StartCommand();
		new SetMapCommand();
		new CenterCommand();
		new BorderCommand();
		new MapTeleportCommand();
	}
	
	private void loadEvents() {

		getServer().getPluginManager().registerEvents(new ModeChangeEventListener(), this);
		getServer().getPluginManager().registerEvents(new JoinQuitEventListener(), this);
		getServer().getPluginManager().registerEvents(new DeathEventListener(), this);
		getServer().getPluginManager().registerEvents(new GamePhaseEventsListener(), this);
		getServer().getPluginManager().registerEvents(new CompassEventListener(), this);
		getServer().getPluginManager().registerEvents(new LanguageEventListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerGUIEventListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerGUIToolEventListener(), this);
		getServer().getPluginManager().registerEvents(new ChestFillEventListener(), this);
		getServer().getPluginManager().registerEvents(new BuildEventListener(), this);
		getServer().getPluginManager().registerEvents(new ClearPlayerEventListener(), this);
		getServer().getPluginManager().registerEvents(new ScoreboardEventListener(), this);

	}

	private void loadDatabase() {
		File database = new File(General.DIRECTORY_DATABASE);
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(database);

		String prefix = "[Survival Games - Database]";
		String host = cfg.getString("host");
		String user = cfg.getString("user");
		String pass = cfg.getString("pass");
		String name = "survivalgames";
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
			if(!db.hasTable(DB_ARENAS)) {
				String query = "CREATE TABLE " + DB_ARENAS + " ("
						+ " name TEXT(100) NOT NULL,"
						+ " maxsize BIGINT NOT NULL,"
						+ " minsize BIGINT NOT NULL,"
						+ " centerx DOUBLE NOT NULL,"
						+ " centery DOUBLE NOT NULL,"
						+ " centerz DOUBLE NOT NULL,"
						+ " pody DOUBLE NOT NULL,"
						+ " pod1x DOUBLE NOT NULL,"
						+ " pod1z DOUBLE NOT NULL,"
						+ " pod2x DOUBLE NOT NULL,"
						+ " pod2z DOUBLE NOT NULL,"
						+ " pod3x DOUBLE NOT NULL,"
						+ " pod3z DOUBLE NOT NULL,"
						+ " pod4x DOUBLE NOT NULL,"
						+ " pod4z DOUBLE NOT NULL,"
						+ " pod5x DOUBLE NOT NULL,"
						+ " pod5z DOUBLE NOT NULL,"
						+ " pod6x DOUBLE NOT NULL,"
						+ " pod6z DOUBLE NOT NULL,"
						+ " pod7x DOUBLE NOT NULL,"
						+ " pod7z DOUBLE NOT NULL,"
						+ " pod8x DOUBLE NOT NULL,"
						+ " pod8z DOUBLE NOT NULL,"
						+ " pod9x DOUBLE NOT NULL,"
						+ " pod9z DOUBLE NOT NULL,"
						+ " pod10x DOUBLE NOT NULL,"
						+ " pod10z DOUBLE NOT NULL,"
						+ " pod11x DOUBLE NOT NULL,"
						+ " pod11z DOUBLE NOT NULL,"
						+ " pod12x DOUBLE NOT NULL,"
						+ " pod12z DOUBLE NOT NULL,"
						+ " pod13x DOUBLE NOT NULL,"
						+ " pod13z DOUBLE NOT NULL,"
						+ " pod14x DOUBLE NOT NULL,"
						+ " pod14z DOUBLE NOT NULL,"
						+ " pod15x DOUBLE NOT NULL,"
						+ " pod15z DOUBLE NOT NULL,"
						+ " pod16x DOUBLE NOT NULL,"
						+ " pod16z DOUBLE NOT NULL,"
						+ " pod17x DOUBLE NOT NULL,"
						+ " pod17z DOUBLE NOT NULL,"
						+ " pod18x DOUBLE NOT NULL,"
						+ " pod18z DOUBLE NOT NULL,"
						+ " pod19x DOUBLE NOT NULL,"
						+ " pod19z DOUBLE NOT NULL,"
						+ " pod20x DOUBLE NOT NULL,"
						+ " pod20z DOUBLE NOT NULL,"
						+ " pod21x DOUBLE NOT NULL,"
						+ " pod21z DOUBLE NOT NULL,"
						+ " pod22x DOUBLE NOT NULL,"
						+ " pod22z DOUBLE NOT NULL,"
						+ " pod23x DOUBLE NOT NULL,"
						+ " pod23z DOUBLE NOT NULL,"
						+ " pod24x DOUBLE NOT NULL,"
						+ " pod24z DOUBLE NOT NULL);";
				db.createTable(query, DB_ARENAS);
			}
			System.out.println(prefix + " Set up done!");
		} catch (SQLException e) {
			System.out.println(prefix + " Setup failed!");
			e.printStackTrace();
		}
	}
	
	public static SurvivalGames getPlugin() {
		return plugin;
	}
	public static Database getDB() { return db; }
}
