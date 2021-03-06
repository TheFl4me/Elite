package com.minecraft.plugin.elite.survivalgames.manager;

import com.minecraft.plugin.elite.general.api.Server;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public enum ChestItem {
	
	BOW(25, new ItemStack(Material.BOW)),
	ARROW(30, new ItemStack(Material.ARROW)),
	IRON_SWORD(3, new ItemStack(Material.IRON_SWORD)),
	STONE_SWORD(15, new ItemStack(Material.STONE_SWORD)),
	WOOD_SWORD(20, new ItemStack(Material.WOOD_SWORD)),
	GOLD_SWORD(15, new ItemStack(Material.GOLD_SWORD)),

	IRON_AXE(10, new ItemStack(Material.IRON_AXE)),
	STONE_AXE(15, new ItemStack(Material.STONE_AXE)),
	WOOD_AXE(15, new ItemStack(Material.WOOD_AXE)),

	IRON_CHEST_PLATE(5, new ItemStack(Material.IRON_CHESTPLATE)),
	IRON_LEGGINGS(6, new ItemStack(Material.IRON_LEGGINGS)),
	IRON_HELMET(7, new ItemStack(Material.IRON_HELMET)),
	IRON_BOOTS(8, new ItemStack(Material.IRON_BOOTS)),
	CHAIN_CHEST_PLATE(11, new ItemStack(Material.CHAINMAIL_CHESTPLATE)),
	CHAIN_LEGGINGS(12, new ItemStack(Material.CHAINMAIL_LEGGINGS)),
	CHAIN_HELMET(13, new ItemStack(Material.CHAINMAIL_HELMET)),
	CHAIN_BOOTS(14, new ItemStack(Material.CHAINMAIL_BOOTS)),
	GOLD_CHEST_PLATE(17, new ItemStack(Material.GOLD_CHESTPLATE)),
	GOLD_LEGGINGS(18, new ItemStack(Material.GOLD_LEGGINGS)),
	GOLD_HELMET(19, new ItemStack(Material.GOLD_HELMET)),
	GOLD_BOOTS(20, new ItemStack(Material.GOLD_BOOTS)),
	LEATHER_CHEST_PLATE(22, new ItemStack(Material.LEATHER_CHESTPLATE)),
	LEATHER_LEGGINGS(23, new ItemStack(Material.LEATHER_LEGGINGS)),
	LEATHER_HELMET(24, new ItemStack(Material.LEATHER_HELMET)),
	LEATHER_BOOTS(25, new ItemStack(Material.LEATHER_BOOTS)),

	SOUP(350, new ItemStack(Material.MUSHROOM_SOUP)),
	APPLE(30, new ItemStack(Material.APPLE)),
	BREAD(30, new ItemStack(Material.BREAD)),
	RAW_PORK_CHOP(25, new ItemStack(Material.PORK)),
	COOKED_PORK_CHOP(15, new ItemStack(Material.GRILLED_PORK)),
	GOLDEN_APPLE(3, new ItemStack(Material.GOLDEN_APPLE)),
	RAW_FISH(25, new ItemStack(Material.RAW_FISH)),
	COOKED_FISH(12, new ItemStack(Material.COOKED_FISH)),
	RAW_BEEF(30, new ItemStack(Material.RAW_BEEF)),
	COOKED_BEEF(15, new ItemStack(Material.COOKED_BEEF)),
	CARROT(30, new ItemStack(Material.CARROT)),
	PUMPKIN_PIE(15, new ItemStack(Material.PUMPKIN_PIE)),
	CAKE(10, new ItemStack(Material.CAKE)),

	COMPASS(5, new ItemStack(Material.COMPASS)),
	FISHING_ROD(20, new ItemStack(Material.FISHING_ROD)),
	
	DIAMOND(3, new ItemStack(Material.DIAMOND)),
	IRON_INGOT(5, new ItemStack(Material.IRON_INGOT)),
	STICK(10, new ItemStack(Material.STICK)),
	EGG(10, new ItemStack(Material.EGG)),
	WEB(10, new ItemStack(Material.WEB)),

	EXP_BOTTLE(5, new ItemStack(Material.EXP_BOTTLE)),
	BOAT(5, new ItemStack(Material.BOAT)),
	LAPIS_LAZULI(10, new ItemStack(Material.INK_SACK, 1, (short) 4)),
	ENDER_PEARL(5, new ItemStack(Material.ENDER_PEARL)),

	DIAMOND_SWORD(1, new ItemStack(Material.DIAMOND_SWORD)),
	DIAMOND_AXE(4, new ItemStack(Material.DIAMOND_AXE)),
	DIAMOND_HELMET(5, new ItemStack(Material.DIAMOND_HELMET)),
	DIAMOND_CHEST_PLATE(2, new ItemStack(Material.DIAMOND_CHESTPLATE)),
	DIAMOND_LEGGINGS(3, new ItemStack(Material.DIAMOND_LEGGINGS)),
	DIAMOND_BOOTS(5, new ItemStack(Material.DIAMOND_BOOTS));
	
	
	private int spawnChance;
	private ItemStack item;

	private static final Collection<ChestItem> SPECIAL_ITEMS = Arrays.asList(DIAMOND_AXE, DIAMOND_BOOTS, DIAMOND_CHEST_PLATE, DIAMOND_HELMET, DIAMOND_LEGGINGS, DIAMOND_SWORD);
	
	ChestItem(int chance, ItemStack item) {
		this.spawnChance = chance;
		this.item = item;
	}
	
	public static List<ChestItem> getAllItems() {
		Server server = Server.get();
		List<ChestItem> list = new ArrayList<>();
		for(ChestItem item : values()) {
			if(item == SOUP && !server.hasSoups())
				continue;
			for(int i = 0; i < item.getSpawnChance(); i++) {
				list.add(item);
			}
		}
		return list;
	}

	public static List<ChestItem> getAllNormalItems() {
		Server server = Server.get();
		List<ChestItem> list = new ArrayList<>();
		for(ChestItem item : values()) {
			if((item == SOUP && !server.hasSoups()) || SPECIAL_ITEMS.contains(item))
				continue;
			for(int i = 0; i < item.getSpawnChance(); i++) {
				list.add(item);
			}
		}
		return list;
	}

	public static List<ChestItem> getAllSpecialItems() {
		Server server = Server.get();
		List<ChestItem> list = new ArrayList<>();
		for(ChestItem item : SPECIAL_ITEMS) {
			for(int i = 0; i < item.getSpawnChance(); i++) {
				list.add(item);
			}
		}
		return list;
	}
	
	public int getSpawnChance() {
		return this.spawnChance;
	}
	
	public ItemStack getItem() {
		return this.item;
	}
	
	public static void setRandom(Inventory inv, boolean special) {
		List<ChestItem> items = (special ? getAllSpecialItems() : getAllNormalItems()) ;
		Random r1 = new Random();
		Random r2 = new Random();
		ChestItem item = items.get(r2.nextInt(items.size()));
		inv.setItem(r1.nextInt(inv.getSize()), item.getItem());
		if(item.isStackable()) {
			Random r3 = new Random();
			int index = r3.nextInt(5);
			for(int i = 0; i < index; i++)
				inv.addItem(item.getItem());
		}
	}
	
	public boolean isStackable() {
		switch(this) {
		case ARROW:
			case APPLE:
			case BREAD:
			case RAW_PORK_CHOP:
			case COOKED_PORK_CHOP:
			case RAW_FISH:
			case COOKED_FISH:
			case RAW_BEEF:
			case COOKED_BEEF:
			case CARROT:
			case PUMPKIN_PIE:
			case STICK:
			case EGG:
			case LAPIS_LAZULI:
			case EXP_BOTTLE:
				return true;
			default:
				return false;
		}
	}
}