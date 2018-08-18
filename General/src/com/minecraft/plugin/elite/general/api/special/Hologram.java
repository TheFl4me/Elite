package com.minecraft.plugin.elite.general.api.special;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Hologram {

    private static HashSet<Hologram> holograms = new HashSet<>();
    private static final double DISTANCE = 0.23;

    private Collection<String> lines = new ArrayList<>();
    private List<Integer> ids = new ArrayList<>();
    private Location location;
    private GeneralPlayer viewer;

    public static Hologram[] getAll() {
        return holograms.toArray(new Hologram[holograms.size()]);
    }

    public static Hologram get(GeneralPlayer p, Location loc) {
        for(Hologram holo : getAll())
            if(holo.getLocation().equals(loc) && holo.getViewer().getUniqueId().equals(p.getUniqueId()))
                return holo;
        return null;
    }

    public Hologram(GeneralPlayer p, String text) {
        this.viewer = p;
        String[] lines = text.split("\n");
        this.lines.addAll(Arrays.asList(lines));
    }

    public Location getLocation() {
        return this.location;
    }

    public GeneralPlayer getViewer() {
        return this.viewer;
    }

    public void destroy() {
        int[] ints = new int[this.ids.size()];
        for (int j = 0; j < ints.length; j++)
            ints[j] = ids.get(j);
        Packet packet = new PacketPlayOutEntityDestroy(ints);
        ((CraftPlayer) this.getViewer().getPlayer()).getHandle().playerConnection.sendPacket(packet);
        holograms.remove(this);
    }

    public void change(String text) {
        final GeneralPlayer p = this.getViewer();
        final Location loc = this.getLocation();
        Bukkit.getScheduler().runTaskLater(General.getPlugin(), () -> {
            for(Player players : Bukkit.getOnlinePlayers()) {
                if(players.getUniqueId().equals(p.getUniqueId())) {
                    Hologram holo = new Hologram(p, text);
                    holo.show(loc);
                    break;
                }
            }
        }, 20);
        this.destroy();
    }

    public void show(Location loc) {
        Location first = loc.clone().add(0, (this.lines.size() / 2F) * DISTANCE, 0);
        for(String line : this.lines) {
            this.ids.add(this.showLine(first.clone(), line));
            first.subtract(0, DISTANCE, 0);
        }
        this.location = loc;
        holograms.add(this);
    }

    public void show(Location loc, long ticks) {
        this.show(loc);
        new BukkitRunnable() {
            @Override
            public void run() {
                destroy();
            }
        }.runTaskLater(General.getPlugin(), ticks);
    }

    private int showLine(Location loc, String text) {
        World world = ((CraftWorld) loc.getWorld()).getHandle();

        EntityArmorStand as = new EntityArmorStand(world);
        as.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        as.setCustomName(ChatColor.translateAlternateColorCodes('&', text));
        as.setCustomNameVisible(true);
        as.setInvisible(true);
        Packet packet = new PacketPlayOutSpawnEntityLiving(as);
        EntityPlayer nmsPlayer = ((CraftPlayer) this.getViewer().getPlayer()).getHandle();
        PlayerConnection connection = nmsPlayer.playerConnection;
        connection.sendPacket(packet);
        return as.getId();
    }
}
