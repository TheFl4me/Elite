package com.minecraft.plugin.elite.general;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.enums.Achievement;
import com.minecraft.plugin.elite.general.api.enums.Rank;
import com.minecraft.plugin.elite.general.api.special.clan.ClanManager;
import com.minecraft.plugin.elite.general.commands.*;
import com.minecraft.plugin.elite.general.commands.admin.*;
import com.minecraft.plugin.elite.general.commands.admin.mode.AdminCommand;
import com.minecraft.plugin.elite.general.commands.admin.mode.BuildCommand;
import com.minecraft.plugin.elite.general.commands.admin.mode.GamemodeCommand;
import com.minecraft.plugin.elite.general.commands.admin.mode.WatchCommand;
import com.minecraft.plugin.elite.general.commands.admin.mode.invis.InvisCommand;
import com.minecraft.plugin.elite.general.commands.admin.mode.invis.VisCommand;
import com.minecraft.plugin.elite.general.commands.admin.staff.ClearStaffCommand;
import com.minecraft.plugin.elite.general.commands.admin.staff.SetRankCommand;
import com.minecraft.plugin.elite.general.commands.admin.staff.SetStaffCommand;
import com.minecraft.plugin.elite.general.commands.antihack.AlertsCommand;
import com.minecraft.plugin.elite.general.commands.antihack.SilentCommand;
import com.minecraft.plugin.elite.general.commands.chat.*;
import com.minecraft.plugin.elite.general.commands.chat.message.ReplyCommand;
import com.minecraft.plugin.elite.general.commands.chat.message.SpyCommand;
import com.minecraft.plugin.elite.general.commands.chat.message.TellCommand;
import com.minecraft.plugin.elite.general.commands.clan.ClanChatCommand;
import com.minecraft.plugin.elite.general.commands.clan.ClanCommand;
import com.minecraft.plugin.elite.general.commands.party.PartyCommand;
import com.minecraft.plugin.elite.general.commands.punish.KickCommand;
import com.minecraft.plugin.elite.general.commands.punish.PunishCommand;
import com.minecraft.plugin.elite.general.commands.punish.UnbanCommand;
import com.minecraft.plugin.elite.general.commands.punish.UnmuteCommand;
import com.minecraft.plugin.elite.general.commands.punish.info.CheckInfoCommand;
import com.minecraft.plugin.elite.general.commands.punish.info.IPInfoCommand;
import com.minecraft.plugin.elite.general.commands.punish.report.ReportClearCommand;
import com.minecraft.plugin.elite.general.commands.punish.report.ReportCommand;
import com.minecraft.plugin.elite.general.commands.punish.report.ReportListCommand;
import com.minecraft.plugin.elite.general.commands.spawn.SetSpawnCommand;
import com.minecraft.plugin.elite.general.commands.spawn.SpawnCommand;
import com.minecraft.plugin.elite.general.commands.timeset.DayCommand;
import com.minecraft.plugin.elite.general.commands.timeset.NightCommand;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.general.database.DatabaseCore;
import com.minecraft.plugin.elite.general.database.MySQLCore;
import com.minecraft.plugin.elite.general.listeners.*;
import com.minecraft.plugin.elite.general.listeners.antihack.ActionStoreEventListener;
import com.minecraft.plugin.elite.general.listeners.antihack.CombatLogEventListener;
import com.minecraft.plugin.elite.general.listeners.antihack.SpamCheckEventListener;
import com.minecraft.plugin.elite.general.listeners.chat.ChatBlacklistListener;
import com.minecraft.plugin.elite.general.listeners.chat.ChatEventListener;
import com.minecraft.plugin.elite.general.listeners.chat.ChatToggleEventListener;
import com.minecraft.plugin.elite.general.listeners.menu.MenuGUIEventListener;
import com.minecraft.plugin.elite.general.listeners.menu.MenuToolEventListener;
import com.minecraft.plugin.elite.general.listeners.punish.*;
import com.minecraft.plugin.elite.general.punish.ban.BanManager;
import com.minecraft.plugin.elite.general.punish.mute.MuteManager;
import com.minecraft.plugin.elite.general.punish.report.ReportManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class General extends JavaPlugin {

    public static final String DIRECTORY_GENERAL = "plugins" + File.separator + "General";
    public static final String DIRECTORY_DATABASE = DIRECTORY_GENERAL + File.separator + "Database.yml";
    public static final String DIRECTORY_PERMISSIONS = DIRECTORY_GENERAL + File.separator + "Permissions.yml";
    public static final String DIRECTORY_BLACKLIST = DIRECTORY_GENERAL + File.separator + "Blacklist.yml";
    public static final String SPACER = ChatColor.STRIKETHROUGH + "----------------------------------";

    public static final String DB_PLAYERS = "players";
    public static final String DB_CHAT_LOGS = "chatlogs";
    public static final String DB_CLANS = "clans";
    public static final String DB_ACHIEVEMENTS = "achievements";
    public static final String DB_STAFF = "staff";
    public static final String DB_BANS = "bans";
    public static final String DB_BAN_HISTORY = "banhistory";
    public static final String DB_MUTES = "mutes";
    public static final String DB_MUTE_HISTORY = "mutehistory";
    public static final String DB_REPORTS = "reports";

    public static final  List<String> STAFF_SLOTS = Arrays.asList("server-owner", "admin-1", "admin-2", "admin-3", "modplus-1", "modplus-2", "modplus-3", "mod-1", "mod-2", "mod-3", "mod-4", "mod-5", "mod-6", "mod-7", "trialmod-1", "trialmod-2", "trialmod-3", "supporter-1", "supporter-2", "supporter-3", "supporter-4", "supporter-5", "builder-1", "builder-2", "builder-3");

    private static General plugin;
    private static Database db;

    public void onEnable() {

        plugin = this;

        loadFiles();
        loadDatabase();
        loadEvents();
        loadCommands();
        Rank.reload();
        ClanManager.load();
        BanManager.reload();
        MuteManager.reload();
        ReportManager.reload();
    }

    public void onDisable() {

        removeScoreboard();
        for (Player players : Bukkit.getOnlinePlayers()) {
            Scoreboard board = players.getScoreboard();
            board.getTeams().forEach(Team::unregister);
            GeneralPlayer all = GeneralPlayer.get(players);
            final String msg = all.getLanguage().get(GeneralLanguage.RELOAD);
            all.getPlayer().kickPlayer(msg);
        }
        for (World world : Bukkit.getWorlds()) {
            List<Entity> entList = world.getEntities();
            entList.forEach((drops) -> {
                if (drops instanceof Item)
                    drops.remove();
            });
        }
    }

    private void loadCommands() {

        new AdminCommand();
        new InvisCommand();
        new VisCommand();
        new WatchCommand();
        new InventorySeeCommand();
        new SpeedCommand();
        new PvPToggleCommand();
        new GamemodeCommand();
        new BuildCommand();
        new PingCommand();
        new SpawnCommand();
        new SetSpawnCommand();
        new HeadCommand();
        new DayCommand();
        new NightCommand();
        new HelpCommand();
        new ClearLagCommand();
        new StatsCommand();
        new ClearCommand();
        new StatusCommand();
        new SetRankCommand();
        new ClanCommand();
        new ClanChatCommand();
        new SpecialKitCommand();
        new AgreeCommand();
        new TellCommand();
        new ReplyCommand();
        new SayCommand();
        new ChatClearCommand();
        new ChatToggleCommand();
        new ChatLogCommand();
        new LanguageCommand();
        new SpyCommand();
        new SupportCommand();
        new SuicideCommand();
        new LagCommand();
        new SetStaffCommand();
        new ClearStaffCommand();
        new PartyCommand();
        new PunishCommand();
        new UnbanCommand();
        new KickCommand();
        new CheckInfoCommand();
        new IPInfoCommand();
        new UnmuteCommand();
        new ReportCommand();
        new ReportListCommand();
        new ReportClearCommand();
        new SilentCommand();
        new AlertsCommand();
    }

    private void loadEvents() {
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        getServer().getPluginManager().registerEvents(new SpecialModeEventListener(), this);
        getServer().getPluginManager().registerEvents(new JoinQuitEventsListener(), this);
        getServer().getPluginManager().registerEvents(new PvPToggleEventListener(), this);
        getServer().getPluginManager().registerEvents(new RankChangeEventListener(), this);
        getServer().getPluginManager().registerEvents(new AFKEventsListener(), this);
        getServer().getPluginManager().registerEvents(new SignEventsListener(), this);
        getServer().getPluginManager().registerEvents(new GUIEventListener(), this);
        getServer().getPluginManager().registerEvents(new AgreeEventsListener(), this);
        getServer().getPluginManager().registerEvents(new ChatEventListener(), this);
        getServer().getPluginManager().registerEvents(new ChatBlacklistListener(), this);
        getServer().getPluginManager().registerEvents(new ChatToggleEventListener(), this);
        getServer().getPluginManager().registerEvents(new ToolEventListener(), this);
        getServer().getPluginManager().registerEvents(new LanguageEventListener(), this);
        getServer().getPluginManager().registerEvents(new AchievementEventListener(), this);
        getServer().getPluginManager().registerEvents(new SupportEventListener(), this);
        getServer().getPluginManager().registerEvents(new MenuGUIEventListener(), this);
        getServer().getPluginManager().registerEvents(new MenuToolEventListener(), this);
        getServer().getPluginManager().registerEvents(new SoupEventListener(), this);
        getServer().getPluginManager().registerEvents(new BanEventListener(), this);
        getServer().getPluginManager().registerEvents(new MuteEventListener(), this);
        getServer().getPluginManager().registerEvents(new LoginEventListener(), this);
        getServer().getPluginManager().registerEvents(new ReportEventListener(), this);
        getServer().getPluginManager().registerEvents(new InfoEventListener(), this);
        getServer().getPluginManager().registerEvents(new DamageEventListener(), this);
        getServer().getPluginManager().registerEvents(new RegionChangeEventListener(), this);
        getServer().getPluginManager().registerEvents(new ActionStoreEventListener(), this);
        getServer().getPluginManager().registerEvents(new CombatLogEventListener(), this);
        getServer().getPluginManager().registerEvents(new SpamCheckEventListener(), this);
    }

    private void loadFiles() {

        File general = new File(DIRECTORY_GENERAL);
        File database = new File(DIRECTORY_DATABASE);
        File permissions = new File(DIRECTORY_PERMISSIONS);
        File blacklist = new File(DIRECTORY_BLACKLIST);
        if (!general.exists())
            general.mkdir();
        if (!blacklist.exists()) {
            try {
                blacklist.createNewFile();
                YamlConfiguration bList = YamlConfiguration.loadConfiguration(blacklist);
                bList.set("insults", Arrays.asList("fuck"));
                bList.set("links", Arrays.asList("http"));
                bList.save(blacklist);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!database.exists()) {
            try {
                database.createNewFile();
                YamlConfiguration cfg = YamlConfiguration.loadConfiguration(database);
                cfg.set("host", "host");
                cfg.set("user", "user");
                cfg.set("pass", "password");
                cfg.set("name", "name");
                cfg.set("port", "port");
                cfg.save(database);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!permissions.exists()) {
            try {
                permissions.createNewFile();
                YamlConfiguration perms = YamlConfiguration.loadConfiguration(permissions);
                for(Rank rank : Rank.values())
                    perms.set("groups." + rank.getName().toLowerCase() + ".permissions", Arrays.asList("this.is.an.example"));
                perms.save(permissions);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadDatabase() {
        File database = new File(DIRECTORY_DATABASE);
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(database);

        String prefix = "[General - Database]";
        String host = cfg.getString("host");
        String user = cfg.getString("user");
        String pass = cfg.getString("pass");
        String name = cfg.getString("name");
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
            if (!db.hasTable(DB_PLAYERS)) {
                String query = "CREATE TABLE " + DB_PLAYERS + " ("
                        + " uuid TEXT(50) NOT NULL,"
                        + " name TEXT(30) NOT NULL,"
                        + " rank TEXT(10) NOT NULL,"
                        + " ip TEXT(30) NOT NULL,"
                        + " agreed BOOLEAN NOT NULL,"
                        + " language TEXT(20) NOT NULL,"
                        + " firstjoin BIGINT NOT NULL DEFAULT 0,"
                        + " lastjoin BIGINT NOT NULL DEFAULT 0,"
                        + " playtime BIGINT NOT NULL DEFAULT 0,"
                        + " kills INT NOT NULL DEFAULT 0,"
                        + " deaths INT NOT NULL DEFAULT 0,"
                        + " prestigetokens BIGINT NOT NULL DEFAULT 0,"
                        + " tokens BIGINT NOT NULL DEFAULT 0,"
                        + " prestige INT NOT NULL DEFAULT 0,"
                        + " level INT NOT NULL DEFAULT 0,"
                        + " exp BIGINT NOT NULL DEFAULT 0,"
                        + " elo BIGINT NOT NULL DEFAULT 0,"
                        + " sentreports INT NOT NULL DEFAULT 0,"
                        + " truereports INT NOT NULL DEFAULT 0,"
                        + " clan TEXT(5),"
                        + " clanrank TEXT(10));";
                db.createTable(query, DB_PLAYERS);
            }

            if (!db.hasTable(DB_CHAT_LOGS)) {
                String query = "CREATE TABLE " + DB_CHAT_LOGS + " ("
                        + " date BIGINT NOT NULL,"
                        + " uuid TEXT(50) NOT NULL,"
                        + " name TEXT(20) NOT NULL,"
                        + " message TEXT(300) NOT NULL,"
                        + " type TEXT(10) NOT NULL);";
                db.createTable(query, DB_CHAT_LOGS);
            }

            if (!db.hasTable(DB_CLANS)) {
                String query = "CREATE TABLE " + DB_CLANS + " ("
                        + " name TEXT(5) NOT NULL);";
                db.createTable(query, DB_CLANS);
            }

            if(!db.hasTable(DB_STAFF)) {
                String query = "CREATE TABLE " + DB_STAFF + " (" +
                        " rank TEXT(50) NOT NULL," +
                        " uuid TEXT(50) NOT NULL," +
                        " role TEXT(50) NOT NULL);";
                db.createTable(query, DB_STAFF);
                for(String rank : STAFF_SLOTS)
                    db.execute("INSERT INTO " + DB_STAFF + " (rank, uuid, role) VALUES (?, ?, ?);", rank, "You?", "staff");
            }

            if(!db.hasTable(DB_ACHIEVEMENTS)) {
                String query = "CREATE TABLE " + DB_ACHIEVEMENTS + " (" +
                        "uuid TEXT(50) NOT NULL);";
                db.createTable(query, DB_ACHIEVEMENTS);
            }
            for(Achievement achievement : Achievement.values())
                if(!db.hasColumn(DB_ACHIEVEMENTS, achievement.toString().toLowerCase()))
                    db.execute("ALTER TABLE " + DB_ACHIEVEMENTS + " ADD " + achievement.toString().toLowerCase() + " INT NOT NULL DEFAULT 0");
            if(!db.hasTable(DB_BANS)) {
                String query = "CREATE TABLE " + DB_BANS + " ("
                        + " target TEXT(50) NOT NULL,"
                        + " id TEXT(50) NOT NULL,"
                        + " reason  TEXT(100) NOT NULL,"
                        + " details TEXT(100) NOT NULL,"
                        + " time  BIGINT NOT NULL DEFAULT 0,"
                        + " date BIGINT NOT NULL,"
                        + " tempban BOOLEAN NOT NULL,"
                        + " banner TEXT(30) NOT NULL);";
                db.createTable(query, DB_BANS);
            }

            if(!db.hasTable(DB_BAN_HISTORY)) {
                String query = "CREATE TABLE " + DB_BAN_HISTORY + " ("
                        + " target TEXT(50) NOT NULL,"
                        + " id TEXT(50) NOT NULL,"
                        + " reason  TEXT(100) NOT NULL,"
                        + " details TEXT(100) NOT NULL,"
                        + " time  BIGINT NOT NULL DEFAULT 0,"
                        + " date BIGINT NOT NULL,"
                        + " tempban BOOLEAN NOT NULL,"
                        + " banner TEXT(30) NOT NULL,"
                        + " unbanner TEXT(30) NOT NULL,"
                        + " unbandate BIGINT NOT NULL);";
                db.createTable(query, DB_BAN_HISTORY);
            }

            if(!db.hasTable(DB_MUTES)) {
                String query = "CREATE TABLE " + DB_MUTES + " ("
                        + " target TEXT(50) NOT NULL,"
                        + " id TEXT(50) NOT NULL,"
                        + " reason  TEXT(100) NOT NULL,"
                        + " details TEXT(100) NOT NULL,"
                        + " time  BIGINT NOT NULL DEFAULT 0,"
                        + " date BIGINT NOT NULL,"
                        + " tempmute BOOLEAN NOT NULL,"
                        + " muter TEXT(30) NOT NULL);";
                db.createTable(query, DB_MUTES);
            }

            if(!db.hasTable(DB_MUTE_HISTORY)) {
                String query = "CREATE TABLE " + DB_MUTE_HISTORY + " ("
                        + " target TEXT(50) NOT NULL,"
                        + " id TEXT(50) NOT NULL,"
                        + " reason  TEXT(100) NOT NULL,"
                        + " details TEXT(100) NOT NULL,"
                        + " time  BIGINT NOT NULL DEFAULT 0,"
                        + " date BIGINT NOT NULL,"
                        + " tempmute BOOLEAN NOT NULL,"
                        + " muter TEXT(30) NOT NULL,"
                        + " unmuter TEXT(30) NOT NULL,"
                        + " unmutedate BIGINT NOT NULL);";
                db.createTable(query, DB_MUTE_HISTORY);
            }

            if(!db.hasTable(DB_REPORTS)) {
                String query = "CREATE TABLE " + DB_REPORTS + " ("
                        + " target TEXT(30) NOT NULL,"
                        + " reporter TEXT(30) NOT NULL,"
                        + " reason TEXT(100) NOT NULL,"
                        + " date BIGINT NOT NULL);";
                db.createTable(query, DB_REPORTS);
            }

            System.out.println(prefix + " Set up done!");
        } catch (SQLException e) {
            System.out.println(prefix + " Setup failed!");
            e.printStackTrace();
        }
    }

    public static General getPlugin() {
        return plugin;
    }

    public static Database getDB() {
        return db;
    }

    public static void removeScoreboard() {
        Server server = Server.get();
        for(Player players : Bukkit.getOnlinePlayers()) {
            Scoreboard board = players.getScoreboard();
            board.getObjectives().forEach(Objective::unregister);
        }
    }
}