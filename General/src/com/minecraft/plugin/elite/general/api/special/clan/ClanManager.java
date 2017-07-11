package com.minecraft.plugin.elite.general.api.special.clan;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.database.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.UUID;

public class ClanManager {

    private static HashSet<Clan> clans = new HashSet<>();
    private static HashSet<ClanInvite> invites = new HashSet<>();

    public static Clan[] getAllClans() {
        return clans.toArray(new Clan[clans.size()]);
    }

    public static Clan get(String name) {
        for (Clan clan : getAllClans())
            if (clan.getName().equalsIgnoreCase(name))
                return clan;
        return null;
    }

    public static Clan get(UUID uuid) {
        for (Clan clan : getAllClans())
            for(UUID members : clan.getMembers())
                if(uuid.equals(members))
                    return clan;
        return null;
    }

    public static void add(Clan clan) {
        clans.add(clan);
    }

    public static void remove(Clan clan) {
        clans.remove(clan);
    }

    public static ClanInvite[] getInvites() {
        return invites.toArray(new ClanInvite[invites.size()]);
    }

    public static ClanInvite[] getInvites(UUID invited) {
        HashSet<ClanInvite> inv = new HashSet<>();
        for (ClanInvite invite : getInvites()) {
            if (invite.getInvited().getUniqueId().equals(invited))
                inv.add(invite);
        }
        return inv.toArray(new ClanInvite[inv.size()]);
    }

    public static void addInvite(ClanInvite invite) {
        invites.add(invite);
    }

    public static void removeInvite(ClanInvite invite) {
        invites.remove(invite);
    }

    public static ClanInvite getInvite(UUID uuid, Clan clan) {
        for (ClanInvite invite : getInvites(uuid))
            if (invite.getClan().equals(clan))
                return invite;
        return null;
    }

    public static void load() {

        clans.clear();

        Database db = General.getDB();
        try {
            ResultSet res = db.select(General.DB_CLANS);
            while (res.next()) {
                String name = res.getString("name");
                Clan clan = new Clan(name);
                ResultSet pRes = db.select(General.DB_PLAYERS);
                while (pRes.next()) {
                    UUID uuid = UUID.fromString(pRes.getString("uuid"));
                    String clanName = pRes.getString("clan");
                    String clanRank = pRes.getString("clanrank");
                    if (clanName.equalsIgnoreCase(name)) {
                        clan.add(uuid);
                        if(clanRank.equalsIgnoreCase(Clan.ClanRank.MOD.getName()))
                            clan.setRank(uuid, Clan.ClanRank.MOD);
                        else if(clanRank.equalsIgnoreCase(Clan.ClanRank.CREATOR.getName()))
                            clan.setRank(uuid, Clan.ClanRank.CREATOR);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
