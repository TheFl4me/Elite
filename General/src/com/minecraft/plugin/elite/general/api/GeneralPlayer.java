package com.minecraft.plugin.elite.general.api;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.antihack.hacks.PlayerAttack;
import com.minecraft.plugin.elite.general.antihack.hacks.PlayerClick;
import com.minecraft.plugin.elite.general.antihack.hacks.PlayerMove;
import com.minecraft.plugin.elite.general.api.abstracts.GUI;
import com.minecraft.plugin.elite.general.api.abstracts.Tool;
import com.minecraft.plugin.elite.general.api.enums.Achievement;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.api.enums.Prefix;
import com.minecraft.plugin.elite.general.api.enums.Rank;
import com.minecraft.plugin.elite.general.api.events.ClearPlayerEvent;
import com.minecraft.plugin.elite.general.api.events.InvisChangeEvent;
import com.minecraft.plugin.elite.general.api.events.kits.KitChangeEvent;
import com.minecraft.plugin.elite.general.api.events.kits.KitEnableEvent;
import com.minecraft.plugin.elite.general.api.events.mode.AdminModeChangeEvent;
import com.minecraft.plugin.elite.general.api.events.mode.WatchModeChangeEvent;
import com.minecraft.plugin.elite.general.api.events.stats.ELOChangeEvent;
import com.minecraft.plugin.elite.general.api.events.stats.ExpChangeEvent;
import com.minecraft.plugin.elite.general.api.events.stats.LevelChangeEvent;
import com.minecraft.plugin.elite.general.api.events.stats.PrestigeChangeEvent;
import com.minecraft.plugin.elite.general.api.interfaces.LanguageNode;
import com.minecraft.plugin.elite.general.api.interfaces.PermissionNode;
import com.minecraft.plugin.elite.general.api.special.PlayerHit;
import com.minecraft.plugin.elite.general.api.special.clan.Clan;
import com.minecraft.plugin.elite.general.api.special.clan.ClanManager;
import com.minecraft.plugin.elite.general.api.special.kits.Kit;
import com.minecraft.plugin.elite.general.api.special.party.Party;
import com.minecraft.plugin.elite.general.api.special.party.PartyManager;
import com.minecraft.plugin.elite.general.database.Database;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class GeneralPlayer {

	private static Map<UUID, GeneralPlayer> players = new HashMap<>();
	private static Map<UUID, GeneralPlayer> loggingInPlayers = new HashMap<>();

	private Player player;

	private Rank invisTo;
	private BukkitRunnable pendingAFK;
	private GUI gui;
	private Set<Tool> tools;
	private boolean adminMode;
	private boolean watching;
	private boolean building;
	private boolean invis;
	private boolean afk;
	private boolean pendingAgree;
	private boolean spy;
	private boolean showAlerts;
	private int killStreak;
	private List<PlayerHit> hits;

	private PlayerMove lastMove;
	private PlayerMove lastOnGround;
	private PlayerMove[] moves;
	private PlayerAttack[] attacks;
	private PlayerClick[] clicks;
	private int moveCount;
	private int attackCount;
	private int clickCount;
	private int lastOnGroundMovesAgo;
	private boolean canFly;
	private boolean canSpeed;
	private BukkitRunnable combatLogTask;
	private BukkitRunnable knockbackTask;
	private long invalid;

	private int cooldownTime;
	private BukkitRunnable cooldownTask;
	private Kit kit;
	private boolean canUseKit;
	private boolean editing;

	public static GeneralPlayer get(Player player) {
		return get(player.getUniqueId());
	}

	public static GeneralPlayer get(UUID uuid) {
		GeneralPlayer result = players.get(uuid);
		if(result == null)
			return loggingInPlayers.get(uuid);
		return result;
	}

	public static GeneralPlayer get(String name) {
		@SuppressWarnings("deprecation") Player p = Bukkit.getPlayer(name);
		if(p != null)
			return get(p.getUniqueId());
		return null;
	}

	public static GeneralPlayer getPlayerLoggingIn(Player player) {
		return getPlayerLoggingIn(player.getUniqueId());
	}

	public static GeneralPlayer getPlayerLoggingIn(UUID uuid) {
		return loggingInPlayers.get(uuid);
	}

    @SuppressWarnings("UnusedReturnValue")
	public static GeneralPlayer login(Player player) {
		GeneralPlayer p = new GeneralPlayer(player);
		loggingInPlayers.put(player.getUniqueId(), p);
		return p;
	}

	public GeneralPlayer(Player player) {
	    this.player = player;
	    this.invisTo = null;
	    this.pendingAFK = null;
	    this.gui = null;
	    this.tools = new HashSet<>();
	    this.adminMode = false;
	    this.watching = false;
	    this.building = false;
	    this.invis = false;
	    this.afk = false;
	    this.pendingAgree = false;
	    this.killStreak = 0;
	    this.spy = false;
		this.hits = new ArrayList<>();

		this.showAlerts = false;

		this.moves = new PlayerMove[50];
		this.attacks = new PlayerAttack[5];
		this.clicks = new PlayerClick[150];

		this.moveCount = 0;
		this.attackCount = 0;
		this.clickCount = 0;
		this.lastOnGroundMovesAgo = 0;

		this.invalid = 0;

		this.canFly = false;
		this.canSpeed = false;

		this.combatLogTask = null;
		this.knockbackTask = null;

		this.cooldownTime = 0;
		this.cooldownTask = null;
		this.kit = null;
		this.canUseKit = false;
		this.editing = false;

		Database db = General.getDB();
		try {
			ResultSet res = db.select(General.DB_PLAYERS, "uuid", this.getUniqueId().toString());
			if(res.next())
				this.setLanguage(Language.valueOf(res.getString("language")));
		} catch(SQLException e) {
			this.setLanguage(Language.ENGLISH);
		}
	}

	public Player getPlayer() {
		return this.player;
	}

	public void logout() {
		loggingInPlayers.remove(this.getUniqueId());
	}

	public void join() {
		loggingInPlayers.remove(this.getUniqueId());
		players.put(this.getUniqueId(), this);
		Database db = General.getDB();
		if(!db.containsValue(General.DB_PLAYERS, "uuid", this.getUniqueId().toString())) {
			db.execute("INSERT INTO " + General.DB_PLAYERS + " (uuid, name, rank, ip, agreed, language, firstjoin, lastjoin, playtime, kills, deaths, tokens, prestige, level, exp, elo, sentreports, truereports, clan, clanrank) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
					this.getUniqueId(), this.getName(), Rank.NORMAL.getName(), this.getIP(), 0, Language.ENGLISH.toString(), System.currentTimeMillis(), System.currentTimeMillis(), 0, 0, 0, 0, 0, 1, 0, 2000, 0, 0, "", "");
			Rank.set(Bukkit.getOfflinePlayer(this.getUniqueId()), Rank.NORMAL);
		} else {
			db.update(General.DB_PLAYERS, "name", this.getName(), "uuid", this.getUniqueId());
			db.update(General.DB_PLAYERS, "ip", this.getIP(), "uuid", this.getUniqueId());
			db.update(General.DB_PLAYERS, "lastjoin", System.currentTimeMillis(), "uuid", this.getUniqueId());
		}
        this.loadPermissions();

        if(!db.containsValue(General.DB_ACHIEVEMENTS, "uuid", this.getUniqueId().toString())) {
			db.execute("INSERT INTO " + General.DB_ACHIEVEMENTS + " (uuid) VALUES (?);", this.getUniqueId());
			for (Achievement achievement : Achievement.values())
				db.update(General.DB_ACHIEVEMENTS, achievement.toString().toLowerCase(), 0, "uuid", this.getUniqueId());
		}

		if(!db.containsValue(General.DB_KITS, "uuid", this.getUniqueId().toString())) {
			db.execute("INSERT INTO " + General.DB_KITS + " (uuid) VALUES (?);", this.getUniqueId());
			for (Kit kit : Kit.values())
				db.update(General.DB_KITS, kit.getName().toLowerCase(), 0, "uuid", this.getUniqueId());
		}

		if(!db.containsValue(General.DB_SETTINGS, "uuid", this.getUniqueId().toString())) {
			db.execute("INSERT INTO " + General.DB_SETTINGS + " (uuid, sword, kititem, redmushroom, brownmushroom, bowl) VALUES (?, ?, ?, ?, ?, ?);", this.getUniqueId(), 0, 1, 15, 16, 17);
		}

		this.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}

	public void leave() {
		this.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		players.remove(this.getUniqueId());
	}

	public String getName() {
		return this.getPlayer().getName();
	}

	public String getChatName() {
		return this.getPlayer().getDisplayName();
	}

	public UUID getUniqueId() {
		return this.getPlayer().getUniqueId();
	}

	public String getIP() {
		return this.getPlayer().getAddress().getAddress().getHostAddress();
	}

	public int getPing() {
		CraftPlayer cp = (CraftPlayer) this.getPlayer();
		EntityPlayer ep = cp.getHandle();
		return ep.ping;
	}

	public void setChatName(String name) {
		this.getPlayer().setDisplayName(name);
	}

	public boolean hasPermission(PermissionNode permission) {
		return this.getPlayer().hasPermission(permission.toString());
	}

    public void loadPermissions() {
	    this.getPlayer().getEffectivePermissions().forEach((perm) -> {
            PermissionAttachment attachment = perm.getAttachment();
            if(attachment != null)
                attachment.unsetPermission(perm.getPermission());
        });
        this.getRank().getPermissions().forEach((perm) -> {
            if(perm != null) {
                PermissionAttachment attachment = this.getPlayer().addAttachment(General.getPlugin(), perm, true);
                attachment.setPermission(perm, true);
            }
        });
    }

	public Language getLanguage() {
		Database db = General.getDB();
		try {
			ResultSet res = db.select(General.DB_PLAYERS, "uuid", this.getUniqueId().toString());
			if(res.next())
				return Language.valueOf(res.getString("language"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Language.ENGLISH;
	}

	public void setLanguage(Language lang) {
		Database db = General.getDB();
		db.update(General.DB_PLAYERS, "language", lang.toString(), "uuid", this.getUniqueId());
	}

	public void sendMessage(LanguageNode node) {
		this.getPlayer().sendMessage(this.getLanguage().get(node));
	}

	public boolean isLagging() {
		return this.getPing() >= 200;
	}

	public long getFirstJoin() {
		Database db = General.getDB();
		try {
			ResultSet res = db.select(General.DB_PLAYERS, "uuid", this.getUniqueId().toString());
			if(res.next())
				return res.getLong("firstjoin");
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public long getLastJoin() {
		Database db = General.getDB();
		try {
			ResultSet res = db.select(General.DB_PLAYERS, "uuid", this.getUniqueId().toString());
			if(res.next())
				return res.getLong("lastjoin");
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public boolean isInRegion(String region) {
    	WorldGuardPlugin wgp = WorldGuardPlugin.inst();
    	ApplicableRegionSet ars = wgp.getRegionManager(this.getPlayer().getWorld()).getApplicableRegions(this.getPlayer().getLocation());
    	for (ProtectedRegion prg : ars)
    		if (prg.getId().equalsIgnoreCase(region))
    			return true;
    	return false;
    }

	public void openGUI(GUI gui, Inventory inv) {
		this.getPlayer().openInventory(inv);
		if(this.isInGUI())
			this.removeGUI();
		this.gui = gui;
	}

	public boolean isInGUI() {
		return this.getGUI() != null;
	}

	public GUI getGUI() {
		return this.gui;
	}

	public void removeGUI() {
		this.gui = null;
	}

	public void giveTool(Tool tool) {
		this.tools.add(tool);
		this.getPlayer().getInventory().setItem(tool.getSlot(), tool.getItem());
	}

	public boolean hasTool() {
		return !this.tools.isEmpty();
	}

	public boolean hasTool(Tool tool) {
		return this.tools.contains(tool);
	}

	public Tool getTool(Tool tool) {
		for(Tool tools : getTools())
			if(tools.equals(tool))
				return tools;
		return null;
	}

	public Tool[] getTools() {
		return this.tools.toArray(new Tool[this.tools.size()]);
	}

	public void removeTool(Tool tool) {
		this.getPlayer().getInventory().clear(tool.getSlot());
		this.tools.remove(tool);
	}

	public void clearTools() {
		for(Tool tool : this.getTools())
			this.getPlayer().getInventory().clear(tool.getSlot());
		this.tools.clear();
	}

	public void setHeaderFooter() {
		Server server = Server.get();

		StringBuilder ranks = new StringBuilder();
		for(Rank rank : Rank.values())
			ranks.append(rank.getDisplayName() + ChatColor.RESET + ChatColor.GRAY + " | ");

	    CraftPlayer craftplayer = (CraftPlayer) this.getPlayer();
		PlayerConnection connection = craftplayer.getHandle().playerConnection;
		String header_text = this.getLanguage().get(GeneralLanguage.HEADER)
				.replaceAll("%domain", server.getDomain());
		String footer_text = this.getLanguage().get(GeneralLanguage.FOOTER)
				.replaceAll("%ranks", ranks.toString().substring(0, ranks.toString().length() - 3));

		IChatBaseComponent header = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + header_text + "\"}");
		IChatBaseComponent footer = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + footer_text + "\"}");
		ByteBuf byteBuffer = ByteBufAllocator.DEFAULT.buffer(header_text.getBytes().length + footer_text.getBytes().length);

        PacketDataSerializer packetDataSerializer = new PacketDataSerializer(byteBuffer);

		Packet packet = new PacketPlayOutPlayerListHeaderFooter();

        try {
            packetDataSerializer.a(header);
            packetDataSerializer.a(footer);
            packet.a(packetDataSerializer);

        } catch (IOException e) {
            e.printStackTrace();
        }
		connection.sendPacket(packet);
	}


	public void sendTitle(LanguageNode node, int fadeInTime, int stayOnScreenTime, int fadeOutTime) {
		this.sendTitle(this.getLanguage().get(node), fadeInTime, stayOnScreenTime, fadeOutTime);
	}

	public void sendSubTitle(LanguageNode node, int fadeInTime, int stayOnScreenTime, int fadeOutTime) {
		this.sendSubTitle(this.getLanguage().get(node), fadeInTime, stayOnScreenTime, fadeOutTime);
	}

	public void sendTitle(String text, int fadeInTime, int stayOnScreenTime, int fadeOutTime) {
    	CraftPlayer craftplayer = (CraftPlayer) this.getPlayer();
		PlayerConnection connection = craftplayer.getHandle().playerConnection;
		IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}");
		Packet title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
		Packet length = new PacketPlayOutTitle(fadeInTime, stayOnScreenTime, fadeOutTime);
		connection.sendPacket(title);
		connection.sendPacket(length);
    }

    public void sendSubTitle(String text, int fadeInTime, int stayOnScreenTime, int fadeOutTime) {
    	sendTitle("", fadeInTime, stayOnScreenTime, fadeOutTime);
    	CraftPlayer craftplayer = (CraftPlayer) this.getPlayer();
		PlayerConnection connection = craftplayer.getHandle().playerConnection;
		IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}");
		Packet title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatTitle);
		Packet length = new PacketPlayOutTitle(fadeInTime, stayOnScreenTime, fadeOutTime);
		connection.sendPacket(title);
		connection.sendPacket(length);
    }

    public void sendHoverMessage(String msg, String extra) {
		IChatBaseComponent comp = IChatBaseComponent.ChatSerializer.a("{ \"text\": \"" + msg + "\" , \"hoverEvent\": {\"action\": \"show_text\" ,\"value\": { \"text\":\"" + extra + "\" }}}");
		Packet packet = new PacketPlayOutChat(comp);
		((CraftPlayer) this.getPlayer()).getHandle().playerConnection.sendPacket(packet);
	}

	public void sendClickMessage(String msg, String cmd, boolean execute) {

		IChatBaseComponent comp = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + msg + "\", \"clickEvent\": {\"action\":\"" + (execute ? "run_command" : "suggest_command") + "\", \"value\":\"" + cmd + "\"}}");
		Packet packet = new PacketPlayOutChat(comp);
		((CraftPlayer) this.getPlayer()).getHandle().playerConnection.sendPacket(packet);
	}

	public void sendActionBar(String msg) {
		IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + msg + "\"}");
		Packet ppoc = new PacketPlayOutChat(cbc, (byte) 2);
		((CraftPlayer) this.getPlayer()).getHandle().playerConnection.sendPacket(ppoc);
	}

    public void clear() {
    	this.getPlayer().getInventory().clear();
		this.getPlayer().getInventory().setArmorContents(null);
    	for(PotionEffect effect : this.getPlayer().getActivePotionEffects())
    		this.getPlayer().removePotionEffect(effect.getType());
		this.getPlayer().setFireTicks(0);
    	this.getPlayer().setFoodLevel(20);
    	this.getPlayer().setHealth(20);
    	this.getPlayer().setMaxHealth(20);
    	this.getPlayer().setExp(0);
    	this.getPlayer().setLevel(0);
    	this.clearHits();
    	ClearPlayerEvent event = new ClearPlayerEvent(this);
		Bukkit.getPluginManager().callEvent(event);

		if(this.isAdminMode() || this.isWatching())
			this.getPlayer().getInventory().setHelmet(new ItemStack(Material.GLASS));
    }

    public void setAgree(boolean agree) {
		Database db = General.getDB();
		db.update(General.DB_PLAYERS, "agreed", (agree ? 1 : 0), "uuid", this.getUniqueId());
		this.pendingAgree(!agree);
	}

	public void pendingAgree(boolean pending) {
		this.pendingAgree = pending;
	}

	public boolean hasAgreed() {
		Database db = General.getDB();
		try {
			ResultSet res = db.select(General.DB_PLAYERS, "uuid", this.getUniqueId().toString());
			return res.next() && res.getBoolean("agreed");
		} catch (SQLException e) {
			return false;
		}
	}

	public boolean isPendingAgree() {
		return this.pendingAgree;
	}

	public void setAdminMode(boolean admin) {
		this.adminMode = admin;
		if(admin) {
			this.getPlayer().getInventory().clear();
			this.getPlayer().getInventory().setArmorContents(null);
			this.getPlayer().setGameMode(GameMode.CREATIVE);
			for(PotionEffect effect : this.getPlayer().getActivePotionEffects())
				this.getPlayer().removePotionEffect(effect.getType());

			if(!this.isInvis())
				this.setInvis(true);
		} else  {
			this.getPlayer().setGameMode(GameMode.SURVIVAL);
			if(this.isInvis())
				this.setInvis(false);
		}
		AdminModeChangeEvent event = new AdminModeChangeEvent(this, admin);
		Bukkit.getPluginManager().callEvent(event);
	}

	public void setWatching(boolean watch) {
		this.watching = watch;
		this.getPlayer().setAllowFlight(watch);
		this.getPlayer().setFlying(watch);
		this.setInvis(watch);
		if(watch) {
			this.getPlayer().getInventory().clear();
			this.getPlayer().getInventory().setArmorContents(null);
			for(PotionEffect effect : this.getPlayer().getActivePotionEffects())
				this.getPlayer().removePotionEffect(effect.getType());
			this.sendMessage(GeneralLanguage.WATCH_WATCHING);
			this.getPlayer().setHealth(20);
			this.getPlayer().setFoodLevel(20);
		}
		WatchModeChangeEvent event = new WatchModeChangeEvent(this, watch);
		Bukkit.getPluginManager().callEvent(event);
	}

	public void setBuilding(boolean build) {
		this.building = build;
	}

	public void setInvis(boolean invisible) {
		if(invisible)
			this.setInvis(Rank.valueOf(this.getRank().ordinal() - 1));
		else {
			this.invis = false;
			this.setInvisTo(null);
			this.sendMessage(GeneralLanguage.INVIS_VIS);
			for (Player players : Bukkit.getOnlinePlayers())
				players.showPlayer(this.getPlayer());
			InvisChangeEvent event = new InvisChangeEvent(this, false);
			Bukkit.getPluginManager().callEvent(event);
		}
	}

	public void setInvis(Rank rank) {
		this.setInvisTo(rank);
		this.invis = true;
		String msg = this.getLanguage().get(GeneralLanguage.INVIS_INVIS).replaceAll("%rank", this.getInvisTo().getDisplayName().toUpperCase());
		this.getPlayer().sendMessage(msg);
		for (Player players : Bukkit.getOnlinePlayers()) {
			GeneralPlayer all = GeneralPlayer.get(players);
			all.getPlayer().showPlayer(this.getPlayer());
			if (all.getRank().ordinal() <= rank.ordinal())
				all.getPlayer().hidePlayer(this.getPlayer());
		}
		InvisChangeEvent event = new InvisChangeEvent(this, true);
		Bukkit.getPluginManager().callEvent(event);
	}

	public void setAFK(boolean away) {
		this.afk = away;
		if(away)  {
			if(this.isPendingAFK())
				this.stopAFKPendingTimer();
		} else {
			this.startAFKPendingTimer();
		}
	}

	public void startAFKPendingTimer() {
		final GeneralPlayer p = this;
	    this.pendingAFK = new BukkitRunnable() {
	    	public void run() {
	    		if(!p.isAFK()) {
	    			p.setAFK(true);
	    			p.sendMessage(GeneralLanguage.AFK_TRUE);
	    		}
	    	}
	    };
	    this.pendingAFK.runTaskLater(General.getPlugin(), 1200);
	}

	public void stopAFKPendingTimer() {
		this.pendingAFK.cancel();
		this.pendingAFK = null;
	}

	public void setSpy(boolean spy) {
		this.spy = spy;
	}

	public void setupRanks() {
		Scoreboard board = this.getPlayer().getScoreboard();
		board.getTeams().forEach(Team::unregister);

		for (Rank rank : Rank.values()) {
			Prefix prefix = Rank.valueOf(rank.getName().toUpperCase()).getPrefix();
			Team team = board.registerNewTeam(rank.getTeamName());
			team.setPrefix(prefix.getColor());
		}
	}

	public void setTag() {
		this.setChatName(this.getRank().getPrefix().getColor() + this.getName());
		for(Player players : Bukkit.getOnlinePlayers()) {
			Scoreboard board = players.getScoreboard();
			for(Player all : Bukkit.getOnlinePlayers()) {
				GeneralPlayer other = get(all);
				Team team = board.getTeam(other.getRank().getTeamName());
				if(team != null) {
					board.getTeams().forEach(teams -> teams.removeEntry(other.getName()));
					team.addEntry(other.getName());
				}
			}
		}
	}

	public void clearTag() {
		for(Player players : Bukkit.getOnlinePlayers()) {
			Scoreboard board = players.getScoreboard();
			board.getTeams().forEach(teams -> teams.removeEntry(this.getName()));
		}
	}

	public boolean canAdminMode() {
		return this.hasPermission(GeneralPermission.MODE_ADMIN);
	}
	public boolean canWatch() {
		return this.hasPermission(GeneralPermission.MODE_WATCH);
	}
	public boolean isAdminMode() {
		return this.adminMode;
	}
	public boolean isWatching() {
		return this.watching;
	}

	public boolean isInvis() {
		return this.invis;
	}
	public boolean isBuilding() {
		return this.building;
	}
	public boolean isAFK() {
		return this.afk;
	}
	public boolean isPendingAFK() {
		return this.pendingAFK != null;
	}

	public boolean canSpy() {
		return this.hasPermission(GeneralPermission.MODE_SPY);
	}
	public boolean isSpy() {
		return this.spy;
	}

	public Rank getInvisTo() {
		return this.invisTo;
	}

	public void setInvisTo(Rank rank) {
		this.invisTo = rank;
	}

	public Rank getRank() {
		return Rank.get(this.getUniqueId());
	}

	public boolean isAdmin() {
		return this.getRank() == Rank.ADMIN;
	}
	public boolean isModPlus() {
		return this.getRank() == Rank.MODPLUS || this.isAdmin();
	}
	public boolean isMod() {
		return this.getRank() == Rank.MOD || this.isModPlus();
	}
	public boolean isSupporter() {
		return this.getRank() == Rank.SUPPORTER || this.isMod();
	}
	public boolean isBuilder() {
		return this.getRank() == Rank.BUILDER || this.isSupporter();
	}
	public boolean isMedia() {
		return this.getRank() == Rank.MEDIA || this.isBuilder();
	}
	public boolean isPremium() {
		return this.getRank() == Rank.PREMIUM || this.isMedia();
	}

	public void setKillStreak(int streak) {
		this.killStreak = streak;
	}

	public void setPlayTime(long time) {
		Database db = General.getDB();
		db.update(General.DB_PLAYERS, "playtime", time, "uuid", this.getUniqueId());
	}

	public int getKillStreak() {
		return this.killStreak;
	}

	public int getKills() {
		Database db = General.getDB();
		try {
			ResultSet res = db.select(General.DB_PLAYERS, "uuid", this.getUniqueId().toString());
			if(res.next())
				return res.getInt("kills");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public int getDeaths() {
		Database db = General.getDB();
		try {
			ResultSet res = db.select(General.DB_PLAYERS, "uuid", this.getUniqueId().toString());
			if(res.next())
				return res.getInt("deaths");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public double getKDR() {
		if(this.getDeaths() == 0)
			return this.getKills();
		if(this.getKills() == 0)
			return 0;
		return (double) this.getKills() / (double) this.getDeaths();
	}
	public long getPlayTime() {
		Database db = General.getDB();
		ResultSet res = db.select(General.DB_PLAYERS, "uuid", this.getUniqueId().toString());
		try {
			if(res.next())
				return res.getLong("playtime");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public void addKill() {
		Database db = General.getDB();
		db.update(General.DB_PLAYERS, "kills", this.getKills() + 1, "uuid", this.getUniqueId());
		setKillStreak(this.getKillStreak() + 1);
	}
	public void addDeath() {
		Database db = General.getDB();
		db.update(General.DB_PLAYERS, "deaths", this.getDeaths() + 1, "uuid", this.getUniqueId());
    }
	public long getTokens() {
		Database db = General.getDB();
		ResultSet res = db.select(General.DB_PLAYERS, "uuid", this.getUniqueId().toString());
		try {
			if(res.next())
				return res.getLong("tokens");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void setTokens(long amount) {
		Database db = General.getDB();
		db.update(General.DB_PLAYERS, "tokens", amount, "uuid", this.getUniqueId());
	}

	public void addTokens(long amount) {
		this.setTokens(this.getTokens() + amount);
	}

	public long getPrestigeTokens() {
		Database db = General.getDB();
		ResultSet res = db.select(General.DB_PLAYERS, "uuid", this.getUniqueId().toString());
		try {
			if(res.next())
				return res.getLong("prestigetokens");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void setPrestigeTokens(long amount) {
		Database db = General.getDB();
		db.update(General.DB_PLAYERS, "prestigetokens", amount, "uuid", this.getUniqueId());
	}

	public int getPrestige() {
		Database db = General.getDB();
		ResultSet res = db.select(General.DB_PLAYERS, "uuid", this.getUniqueId().toString());
		try {
			if(res.next())
				return res.getInt("prestige");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public ChatColor getPrestigePrefix() {
		switch(this.getPrestige()) {
		case 1: return ChatColor.GREEN;
		case 2: return ChatColor.YELLOW;
		case 3: return ChatColor.GRAY;
		case 4: return ChatColor.DARK_GRAY;
		case 5: return ChatColor.DARK_PURPLE;
		case 6: return ChatColor.BLUE;
		case 7: return ChatColor.BLACK;
		case 8: return ChatColor.RED;
		case 9: return ChatColor.DARK_RED;
		case 10: return ChatColor.GOLD;
		default: return ChatColor.WHITE;
		}
	}

	public String getLevelPrefix() {
		return ChatColor.GRAY + "[" + this.getPrestigePrefix() + (this.getPrestige() == 10 ? "MAX" : Integer.toString(this.getLevel())) + ChatColor.GRAY + "] " + ChatColor.RESET;
	}

	public boolean isMasterPrestige() {
		return this.getPrestige() == 10;
	}

	public void prestige() {
		PrestigeChangeEvent event = new PrestigeChangeEvent(this, this.getPrestige(), this.getPrestige() + 1);
		this.setPrestige(this.getPrestige() + 1);
		Bukkit.getPluginManager().callEvent(event);
		this.setLevel(1);
		this.setTokens(0);
		this.setExp(0);
		this.setPrestigeTokens(this.getPrestigeTokens() + 1);
	}

	public void setPrestige(int prestige) {
		Database db = General.getDB();
		db.update(General.DB_PLAYERS, "prestige", prestige, "uuid", this.getUniqueId());
	}

	public int getLevel() {
		Database db = General.getDB();
		ResultSet res = db.select(General.DB_PLAYERS, "uuid", this.getUniqueId().toString());
		try {
			if(res.next())
				return res.getInt("level");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public boolean isMaxLevel() {
		return this.getLevel() >= 55 || this.isMasterPrestige();
	}

	public long getRequiredExpForNextLevel(int level) {
		return (((long) level + 1) * 100L) - this.getExp();
	}

	public void setLevel(int level) {
		Database db = General.getDB();
		LevelChangeEvent event = new LevelChangeEvent(this, this.getLevel(), level);
		db.update(General.DB_PLAYERS, "level", level, "uuid", this.getUniqueId());
		Bukkit.getPluginManager().callEvent(event);
	}

	public long getExp() {
		Database db = General.getDB();
		ResultSet res = db.select(General.DB_PLAYERS, "uuid", this.getUniqueId().toString());
		try {
			if(res.next())
				return res.getInt("exp");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void addExp(long exp) {
		if(!this.isMasterPrestige() && !this.isMaxLevel()) {
			long nextLevel = this.getRequiredExpForNextLevel(this.getLevel());
			long remaining = exp;
		    this.sendSubTitle("+" + Long.toString(exp) + " Exp", 1, 20, 3);
			while(remaining > 0) {
				if(nextLevel <= remaining) {
					if(this.getLevel() + 1 == 55)  {
						this.setLevel(55);
						this.setExp(0);
						break;
					}
					this.setLevel(this.getLevel() + 1);
					nextLevel = this.getRequiredExpForNextLevel(this.getLevel());
					remaining -= nextLevel;
					this.setExp(0);
				} else {
					this.setExp(this.getExp() + remaining);
					break;
				}
			}
		}
	}

	public void setExp(long exp) {
		Database db = General.getDB();
		ExpChangeEvent event = new ExpChangeEvent(this, exp);
		db.update(General.DB_PLAYERS, "exp", exp, "uuid", this.getUniqueId());
		Bukkit.getPluginManager().callEvent(event);
	}

	public void addExpToAttackers() {
		DecimalFormat df = new DecimalFormat("00.0");
		double totalDamage = 0.0;
		HashMap<UUID, Double> damagePerPlayer = new HashMap<>();
		for(PlayerHit hit : this.getHits()) {
			double damage = hit.getDamage();
			totalDamage += damage;
			if(damagePerPlayer.containsKey(hit.getAttacker()))
				damage += damagePerPlayer.get(hit.getAttacker());
			damagePerPlayer.put(hit.getAttacker(), damage);
		}

		for(UUID uuid : damagePerPlayer.keySet()) {
			GeneralPlayer z = get(uuid);
			if(z != null) {
				double exp = damagePerPlayer.get(uuid) * ((this.getPrestige() + 1) * 100) / totalDamage;
				double percent = damagePerPlayer.get(uuid) * 100 / totalDamage;
				z.addExp(Math.round(exp));
				z.getPlayer().sendMessage(z.getLanguage().get(GeneralLanguage.DAMAGE_PERCENT)
						.replaceAll("%percent", df.format(percent))
						.replaceAll("%player", this.getChatName()));
			}
		}
	}

	public long getELO() {
		Database db = General.getDB();
		ResultSet res = db.select(General.DB_PLAYERS, "uuid", this.getUniqueId().toString());
		try {
			if(res.next())
				return res.getLong("elo");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 2000;
	}

	public void setELO(long elo) {
		Database db = General.getDB();
		ELOChangeEvent event = new ELOChangeEvent(this, elo);
		db.update(General.DB_PLAYERS, "elo", (elo < 0 ? 0 : elo), "uuid", this.getUniqueId());
		Bukkit.getPluginManager().callEvent(event);
	}

	public List<PlayerHit> getHits() {
		return new ArrayList<>(this.hits);
	}

	public void saveHit(PlayerHit hit) {
		this.hits.add(hit);
	}

	public void clearHits() {
		this.hits.clear();
	}


	public Clan getClan() {
		return ClanManager.get(this.getUniqueId());
	}

	public String getClanPrefix() {
		return this.getClan() == null ? "" : ChatColor.GRAY + "[" + ChatColor.WHITE + this.getClan().getName() + ChatColor.GRAY + "] ";
	}

	public Party getParty() {
		return PartyManager.get(this);
	}

	public boolean hasAchievement(Achievement achievement) {
		Database db = General.getDB();
		try {
			ResultSet type = db.select(General.DB_ACHIEVEMENTS, "uuid", this.getUniqueId().toString());
			if(type.next()) {
				return type.getInt(achievement.toString().toLowerCase()) > 0;
			}
		} catch(SQLException e) {
			return false;
		}
		return false;
	}

	public void giveAchievement(Achievement achievement) {
		Database db = General.getDB();
		db.update(General.DB_ACHIEVEMENTS, achievement.toString().toLowerCase(), 1, "uuid", this.getUniqueId());
		this.getPlayer().sendMessage(this.getLanguage().get(GeneralLanguage.ACHIEVEMENT_UNLOCKED).replaceAll("%achievement", achievement.getName(this.getLanguage())));
		this.getPlayer().playSound(this.getPlayer().getLocation(), Sound.LEVEL_UP, 1, 1);
		this.addExp(achievement.getExp());
		this.addTokens(achievement.getTokens());
	}

	public float getLineOfSightAngleTo(Entity ent) {
		Vector p2p = ent.getLocation().toVector().subtract(this.getPlayer().getLocation().toVector()).normalize();
		Vector sight = this.getPlayer().getEyeLocation().getDirection().normalize();
		return sight.angle(p2p);
	}

	public boolean hasLineOfSight(Entity ent) {
		return this.hasLineOfSight(ent, 20);
	}

	public boolean hasLineOfSight(Entity ent, double degree) {
		return this.getLineOfSightAngleTo(ent) < Math.toRadians(degree);
	}

	public boolean canBypassChecks() {
		return this.hasPermission(GeneralPermission.ANTI_HACK_BYPASS);
	}

	public void showAlerts(boolean alerts) {
		this.showAlerts = alerts;
	}

	public boolean canViewAlerts() {
		return this.showAlerts;
	}

	public boolean hasAlertsPerm() {
		return this.hasPermission(GeneralPermission.ANTI_HACK_ALERTS);
	}

	public void setCombatLog() {
		if(this.isCombatLog()) {
			this.getCombatLogTask().cancel();
			this.setCombatLogTask(null);
		}
		this.setCombatLogTask(new BukkitRunnable() {
			@Override
			public void run() {
				GeneralPlayer p = get(getUniqueId());
				if(p != null)
					if (p.isCombatLog()) {
						p.sendMessage(GeneralLanguage.COMBATLOG_SAFE);
						p.getCombatLogTask().cancel();
						p.setCombatLogTask(null);
					}
				else
					cancel();
			}
		});
		this.getCombatLogTask().runTaskLater(General.getPlugin(), 200);
	}

	public boolean isCombatLog() {
		return this.combatLogTask != null;
	}

	public BukkitRunnable getCombatLogTask() {
		return this.combatLogTask;
	}

	public void setCombatLogTask(BukkitRunnable runnable) {
		this.combatLogTask = runnable;
	}

	public boolean hasKnockback() {
		return this.knockbackTask != null;
	}

	public BukkitRunnable getKnockbackTask() {
		return this.knockbackTask;
	}

	public void setKnockbackTask(BukkitRunnable runnable) {
		this.knockbackTask = runnable;
	}

	public void invalidate() {
		this.invalidate(2000);
	}

	public void invalidate(long ms) {
		this.invalid = System.currentTimeMillis() + ms;
	}

	public boolean isValid() {
		return System.currentTimeMillis() > this.invalid;
	}


	public void setLastOnGround(PlayerMove move) {
		this.lastOnGround = move;
	}

	public void setLastOnGroundMovesAgo(int i) {
		this.lastOnGroundMovesAgo = i;
	}

	public PlayerMove getLastOnGround() {
		return this.lastOnGround;
	}

	public int getLastOnGroundMovesAgo() {
		return this.lastOnGroundMovesAgo;
	}

	public void setLastMove(PlayerMove move) {
		this.lastMove = move;
	}

	public PlayerMove getLastMove() {
		return this.lastMove;
	}

	public void setMoveCount(int i) {
		this.moveCount = i;
	}

	public int getMoveCount() {
		return this.moveCount;
	}

	public PlayerMove[] getMoves() {
		return this.moves;
	}

	public void setMoves(PlayerMove[] moves) {
		this.moves = moves;
	}

	public PlayerMove getMovesAgo(int x) {
		if (this.getMoveCount() - x < 0)
			return null;
		int index = Math.abs(this.getMoveCount() - x) % this.getMoves().length;
		return this.getMoves()[index];
	}



	public PlayerAttack[] getAttacks() {
		return this.attacks;
	}

	public int getAttackCount() {
		return this.attackCount;
	}

	public void setAttackCount(int i) {
		this.attackCount = i;
	}

	public PlayerAttack getDamageAgo(int x) {
		if (this.getAttackCount() - x < 0)
			return null;
		int index = Math.abs(this.getAttackCount() - x) % this.getAttacks().length;
		return this.getAttacks()[index];
	}

	public PlayerClick[] getClicks() {
		return this.clicks;
	}

	public int getClickCount() {
		return this.clickCount;
	}

	public void setClickCount(int i) {
		this.clickCount = i;
	}

	public PlayerClick getClicksAgo(int x) {
		if (this.getClickCount() - x < 0)
			return null;
		int index = Math.abs(this.getClickCount() - x) % this.getClicks().length;
		return this.getClicks()[index];
	}


	public boolean isCanFly() {
		return this.canFly || this.getPlayer().getAllowFlight();
	}

	public boolean isCanSpeed() {
		return canSpeed;
	}

	public void setCanFly(boolean fly) {
		this.canFly = fly;
	}

	public void setCanSpeed(boolean speed) {
		this.canSpeed = speed;
	}

	public int getCooldownTime() {
		return this.cooldownTime;
	}

	public void setCooldownTime(int seconds) {
		this.cooldownTime = seconds;
		this.sendActionBar((seconds > 0 ? ChatColor.RED.toString() + ChatColor.BOLD + Integer.toString(seconds) : " "));
	}

	public BukkitRunnable getCooldownTask() {
		return this.cooldownTask;
	}

	public boolean hasCooldown() {
		return this.cooldownTask != null;
	}

	public void startCooldown(int seconds) {
		this.setCooldownTime(seconds);
		this.cooldownTask = new BukkitRunnable() {
			public void run() {
				if(hasCooldown()) {
					final int index = getCooldownTime();
					setCooldownTime(index - 1);
					if(getCooldownTime() <= 0)
						stopCooldown();
				}
			}
		};
		this.cooldownTask.runTaskTimer(General.getPlugin(), 0, 20);
	}

	public void stopCooldown() {
		if(this.getCooldownTask() != null)
			this.getCooldownTask().cancel();
		this.cooldownTime = 0;
		this.cooldownTask = null;
	}

	public boolean isNearRogue() {
		for(Entity ent : this.getPlayer().getNearbyEntities(10, 10, 10)) {
			if(ent instanceof Player) {
				GeneralPlayer z = GeneralPlayer.get((Player) ent);
				if(z.hasKit(Kit.ROGUE))
					return true;
			}
		}
		return false;
	}

	public Kit getKit() {
		return this.kit;
	}

	public boolean hasKit() {
		return this.kit != null;
	}

	public boolean hasKit(Kit kit) {
		return this.getKit() != null && this.getKit() == kit;
	}

	public boolean hasKitPermission(Kit kit) {
		return kit.getPermissionType(this.getUniqueId()) > 0 || this.isMasterPrestige() || General.getFreeKits().contains(kit);
	}

	public void setKit(Kit kit) {
		this.kit = kit;
	}

	public void clearKit() {
		if(this.hasKit()) {
			if(this.hasKit(Kit.PHANTOM)) {
				this.getPlayer().setFlying(false);
				this.getPlayer().setAllowFlight(false);
			}
			this.stopCooldown();
			this.setKit(null);

			this.setCanFly(false);
			this.setCanSpeed(false);
		}
	}

	public void giveKit(Kit kit) {
		KitChangeEvent event = new KitChangeEvent(this, this.getKit(), kit);
		Bukkit.getPluginManager().callEvent(event);

		this.clearKit();
		this.setKit(kit);

		switch(kit) {
			case KANGAROO:
			case PHANTOM:
				this.setCanFly(true);
				this.setCanSpeed(true);
				break;
		}

		this.getPlayer().sendMessage(this.getLanguage().get(GeneralLanguage.KIT_GIVE)
				.replaceAll("%kit", kit.getName()));
	}

	public void enableKit() {
		if (this.hasTool())
			this.clearTools();
		this.getPlayer().getInventory().clear();

		if (this.getKit() == null)
			this.giveKit(Kit.PVP);

		if(this.getKit() == Kit.SURPRISE) {
			Random r = new Random();
			int i = r.nextInt(Kit.values().length - 1);
			this.giveKit(Kit.values()[i]);
		}

		this.getPlayer().setGameMode(GameMode.SURVIVAL);
		this.getPlayer().getActivePotionEffects().clear();
		this.getPlayer().getInventory().setArmorContents(null);
		this.getPlayer().setFoodLevel(20);
		this.getPlayer().setHealth(20);
		this.getPlayer().setMaxHealth(20);
		this.getPlayer().setExp(0);
		this.getPlayer().setLevel(0);

		Server server = Server.get();
		ItemStack item = this.getKit().getItem();
		if(item != null) {
			String name = this.getKit().getItemName(this.getLanguage());
			if(name != null)
				server.rename(item, name);
			this.setItem(SlotType.KIT_ITEM, item);
		}
		if(this.getKit() == Kit.ARCHER)
			this.getPlayer().getInventory().addItem(new ItemStack(Material.ARROW, 10));

		KitEnableEvent event = new KitEnableEvent(this, this.getKit());
		Bukkit.getPluginManager().callEvent(event);
	}

	public boolean canUseKit() {
		return this.canUseKit;
	}

	public void setCanUseKit(boolean bool) {
		this.canUseKit = bool;
	}

	public int getSlot(SlotType mat) {
		Database db = General.getDB();
		try {
			ResultSet res = db.select(General.DB_SETTINGS, "uuid", this.getUniqueId().toString());
			if(res.next()) {
				switch(mat) {
					case SWORD:
						return res.getInt("sword");
					case KIT_ITEM:
						return res.getInt("kititem");
					case RED_MUSHROOM:
						return res.getInt("redmushroom");
					case BROWN_MUSHROOM:
						return res.getInt("brownmushroom");
					case BOWL:
						return res.getInt("bowl");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		switch(mat) {
			case SWORD:
				return 0;
			case KIT_ITEM:
				return 1;
			case RED_MUSHROOM:
				return 15;
			case BROWN_MUSHROOM:
				return 16;
			case BOWL:
				return 17;
			default:
				return 0;
		}
	}

	public void setSlot(Material mat, int id) {
		Database db = General.getDB();
		String column = null;
		switch(mat) {
			case STONE_SWORD:
				column = "sword";
				break;
			case NETHER_STAR:
				column = "kititem";
				break;
			case RED_MUSHROOM:
				column = "redmushroom";
				break;
			case BROWN_MUSHROOM:
				column = "brownmushroom";
				break;
			case BOWL:
				column = "bowl";
				break;
		}
		if(column != null) {
			db.update(General.DB_SETTINGS, column, id, "uuid", this.getUniqueId());
		}
	}

	public void setItem(SlotType slot, ItemStack item) {
		this.getPlayer().getInventory().setItem(this.getSlot(slot), item);
	}

	public void setEditing(boolean editing) {
		this.editing = editing;
	}

	public boolean isEditing() {
		return this.editing;
	}

	public enum SlotType {
		SWORD,
		KIT_ITEM,
		RED_MUSHROOM,
		BROWN_MUSHROOM,
		BOWL,
	}
}