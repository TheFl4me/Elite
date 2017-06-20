package com.minecraft.plugin.elite.general.api.special.party;

import com.minecraft.plugin.elite.general.api.ePlayer;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class Party {

    private UUID id;
    private List<UUID> members;
    private UUID creator;

    public Party(ePlayer creator) {
        this.members = new ArrayList<>();
        this.members.add(creator.getUniqueId());
        this.id = UUID.randomUUID();
        this.creator = creator.getUniqueId();
        PartyManager.add(this);
    }

    public void delete() {
        PartyManager.remove(this);
    }

    public Iterable<ePlayer> getMembers() {
        List<ePlayer> list = new ArrayList<>();
        this.members.forEach((uuid) -> list.add(ePlayer.get(uuid)));
        return list;
    }

    public PartyInvite[] getInvites() {
        HashSet<PartyInvite> inv = new HashSet<>();
        for (PartyInvite invite : PartyManager.getInvites()) {
            if (invite.getParty().getUniqueId().equals(this.getUniqueId()))
                inv.add(invite);
        }
        return inv.toArray(new PartyInvite[inv.size()]);
    }

    public PartyInvite getInvite(ePlayer invited) {
        for (PartyInvite invite : this.getInvites()) {
            if (invite.getInvited().equals(invited))
                return invite;
        }
        return null;
    }

    public UUID getUniqueId() {
        return this.id;
    }

    public ePlayer getCreator() {
        return ePlayer.get(this.creator);
    }

    public void add(ePlayer p) {
        this.members.add(p.getUniqueId());
    }

    public void remove(ePlayer p) {
        this.members.remove(p.getUniqueId());
    }

    public boolean isCreator(ePlayer p) {
        return this.getCreator().getUniqueId().equals(p.getUniqueId());
    }

    public boolean hasInvited(ePlayer p) {
        for (PartyInvite invite : PartyManager.getInvites(p))
            if (invite.getParty().getUniqueId().equals(this.getUniqueId()))
                return true;
        return false;
    }

    public void sendMessage(String message, ePlayer p) {
        for (ePlayer all : this.getMembers())
            all.getPlayer().sendMessage(ChatColor.GRAY + "[" + ChatColor.YELLOW + "PartyChat" + ChatColor.GRAY + "] " + ChatColor.RESET + p.getChatName() + " > " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', message));
    }
}
