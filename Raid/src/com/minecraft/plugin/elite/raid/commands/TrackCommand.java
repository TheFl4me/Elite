package com.minecraft.plugin.elite.raid.commands;

import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.raid.Raid;
import com.minecraft.plugin.elite.raid.RaidLanguage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class TrackCommand extends eCommand {

	public TrackCommand() {
		super("track", "eraid.track", false);
	}

	public boolean execute(CommandSender cs, Command cmd, String[] args) {
		
		ePlayer p = ePlayer.get((Player) cs);
		Location loc = p.getPlayer().getLocation();
		Block under = loc.getBlock().getRelative(BlockFace.DOWN);
		if(under.getType() != Material.DIAMOND_BLOCK) {
			p.sendMessage(RaidLanguage.TRACK_NULL);
			return true;
		}
		if(!p.getPlayer().getInventory().contains(Material.COMPASS)) {
			p.sendMessage(RaidLanguage.TRACK_NO_COMPASS);
			return true;
		}
		if(!(getRange(loc, BlockFace.NORTH) == getRange(loc, BlockFace.EAST) && getRange(loc, BlockFace.NORTH) == getRange(loc, BlockFace.SOUTH) && getRange(loc, BlockFace.NORTH) == getRange(loc, BlockFace.WEST))) {
			p.sendMessage(RaidLanguage.TRACK_NOT_EQUAL);
			return true;
		}
		double range = getRange(loc, BlockFace.NORTH);
		ePlayer nearest = null;
		boolean sameClan = false;
		for(Entity entity : p.getPlayer().getNearbyEntities(range, range, range)) {
			if(entity instanceof Player) {
				ePlayer potential = ePlayer.get((Player) entity);
				if(!potential.isAdminMode() && !potential.isInvis() && !potential.isInRegion(Raid.SPAWN)) {
					if(p.getClan() != null && potential.getClan() != null && p.getClan().getName().equalsIgnoreCase(potential.getClan().getName())) {
						sameClan = true;
					}	
					if(!sameClan) {
						if(nearest == null) {
							nearest = potential;
						}
						if(potential.getPlayer().getLocation().distance(loc) < nearest.getPlayer().getLocation().distance(loc)) {
							nearest = potential;
						}
					}
				}
			}		
		}
		if(nearest != null) {
			final Location target = nearest.getPlayer().getLocation();
			p.getPlayer().setCompassTarget(target);
			p.getPlayer().sendMessage(p.getLanguage().get(RaidLanguage.TRACK_CONFIRMED)
					.replaceAll("%player", nearest.getChatName()));
			return true;
		} else {
			p.getPlayer().sendMessage(p.getLanguage().get(RaidLanguage.TRACK_NO_ENTITY_FOUND).replaceAll("%radius", Double.toString(range)));
			return true;	
		}
	}
	
	private double getRange(Location loc, BlockFace face) {
		Block under = loc.getBlock().getRelative(BlockFace.DOWN);
		Block next = under.getRelative(face);
		double range = 0;
		int i = 0;
		while(next.getType() == Material.OBSIDIAN) {
			Block next2 = next.getRelative(face);
			i++;
			next = next2;
		}
		if(next.getType() == Material.GOLD_BLOCK)
			range = i * 25;
		return range;
	}
}