package com.minecraft.plugin.elite.raid.manager;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.raid.Raid;
import com.minecraft.plugin.elite.raid.RaidLanguage;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class Warp {
	
	private String name;
	private Location loc;
	private String owner;
	private WarpType type;
	
	public Warp(String warpName, Location warpLoc, String owner, WarpType type) {
		this.name = warpName;
		this.loc = warpLoc;
		this.owner = owner;
		this.type = type;
		WarpManager.add(this);
	}
	
	public void saveToDB() {
		Database db = Raid.getDB();
		db.execute("INSERT INTO " + Raid.DB_WARP + " (name, type, owner, x, y, z) VALUES (?, ?, ?, ?, ?, ?);", this.getName(), this.getType(), this.getOwner(), this.getLocation().getX(), this.getLocation().getY(), this.getLocation().getZ());
	}
	
	public Location getLocation() {
		return this.loc;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getOwner() {
		return this.owner;
	}
	
	public WarpType getType() {
		return this.type;
	}
	
	public void delete() {
    	Database db = Raid.getDB();
		db.delete(Raid.DB_WARP, "name", this.getName());
    	WarpManager.remove(this);
    }
	
	public void teleport(final ePlayer p) {
    	final Warp warp = this;
		WarpManager.addTimer(p.getUniqueId(), new BukkitRunnable() {
			public void run() {
				if(WarpManager.getTimers().containsKey(p.getUniqueId())) {
					p.sendMessage(RaidLanguage.WARP_CONFIRMED);
					WarpManager.getTimers().remove(p.getUniqueId());
					p.getPlayer().teleport(warp.getLocation());
				}
				cancel();
			}
		});
		WarpManager.getTimers().get(p.getUniqueId()).runTaskLater(Raid.getPlugin(), 100);
    }
	
	public enum WarpType {
		
		PLAYER(),
		CLAN()
	}
}
