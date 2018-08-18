package com.minecraft.plugin.elite.general.commands.clan;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.api.enums.Rank;
import com.minecraft.plugin.elite.general.api.special.clan.Clan;
import com.minecraft.plugin.elite.general.api.special.clan.ClanInvite;
import com.minecraft.plugin.elite.general.api.special.clan.ClanManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ClanCommand extends GeneralCommand implements TabCompleter {

    public ClanCommand() {
        super("clan", GeneralPermission.CLAN, false);
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String lbl, String[] args) {
        if (args.length == 1)
            return Arrays.asList("create", "delete", "info", "invite", "uninvite", "accept", "deny", "promote", "demote", "kick", "leave", "help");
        return null;
    }

    @SuppressWarnings("deprecation")
    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("create")) {
                if(p.getClan() == null) {
                    if (args[1].length() <= 4) {
                        if (ClanManager.get(args[1]) == null) {
                            Clan clan = new Clan(args[1]);
                            clan.saveToDB();
                            clan.add(p.getUniqueId());
                            clan.setRank(p.getUniqueId(), Clan.ClanRank.CREATOR);
                            p.sendMessage(GeneralLanguage.CLAN_CREATED);
                            return true;
                        } else {
                            p.sendMessage(GeneralLanguage.CLAN_EXIST);
                            return true;
                        }
                    } else {
                        p.sendMessage(GeneralLanguage.CLAN_NAME_MAX_SIZE);
                        return true;
                    }
                } else {
                    p.sendMessage(GeneralLanguage.CLAN_HAS_CLAN);
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("delete")) {
                Clan clan = ClanManager.get(args[1]);
                if (clan != null) {
                    if (clan.isCreator(p.getUniqueId()) || p.hasPermission(GeneralPermission.CLAN_ADMIN)) {
                        for (UUID uuid : clan.getMembers()) {
                            GeneralPlayer z = GeneralPlayer.get(uuid);
                            if (z != null)
                                p.sendMessage(GeneralLanguage.CLAN_DELETED);
                        }
                        clan.delete();
                        return true;
                    } else {
                        p.sendMessage(GeneralLanguage.CLAN_RANK_ERROR);
                        return true;
                    }
                } else {
                    p.sendMessage(GeneralLanguage.CLAN_NONE);
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("info")) {
                Clan clan = ClanManager.get(args[1]);
                if (clan != null) {
                    StringBuilder info = new StringBuilder();
                    info.append(ChatColor.GOLD + General.SPACER + "\n");
                    info.append(ChatColor.GOLD + clan.getName() + ":" + "\n");
                    for (UUID uuid : clan.getMembers()) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                        info.append(ChatColor.GOLD + "> " + Rank.get(offlinePlayer).getPrefix().getColor() + offlinePlayer.getName() + ChatColor.GRAY + " (Clan " + clan.getRank(uuid) + ") " + "[" + (offlinePlayer.isOnline() ? ChatColor.GREEN + "ONLINE" : ChatColor.RED + "OFFLINE") + ChatColor.GRAY + "]\n");
                    }
                    info.append(ChatColor.GOLD + General.SPACER);
                    p.getPlayer().sendMessage(info.toString());
                    return true;
                } else {
                    p.sendMessage(GeneralLanguage.CLAN_EXIST_NOT);
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("invite")) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                Clan otherClan = ClanManager.get(offlinePlayer.getUniqueId());
                if (otherClan == null) {
                    Clan clan = p.getClan();
                    if (clan != null) {
                        if (clan.isMod(p.getUniqueId())) {
                            if (!clan.hasInvited(offlinePlayer.getUniqueId())) {
                                Rank rank = Rank.get(offlinePlayer);
                                String inviteMsgSent = p.getLanguage().get(GeneralLanguage.CLAN_INVITE_SENT)
                                        .replaceAll("%p", rank.getPrefix().getColor() + offlinePlayer.getName());
                                p.getPlayer().sendMessage(inviteMsgSent);
                                if (offlinePlayer.isOnline()) {
                                    GeneralPlayer z = GeneralPlayer.get(offlinePlayer.getUniqueId());
                                    z.getPlayer().sendMessage(z.getLanguage().get(GeneralLanguage.CLAN_INVITE_RECEIVED).replaceAll("%clan", clan.getName()));
                                    z.sendClickMessage(z.getLanguage().get(GeneralLanguage.CLAN_INVITE_RECEIVED_CLICK_ACCEPT).replaceAll("%clan", clan.getName()), "/clan accept " + clan.getName(), false);
                                    z.sendClickMessage(z.getLanguage().get(GeneralLanguage.CLAN_INVITE_RECEIVED_CLICK_DENY).replaceAll("%clan", clan.getName()), "/clan deny " + clan.getName(), false);
                                }
                                new ClanInvite(p.getUniqueId(), offlinePlayer.getUniqueId(), clan);
                                return true;
                            } else {
                                p.sendMessage(GeneralLanguage.CLAN_INVITE_ALREADY);
                                return true;
                            }
                        } else {
                            p.sendMessage(GeneralLanguage.CLAN_RANK_ERROR);
                            return true;
                        }
                    } else {
                        p.sendMessage(GeneralLanguage.CLAN_NONE);
                        return true;
                    }

                } else {
                    p.sendMessage(GeneralLanguage.CLAN_HAS_CLAN);
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("uninvite")) {
                Clan clan = p.getClan();
                if (clan != null) {
                    if (clan.isMod(p.getUniqueId())) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                        ClanInvite invite = ClanManager.getInvite(offlinePlayer.getUniqueId(), clan);
                        if (invite != null) {
                            invite.delete();
                            Rank rank = Rank.get(offlinePlayer);
                            String uninvite = p.getLanguage().get(GeneralLanguage.CLAN_INVITE_REVOKED)
                                    .replaceAll("%p", rank.getPrefix().getColor() + offlinePlayer.getName());
                            p.getPlayer().sendMessage(uninvite);
                            return true;
                        } else {
                            p.sendMessage(GeneralLanguage.CLAN_INVITE_NOT_OTHER);
                            return true;
                        }
                    } else {
                        p.sendMessage(GeneralLanguage.CLAN_RANK_ERROR);
                        return true;
                    }
                } else {
                    p.sendMessage(GeneralLanguage.CLAN_NONE);
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("accept")) {
                if (p.getClan() == null) {
                    Clan clan = ClanManager.get(args[1]);
                    if (clan != null) {
                        ClanInvite invite = ClanManager.getInvite(p.getUniqueId(), clan);
                        if (invite != null) {
                            clan.add(p.getUniqueId());
                            p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.CLAN_INVITE_ACCEPT)
                                    .replaceAll("%clan", clan.getName()));
                            invite.delete();
                            return true;
                        } else {
                            p.sendMessage(GeneralLanguage.CLAN_INVITE_NOT_YOU);
                            return true;
                        }
                    } else {
                        p.sendMessage(GeneralLanguage.CLAN_EXIST_NOT);
                        return true;
                    }
                } else {
                    p.sendMessage(GeneralLanguage.CLAN_HAS_CLAN);
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("deny")) {
                Clan clan = ClanManager.get(args[1]);
                if (clan != null) {
                    ClanInvite invite = ClanManager.getInvite(p.getUniqueId(), clan);
                    if (invite != null) {
                        p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.CLAN_INVITE_DENY)
                                .replaceAll("%clan", clan.getName()));
                        invite.delete();
                        return true;
                    } else {
                        p.sendMessage(GeneralLanguage.CLAN_INVITE_NOT_YOU);
                        return true;
                    }
                } else {
                    p.sendMessage(GeneralLanguage.CLAN_EXIST_NOT);
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("promote")) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                Clan clan = p.getClan();
                if (clan != null) {
                    if (clan.isCreator(p.getUniqueId())) {
                        Clan zClan = ClanManager.get(offlinePlayer.getUniqueId());
                        if (zClan != null && clan.equals(zClan)) {
                            if (clan.isNormal(offlinePlayer.getUniqueId())) {
                                clan.setRank(offlinePlayer.getUniqueId(), Clan.ClanRank.MOD);
                                Rank rank = Rank.get(offlinePlayer);
                                p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.CLAN_PROMOTED)
                                        .replaceAll("%p", rank.getPrefix().getColor() + offlinePlayer.getName()));
                                if (offlinePlayer.isOnline()) {
                                    GeneralPlayer z = GeneralPlayer.get(offlinePlayer.getUniqueId());
                                    z.getPlayer().sendMessage(z.getLanguage().get(GeneralLanguage.CLAN_PROMOTED)
                                            .replaceAll("%p", rank.getPrefix().getColor() + offlinePlayer.getName()));
                                }
                                return true;
                            } else {
                                p.sendMessage(GeneralLanguage.CLAN_PROMOTED_ALREADY);
                                return true;
                            }
                        } else {
                            p.sendMessage(GeneralLanguage.CLAN_NOT_SAME);
                            return true;
                        }
                    } else {
                        p.sendMessage(GeneralLanguage.CLAN_RANK_ERROR);
                        return true;
                    }
                } else {
                    p.sendMessage(GeneralLanguage.CLAN_NONE);
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("demote")) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                Clan clan = p.getClan();
                if (clan != null) {
                    if (clan.isCreator(p.getUniqueId())) {
                        Clan zClan = ClanManager.get(offlinePlayer.getUniqueId());
                        if (zClan != null && clan.equals(zClan)) {
                            if (clan.isMod(offlinePlayer.getUniqueId())) {
                                clan.setRank(offlinePlayer.getUniqueId(), Clan.ClanRank.NORMAL);
                                Rank rank = Rank.get(offlinePlayer);
                                p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.CLAN_DEMOTED)
                                        .replaceAll("%p", rank.getPrefix().getColor() + offlinePlayer.getName()));
                                if (offlinePlayer.isOnline())
                                    ((Player) offlinePlayer).sendMessage(GeneralPlayer.get(offlinePlayer.getUniqueId()).getLanguage().get(GeneralLanguage.CLAN_DEMOTED)
                                            .replaceAll("%p", rank.getPrefix().getColor() + offlinePlayer.getName()));
                                return true;
                            } else {
                                p.sendMessage(GeneralLanguage.CLAN_DEMOTED_ALREADY);
                                return true;
                            }
                        } else {
                            p.sendMessage(GeneralLanguage.CLAN_NOT_SAME);
                            return true;
                        }
                    } else {
                        p.sendMessage(GeneralLanguage.CLAN_RANK_ERROR);
                        return true;
                    }
                } else {
                    p.sendMessage(GeneralLanguage.CLAN_NONE);
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("kick")) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                Clan clan = p.getClan();
                if (clan != null) {
                    if (clan.isMod(p.getUniqueId())) {
                        Clan zClan = ClanManager.get(offlinePlayer.getUniqueId());
                        if (zClan != null && clan.equals(zClan)) {
                            if (clan.isCreator(p.getUniqueId()) || (clan.isMod(p.getUniqueId()) && clan.isNormal(offlinePlayer.getUniqueId()))) {
                                clan.remove(offlinePlayer.getUniqueId());
                                for(UUID uuid : clan.getMembers()) {
                                    GeneralPlayer all = GeneralPlayer.get(uuid);
                                    if(all != null)
                                        all.getPlayer().sendMessage(all.getLanguage().get(GeneralLanguage.CLAN_KICKED)
                                                .replaceAll("%p", Rank.get(offlinePlayer).getPrefix().getColor() + offlinePlayer.getName()));
                                }
                                return true;
                            } else {
                                p.sendMessage(GeneralLanguage.CLAN_HIGHER_RANK);
                                return true;
                            }
                        } else {
                            p.sendMessage(GeneralLanguage.CLAN_NOT_SAME);
                            return true;
                        }
                    } else {
                        p.sendMessage(GeneralLanguage.CLAN_RANK_ERROR);
                        return true;
                    }
                } else {
                    p.sendMessage(GeneralLanguage.CLAN_NONE);
                    return true;
                }
            } else {
                p.sendMessage(GeneralLanguage.CLAN_USAGE);
                return true;
            }
        } else if (args.length > 0) {
            if (args[0].equalsIgnoreCase("leave")) {
                Clan clan = p.getClan();
                if (clan != null) {
                    if (clan.isCreator(p.getUniqueId())) {
                        for(UUID uuid : clan.getMembers()) {
                            GeneralPlayer all = GeneralPlayer.get(uuid);
                            if(all != null)
                                p.sendMessage(GeneralLanguage.CLAN_DELETED);
                        }
                        clan.delete();
                        return true;
                    }
                    clan.remove(p.getUniqueId());
                    p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.CLAN_LEAVE)
                            .replaceAll("%clan", clan.getName()));
                    return true;
                } else {
                    p.sendMessage(GeneralLanguage.CLAN_NONE);
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("help")) {
                p.sendMessage(GeneralLanguage.CLAN_HELP);
                return true;
            } else {
                p.sendMessage(GeneralLanguage.CLAN_USAGE);
                return true;
            }
        } else {
            p.sendMessage(GeneralLanguage.CLAN_USAGE);
            return true;
        }
    }
}
