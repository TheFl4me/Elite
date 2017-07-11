package com.minecraft.plugin.elite.general.api.special.clan;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class ClanInvite {

    private OfflinePlayer inviter;
    private OfflinePlayer invited;
    private Clan clan;

    public ClanInvite(UUID inviter, UUID invited, Clan clan) {
        this.inviter = Bukkit.getOfflinePlayer(inviter);
        this.invited = Bukkit.getOfflinePlayer(invited);
        this.clan = clan;
        ClanManager.addInvite(this);
    }

    public void delete() {
        ClanManager.removeInvite(this);
    }

    public OfflinePlayer getInviter() {
        return this.inviter;
    }

    public OfflinePlayer getInvited() {
        return this.invited;
    }

    public Clan getClan() {
        return this.clan;
    }
}