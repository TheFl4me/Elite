package com.minecraft.plugin.elite.general.api.special.party;

import com.minecraft.plugin.elite.general.api.ePlayer;

import java.util.UUID;

public class PartyInvite {

    private UUID inviter;
    private UUID invited;
    private Party party;

    public PartyInvite(ePlayer inviter, ePlayer invited, Party party) {
        this.inviter = inviter.getUniqueId();
        this.invited = invited.getUniqueId();
        this.party = party;
        PartyManager.addInvite(this);
    }

    public void delete() {
        PartyManager.removeInvite(this);
    }

    public ePlayer getInviter() {
        return ePlayer.get(this.inviter);
    }

    public ePlayer getInvited() {
        return ePlayer.get(this.invited);
    }

    public Party getParty() {
        return this.party;
    }
}