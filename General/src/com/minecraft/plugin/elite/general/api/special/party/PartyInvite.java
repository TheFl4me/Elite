package com.minecraft.plugin.elite.general.api.special.party;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;

import java.util.UUID;

public class PartyInvite {

    private UUID inviter;
    private UUID invited;
    private Party party;

    public PartyInvite(GeneralPlayer inviter, GeneralPlayer invited, Party party) {
        this.inviter = inviter.getUniqueId();
        this.invited = invited.getUniqueId();
        this.party = party;
        PartyManager.addInvite(this);
    }

    public void delete() {
        PartyManager.removeInvite(this);
    }

    public GeneralPlayer getInviter() {
        return GeneralPlayer.get(this.inviter);
    }

    public GeneralPlayer getInvited() {
        return GeneralPlayer.get(this.invited);
    }

    public Party getParty() {
        return this.party;
    }
}