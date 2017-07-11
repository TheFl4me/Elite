package com.minecraft.plugin.elite.survivalgames.manager.arena;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.enums.Rank;
import com.minecraft.plugin.elite.general.api.interfaces.LanguageNode;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.survivalgames.SurvivalGames;
import com.minecraft.plugin.elite.survivalgames.SurvivalGamesLanguage;
import com.minecraft.plugin.elite.survivalgames.manager.GamePhase;
import com.minecraft.plugin.elite.survivalgames.manager.Lobby;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Arena {

	private String name;
	private World world;
	private Collection<Chest> loadedChests;
	private List<UUID> players;
	private int startPlayerSize;
	private List<Location> pods;
	private Location center;
	private int maxSize;
	private int minSize;
	private GamePhase phase;
	private int countdownTime;
    private BukkitRunnable countdownTask;
	private BukkitRunnable shrinkTask;
	private long playTime;
	private BukkitRunnable playTimeTask;
	private HashMap<UUID, Integer> killCount;
	private UUID winner;

	public Arena(World world) {
		this.name = world.getName();
		this.phase = GamePhase.WAITING;
		this.world = world;
		this.shrinkTask = null;
		this.countdownTask = null;
		this.playTimeTask = null;
		this.winner = null;
		this.killCount = new HashMap<>();
		this.loadedChests = new ArrayList<>();
		List<UUID> pList = new ArrayList<>();
		for(Player pall : Bukkit.getOnlinePlayers()) {
			GeneralPlayer all = GeneralPlayer.get(pall);
			if(!all.isInvis() && !pList.contains(all.getUniqueId()))
				pList.add(all.getUniqueId());
		}
		this.players = pList;
		this.startPlayerSize = 0;
		Database db = SurvivalGames.getDB();
		List<Location> locList = new ArrayList<>();
		try {
			ResultSet coordinates = db.select(SurvivalGames.DB_ARENAS, "name", world.getName());
			while(coordinates.next()) {
				this.center = new Location(world, coordinates.getDouble("centerx"), coordinates.getDouble("centery"), coordinates.getDouble("centerz"));
				world.setSpawnLocation(this.center.getBlockX(), this.center.getBlockY(), this.center.getBlockZ());
				this.minSize = coordinates.getInt("minsize");
				this.maxSize = coordinates.getInt("maxsize");
				double y = coordinates.getDouble("pody");
				for(int i = 0; i < 24; i++) {
					double x = coordinates.getDouble("pod" + Integer.toString(i + 1) + "x");
					double z = coordinates.getDouble("pod" + Integer.toString(i + 1) + "z");
					Location loc = new Location(world, x, y, z);
					locList.add(loc);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.pods = locList;
	}

	public String getName() {
		return this.name;
	}

	public World getWorld() {
		return this.world;
	}

	public int getStartPlayerSize() {
		return this.startPlayerSize;
	}

	public List<GeneralPlayer> getPlayers() {
		List<GeneralPlayer> list = new ArrayList<>();
		this.players.forEach((players) -> list.add(GeneralPlayer.get(players)));
		return list;
	}

	public void addPlayer(GeneralPlayer p) {
		this.players.add(p.getUniqueId());
		this.killCount.put(p.getUniqueId(), 0);
	}

	public void removePlayer(GeneralPlayer p) {
		this.players.remove(p.getUniqueId());
		this.killCount.remove(p.getUniqueId());
		/*if(this.getPlayers().size() <= this.getPods().size() / 10) {
			this.startShrinking();
		}*/
		if(this.getPlayers().size() == 1) {
			this.win(this.getPlayers().get(0));
		}
		this.updateScoreboard();
	}

	public void removePlayer(GeneralPlayer p, LanguageNode node) {
		this.getWorld().strikeLightningEffect(p.getPlayer().getLocation());
		for (Player players : Bukkit.getOnlinePlayers()) {
			this.getWorld().playSound(players.getLocation(), Sound.AMBIENCE_THUNDER, 1, 1);
			players.sendMessage(GeneralPlayer.get(players).getLanguage().get(node)
					.replaceAll("%player", p.getName())
					.replaceAll("%remaining", Integer.toString(this.getPlayers().size() - 1)));
		}
		this.removePlayer(p);
	}

	public List<Location> getPods() {
		return this.pods;
	}

	public Location getCenter() {
		return this.center;
	}

	public int getMaxSize() {
		return this.maxSize;
	}

	public Collection<Chest> getLoadedChests() {
		Collection<Chest> locList = new ArrayList<>();
		locList.addAll(this.loadedChests);
		return locList;
	}

	public void addChest(Chest chest) {
		this.loadedChests.add(chest);
	}

	public void clearChests() {
		this.loadedChests.clear();
	}

	public int getMinSize() {
		return this.minSize;
	}

	public GamePhase getGamePhase() {
		return this.phase;
	}

	public void setGamePhase(GamePhase phase) {
		this.phase = phase;
	}

	public boolean deathMatchStarted() {
		return getShrinkTask() != null;
	}

	public OfflinePlayer getWinner() {
		if(this.winner == null)
			return null;
		return Bukkit.getOfflinePlayer(this.winner);
	}

	public void setWinner(UUID uuid) {
		this.winner = uuid;
	}

	public int getKills(GeneralPlayer p) {
		return this.killCount.get(p.getUniqueId());
	}

	public void addKill(GeneralPlayer p) {
		final int i = this.getKills(p);
		this.killCount.remove(p.getUniqueId());
		this.killCount.put(p.getUniqueId(), i + 1);
		this.updateScoreboard();
	}

	public boolean hasStartedCountdown() {
		return this.countdownTask != null;
	}

	public int getCountdownTime() {
		return this.countdownTime;
	}

	public void setCountdownTime(int seconds) {
		this.countdownTime = seconds;
	}

	public BukkitRunnable getCountdownTask() {
		return this.countdownTask;
	}

	public void endCountdown() {
		this.getCountdownTask().cancel();
		this.countdownTask = null;
	}

	public void startCountDown() {
		this.startPlayerSize = this.getPlayers().size();
		if(!hasStartedCountdown()) {
			this.setCountdownTime(11);
			for(Player players : Bukkit.getOnlinePlayers()) {
				GeneralPlayer all = GeneralPlayer.get(players);
				all.sendTitle(SurvivalGamesLanguage.ARENA_WARNING, 1, 220, 1);
			}
			this.countdownTask = new BukkitRunnable() {
				public void run() {
					if(hasStartedCountdown()) {
						setCountdownTime(getCountdownTime() - 1);
						if(getCountdownTime() == 0) {
							setGamePhase(GamePhase.MAIN);
							for(Player players : Bukkit.getOnlinePlayers()) {
								GeneralPlayer all = GeneralPlayer.get(players);
								all.getPlayer().setLevel(0);
								all.getPlayer().playSound(all.getPlayer().getLocation(), Sound.EXPLODE, 1, 1);
								all.sendMessage(SurvivalGamesLanguage.ARENA_START);
							}
							scheduleShrinking();
	        				endCountdown();
	        				setPlayTime(0);
	        				startPlayTimeClock();
						} else {
							for(Player players : Bukkit.getOnlinePlayers()) {
								GeneralPlayer all = GeneralPlayer.get(players);
								all.getPlayer().setLevel(getCountdownTime());
								all.getPlayer().playSound(all.getPlayer().getLocation(), Sound.NOTE_PLING, 1, 1);
								all.getPlayer().sendMessage(all.getLanguage().get(SurvivalGamesLanguage.ARENA_COUNTDOWN)
										.replaceAll("%seconds", Integer.toString(getCountdownTime())));
							}
						}
					}
				}
			};
			getCountdownTask().runTaskTimer(SurvivalGames.getPlugin(), 20, 20);
		}
	}

	public void scheduleShrinking() {
		Bukkit.getScheduler().runTaskLater(SurvivalGames.getPlugin(), () -> {
            if(!deathMatchStarted()) {
                setGamePhase(GamePhase.DEATHMATCH);
                startShrinking();
            }
        }, 6000);
	}

	public BukkitRunnable getShrinkTask() {
		return this.shrinkTask;
	}

	public void startShrinking() {
		Arena arena = ArenaManager.get(this.getName());
		for(Player all : Bukkit.getOnlinePlayers()) {
			GeneralPlayer p = GeneralPlayer.get(all);
			p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.PORTAL_TRAVEL, 1, 1);
			p.sendMessage(SurvivalGamesLanguage.SHRINK_START);
		}
		this.shrinkTask = new BukkitRunnable() {
			public void run() {
				WorldBorder border = arena.getWorld().getWorldBorder();
				if(border.getSize() > arena.getMinSize()) {
					border.setSize(border.getSize() - 1);
				} else {
					for(Player all : Bukkit.getOnlinePlayers()) {
						GeneralPlayer p = GeneralPlayer.get(all);
						p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.PORTAL_TRAVEL, 1, 1);
						p.sendMessage(SurvivalGamesLanguage.SHRINK_END);
					}
					arena.clearChests();
					Bukkit.getScheduler().runTaskLater(SurvivalGames.getPlugin(), () -> {
                        if(getGamePhase() != GamePhase.END) {
							GeneralPlayer tempWinner = null;
                            for(GeneralPlayer all : getPlayers())
                                if(tempWinner == null || arena.getKills(tempWinner) < arena.getKills(all))
									tempWinner = all;
                            if(tempWinner != null)
								arena.win(tempWinner);
                            else
								arena.end();
                        }
                    }, 6000);
					cancel();
					shrinkTask = null;
				}
			}
		};
		this.getShrinkTask().runTaskTimer(SurvivalGames.getPlugin(), 0, 20);
	}

	public long getPlayTime() {
		return this.playTime;
	}

	public void setPlayTime(long i) {
		this.playTime = i;
	}

	public BukkitRunnable getPlayTimeTask() {
		return this.playTimeTask;
	}

	public void startPlayTimeClock() {
		this.playTimeTask = new BukkitRunnable() {
			@Override
			public void run() {
				setPlayTime(getPlayTime() + 1000);
				updateScoreboard();
			}
		};
		this.getPlayTimeTask().runTaskTimer(SurvivalGames.getPlugin(), 0, 20);
	}

	public void end() {
		for(Player players : Bukkit.getOnlinePlayers()) {
			GeneralPlayer all = GeneralPlayer.get(players);
			if(this.getWinner() == null)
				all.getPlayer().kickPlayer(all.getLanguage().get(SurvivalGamesLanguage.KICK_END));
			else
				all.getPlayer().kickPlayer(all.getLanguage().get(SurvivalGamesLanguage.KICK_END_WINNER)
					.replaceAll("%winner", Rank.get(this.getWinner()).getPrefix().getColor() + this.getWinner().getName()));
		}
		this.getPlayTimeTask().cancel();
		General.removeScoreboard();
		Bukkit.getServer().shutdown();
	}

	public void win(GeneralPlayer p) {
		this.setWinner(p.getUniqueId());
		this.setGamePhase(GamePhase.END);
		p.addExp(5000);
		for(Player players : Bukkit.getOnlinePlayers()) {
			GeneralPlayer all = GeneralPlayer.get(players);
			all.sendTitle(all.getLanguage().get(SurvivalGamesLanguage.WIN).replaceAll("%player", p.getName()), 1, 30, 1);
		}
		Bukkit.getScheduler().runTaskTimer(SurvivalGames.getPlugin(), () -> {
			if(p != null)
				p.getPlayer().getWorld().spawnEntity(p.getPlayer().getLocation(), EntityType.FIREWORK);
			for(Player players : Bukkit.getOnlinePlayers()) {
				GeneralPlayer all = GeneralPlayer.get(players);
				all.getPlayer().sendMessage(all.getLanguage().get(SurvivalGamesLanguage.WIN).replaceAll("%player", this.getWinner().getName()));
			}
        }, 40, 40);
		Bukkit.getScheduler().runTaskLater(SurvivalGames.getPlugin(), this::end, 600);
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

			if(this.getPlayers().contains(p)) {
				Score killsTitle = obj.getScore(p.getLanguage().get(SurvivalGamesLanguage.SCOREBOARD_KILLS));
				killsTitle.setScore(5);

				Score killsValue = obj.getScore(color + Integer.toString(this.getKills(p)));
				killsValue.setScore(4);
			}

			Score timeTitle = obj.getScore(p.getLanguage().get(SurvivalGamesLanguage.SCOREBOARD_PLAYTIME));
			timeTitle.setScore(3);

			Score timeValue = obj.getScore(color + server.getTimeDigital(this.getPlayTime()));
			timeValue.setScore(2);

			Score playersTitle = obj.getScore(p.getLanguage().get(SurvivalGamesLanguage.SCOREBOARD_PLAYERS));
			playersTitle.setScore(1);

			Score playerCount = obj.getScore(color + Integer.toString(this.getPlayers().size()) + "/" + Integer.toString(this.getStartPlayerSize()));
			playerCount.setScore(0);
		}
	}
}
