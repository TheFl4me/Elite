package com.minecraft.plugin.elite.survivalgames.manager;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.survivalgames.SurvivalGames;
import com.minecraft.plugin.elite.survivalgames.SurvivalGamesLanguage;
import com.minecraft.plugin.elite.survivalgames.manager.arena.Arena;
import com.minecraft.plugin.elite.survivalgames.manager.arena.ArenaManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class Lobby {
	
	private List<GeneralPlayer> players;
	private Arena arena;
	private boolean active;
	private int countdownTime;
    private BukkitRunnable countdownTask;
    
    private static Lobby lobby = null;
	
	public static void create() {
		Lobby lob = new Lobby();
		lobby = lob;
		lob.setActive(true);
		List<Arena> arenas = ArenaManager.getAll();
		Random r = new Random();
		int index = r.nextInt(arenas.size());
		Arena arena = arenas.get(index);
		lob.setArena(arena);
	}
	
	public static Lobby get() {
		return lobby;
	}
	
	public Lobby() {
		List<GeneralPlayer> list = new ArrayList<>();
		for(Player players : Bukkit.getOnlinePlayers()) {
			GeneralPlayer all = GeneralPlayer.get(players);
			if(!all.isInvis())
				list.add(all);
		}
		this.players = list;
		this.arena = null;
		this.countdownTask = null;
	}
	
	public Arena getArena() {
		return this.arena;
	}
	
	public void setArena(Arena arena) {
		this.arena = arena;
	}
	
	public World getWorld() {
		return Bukkit.getWorld("world");
	}
	
	public boolean isActive() {
		return this.active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isFull() {
		return this.getPlayers().size() >= 24;
	}
	
	public int neededPlayersToStart() {
		return 10;
	}
	
	public boolean hasNonPremiumPlayer() {
		for(GeneralPlayer all : this.getPlayers())
			if(!all.isPremium())
				return true;
		return false;
	}
	
	public void kickNormalPlayer() {
		Server server = Server.get();
		for(GeneralPlayer all : this.getPlayers()) {
			if(!all.isPremium()) {
				all.getPlayer().kickPlayer(all.getLanguage().get(SurvivalGamesLanguage.KICK_REPLACED)
						.replaceAll("%domain", server.getDomain()));
				return;
			}
		}
	}
	
	public List<GeneralPlayer> getPlayers() {
		List<GeneralPlayer> list = new ArrayList<>();
		list.addAll(this.players);
		return list;
	}
	
	public void addPlayer(GeneralPlayer p) {
		this.players.add(p);
		for(Player players : Bukkit.getOnlinePlayers()) {
			GeneralPlayer all = GeneralPlayer.get(players);
			all.getPlayer().sendMessage(all.getLanguage().get(GeneralLanguage.JOINED).replaceAll("%p", p.getName()));
		}
		if(!this.hasStartedCountdown() && this.getPlayers().size() >= this.neededPlayersToStart() && !this.isFull()) {
			this.startCountDown(60);
			return;
		}
		if(this.isFull() && this.hasStartedCountdown())
			this.setCountdownTime(10);
		this.updateScoreboard();
	}
	
	public void removePlayer(GeneralPlayer p) {
		this.players.remove(p);
		for(Player players : Bukkit.getOnlinePlayers()) {
			GeneralPlayer all = GeneralPlayer.get(players);
			all.getPlayer().sendMessage(all.getLanguage().get(GeneralLanguage.LEFT).replaceAll("%p", p.getName()));
		}
		if(this.hasStartedCountdown() && this.getPlayers().size() < this.neededPlayersToStart()) {
			this.endCountdown();
		}
		this.updateScoreboard();
	}
	
	public boolean hasStartedCountdown() {
		return this.countdownTask != null;
	}
	
	public int getCountdownTime() {
		return this.countdownTime;
	}
	
	public BukkitRunnable getCountdownTask() {
		return this.countdownTask;
	}
	
	public void endCountdown() {
		this.getCountdownTask().cancel();
		this.countdownTask = null;
		General.removeScoreboard();
	}
	
	public void setCountdownTime(int seconds) {
		this.countdownTime = seconds;
	}
	
	public void startCountDown(int seconds) {
		List<Integer> timeKeys = Arrays.asList(50, 40, 30, 20, 10, 5, 4, 3, 2, 1);
		this.setCountdownTime(seconds);
		for(Player players : Bukkit.getOnlinePlayers()) {
			players.setLevel(this.getCountdownTime());
			players.sendMessage(GeneralPlayer.get(players).getLanguage().get(SurvivalGamesLanguage.LOBBY_COUNTDOWN).replaceAll("%seconds", Integer.toString(this.getCountdownTime())));
		}
		lobby.updateScoreboard();
		this.countdownTask = new BukkitRunnable() {
			public void run() {
				if(lobby.hasStartedCountdown()) {
					lobby.setCountdownTime(lobby.getCountdownTime() - 1);
					lobby.updateScoreboard();
					for(Player players : Bukkit.getOnlinePlayers())
						players.setLevel(lobby.getCountdownTime());
					if(timeKeys.contains(lobby.getCountdownTime())) {
						for(Player all : Bukkit.getOnlinePlayers())
							all.sendMessage(GeneralPlayer.get(all).getLanguage().get(SurvivalGamesLanguage.LOBBY_COUNTDOWN)
									.replaceAll("%seconds", Integer.toString(getCountdownTime())));
					} else if(lobby.getCountdownTime() == 0) {
                        Arena arena = getArena();
        				arena.setGamePhase(GamePhase.WAITING);
						lobby.setActive(false);
        				for(Player players : Bukkit.getOnlinePlayers()) {
							GeneralPlayer spec = GeneralPlayer.get(players);
        					if(!lobby.getPlayers().contains(spec)) {
        						spec.getPlayer().teleport(arena.getCenter());
        						if(spec.canAdminMode() && !spec.isWatching())
        							spec.setAdminMode(true);
        						else if(spec.canWatch())
        							spec.setWatching(true);
        						
        					}
        				}
        				for(int i = 0; i < lobby.getPlayers().size(); i++) {
							GeneralPlayer p = lobby.getPlayers().get(i);
        					Location loc = arena.getPods().get(i);			
        					loc.setDirection(arena.getCenter().toVector().subtract(loc.toVector()));
        					p.getPlayer().teleport(loc);
        					p.clear();
        					arena.addPlayer(p);
        				}
						lobby.endCountdown();
        				arena.startCountDown();
						arena.updateScoreboard();
					}
				}
			}
		};
		this.getCountdownTask().runTaskTimer(SurvivalGames.getPlugin(), 20, 20);
	}

	public void updateScoreboard() {
		Lobby lobby = Lobby.get();
		Server server = Server.get();
		for(Player players : Bukkit.getOnlinePlayers()) {
			GeneralPlayer p = GeneralPlayer.get(players);
			ChatColor color = ChatColor.GOLD;
			Scoreboard board = players.getScoreboard();
			board.getObjectives().forEach(Objective::unregister);

			Objective obj = board.registerNewObjective("test", "dummy");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			obj.setDisplayName(p.getLanguage().get(SurvivalGamesLanguage.SCOREBOARD_TITLE)
					.replaceAll("%domain", server.getDomain()));

			Score mapTitle = obj.getScore(p.getLanguage().get(SurvivalGamesLanguage.SCOREBOARD_MAP));
			mapTitle.setScore(5);

			Score map = obj.getScore(color + lobby.getArena().getName());
			map.setScore(4);

			Score timeTitle = obj.getScore(p.getLanguage().get(SurvivalGamesLanguage.SCOREBOARD_TIME));
			timeTitle.setScore(3);

			Score time = obj.getScore(color + server.getTimeDigital(lobby.getCountdownTime() * 1000));
			time.setScore(2);

			Score playersTitle = obj.getScore(p.getLanguage().get(SurvivalGamesLanguage.SCOREBOARD_PLAYERS));
			playersTitle.setScore(1);

			Score playerCount = obj.getScore(color + Integer.toString(this.getPlayers().size()) + "/24");
			playerCount.setScore(0);
		}
	}
}