package com.minecraft.plugin.elite.general.commands.party;

import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.api.special.party.Party;
import com.minecraft.plugin.elite.general.api.special.party.PartyInvite;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class PartyCommand extends GeneralCommand implements TabCompleter {

    public PartyCommand() {
        super("party", GeneralPermission.PARTY, false);
    }

    public List<String> onTabComplete(CommandSender cs, Command cmd, String lbl, String[] args) {
        if (args.length == 1)
            return Arrays.asList("create", "delete", "invite", "uninvite", "accept", "deny", "kick", "leave", "help");
        return null;
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        if (args.length > 1) {
            GeneralPlayer z = GeneralPlayer.get(args[1]);
            if (z != null) {
                if (args[0].equalsIgnoreCase("invite")) {
                    Party other = z.getParty();
                    if (other == null) {
                        Party party = p.getParty();
                        if (party != null) {
                            if (party.isCreator(p)) {
                                if (!party.hasInvited(z)) {
                                    p.getPlayer().sendMessage(p.getLanguage().get(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_INVITE)
                                            .replaceAll("%p", z.getName()));
                                    z.getPlayer().sendMessage(p.getLanguage().get(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_INVITE_RECEIVED)
                                            .replaceAll("%p", p.getName()));
                                    new PartyInvite(p, z, party);
                                    return true;
                                } else {
                                    p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_INVITE_ALREADY);
                                    return true;
                                }
                            } else {
                                p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_NOT_CREATOR);
                                return true;
                            }
                        } else {
                            p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_NONE_YOU);
                            return true;
                        }

                    } else {
                        p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_ALREADY_OTHER);
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("uninvite")) {
                    Party party = p.getParty();
                    if (party != null) {
                        if (party.isCreator(p)) {
                            PartyInvite invite = party.getInvite(z);
                            if (invite != null) {
                                invite.delete();
                                p.getPlayer().sendMessage(p.getLanguage().get(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_INVITE_REVOKED)
                                        .replaceAll("%p", invite.getInvited().getName()));
                                return true;
                            } else {
                                p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_INVITE_NULL);
                                return true;
                            }
                        } else {
                            p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_NOT_CREATOR);
                            return true;
                        }
                    } else {
                        p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_NONE_YOU);
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("accept")) {
                    if (p.getParty() == null) {
                        Party party = z.getParty();
                        if (party != null) {
                            PartyInvite invite = party.getInvite(p);
                            if (invite != null) {
                                party.add(p);
                                p.getPlayer().sendMessage(p.getLanguage().get(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_INVITE_ACCEPT)
                                        .replaceAll("%p", party.getCreator().getName()));
                                invite.delete();
                                return true;
                            } else {
                                p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_INVITE_NOT_YOU);
                                return true;
                            }
                        } else {
                            p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_INVITE_NOT_YOU);
                            return true;
                        }
                    } else {
                        p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_ALREADY);
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("deny")) {
                    Party party = z.getParty();
                    if (party != null) {
                        PartyInvite invite = party.getInvite(p);
                        if (invite != null) {
                            p.getPlayer().sendMessage(p.getLanguage().get(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_INVITE_DENY)
                                    .replaceAll("%p", party.getCreator().getName()));
                            invite.delete();
                            return true;
                        } else {
                            p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_INVITE_NOT_YOU);
                            return true;
                        }
                    } else {
                        p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_INVITE_NOT_YOU);
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("kick")) {
                    Party party = p.getParty();
                    if (party != null) {
                        if (party.isCreator(p)) {
                            Party zParty = z.getParty();
                            if (zParty != null && party.getUniqueId().equals(zParty.getUniqueId())) {
                                party.remove(z);
                                p.getPlayer().sendMessage(p.getLanguage().get(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_KICKED)
                                        .replaceAll("%p", z.getName()));
                                return true;
                            } else {
                                p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_NOT_SAME);
                                return true;
                            }
                        } else {
                            p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_NOT_CREATOR);
                            return true;
                        }
                    } else {
                        p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_NONE_YOU);
                        return true;
                    }
                } else {
                    p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_USAGE);
                    return true;
                }
            } else {
                p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.NO_TARGET);
                return true;
            }
        } else if (args.length > 0) {
            if (args[0].equalsIgnoreCase("leave")) {
                Party party = p.getParty();
                if (party != null) {
                    p.getPlayer().sendMessage(p.getLanguage().get(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_LEAVE)
                            .replaceAll("%p", party.getCreator().getName()));
                    if (party.isCreator(p)) {
                        for (GeneralPlayer members : party.getMembers())
                            members.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_DELETED);
                        party.delete();
                        return true;
                    }
                    party.remove(p);
                    return true;
                } else {
                    p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_NONE_YOU);
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("create")) {
                if (p.getParty() == null) {
                    new Party(p);
                    p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_CREATED);
                    return true;
                } else {
                    p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_ALREADY);
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("delete")) {
                Party party = p.getParty();
                if (party != null) {
                    if (party.isCreator(p)) {
                        for (GeneralPlayer members : party.getMembers())
                            members.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_DELETED);
                        party.delete();
                        return true;
                    } else {
                        p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_NOT_CREATOR);
                        return true;
                    }
                } else {
                    p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_NONE_YOU);
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("help")) {
                p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_HELP);
                return true;
            } else {
                p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_USAGE);
                return true;
            }
        } else {
            p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.PARTY_USAGE);
            return true;
        }
    }
}