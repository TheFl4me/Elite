package com.minecraft.plugin.elite.general.api.enums;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.api.events.stats.RankChangeEvent;
import com.minecraft.plugin.elite.general.api.interfaces.PermissionNode;
import com.minecraft.plugin.elite.general.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public enum Rank {

    NORMAL(Collections.emptyList()),
    PREMIUM(Collections.singletonList(NORMAL)),
    MEDIA(Collections.singletonList(PREMIUM)),
    BUILDER(Collections.singletonList(MEDIA)),
    SUPPORTER(Collections.singletonList(MEDIA)),
    MOD(Collections.singletonList(SUPPORTER)),
    MODPLUS(Arrays.asList(MOD, BUILDER)),
    ADMIN(Collections.singletonList(MODPLUS));

    private final String name;
    private final Prefix prefix;
    private final List<Rank> inheritance;
    private final List<String> perms;

    private static Map<UUID, Rank> ranks = new HashMap<>();

    public static void set(OfflinePlayer offlinePlayer, Rank rank) {
        RankChangeEvent event = new RankChangeEvent(offlinePlayer, rank, Rank.get(offlinePlayer.getUniqueId()));
        Database db = General.getDB();
        db.update("players", "rank", rank.getName(), "uuid", offlinePlayer.getUniqueId());
        ranks.put(offlinePlayer.getUniqueId(), rank);
        Bukkit.getPluginManager().callEvent(event);
    }

    public static Rank get(UUID uuid) {
        return ranks.get(uuid);
    }

    public static Rank get(OfflinePlayer p) {
        return get(p.getUniqueId());
    }

    public static void reload() {
        ranks.clear();
        Database db = General.getDB();
        try {
            ResultSet res = db.select(General.DB_PLAYERS);
            while (res.next()) {
                Rank rank = valueOf(res.getString("rank").toUpperCase());
                UUID uuid = UUID.fromString(res.getString("uuid"));
                ranks.put(uuid, rank);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Rank valueOf(int i) {
        for(Rank rank : values())
            if(rank.ordinal() == i)
                return rank;
        return null;
    }

    Rank(List<Rank> inherits) {
        this.name = this.toString();
        this.prefix = Prefix.valueOf(this.toString().toUpperCase());
        this.inheritance = inherits;
        File permissions = new File(General.DIRECTORY_PERMISSIONS);
        YamlConfiguration perms = YamlConfiguration.loadConfiguration(permissions);
        List<String> finalPerms = new ArrayList<>();
        perms.getStringList("groups." + this.getName().toLowerCase() + ".permissions").forEach((perm) -> {
            if (!finalPerms.contains(perm.toLowerCase()))
                finalPerms.add(perm.toLowerCase());
        });

        if (!this.getInheritance().isEmpty())
            this.getInheritance().forEach((inheritanceRanks) ->
                    inheritanceRanks.getPermissions().forEach((inheritPerms) -> {
                        if (!finalPerms.contains(inheritPerms.toLowerCase())) {
                            finalPerms.add(inheritPerms.toLowerCase());
                        }
                    }));
        this.perms = finalPerms;
    }

    public String getName() {
        return this.name;
    }

    public Prefix getPrefix() {
        return this.prefix;
    }

    public String getDisplayName() {
        return this.getPrefix().getColor() + this.getName();
    }

    public Collection<Rank> getInheritance() {
        return this.inheritance;
    }

    public Collection<String> getPermissions() {
        return this.perms;
    }

    public boolean hasPermission(PermissionNode node) {
        return this.getPermissions().contains(node.toString());
    }

    public boolean hasPermission(String string) {
        return this.getPermissions().contains(string.toLowerCase());
    }

    public String getTeamLetter() {
        List<String> alpha = Arrays.asList("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z");
        int i = this.ordinal() * (-1) + alpha.size() - 1;
        return alpha.get(i);
    }

    public String getTeamName() {
        return this.getTeamLetter().toLowerCase() + "-" + this.getName().toLowerCase();
    }
}
