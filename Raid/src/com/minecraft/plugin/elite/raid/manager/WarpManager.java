package com.minecraft.plugin.elite.raid.manager;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.special.clan.Clan;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.raid.Raid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class WarpManager {

    private static HashSet<Warp> warps = new HashSet<>();
    private static Map<UUID, BukkitRunnable> warping = new HashMap<>();

    public static Warp[] getAllWarps() {
        return warps.toArray(new Warp[warps.size()]);
    }

    public static Warp[] getPlayerWarps(ePlayer p) {
        HashSet<Warp> list = new HashSet<>();
        for(Warp warp : getAllWarps()) {
            if(warp.getOwner().equalsIgnoreCase(p.getUniqueId().toString()) && warp.getType() == Warp.WarpType.PLAYER)
                list.add(warp);
        }
        return list.toArray(new Warp[list.size()]);
    }

    public static Warp[] getClanWarps(Clan clan) {
        HashSet<Warp> list = new HashSet<>();
        for(Warp warp : getAllWarps()) {
            if(warp.getOwner().equalsIgnoreCase(clan.getName()) && warp.getType() == Warp.WarpType.CLAN)
                list.add(warp);
        }
        return list.toArray(new Warp[list.size()]);
    }

    public static Warp[] getAvailableWarps(ePlayer p) {
        HashSet<Warp> list = new HashSet<>();
        Collections.addAll(list, getPlayerWarps(p));
        if(p.getClan() != null)
            Collections.addAll(list, getClanWarps(p.getClan()));
        return list.toArray(new Warp[list.size()]);
    }

    public static Warp getPlayerWarp(String warpName, ePlayer p) {
        if(getAllWarps().length > 0) {
            for(Warp warp : getAllWarps()) {
                if(warp.getType() == Warp.WarpType.PLAYER && warp.getOwner().equalsIgnoreCase(p.getUniqueId().toString()))
                    return warp;
            }
        }
        return null;
    }

    public static Warp getClanWarp(String warpName, Clan clan) {
        if(getAllWarps().length > 0) {
            for(Warp warp : getAllWarps()) {
                if(warp.getName().equalsIgnoreCase(warpName) && warp.getType() == Warp.WarpType.CLAN && warp.getOwner().equalsIgnoreCase(clan.getName()))
                    return warp;
            }
        }
        return null;
    }

    public static Warp get(String warpName, ePlayer p) {
        if(getAllWarps().length > 0) {
            for(Warp warp : getAllWarps()) {
                boolean bool = false;
                if(warp.getType() == Warp.WarpType.PLAYER && warp.getOwner().equalsIgnoreCase(p.getUniqueId().toString()))
                    bool = true;
                if(p.getClan() != null && (warp.getType() == Warp.WarpType.CLAN && warp.getOwner().equalsIgnoreCase(p.getClan().getName())))
                    bool = true;
                if(bool && warp.getName().equalsIgnoreCase(warpName))
                    return warp;
            }
        }
        return null;
    }

    public static int getMaxWarps(ePlayer p) {
        if(p.isPremium())
            return 10;
        else
            return 5;
    }

    public static void add(Warp warp)  {
        warps.add(warp);
    }

    public static void remove(Warp warp) {
        warps.remove(warp);
    }

    public static void cancel(ePlayer p) {
        if(warping.containsKey(p.getUniqueId())) {
            warping.get(p.getUniqueId()).cancel();
            warping.remove(p.getUniqueId());
        }
    }

    public static void addTimer(UUID uuid, BukkitRunnable runnable) {
        warping.put(uuid, runnable);
    }

    public static Map<UUID, BukkitRunnable> getTimers() {
        return warping;
    }

    public static void load() {

        warps.clear();

        Database db = Raid.getDB();

        try {
            ResultSet warpRes = db.select(Raid.DB_WARP);
            while(warpRes.next()) {
                String warpName = warpRes.getString("name");
                double x = warpRes.getDouble("x");
                double y = warpRes.getDouble("y");
                double z = warpRes.getDouble("z");
                Location loc = new Location(Bukkit.getWorld("world"), x, y, z);
                String owner = warpRes.getString("owner");
                Warp.WarpType type = Warp.WarpType.valueOf(warpRes.getString("type").toUpperCase());
                new Warp(warpName, loc, owner, type);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
