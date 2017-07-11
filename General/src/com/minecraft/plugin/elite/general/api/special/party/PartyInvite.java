package com.minecraft.plugin.elite.general.api.special.party;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;

public class PartyInvite {

    private GeneralPlayer inviter;
    private GeneralPlayer invited;
    private Party party;

    public PartyInvite(GeneralPlayer inviter, GeneralPlayer invited, Party party) {
        this.inviter = inviter;
        this.invited = invited;
        this.party = party;
        PartyManager.addInvite(this);
    }

    public void delete() {
        PartyManager.removeInvite(this);
    }

    public GeneralPlayer getInviter() {
        return this.inviter;
    }

    public GeneralPlayer getInvited() {
        return this.invited;
    }

    public Party getParty() {
        return this.party;
    }
}