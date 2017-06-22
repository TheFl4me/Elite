package com.minecraft.plugin.elite.kitpvp.manager.duel.custom;

import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.abstracts.GUI;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import com.minecraft.plugin.elite.kitpvp.manager.duel.custom.DuelSetup;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DuelGUI extends GUI {

	private DuelSetup setup;

	public DuelGUI(Language lang, DuelSetup setup) {
		super(lang);
		this.setup = setup;
	}

	public DuelSetup getSetup() {
		return this.setup;
	}
	
	public Inventory main(ePlayer p) {
		Server server = Server.get();
		this.build(54, this.getLanguage().get(KitPvPLanguage.DUEL_GUI_TITLE));
		
		ItemStack helmet = new ItemStack(Material.IRON_HELMET);
		ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE);
		ItemStack leg = new ItemStack(Material.IRON_LEGGINGS);
		ItemStack boots = new ItemStack(Material.IRON_BOOTS);
		
		ItemStack sword = new ItemStack(Material.IRON_SWORD);
		ItemStack soup = new ItemStack(Material.MUSHROOM_SOUP);
		ItemStack mushroom = new ItemStack(Material.BROWN_MUSHROOM);
		ItemStack fishing = new ItemStack(Material.FISHING_ROD);

		server.rename(mushroom, this.getLanguage().get(KitPvPLanguage.DUEL_GUI_RECRAFT));

		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE);
		Server.get().rename(glass, " ");

		ItemStack abort;
		ItemStack done;
		if(this.getSetup().getStandby().getUniqueId().equals(p.getUniqueId())) {
			abort = glass;
			done = glass;
		} else {
			abort = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
			server.rename(abort, this.getLanguage().get(KitPvPLanguage.DUEL_GUI_CANCEL));
			done = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 13);
			server.rename(done, this.getLanguage().get(KitPvPLanguage.DUEL_GUI_DONE));
		}

		ItemStack head1 = server.playerHead(this.getSetup().getEditor().getName());
		server.rename(head1, this.getLanguage().get(KitPvPLanguage.DUEL_GUI_EDITOR)
				.replaceAll("%p", this.getSetup().getEditor().getName()));

		ItemStack head2 = server.playerHead(this.getSetup().getStandby().getName());
		server.rename(head2, this.getLanguage().get(KitPvPLanguage.DUEL_GUI_STANDBY)
				.replaceAll("%p", this.getSetup().getStandby().getName()));
		
		this.fill(glass);
		
		this.getInventory().setItem(DuelSetup.Slot.HELMET.getSlot(), helmet);
		this.getInventory().setItem(DuelSetup.Slot.CHESTPLATE.getSlot(), chest);
		this.getInventory().setItem(DuelSetup.Slot.LEGGINGS.getSlot(), leg);
		this.getInventory().setItem(DuelSetup.Slot.BOOTS.getSlot(), boots);
		
		this.getInventory().setItem(DuelSetup.Slot.SOUP.getSlot(), soup);
		this.getInventory().setItem(DuelSetup.Slot.SWORD.getSlot(), sword);
		this.getInventory().setItem(DuelSetup.Slot.RECRAFT.getSlot(), mushroom);
		this.getInventory().setItem(DuelSetup.Slot.FISHING_ROD.getSlot(), fishing);

		this.getInventory().setItem(3, head1);
		this.getInventory().setItem(5, head2);
		
		this.getInventory().setItem(9, abort);
		this.getInventory().setItem(18, abort);
		this.getInventory().setItem(27, abort);
		this.getInventory().setItem(36, abort);
		
		this.getInventory().setItem(17, done);
		this.getInventory().setItem(26, done);
		this.getInventory().setItem(35, done);
		this.getInventory().setItem(44, done);
		return this.getInventory();
	}
}