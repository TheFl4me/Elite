package com.minecraft.plugin.elite.general.api.special.clan;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.database.Database;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Clan {

    private String name;
    private List<UUID> members;
    private HashMap<UUID, ClanRank> ranks;

    public Clan(String name) {
        this.name = name;
        this.members = new ArrayList<>();
        this.ranks = new HashMap<>();
        ClanManager.add(this);
    }

    public void saveToDB() {
        Database db = General.getDB();
        db.execute("INSERT INTO " + General.DB_CLANS + "(name) VALUES (?);", this.getName());
    }

    public void delete() {
        Database db = General.getDB();
        db.delete(General.DB_CLANS, "name", this.getName());
        ClanManager.remove(this);
    }

    public String getName() {
        return this.name;
    }

    public Iterable<UUID> getMembers() {
        return this.members;
    }

    public void add(UUID uuid) {
        this.members.add(uuid);
        Database db = General.getDB();
        db.update(General.DB_PLAYERS, "clan", this.getName(), "uuid", uuid);
        this.setRank(uuid, ClanRank.NORMAL);
    }

    public void remove(UUID uuid) {
        this.members.remove(uuid);
        Database db = General.getDB();
        db.update(General.DB_PLAYERS, "clan", "", "uuid", uuid);
        db.update(General.DB_PLAYERS, "clanrank", "", "uuid", uuid);
        if (this.ranks.containsKey(uuid))
            this.ranks.remove(uuid);
    }

    public void setRank(UUID uuid, ClanRank rank) {
        if (this.ranks.containsKey(uuid))
            this.ranks.remove(uuid);
        this.ranks.put(uuid, rank);
        Database db = General.getDB();
        db.update(General.DB_PLAYERS, "clanrank", rank.getName(), "uuid", uuid);
    }

    public ClanRank getRank(UUID uuid) {
        return this.ranks.get(uuid);
    }

    public boolean isCreator(UUID uuid) {
        return this.getRank(uuid) == ClanRank.CREATOR;
    }

    public boolean isMod(UUID uuid) {
        return this.getRank(uuid) == ClanRank.MOD || this.isCreator(uuid);
    }

    public boolean isNormal(UUID uuid) {
        return !this.isCreator(uuid) && !this.isMod(uuid);
    }

    public boolean hasInvited(UUID uuid) {
        for (ClanInvite invite : ClanManager.getInvites(uuid))
            if(invite.getClan().getName().equalsIgnoreCase(this.getName()))
                return true;
        return false;
    }

    public void sendMessage(String message, ePlayer p) {
        for (UUID uuid : getMembers()) {
            ePlayer all = ePlayer.get(uuid);
            if (all != null)
                all.getPlayer().sendMessage(ChatColor.GRAY + "[" + ChatColor.DARK_PURPLE + "ClanChat" + ChatColor.GRAY + "] " + ChatColor.RESET + p.getChatName() + " > " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    public enum ClanRank {

        CREATOR("creator"),
        MOD("mod"),
        NORMAL("normal");

        private String rank;

        ClanRank(String rank) {
            this.rank = rank;
        }

        public String getName() {
            return this.rank;
        }
    }
}
