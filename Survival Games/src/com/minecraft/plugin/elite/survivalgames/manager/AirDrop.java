package com.minecraft.plugin.elite.survivalgames.manager;

import com.minecraft.plugin.elite.survivalgames.SurvivalGames;
import com.minecraft.plugin.elite.survivalgames.manager.arena.Arena;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AirDrop {

	private EnderDragon dragon;
	private Location dropPoint;
	private Location startPoint;
	private Location endPoint;
	private Location dropLocation;
	private Arena arena;
	private List<Block> structure;
	private List<Block> chests;
	private BukkitRunnable dropTask;
	private BukkitRunnable spawnTask;
	private int spawnTimer;

	public AirDrop(Arena arena) {
		this.arena = arena;

		double y = this.getArena().getCenter().getBlockY() + 150;
		int minX = this.getArena().getCenter().getBlockX() - this.getArena().getMaxSize() / 2;
		int maxX = minX + this.getArena().getMaxSize();
		int minZ = this.getArena().getCenter().getBlockZ() - this.getArena().getMaxSize() / 2;
		int maxZ = minZ + this.getArena().getMaxSize();

		Random r1 = new Random();
		double rX = minX + r1.nextInt(maxX);
		Random r2 = new Random();
		double rZ = minZ + r2.nextInt(maxZ);

		this.dropPoint = new Location(this.getArena().getWorld(), rX, y, rZ);
		Location spawnPoint;
		Location endPoint;

		Random r3 = new Random();
		int direction = r3.nextInt(3); //choose from which border side the dragon should fly from
		switch (direction) {
			case 0:
				spawnPoint = new Location(this.getArena().getWorld(), minX, y, rZ);
				endPoint = new Location(this.getArena().getWorld(), maxX, y, rZ);
				break;
			case 1:
				spawnPoint = new Location(this.getArena().getWorld(), maxX, y, rZ);
				endPoint = new Location(this.getArena().getWorld(), minX, y, rZ);
				break;
			case 2:
				spawnPoint = new Location(this.getArena().getWorld(), rX, y, minZ);
				endPoint = new Location(this.getArena().getWorld(), rX, y, maxZ);
				break;
			default:
				spawnPoint = new Location(this.getArena().getWorld(), rX, y, maxZ);
				endPoint = new Location(this.getArena().getWorld(), rX, y, minZ);
				break;
		}
		this.dragon = null;
		this.startPoint = spawnPoint;
		this.endPoint = endPoint;
		this.structure = new ArrayList<>();
		this.chests = new ArrayList<>();
		this.dropTask = null;
		this.spawnTask = null;
		this.spawnTimer = 0;

		Location tempDropLoc = this.getDropPoint().clone();
		List<Block> tempStructure = new ArrayList<>();
		List<Block> tempChests = new ArrayList<>();
		boolean centerClear = true;
		while (centerClear) {
			Block center = tempDropLoc.getBlock();
			Block north = center.getRelative(BlockFace.NORTH);
			Block north_west = north.getRelative(BlockFace.WEST);
			Block west = north_west.getRelative(BlockFace.SOUTH);
			Block south_west = west.getRelative(BlockFace.SOUTH);
			Block south = south_west.getRelative(BlockFace.EAST);
			Block south_east = south.getRelative(BlockFace.EAST);
			Block east = south_east.getRelative(BlockFace.NORTH);
			Block north_east = east.getRelative(BlockFace.NORTH);
			if (center.getType() != Material.AIR || north.getType() != Material.AIR || north_west.getType() != Material.AIR || west.getType() != Material.AIR || south_west.getType() != Material.AIR || south.getType() != Material.AIR || south_east.getType() != Material.AIR || east.getType() != Material.AIR || north_east.getType() != Material.AIR) {
				centerClear = false;
				tempDropLoc.setY(tempDropLoc.getBlockY() + 1);

				tempStructure.add(center);
				tempStructure.add(north);
				tempStructure.add(north_west);
				tempStructure.add(west);
				tempStructure.add(south_west);
				tempStructure.add(south);
				tempStructure.add(south_east);
				tempStructure.add(east);
				tempStructure.add(north);

				tempDropLoc.setY(tempDropLoc.getBlockY() + 1);

				this.dropLocation = center.getLocation();
				this.structure = tempStructure;

				tempChests.add(north_west);
				tempChests.add(south_west);
				tempChests.add(south_east);
				tempChests.add(north_east);

				this.chests = tempChests;
			} else {
				tempDropLoc.setY(tempDropLoc.getBlockY() - 1);
			}
		}
	}

	public Arena getArena() {
		return this.arena;
	}

	public EnderDragon getDragon() {
		return this.dragon;
	}

	public Location getDropPoint() {
		return this.dropPoint;
	}

	public Location getDropLocation() {
		return this.dropLocation;
	}

	public Location getStartPoint() {
		return this.startPoint;
	}

	public Location getEndPoint() {
		return this.endPoint;
	}

	public List<Block> getStructureBlocks() {
		return this.structure;
	}

	public List<Block> getChestBlocks() {
		return this.chests;
	}

	public BukkitRunnable getDropTask() {
		return this.dropTask;
	}

	public BukkitRunnable getSpawnTask() {
		return this.spawnTask;
	}

	public int getSpawnTimer() {
		return this.spawnTimer;
	}

	public void spawnDragon() {

		Vector start = this.getStartPoint().toVector();
		Vector end  = this.getEndPoint().toVector();
		Vector vector = start.subtract(end);

		this.getStartPoint().setDirection(vector);
		this.dragon = (EnderDragon) this.getArena().getWorld().spawnEntity(this.getStartPoint(), EntityType.ENDER_DRAGON);
		this.getDragon().setVelocity(vector.normalize());
		this.getDragon().setCustomName("AIRDROP");

		this.dropTask = new BukkitRunnable() {
			@Override
			public void run() {
				if (getDragon().getLocation().equals(getDropPoint())) {
					drop();
				}
				if (getDragon().getLocation().equals(getEndPoint())) {
					getDragon().remove();
					cancel();
				}
			}
		};
		this.getDropTask().runTaskTimer(SurvivalGames.getPlugin(), 0, 1);
	}

	private void drop() {
		//noinspection deprecation
		this.getArena().getWorld().spawnFallingBlock(this.getDropPoint(), Material.DRAGON_EGG, (byte) 0);

		for (Block block : this.getStructureBlocks()) {
			block.setType(Material.GLASS);
		}
		this.spawnTask = new BukkitRunnable() {
			@Override
			public void run() {
				if (getSpawnTimer() == 20) {
					for (Block block : getChestBlocks()) {
						block.setType(Material.CHEST);
					}
					getDropLocation().getBlock().setType(Material.ENCHANTMENT_TABLE);
					spawnTimer = 0;
					cancel();
				} else {
					getArena().getWorld().playSound(getDropLocation(), Sound.DIG_STONE, 10, 0);
					getArena().getWorld().playEffect(getDropLocation(), Effect.ENDER_SIGNAL, 0);
					getArena().getWorld().playEffect(getDropLocation(), Effect.MOBSPAWNER_FLAMES, 0);
					getArena().getWorld().playEffect(getDropLocation(), Effect.SMOKE, 0);
					spawnTimer = getSpawnTimer() + 1;
				}
			}
		};
		this.getSpawnTask().runTaskTimer(SurvivalGames.getPlugin(), 0 , 20);

		Bukkit.getScheduler().runTaskLater(SurvivalGames.getPlugin(), () -> {
			for (Block block : getChestBlocks()) {
				block.setType(Material.CHEST);
			}
			getDropLocation().getBlock().setType(Material.ENCHANTMENT_TABLE);
		}, 200);
	}

	public void cleanUp() {
		for (Block block : this.getChestBlocks()) {
			block.setType(Material.AIR);
		}
		for (Block block : this.getStructureBlocks()) {
			block.setType(Material.AIR);
		}
		if (this.getDragon() != null)
			this.getDragon().remove();
		if (this.getDropTask() != null)
			this.getDropTask().cancel();
		if (this.getSpawnTask() != null)
			this.getSpawnTask().cancel();
	}
}
