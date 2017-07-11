package com.minecraft.plugin.elite.general.api.special;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class Bot {

    private static HashSet<Bot> bots = new HashSet<>();

    private EntityPlayer entity;
    private UUID viewer;

    public static Bot[] getAll() {
        return bots.toArray(new Bot[bots.size()]);
    }

    public static List<Bot> getBots(GeneralPlayer p) {
        List<Bot> tempList = new ArrayList<>();
        for(Bot bot : getAll())
            if(bot.getViewer().getUniqueId().equals(p.getUniqueId()))
                tempList.add(bot);
        return tempList;
    }

    public static Bot get(UUID uuid) {
        for(Bot bot : getAll())
            if(bot.getEntity().getProfile().getId().equals(uuid))
                return bot;
        return null;
    }

    public Bot(GeneralPlayer p, OfflinePlayer offp) {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) p.getPlayer().getWorld()).getHandle();

        this.viewer = p.getUniqueId();
        this.entity = new EntityPlayer(server, world, new GameProfile(offp.getUniqueId(), offp.getName()), new PlayerInteractManager(world));
    }

    public GeneralPlayer getViewer() {
        return GeneralPlayer.get(this.viewer);
    }

    public EntityPlayer getEntity() {
        return this.entity;
    }

    public void spawn(Location loc) {
        this.getEntity().setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());

        PlayerConnection connection = ((CraftPlayer)this.getViewer().getPlayer()).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this.getEntity()));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(this.getEntity()));
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.getEntity()));
        bots.add(this);
    }

    public void destroy() {
        PlayerConnection connection = ((CraftPlayer)this.getViewer().getPlayer()).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.getEntity()));
        connection.sendPacket(new PacketPlayOutEntityDestroy(this.getEntity().getId()));
        bots.remove(this);
    }
}
