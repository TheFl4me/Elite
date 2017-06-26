package com.minecraft.plugin.elite.kitpvp.manager.duel;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.kitpvp.KitPvP;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DuelManager {

    private static Set<Duel> duels = new HashSet<>();
    private static Set<DuelRequest> requests = new HashSet<>();
    private static List<UUID> queue = new ArrayList<>();

    public static Duel[] getAll() {
        return duels.toArray(new Duel[duels.size()]);
    }
    public static DuelRequest[] getAllRequests() {
        return requests.toArray(new DuelRequest[requests.size()]);
    }

    public static List<UUID> getQueue() {
        List<UUID> tempList = new ArrayList<>();
        tempList.addAll(queue);
        return tempList;
    }

    public static Duel get(ePlayer p) {
        for(Duel duel : getAll())
            if(duel.getPlayers().contains(p.getUniqueId()))
                return duel;
        return null;
    }

    public static DuelRequest getRequest(ePlayer inviter, ePlayer invited) {
        for(DuelRequest request : getAllRequests()) {
            if(request.getInviter().getUniqueId().equals(inviter.getUniqueId()) && request.getInvited().getUniqueId().equals(invited.getUniqueId())) {
                return request;
            }
        }
        return null;
    }

    public static DuelRequest getRequest(ePlayer inviter) {
        for(DuelRequest request : getAllRequests()) {
            if(request.getInviter().getUniqueId().equals(inviter.getUniqueId()))
                return request;
        }
        return null;
    }

    public static void add(Duel duel) {
        duels.add(duel);
    }

    public static void remove(Duel duel) {
        duels.remove(duel);
    }

    public static void addRequest(DuelRequest request) {
        requests.add(request);
    }

    public static void removeRequest(DuelRequest request) {
        requests.remove(request);
    }

    public static void addToQueue(UUID uuid) {
        queue.add(uuid);
    }

    public static void removeFromQueue(UUID uuid) {
        queue.remove(uuid);
    }

    public static Location getDuelSpawn() {
        Database db = General.getDB();
        try {
            ResultSet res = db.select(KitPvP.DB_DUEL, "location", "duelspawn");
            if(res.next())
                return new Location(Bukkit.getWorld("world"), res.getDouble("loc-x"), res.getDouble("loc-y"), res.getDouble("loc-z"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
