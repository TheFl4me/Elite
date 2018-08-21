package com.minecraft.plugin.elite.kitpvp;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.special.Hologram;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.general.database.DatabaseCore;
import com.minecraft.plugin.elite.general.database.MySQLCore;
import com.minecraft.plugin.elite.kitpvp.commands.duel.SetDuelLocationCommand;
import com.minecraft.plugin.elite.kitpvp.commands.duel.SetDuelSpawnCommand;
import com.minecraft.plugin.elite.kitpvp.commands.holograms.SetHologramCommand;
import com.minecraft.plugin.elite.kitpvp.listeners.BossBarEventListener;
import com.minecraft.plugin.elite.kitpvp.listeners.DuelEventListener;
import com.minecraft.plugin.elite.kitpvp.listeners.RegionChangeEventListener;
import com.minecraft.plugin.elite.kitpvp.listeners.basic.*;
import com.minecraft.plugin.elite.kitpvp.listeners.death.DeathEventListener;
import com.minecraft.plugin.elite.kitpvp.listeners.death.RespawnEventListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KitPvP extends JavaPlugin {

	public static final String REGION_SPAWN = "spawn";
	public static final String REGION_EHG = "ehg";
	public static final String REGION_FEAST = "feast";
	public static final String REGION_DUEL = "duel";

	public static final String DB_DUEL = "duels";
	public static final String DB_HOLOGRAMS = "holograms";

	private static KitPvP plugin;
	private static Database db;

	public void onDisable() {
	}

	public void onEnable() {

		plugin = this;

		loadDatabase();
		loadCommands();
		loadEvents();

		Server server = new Server("KitPvP");
		server.initiate();

		World world = Bukkit.getWorld("world");

		//world.getWorldBorder().setCenter(world.getSpawnLocation());
		//world.getWorldBorder().setSize(100);
	}


	private void loadCommands() {
		new SetDuelSpawnCommand();
		new SetDuelLocationCommand();
		new SetHologramCommand();
	}

	private void loadEvents() {
		getServer().getPluginManager().registerEvents(new RespawnEventListener(), this);
		getServer().getPluginManager().registerEvents(new JoinQuitEventsListener(), this);
		getServer().getPluginManager().registerEvents(new GUIEventListener(), this);
		getServer().getPluginManager().registerEvents(new ToolEventListener(), this);
		getServer().getPluginManager().registerEvents(new SpawnEventsListener(), this);
		getServer().getPluginManager().registerEvents(new DuelEventListener(), this);
		getServer().getPluginManager().registerEvents(new DeathEventListener(), this);
		getServer().getPluginManager().registerEvents(new ModeChangeEventListener(), this);
		getServer().getPluginManager().registerEvents(new ClearPlayerEventListener(), this);
		getServer().getPluginManager().registerEvents(new LanguageEventListener(), this);
		getServer().getPluginManager().registerEvents(new ScoreboardEventListener(), this);
		getServer().getPluginManager().registerEvents(new BossBarEventListener(), this);
		getServer().getPluginManager().registerEvents(new BuildEventListener(), this);
		getServer().getPluginManager().registerEvents(new RegionChangeEventListener(), this);
	}

	private void loadDatabase() {
		File database = new File(General.DIRECTORY_DATABASE);
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(database);

		String prefix = "[KitPvP - Database]";
		String host = cfg.getString("host");
		String user = cfg.getString("user");
		String pass = cfg.getString("pass");
		String name = "kitpvp";
		String port = cfg.getString("port");

		DatabaseCore core = new MySQLCore(host, user, pass, name, port);

		System.out.println(prefix + " Connecting...");
		try {
			db = new Database(core);
		} catch (Exception e) {
			System.out.println(prefix + " Connection failed! Shutting down server...");
			e.printStackTrace();
			Bukkit.getServer().shutdown();
			return;
		}
		System.out.println(prefix + " Connected!");
		System.out.println(prefix + " Setting up...");
		try {
			if (!db.hasTable(DB_DUEL)) {
				String query = "CREATE TABLE " + DB_DUEL + " (" +
						"location TEXT(100) NOT NULL," +
						"locx DOUBLE NOT NULL," +
						"locy DOUBLE NOT NULL," +
						"locz DOUBLE NOT NULL);";
				db.createTable(query, DB_DUEL);
				db.execute("INSERT INTO " + DB_DUEL + " (location, locx, locy, locz) VALUES (?, ?, ?, ?);", "duelspawn", 100.0, 0.0, 0.0);
				db.execute("INSERT INTO " + DB_DUEL + " (location, locx, locy, locz) VALUES (?, ?, ?, ?);", "loc1", 100.0, 0.0, 0.0);
				db.execute("INSERT INTO " + DB_DUEL + " (location, locx, locy, locz) VALUES (?, ?, ?, ?);", "loc2", 100.0, 0.0, 0.0);
			}

			if (!db.hasTable(DB_HOLOGRAMS)) {
				String query = "CREATE TABLE " + DB_HOLOGRAMS + " (" +
						"name TEXT(100) NOT NULL," +
						"locx DOUBLE NOT NULL," +
						"locy DOUBLE NOT NULL," +
						"locz DOUBLE NOT NULL);";
				db.createTable(query, DB_HOLOGRAMS);
			}

			System.out.println(prefix + " Set up done!");
		} catch (SQLException e) {
			System.out.println(prefix + " Setup failed!");
			e.printStackTrace();
		}
	}

	public static KitPvP getPlugin() {
		return plugin;
	}

	public static Database getDB() {
		return db;
	}


	public static void updateScoreboard() {
		Server server = Server.get();
		for (Player players : Bukkit.getOnlinePlayers()) {
			GeneralPlayer p = GeneralPlayer.get(players);
			ChatColor color = ChatColor.GOLD;
			Scoreboard board = players.getScoreboard();
			board.getObjectives().forEach(Objective::unregister);

			Objective obj = board.registerNewObjective("test", "dummy");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			obj.setDisplayName(p.getLanguage().get(KitPvPLanguage.SCOREBOARD_TITLE)
					.replaceAll("%domain", server.getDomain()));

			Score kitTitle = obj.getScore(p.getLanguage().get(KitPvPLanguage.SCOREBOARD_KIT));
			kitTitle.setScore(7);

			Score kitValue = obj.getScore(color + (p.getKit() == null ? "-" : p.getKit().getName()));
			kitValue.setScore(6);

			Score eloTitle = obj.getScore(p.getLanguage().get(KitPvPLanguage.SCOREBOARD_ELO));
			eloTitle.setScore(5);

			Score eloValue = obj.getScore(color + Long.toString(p.getELO()) + " ");
			eloValue.setScore(4);

			Score killsTitle = obj.getScore(p.getLanguage().get(KitPvPLanguage.SCOREBOARD_KILLS));
			killsTitle.setScore(3);

			Score killsValue = obj.getScore(color + Integer.toString(p.getKills()) + " (" + Integer.toString(p.getKillStreak()) + ")");
			killsValue.setScore(2);

			Score deathsTitle = obj.getScore(p.getLanguage().get(KitPvPLanguage.SCOREBOARD_DEATHS));
			deathsTitle.setScore(1);

			Score deathsValue = obj.getScore(color + Integer.toString(p.getDeaths()));
			deathsValue.setScore(0);
		}
	}

	public static void reloadHolograms() {
		for (Hologram holo : Hologram.getAll())
			holo.destroy();

		World world = Bukkit.getWorld("world");
		Database db = KitPvP.getDB();
		for (Player players : Bukkit.getOnlinePlayers()) {
			GeneralPlayer all = GeneralPlayer.get(players);
			loadHolograms(all);
		}
	}

	public static void loadHolograms(GeneralPlayer p) {
		Database db = KitPvP.getDB();
		World world = Bukkit.getWorld("world");

		try {
			ResultSet feastRes = db.select(DB_HOLOGRAMS, "name", "feast");
			if (feastRes.next()) {
				Location holoLoc = new Location(world, feastRes.getDouble("locx"), feastRes.getDouble("locy"), feastRes.getDouble("locz"));
				Hologram holo = new Hologram(p, p.getLanguage().get(KitPvPLanguage.HOLOGRAM_FEAST));
				holo.show(holoLoc);
			}

			ResultSet ehgRes = db.select(DB_HOLOGRAMS, "name", "ehg");
			if (ehgRes.next()) {
				Location holoLoc = new Location(world, ehgRes.getDouble("locx"), ehgRes.getDouble("locy"), ehgRes.getDouble("locz"));
				Hologram holo = new Hologram(p, p.getLanguage().get(KitPvPLanguage.HOLOGRAM_EHG));
				holo.show(holoLoc);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}