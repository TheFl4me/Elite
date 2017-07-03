package com.minecraft.plugin.elite.kitpvp.listeners.basic;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.kitpvp.KitPvP;
import com.minecraft.plugin.elite.kitpvp.manager.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitEventsListener implements Listener {
	
	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent e) {
		ePlayer p = ePlayer.get(e.getPlayer());
		p.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());
		Database db = KitPvP.getDB();
		if(!db.containsValue(KitPvP.DB_KITS, "uuid", p.getUniqueId().toString())) {
			db.execute("INSERT INTO " + KitPvP.DB_KITS + " (uuid) VALUES (?);", p.getUniqueId());
			for (Kit kit : Kit.values())
				db.update(KitPvP.DB_KITS, kit.getName().toLowerCase(), 0, "uuid", p.getUniqueId());
		}
		if(!db.containsValue(KitPvP.DB_SETTINGS, "uuid", p.getUniqueId().toString())) {
			db.execute("INSERT INTO " + KitPvP.DB_SETTINGS + " (uuid, sword, kititem, redmushroom, brownmushroom, bowl) VALUES (?, ?, ?, ?, ?, ?);", p.getUniqueId(), 0, 1, 15, 16, 17);
		}
		KitPvP.updateScoreboard();
		KitPvP.loadHolograms(p);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerQuit(PlayerQuitEvent e) {		
		ePlayer p = ePlayer.get(e.getPlayer());
		p.clear();
	}
}