package com.minecraft.plugin.elite.kitpvp.listeners;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.events.kits.KitEnableEvent;
import com.minecraft.plugin.elite.kitpvp.KitPvP;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class KitEnableEventListener implements Listener {

	@EventHandler
	public void onKitEnable(KitEnableEvent e) {
		GeneralPlayer p = e.getPlayer();
		PlayerInventory inv = p.getPlayer().getInventory();
		ItemStack sword;
		sword = new ItemStack(Material.STONE_SWORD);
		boolean armor = false;
		if (p.isInRegion(KitPvP.REGION_FEAST))
			armor = true;
		if (armor) {
			sword = new ItemStack(Material.IRON_SWORD);
			inv.setArmorContents(null);
			inv.setHelmet(new ItemStack(Material.IRON_HELMET));
			inv.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
			inv.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			inv.setBoots(new ItemStack(Material.IRON_BOOTS));
		}

		p.setItem(GeneralPlayer.SlotType.SWORD, sword);
		p.setItem(GeneralPlayer.SlotType.BOWL, (new ItemStack(Material.BOWL, 32)));
		p.setItem(GeneralPlayer.SlotType.BROWN_MUSHROOM, (new ItemStack(Material.BROWN_MUSHROOM, 32)));
		p.setItem(GeneralPlayer.SlotType.RED_MUSHROOM, (new ItemStack(Material.RED_MUSHROOM, 32)));

		for (int i = 0; i < inv.getSize(); i++)
			inv.addItem(new ItemStack(Material.MUSHROOM_SOUP));
	}
}
