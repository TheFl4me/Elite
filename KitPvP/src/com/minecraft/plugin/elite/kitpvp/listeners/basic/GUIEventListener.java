package com.minecraft.plugin.elite.kitpvp.listeners.basic;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.events.GUIClickEvent;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import com.minecraft.plugin.elite.kitpvp.manager.custom.DuelGUI;
import com.minecraft.plugin.elite.kitpvp.manager.custom.DuelSetup;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIEventListener implements Listener {
	
	@EventHandler
	public void clickItemInGUI(GUIClickEvent e) {
		GeneralPlayer p = e.getPlayer();
		Server server = Server.get();
		ItemStack item = e.getItem();
		ItemMeta itemMeta = item.getItemMeta();
		if(e.getGUI() instanceof DuelGUI) {
			DuelGUI duelgui = (DuelGUI) p.getGUI();
			DuelSetup setup = duelgui.getSetup();
			if(p.getPlayer().getUniqueId().equals(setup.getEditor().getUniqueId())) {
				if(itemMeta.hasDisplayName()) {
					if(itemMeta.getDisplayName().equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(KitPvPLanguage.DUEL_GUI_CANCEL))) {
						setup.setEditing(false);
						setup.abort();
					}
					else if(itemMeta.getDisplayName().equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(KitPvPLanguage.DUEL_GUI_DONE)))
						setup.switchRoles();
					else if(itemMeta.getDisplayName().equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(KitPvPLanguage.DUEL_GUI_EDIT)))
						setup.edit();
					else if(itemMeta.getDisplayName().equalsIgnoreCase(p.getLanguage().getOnlyFirstLine(KitPvPLanguage.DUEL_GUI_ACCEPT)))
						setup.accept();
					if(setup.getPhase(p) != DuelSetup.Phase.PENDING) {
						int i = e.getSlot();
						Material mat = setup.next(item, i);
						if(mat != null) {
							ItemStack nextItem = new ItemStack(mat);
							if (nextItem.getType() == Material.BROWN_MUSHROOM)
								Server.get().rename(nextItem, p.getLanguage().get(KitPvPLanguage.DUEL_GUI_RE_CRAFT));
							setup.change(i, nextItem);
						}
					}
					return;
				}
				if(setup.getPhase(p) != DuelSetup.Phase.PENDING) {
					int i = e.getSlot();
					Material mat = setup.next(item, i);
					if(mat != null) {
						ItemStack nextItem = new ItemStack(mat);
						if (nextItem.getType() == Material.BROWN_MUSHROOM)
							Server.get().rename(nextItem, p.getLanguage().get(KitPvPLanguage.DUEL_GUI_RE_CRAFT));
						setup.change(i, nextItem);
					}
				}
			}
		}
	}
}
