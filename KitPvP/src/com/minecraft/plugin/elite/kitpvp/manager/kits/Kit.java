package com.minecraft.plugin.elite.kitpvp.manager.kits;

import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.api.interfaces.LanguageNode;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.kitpvp.KitPvP;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public enum Kit {
	
	PVP(5, 1, null, null, new ItemStack(Material.DIAMOND_SWORD), KitPvPLanguage.KIT_PVP_ABILITY, KitPvPLanguage.KIT_PVP_DESCRIPTION),
	ARCHER(5, 1, new ItemStack(Material.BOW), null, new ItemStack(Material.BOW), KitPvPLanguage.KIT_ARCHER_ABILITY, KitPvPLanguage.KIT_ARCHER_DESCRIPTION),
	BACKUP(5, 2, new ItemStack(Material.BOOK), KitPvPLanguage.KIT_BACKUP_ITEM, new ItemStack(Material.BOOK), KitPvPLanguage.KIT_BACKUP_ABILITY, KitPvPLanguage.KIT_BACKUP_DESCRIPTION),
	FISHERMAN(5, 18, new ItemStack(Material.FISHING_ROD), null, new ItemStack(Material.FISHING_ROD), KitPvPLanguage.KIT_FISHERMAN_ABILITY, KitPvPLanguage.KIT_FISHERMAN_DESCRIPTION),
	KANGAROO(5, 46, new ItemStack(Material.FIREWORK), KitPvPLanguage.KIT_KANGAROO_ITEM, new ItemStack(Material.FIREWORK), KitPvPLanguage.KIT_KANGAROO_ABILITY, KitPvPLanguage.KIT_KANGAROO_DESCRIPTION),
	PHANTOM(5, 32, new ItemStack(Material.FEATHER), null, new ItemStack(Material.FEATHER), KitPvPLanguage.KIT_PHANTOM_ABILITY, KitPvPLanguage.KIT_PHANTOM_DESCRIPTION),
	THOR(5, 34, new ItemStack(Material.WOOD_AXE), KitPvPLanguage.KIT_THOR_ITEM, new ItemStack(Material.WOOD_AXE), KitPvPLanguage.KIT_THOR_ABILITY, KitPvPLanguage.KIT_THOR_DESCRIPTION),
	TURTLE(5, 6, null, null, new ItemStack(Material.DIAMOND_CHESTPLATE), KitPvPLanguage.KIT_TURTLE_ABILITY, KitPvPLanguage.KIT_TURTLE_DESCRIPTION),
	ANCHOR(5, 22, null, null, new ItemStack(Material.ANVIL), KitPvPLanguage.KIT_ANCHOR_ABILITY, KitPvPLanguage.KIT_ANCHOR_DESCRIPTION),
	VIPER(5, 28, null, null, new ItemStack(Material.INK_SACK, 1, (short) 2), KitPvPLanguage.KIT_VIPER_ABILITY, KitPvPLanguage.KIT_VIPER_DESCRIPTION),
	SNAIL(5, 24, null, null, new ItemStack(Material.WEB), KitPvPLanguage.KIT_SNAIL_ABILITY, KitPvPLanguage.KIT_SNAIL_DESCRIPTION),
	STOMPER(5, 54, null, null, new ItemStack(Material.DIAMOND_BOOTS), KitPvPLanguage.KIT_STOMPER_ABILITY, KitPvPLanguage.KIT_STOMPER_DESCRIPTION),
	ENDERMAGE(5, 42, new ItemStack(Material.NETHER_BRICK_ITEM), KitPvPLanguage.KIT_ENDERMAGE_ITEM, new ItemStack(Material.ENDER_PORTAL_FRAME), KitPvPLanguage.KIT_ENDERMAGE_ABILITY, KitPvPLanguage.KIT_ENDERMAGE_DESCRIPTION),
	ROGUE(5, 48, null, null, new ItemStack(Material.REDSTONE), KitPvPLanguage.KIT_ROGUE_ABILITY, KitPvPLanguage.KIT_ROGUE_DESCRIPTION),
	NEO(5, 20, null, null, new ItemStack(Material.IRON_CHESTPLATE), KitPvPLanguage.KIT_NEO_ABILITY, KitPvPLanguage.KIT_NEO_DESCRIPTION),
	MONK(5, 10, new ItemStack(Material.BLAZE_ROD), null, new ItemStack(Material.BLAZE_ROD), KitPvPLanguage.KIT_MONK_ABILITY, KitPvPLanguage.KIT_MONK_DESCRIPTION),
	SURPRISE(5, 8, null, null, new ItemStack(Material.CAKE), KitPvPLanguage.KIT_SURPRISE_ABILITY, KitPvPLanguage.KIT_SURPRISE_DESCRIPTION),
	POSEIDON(5, 14, null, null, new ItemStack(Material.WATER_BUCKET), KitPvPLanguage.KIT_POSEIDON_ABILITY, KitPvPLanguage.KIT_POSEIDON_DESCRIPTION),
	NINJA(5, 52, null, null, new ItemStack(Material.NETHER_STAR), KitPvPLanguage.KIT_NINJA_ABILITY, KitPvPLanguage.KIT_NINJA_DESCRIPTION),
	SWITCHER(5, 16, new ItemStack(Material.SNOW_BALL, 10), null, new ItemStack(Material.SNOW_BALL), KitPvPLanguage.KIT_SWITCHER_ABILITY, KitPvPLanguage.KIT_SWITCHER_DESCRIPTION),
	MAGMA(5, 26, null, null, new ItemStack(Material.LAVA_BUCKET), KitPvPLanguage.KIT_MAGMA_ABILITY, KitPvPLanguage.KIT_MAGMA_DESCRIPTION),
	REAPER(5, 30, new ItemStack(Material.STONE_HOE), KitPvPLanguage.KIT_REAPER_ITEM, new ItemStack(Material.STONE_HOE), KitPvPLanguage.KIT_REAPER_ABILITY, KitPvPLanguage.KIT_REAPER_DESCRIPTION),
	FROSTY(5, 12, null, null, new ItemStack(Material.SNOW_BLOCK), KitPvPLanguage.KIT_FROSTY_ABILITY, KitPvPLanguage.KIT_FROSTY_DESCRIPTION),
	GRANDPA(5, 4, new ItemStack(Material.STICK), KitPvPLanguage.KIT_GRANDPA_ITEM, new ItemStack(Material.STICK), KitPvPLanguage.KIT_GRANDPA_ABILITY, KitPvPLanguage.KIT_GRANDPA_DESCRIPTION),
	TITAN(5, 50, new ItemStack(Material.BEDROCK), KitPvPLanguage.KIT_TITAN_ITEM, new ItemStack(Material.BEDROCK), KitPvPLanguage.KIT_TITAN_ABILITY, KitPvPLanguage.KIT_TITAN_DESCRIPTION),
	TANK(5, 44, null, null, new ItemStack(Material.TNT), KitPvPLanguage.KIT_TANK_ABILITY, KitPvPLanguage.KIT_TANK_DESCRIPTION),
	BERSERKER(5, 40, null, null, new ItemStack(Material.POTION, 1, (short) 9), KitPvPLanguage.KIT_BERSERKER_ABILITY, KitPvPLanguage.KIT_BERSERKER_DESCRIPTION),
	HULK(5, 36, null, null, new ItemStack(Material.SADDLE), KitPvPLanguage.KIT_HULK_ABILITY, KitPvPLanguage.KIT_HULK_DESCRIPTION),
	RAIJIN(5, 38, new ItemStack(Material.ARROW, 5), null, new ItemStack(Material.ARROW), KitPvPLanguage.KIT_RAIJIN_ABILITY, KitPvPLanguage.KIT_RAIJIN_DESCRIPTION),
	GLADIATOR(5, 55, new ItemStack(Material.IRON_FENCE), KitPvPLanguage.KIT_GLADIATOR_ITEM, new ItemStack(Material.IRON_FENCE), KitPvPLanguage.KIT_GLADIATOR_ABILITY, KitPvPLanguage.KIT_GLADIATOR_DESCRIPTION),
	REPULSE(5, 33, new ItemStack(Material.FIREWORK_CHARGE), KitPvPLanguage.KIT_REPULSE_ITEM, new ItemStack(Material.FIREWORK_CHARGE), KitPvPLanguage.KIT_REPULSE_ABILITY, KitPvPLanguage.KIT_REPULSE_DESCRIPTION);
	//TODO: Madman, Grappler, Glider, Blink, Spectre, Planetary Devastation, Cannibal,
	
	private int price;
	private int level;
	private ItemStack icon;
	private ItemStack item;
	private LanguageNode itemName;
	private LanguageNode desc;
	private LanguageNode ability;

	Kit(int price, int level, ItemStack item, LanguageNode itemName, ItemStack icon, LanguageNode ability, LanguageNode desc) {
		this.price = price;
		this.level = level;
		this.item = item;
		this.icon = icon;
		this.itemName = itemName;
		this.ability = ability;
		this.desc = desc;
	}

	public String getName() {
		String name = this.toString().toLowerCase();
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}
	
	public int getPrice() {
		return this.price;
	}

	public int getLevel() {
		return this.level;
	}
	
	public ItemStack getItem() {
		if(this.item != null) {
			if(this == GRANDPA) {
				this.item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
			}
			return this.item;
		}
		return null;
	}
	
	public ItemStack getIcon(ChatColor color) {
		ItemStack item = this.icon;
		Server server = Server.get();
		server.rename(item, color + this.getName());
		return item;
	}

	public void givePermission(UUID uuid, int code) {
		Database db = KitPvP.getDB();
		db.update(KitPvP.DB_KITS, this.getName().toLowerCase(), code, "uuid", uuid);
	}

	public int getPermissionType(UUID uuid) {
		Database db = KitPvP.getDB();
		try {
			ResultSet type = db.select(KitPvP.DB_KITS, "uuid", uuid.toString());
			if(type.next()) {
				return type.getInt(this.getName().toLowerCase());
			}
		} catch(SQLException e) {
			return 0;
		}
		return 0;
	}

	public String getItemName(Language lang) {
		if(this.itemName == null)
			return null;
		return lang.get(this.itemName);
	}
	
	public String getAbility(Language lang) {
		return lang.get(this.ability);
	}
	
	public String getDescription(Language lang) {
		return lang.get(this.desc);
	}
}