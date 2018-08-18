package com.minecraft.plugin.elite.raid.commands;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.raid.RaidLanguage;
import com.minecraft.plugin.elite.raid.RaidPermission;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class RandomSpawnCommand extends GeneralCommand {

	public RandomSpawnCommand() {
		super("randomspawn", RaidPermission.SPAWN, false);
	}
	
	private HashMap<UUID, Integer> spawns = new HashMap<>();

	public boolean execute(CommandSender cs, Command cmd, String[] args) {

		GeneralPlayer p = GeneralPlayer.get((Player) cs);
		Server server = Server.get();
		if(spawns.containsKey(p.getUniqueId())) {
			if(spawns.get(p.getUniqueId()) >= getSpawnLimit(p)) {
				p.getPlayer().sendMessage(p.getLanguage().get(RaidLanguage.SPAWN_EXPIRED).replaceAll("%domain", server.getDomain()));
				return true;
			}
		}
		int count;
		if(spawns.containsKey(p.getUniqueId()))
			count = spawns.get(p.getUniqueId()) + 1;
		else
			count = 1;
		spawns.put(p.getUniqueId(), count);
		Random r1 = new Random();
		Random r2 = new Random();
		World world = Bukkit.getWorld("world");
		int size = (int) world.getWorldBorder().getSize();
		int x = (int) (world.getSpawnLocation().getX() + ((r1.nextInt(size) + 1) - (size / 2)));
		int z = (int) (world.getSpawnLocation().getZ() + ((r2.nextInt(size) + 1) - (size / 2)));
		Location loc = new Location(world, x, world.getHighestBlockAt(x, z).getY(), z);
		p.getPlayer().teleport(loc);
		p.getPlayer().sendMessage(p.getLanguage().get(RaidLanguage.SPAWN_CONFIRMED)
				.replaceAll("%remaining", Integer.toString(getSpawnLimit(p) - spawns.get(p.getUniqueId()))));
		return true;
	}
	
	private int getSpawnLimit(GeneralPlayer p) {
		if(p.isPremium())
			return 10;
		else
			return 5;
	}
}