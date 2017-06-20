package com.minecraft.plugin.elite.kitpvp.manager.duel;

import com.minecraft.plugin.elite.general.api.ePlayer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DuelManager {

    private static Set<Duel> duels = new HashSet<>();
    private static Collection<DuelRequest> requests = new HashSet<>();

    public static Duel[] getAll() {
        return duels.toArray(new Duel[duels.size()]);
    }

    public static Duel get(ePlayer p) {
        for(Duel duel : getAll())
            if(duel.getPlayers().contains(p.getUniqueId()))
                return duel;
        return null;
    }

    public static DuelRequest getRequest(ePlayer inviter, ePlayer invited) {
        for(DuelRequest request : requests) {
            if(request.getInviter().getUniqueId().equals(inviter.getUniqueId()) && request.getInvited().getUniqueId().equals(invited.getUniqueId())) {
                return request;
            }
        }
        return null;
    }

    public static DuelRequest getRequest(ePlayer inviter) {
        for(DuelRequest request : requests) {
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
}
