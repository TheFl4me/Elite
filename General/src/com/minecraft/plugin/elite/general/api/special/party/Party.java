package com.minecraft.plugin.elite.general.api.special.party;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class Party {

    private UUID id;
    private List<GeneralPlayer> members;
    private GeneralPlayer creator;

    public Party(GeneralPlayer creator) {
        this.members = new ArrayList<>();
        this.members.add(creator);
        this.id = UUID.randomUUID();
        this.creator = creator;
        PartyManager.add(this);
    }

    public void delete() {
        PartyManager.remove(this);
    }

    public Iterable<GeneralPlayer> getMembers() {
        return new ArrayList<>(this.members);
    }

    public PartyInvite[] getInvites() {
        HashSet<PartyInvite> inv = new HashSet<>();
        for (PartyInvite invite : PartyManager.getInvites()) {
            if (invite.getParty().getUniqueId().equals(this.getUniqueId()))
                inv.add(invite);
        }
        return inv.toArray(new PartyInvite[inv.size()]);
    }

    public PartyInvite getInvite(GeneralPlayer invited) {
        for (PartyInvite invite : this.getInvites()) {
            if (invite.getInvited().equals(invited))
                return invite;
        }
        return null;
    }

    public UUID getUniqueId() {
        return this.id;
    }

    public GeneralPlayer getCreator() {
        return this.creator;
    }

    public void add(GeneralPlayer p) {
        this.members.add(p);
    }

    public void remove(GeneralPlayer p) {
        this.members.remove(p);
    }

    public boolean isCreator(GeneralPlayer p) {
        return this.getCreator().getUniqueId().equals(p.getUniqueId());
    }

    public boolean hasInvited(GeneralPlayer p) {
        for (PartyInvite invite : PartyManager.getInvites(p))
            if (invite.getParty().getUniqueId().equals(this.getUniqueId()))
                return true;
        return false;
    }

    public void sendMessage(String message, GeneralPlayer p) {
        for (GeneralPlayer all : this.getMembers())
            all.getPlayer().sendMessage(ChatColor.GRAY + "[" + ChatColor.YELLOW + "PartyChat" + ChatColor.GRAY + "] " + ChatColor.RESET + p.getChatName() + " > " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', message));
    }
}
