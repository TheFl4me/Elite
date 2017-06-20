package com.minecraft.plugin.elite.general.api.special.clan;

import java.util.UUID;

public class ClanInvite {

    private UUID inviter;
    private UUID invited;
    private Clan clan;

    public ClanInvite(UUID inviter, UUID invited, Clan clan) {
        this.inviter = inviter;
        this.invited = invited;
        this.clan = clan;
        ClanManager.addInvite(this);
    }

    public void delete() {
        ClanManager.removeInvite(this);
    }

    public UUID getInviter() {
        return this.inviter;
    }

    public UUID getInvited() {
        return this.invited;
    }

    public Clan getClan() {
        return this.clan;
    }
}