package com.minecraft.plugin.elite.kitpvp.manager;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.kitpvp.KitPvP;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import com.minecraft.plugin.elite.kitpvp.manager.kits.Kit;
import com.minecraft.plugin.elite.kitpvp.manager.kits.events.KitChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class KitPlayer extends GeneralPlayer {

	private int cooldownTime;
	private BukkitRunnable cooldownTask;
	private Kit kit;
	private boolean editing;

	private static Map<UUID, KitPlayer> players = new HashMap<>();
	private static Map<UUID, KitPlayer> loggingInPlayers = new HashMap<>();

	public static KitPlayer get(Player player) {
		return get(player.getUniqueId());
	}

	public static KitPlayer get(UUID uuid) {
		KitPlayer result = players.get(uuid);
		if(result == null) {
			KitPlayer login_result = loggingInPlayers.get(uuid);
			if(login_result == null) {
				return null;
			}
			return login_result;
		}
		return result;
	}

	public static KitPlayer get(String name) {
		@SuppressWarnings("deprecation") UUID uuid = Bukkit.getPlayer(name).getUniqueId();
		if(uuid != null) {
			KitPlayer result = get(uuid);
			if(result != null)
				return result;
		}
		return null;
	}

	public static KitPlayer getPlayerLoggingIn(Player player) {
		return getPlayerLoggingIn(player.getUniqueId());
	}

	public static KitPlayer getPlayerLoggingIn(UUID uuid) {
		return loggingInPlayers.get(uuid);
	}

	@SuppressWarnings("UnusedReturnValue")
	public static KitPlayer login(Player player) {
		KitPlayer p = new KitPlayer(player);
		loggingInPlayers.put(player.getUniqueId(), p);
		return p;
	}

	@Override
	public void logout() {
		loggingInPlayers.remove(this.getUniqueId());
	}

	@Override
	public void join() {
		loggingInPlayers.remove(this.getUniqueId());
		players.put(this.getUniqueId(), this);
	}

	@Override
	public void leave() {
		players.remove(this.getUniqueId());
	}

	public KitPlayer(Player player) {
		super(player);
		this.cooldownTime = 0;
		this.cooldownTask = null;
		this.kit = null;
		this.editing = false;
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
		final KitPlayer p = this;
		this.setCooldownTime(seconds);
		this.cooldownTask = new BukkitRunnable() {
			public void run() {
				if(p.hasCooldown()) {
					final int index = p.getCooldownTime();
					p.setCooldownTime(index - 1);
					if(p.getCooldownTime() <= 0)
						p.stopCooldown();
				}
			}
		};
		this.cooldownTask.runTaskTimer(KitPvP.getPlugin(), 0, 20);
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
				KitPlayer z = KitPlayer.get((Player) ent);
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
		return kit.getPermissionType(this.getUniqueId()) > 0 || this.isMasterPrestige() || KitPvP.getFreeKits().contains(kit);
	}

	public void setKit(Kit kit) {
		this.kit = kit;
		KitPvP.updateScoreboard();
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
		this.clearKit();
		if(kit == Kit.SURPRISE) {
			Random r = new Random();
			int i = r.nextInt(Kit.values().length - 1);
			kit = Kit.values()[i];
		}

		this.setKit(kit);

		KitChangeEvent event = new KitChangeEvent(this, kit);
		Bukkit.getPluginManager().callEvent(event);

		switch(kit) {
			case KANGAROO:
				this.setCanFly(true);
				this.setCanSpeed(true);
				break;
			case PHANTOM:
				this.setCanFly(true);
				this.setCanSpeed(true);
				break;
		}

		this.getPlayer().sendMessage(this.getLanguage().get(KitPvPLanguage.KIT_GIVE)
				.replaceAll("%kit", kit.getName()));
	}

	public void giveKitInv(boolean armor) {
		Inventory inv = this.getPlayer().getInventory();

		this.getPlayer().setGameMode(GameMode.SURVIVAL);
		inv.clear();
		this.getPlayer().getActivePotionEffects().clear();
		this.getPlayer().getInventory().setArmorContents(null);
		this.getPlayer().setFoodLevel(20);
		this.getPlayer().setHealth(20);
		this.getPlayer().setMaxHealth(20);
		this.getPlayer().setExp(0);
		this.getPlayer().setLevel(0);

		ItemStack sword;
		if(armor)
			sword = new ItemStack(Material.IRON_SWORD);
		else
			sword = new ItemStack(Material.STONE_SWORD);
		this.setItem(SlotType.SWORD, sword);

		Server server = Server.get();
		if(this.hasKit()) {
			ItemStack item = this.getKit().getItem();
			if(item != null) {
				String name = this.getKit().getItemName(this.getLanguage());
				if(name != null)
					server.rename(item, name);
				this.setItem(SlotType.KIT_ITEM, item);
			}
			if(this.getKit() == Kit.ARCHER)
				inv.addItem(new ItemStack(Material.ARROW, 10));
		}

		this.addDefaults(armor);
	}

	public void addDefaults(boolean armor) {
		PlayerInventory inv = this.getPlayer().getInventory();

		if(armor) {
			inv.setArmorContents(null);
			inv.setHelmet(new ItemStack(Material.IRON_HELMET));
			inv.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
			inv.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			inv.setBoots(new ItemStack(Material.IRON_BOOTS));
		}

		this.setItem(SlotType.BOWL, (new ItemStack(Material.BOWL, 32)));
		this.setItem(SlotType.BROWN_MUSHROOM, (new ItemStack(Material.BROWN_MUSHROOM, 32)));
		this.setItem(SlotType.RED_MUSHROOM, (new ItemStack(Material.RED_MUSHROOM, 32)));

		for(int i = 0; i < inv.getSize(); i++)
			inv.addItem(new ItemStack(Material.MUSHROOM_SOUP));
	}

	public int getSlot(SlotType mat) {
		Database db = General.getDB();
		try {
			ResultSet res = db.select(KitPvP.DB_SETTINGS, "uuid", this.getUniqueId().toString());
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
			db.update(KitPvP.DB_SETTINGS, column, id, "uuid", this.getUniqueId());
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
