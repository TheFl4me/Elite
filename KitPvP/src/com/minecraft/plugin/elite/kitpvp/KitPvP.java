package com.minecraft.plugin.elite.kitpvp;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.general.database.DatabaseCore;
import com.minecraft.plugin.elite.general.database.MySQLCore;
import com.minecraft.plugin.elite.kitpvp.commands.FreeKitsCommand;
import com.minecraft.plugin.elite.kitpvp.commands.KitCommand;
import com.minecraft.plugin.elite.kitpvp.commands.KitInfoCommand;
import com.minecraft.plugin.elite.kitpvp.listeners.BossBarEventListener;
import com.minecraft.plugin.elite.kitpvp.listeners.DuelEventListener;
import com.minecraft.plugin.elite.kitpvp.listeners.EventListener;
import com.minecraft.plugin.elite.kitpvp.listeners.KitEventListener;
import com.minecraft.plugin.elite.kitpvp.listeners.LevelUpEventListener;
import com.minecraft.plugin.elite.kitpvp.listeners.basic.ClearPlayerEventListener;
import com.minecraft.plugin.elite.kitpvp.listeners.basic.GUIEventListener;
import com.minecraft.plugin.elite.kitpvp.listeners.basic.JoinQuitEventsListener;
import com.minecraft.plugin.elite.kitpvp.listeners.basic.LanguageEventListener;
import com.minecraft.plugin.elite.kitpvp.listeners.basic.ModeChangeEventListener;
import com.minecraft.plugin.elite.kitpvp.listeners.basic.ScoreboardEventListener;
import com.minecraft.plugin.elite.kitpvp.listeners.basic.SpawnEventsListener;
import com.minecraft.plugin.elite.kitpvp.listeners.basic.ToolEventListener;
import com.minecraft.plugin.elite.kitpvp.listeners.death.DeathEventListener;
import com.minecraft.plugin.elite.kitpvp.listeners.death.RespawnEventListener;
import com.minecraft.plugin.elite.kitpvp.manager.KitPlayer;
import com.minecraft.plugin.elite.kitpvp.manager.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class KitPvP extends JavaPlugin {
	
	public static final String SPAWN_REGION = "spawn";
	public static final String DB_KITS = "kits";
    
	private static KitPvP plugin;
	private static Database db;
	
	public void onEnable() {
		
		plugin = this;

		freekits.add(Kit.PVP);
		freekits.add(Kit.ARCHER);

		loadDatabase();
		loadCmds();
		loadEvents();

		Server server = new Server("KitPvP");
		server.initiate();
		
		World world = Bukkit.getWorld("world");
		
		//world.getWorldBorder().setCenter(world.getSpawnLocation());
		//world.getWorldBorder().setSize(100);
	}
	
    private void loadCmds() {		
		new KitCommand();
		new KitInfoCommand();
		new FreeKitsCommand();
	}
	
    private void loadEvents() {		
    	getServer().getPluginManager().registerEvents(new EventListener(), this);
    	getServer().getPluginManager().registerEvents(new KitEventListener(), this);
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
		getServer().getPluginManager().registerEvents(new LevelUpEventListener(), this);
		getServer().getPluginManager().registerEvents(new ScoreboardEventListener(), this);
		getServer().getPluginManager().registerEvents(new BossBarEventListener(), this);
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
			if(!db.hasTable(DB_KITS)) {
				String query = "CREATE TABLE " + DB_KITS + " (" +
						"uuid TEXT(50) NOT NULL);";
				db.createTable(query, DB_KITS);
			}
			for(Kit kit : Kit.values())
				if(!db.hasColumn(DB_KITS, kit.getName().toLowerCase()))
					db.execute("ALTER TABLE " + DB_KITS + " ADD " + kit.getName().toLowerCase() + " INT NOT NULL DEFAULT 0");

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

	private static Collection<Kit> freekits = new ArrayList<>();

	public static Collection<Kit> getFreeKits() {
		return freekits;
	}

	public static void setAllKitsFree(boolean free) {
			for(Kit kit : Kit.values())
				if(kit != Kit.ARCHER && kit != Kit.PVP)
					if(free)
						freekits.add(kit);
					else
		            	freekits.remove(kit);
	}

	public static void updateScoreboard() {
		Server server = Server.get();
		for(Player players : Bukkit.getOnlinePlayers()) {
			ePlayer p = ePlayer.get(players);
			KitPlayer kp = KitPlayer.get(players);
			ChatColor color = ChatColor.GOLD;
			Scoreboard board = players.getScoreboard();
			board.getObjectives().forEach(Objective::unregister);

			Objective obj = board.registerNewObjective("test", "dummy");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			obj.setDisplayName(p.getLanguage().get(KitPvPLanguage.SCOREBOARD_TITLE)
					.replaceAll("%domain", server.getDomain()));

			Score kitTitle = obj.getScore(p.getLanguage().get(KitPvPLanguage.SCOREBOARD_KIT));
			kitTitle.setScore(5);

			Score kitValue = obj.getScore(color + (kp.getKit() == null ? "-" : kp.getKit().getName()));
			kitValue.setScore(4);

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
}