package com.minecraft.plugin.elite.general.api.special;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.api.ePlayer;
import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class BossBar {

    private ePlayer viewer;
    private EntityEnderDragon dragon;
    private BukkitRunnable showTask;

    private static Set<BossBar> bars = new HashSet<>();

    public static BossBar[] getAll() {
        return bars.toArray(new BossBar[bars.size()]);
    }

    public static BossBar get(ePlayer p) {
        for(BossBar bar : getAll()) {
            if(bar.getViewer().getUniqueId().equals(p.getUniqueId())) {
                return bar;
            }
        }
        return null;
    }

    public BossBar(ePlayer p) {
        WorldServer world = ((CraftWorld) p.getPlayer().getWorld()).getHandle();
        this.dragon = new EntityEnderDragon(world);
        this.viewer = p;
        this.showTask = null;
        bars.add(this);
    }

    public ePlayer getViewer() {
        return this.viewer;
    }

    public EntityEnderDragon getDragon() {
        return this.dragon;
    }

    public BukkitRunnable getShowTask() {
        return this.showTask;
    }

    public void setShowTask(BukkitRunnable runnable) {
        this.showTask = runnable;
    }

    public void show(String string, int ticks) {
        this.show(string);
        this.setShowTask(new BukkitRunnable() {
            @Override
            public void run() {
                destroy();
            }
        });
        this.getShowTask().runTaskLater(General.getPlugin(), ticks);
    }

    public void show(String string) {
        Location loc = this.getViewer().getPlayer().getLocation();

        this.getDragon().setLocation(loc.getX(), loc.getY() - 10, loc.getZ(), 0, 0);
        this.getDragon().b(true);//remove sound

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(this.getDragon());

        this.getDragon().setCustomName(string);

        ((CraftPlayer) this.getViewer().getPlayer()).getHandle().playerConnection.sendPacket(packet);
    }

    public void destroy() {
        Packet packet = new PacketPlayOutEntityDestroy(this.getDragon().getId());
        ((CraftPlayer) this.getViewer().getPlayer()).getHandle().playerConnection.sendPacket(packet);
        if(this.getShowTask() != null) {
            this.getShowTask().cancel();
            this.setShowTask(null);
        }
        bars.remove(this);
    }

    public void changeTo(String text) {
        final ePlayer p = this.getViewer();
        this.destroy();
        BossBar bar = new BossBar(p);
        bar.show(text);
    }

    public void changeTo(String text, int ticks) {
        final ePlayer p = this.getViewer();
        this.destroy();
        BossBar bar = new BossBar(p);
        bar.show(text, ticks);
    }
}
