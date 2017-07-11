package com.minecraft.plugin.elite.general.api.special.party;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;

import java.util.HashSet;

public class PartyManager {

    private static HashSet<Party> parties = new HashSet<>();
    private static HashSet<PartyInvite> invites = new HashSet<>();

    public static Party[] getParties() {
        return parties.toArray(new Party[parties.size()]);
    }

    public static Party get(GeneralPlayer p) {
        for (Party party : getParties())
            if (party.getUniqueId().equals(p.getUniqueId())) {
                return party;
            }
        return null;
    }

    public static void add(Party party) {
        parties.add(party);
    }

    public static void remove(Party party) {
        parties.remove(party);
    }

    public static PartyInvite[] getInvites() {
        return invites.toArray(new PartyInvite[invites.size()]);
    }

    public static PartyInvite[] getInvites(GeneralPlayer invited) {
        HashSet<PartyInvite> inv = new HashSet<>();
        for (PartyInvite invite : getInvites()) {
            if (invite.getInvited().equals(invited))
                inv.add(invite);
        }
        return inv.toArray(new PartyInvite[inv.size()]);
    }

    public static void addInvite(PartyInvite invite) {
        invites.add(invite);
    }

    public static void removeInvite(PartyInvite invite) {
        invites.remove(invite);
    }
}
