package com.minecraft.plugin.elite.general.listeners;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.api.enums.Prefix;
import com.minecraft.plugin.elite.general.api.events.LanguageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class LanguageEventListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLangChange(LanguageEvent e) {
        Language lang = e.getLanguage();
        switch(lang) {
            case ENGLISH:
                lang.addLangNode(GeneralLanguage.RELOAD, "&cThe server has been reloaded/restarted.\n&cYou have been kicked to avoid some bugs.");
                lang.addLangNode(GeneralLanguage.SYNTAX, "&cSyntax error! Usage: ");
                lang.addLangNode(GeneralLanguage.NOPERM, "Unknown command.");
                lang.addLangNode(GeneralLanguage.ONLY_PLAYER, "Only players are allowed to to this command.");
                lang.addLangNode(GeneralLanguage.NO_TARGET, "&4Player not found.");
                lang.addLangNode(GeneralLanguage.NEVER_JOINED, "&cPlayer never joined this server.");
                lang.addLangNode(GeneralLanguage.LEFT, "&e%p left the game.");
                lang.addLangNode(GeneralLanguage.JOINED, "&e%p joined the game.");
                lang.addLangNode(GeneralLanguage.ARG_INVALID, "&c\"%arg\" is not a valid argument.\n&cPress <TAB> for a list of arguments.");
                lang.addLangNode(GeneralLanguage.DB_CHECK, "&aChecking database...");
                lang.addLangNode(GeneralLanguage.DB_CHECK_FAIL, "&cError checking the database!\n&cCheck console for more information.");

                lang.addLangNode(GeneralLanguage.UNIT_YEAR, " year ");
                lang.addLangNode(GeneralLanguage.UNIT_YEAR_PLURAL, " years ");
                lang.addLangNode(GeneralLanguage.UNIT_MONTH, " month ");
                lang.addLangNode(GeneralLanguage.UNIT_MONTH_PLURAL, " months ");
                lang.addLangNode(GeneralLanguage.UNIT_WEEK, " week ");
                lang.addLangNode(GeneralLanguage.UNIT_WEEK_PLURAL, " weeks ");
                lang.addLangNode(GeneralLanguage.UNIT_DAY, " day ");
                lang.addLangNode(GeneralLanguage.UNIT_DAY_PLURAL, " days ");
                lang.addLangNode(GeneralLanguage.UNIT_HOUR, " hour ");
                lang.addLangNode(GeneralLanguage.UNIT_HOUR_PLURAL, " hours ");
                lang.addLangNode(GeneralLanguage.UNIT_MINUTE, " minute ");
                lang.addLangNode(GeneralLanguage.UNIT_MINUTE_PLURAL, " minutes ");
                lang.addLangNode(GeneralLanguage.UNIT_SECOND, " second ");
                lang.addLangNode(GeneralLanguage.UNIT_SECOND_PLURAL, " seconds ");

                lang.addLangNode(GeneralLanguage.GUI_BACK, "&aBack");

                lang.addLangNode(GeneralLanguage.HELP, "&b" + General.SPACER +
                        "\n&r/tell <player> <message> &b- Send private messages to other players." +
                        "\n&r/report <player> &b- Report a player for breaking the rules." +
                        "\n&r/chatlog <player> &b- See the last 15 sent messages of a player." +
                        "\n&r/afk &b- Toggle afk status." +
                        "\n&r/clan help &b- Get a list of all clan commands." +
                        "\n&r/party help &b- Get a list of all party commands." +
                        "\n&b" + General.SPACER);

                lang.addLangNode(GeneralLanguage.HEADER, "&6%domain &7- Tell your friends");
                lang.addLangNode(GeneralLanguage.FOOTER, "%ranks");

                lang.addLangNode(GeneralLanguage.STAFF_UPDATED, "&aStaff list updated.");
                lang.addLangNode(GeneralLanguage.STAFF_NULL, "&cThe slot does not exist.");
                lang.addLangNode(GeneralLanguage.STAFF_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/setstaff <slot> <player> <role>");
                lang.addLangNode(GeneralLanguage.STAFF_CLEAR_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/clearstaff <slot>");

                lang.addLangNode(GeneralLanguage.LANGUAGE, "&7Language is now set to &rENGLISH.");
                lang.addLangNode(GeneralLanguage.LANGUAGE_NULL, "&cThis Language does not exist on our network!");
                lang.addLangNode(GeneralLanguage.LANGUAGE_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/language <language>");

                lang.addLangNode(GeneralLanguage.LEVEL_UP, "&6Level Up!");

                lang.addLangNode(GeneralLanguage.DAMAGE_PERCENT, "&aYou were responsible for &6%percent%\n&aof the damage dealt to %player&a. ");

                lang.addLangNode(GeneralLanguage.RANK_SET, "&7%z is now a %rank&7.");
                lang.addLangNode(GeneralLanguage.RANK_SET_YOU, "&6You are now a %rank&6.");
                lang.addLangNode(GeneralLanguage.RANK_INVALID, "&cThat rank does not exist.");
                lang.addLangNode(GeneralLanguage.RANK_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/setrank <player> <rank");

                lang.addLangNode(GeneralLanguage.WELCOME, "\n\n\n&bWelcome to %server\n&bDo &r/help &bfor a list of commands.");
                lang.addLangNode(GeneralLanguage.NOT_ADMIN_MODE, "&cYou must be in admin mode to do this.");

                lang.addLangNode(GeneralLanguage.CLEAR, "&7Inventory cleared of %p&7");
                lang.addLangNode(GeneralLanguage.CLEAR_NOPERM, "&cYou cannot clear the inventory of this player.");

                lang.addLangNode(GeneralLanguage.SPAWN_SET, "&6Global spawn has been set.");

                lang.addLangNode(GeneralLanguage.INVIS_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/invis <rank>");
                lang.addLangNode(GeneralLanguage.INVIS_NOPERM, "&cYou can only become invisible to ranks below yours.");
                lang.addLangNode(GeneralLanguage.INVIS_INVIS, "&dYou are now invisible to %rank &dand below.");
                lang.addLangNode(GeneralLanguage.INVIS_VIS, "&aYou are now visible.");
                lang.addLangNode(GeneralLanguage.INVIS_VIS_ALREADY, "&eYou already are visible.");

                lang.addLangNode(GeneralLanguage.WATCH_WATCHING, "&bYou are now watching.");

                lang.addLangNode(GeneralLanguage.MODE_STILL_ADMIN, "&4You are still in admin mode.");
                lang.addLangNode(GeneralLanguage.MODE_STILL_WATCH, "&4You are still watching.");

                lang.addLangNode(GeneralLanguage.INVSEE_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/invsee <player>");

                lang.addLangNode(GeneralLanguage.SPEED_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/speed <1-10>");
                lang.addLangNode(GeneralLanguage.SPEED_FLY, "&aYour FLY speed is now &r%speed&a.");
                lang.addLangNode(GeneralLanguage.SPEED_WALK, "&aYour WALK speed is now &r%speed&a.");

                lang.addLangNode(GeneralLanguage.HEAD_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/head <player>");

                lang.addLangNode(GeneralLanguage.PVP_ENABLED, "&7PvP is now &rENABLED&7.");
                lang.addLangNode(GeneralLanguage.PVP_DISABLED, "&7PvP is now &rDISABLED&7.");
                lang.addLangNode(GeneralLanguage.PVP_DISABLED_ON_HIT, "&cPVP IS DISABLED!");

                lang.addLangNode(GeneralLanguage.GAMEMODE_SET_OTHER, "&7%p&7 is now in &f%gm &7mode.");
                lang.addLangNode(GeneralLanguage.GAMEMODE_SET_YOU, "&7You are now in &f%gm &7mode.");
                lang.addLangNode(GeneralLanguage.GAMEMODE_USAGE, "/gamemode <gamemode> [mode]");

                lang.addLangNode(GeneralLanguage.BUILD_ENABLED, "&aYou are able to build now.");
                lang.addLangNode(GeneralLanguage.BUILD_DISABLED, "&aYou are no longer able to build now.");

                lang.addLangNode(GeneralLanguage.PING, "&7%z's connection to the server is : %ping MS");

                lang.addLangNode(GeneralLanguage.CLEAR_LAG, "&aAll dropped items have been removed from this world.");

                lang.addLangNode(GeneralLanguage.KILL_ALL, "&cAll non-player entities in this world have been killed.");

                lang.addLangNode(GeneralLanguage.STATS, "&a" + General.SPACER + "\n" +
                        "&aStats of %player:\n" +
                        "&aLevel -> &r%level\n" +
                        "&aPrestige -> &r%prestige\n" +
                        "&aKills -> &r%kills\n" +
                        "&aDeaths -> &r%deaths\n" +
                        "&aKDR -> &r%kdr\n" +
                        "&aELO -> &r%elo\n" +
                        "&aJoin date -> &r%joindate\n" +
                        "&aLast online -> &r%lastonline\n" +
                        "&aPlaytime -> &r%playtime\n" +
                        "&a" + General.SPACER);

                lang.addLangNode(GeneralLanguage.JOIN_WHITELIST, "&cThe server is currently in maintenance.\n" +
                        "&cTry again soon.");
                lang.addLangNode(GeneralLanguage.JOIN_FULL, "&cToo many players online!\n" +
                        "&cYou can buy &6Premium &cto join anyway!\n\n" +
                        "&6Store: &b&uwww.%domain/store");

                lang.addLangNode(GeneralLanguage.PUNISH_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/punish <player> <reason> <details>");
                lang.addLangNode(GeneralLanguage.PUNISH_NOPERM, "&cYou cannot punish this player.");
                lang.addLangNode(GeneralLanguage.PUNISH_BAN_NOPERM, "&cYou are not too low ranked to punish for that reason.");

                lang.addLangNode(GeneralLanguage.BAN_WARNING, "&4&cYOU HAVE BEEN BANNED!");
                lang.addLangNode(GeneralLanguage.BAN_BANNED, "&7%z has been banned.");
                lang.addLangNode(GeneralLanguage.BAN_SCREEN, "&cYou were banned from %name\n\n" +
                        "&8Ban-ID: &f%id\n" +
                        "&8Reason: &f%reason\n" +
                        "&8Duration: &f%duration\n" +
                        "&8Remaining: &f%remaining\n" +
                        "&8Ban date: &f%bandate\n" +
                        "&8Current date: &f%currentdate\n\n" +
                        "&8You can make an unban request at: &fwww.%domain/unban");
                lang.addLangNode(GeneralLanguage.BAN_INFO, "&8Ban-ID: &f%id\n" +
                        "&8Target: &f%target\n" +
                        "&8Reason: &f%reason\n" +
                        "&8Details: &f%details\n" +
                        "&8Duration: &f%duration\n" +
                        "&8Remaining: &f%remaining\n" +
                        "&8Punish Date: &f%date\n" +
                        "&8Punisher: &f%punisher");
                lang.addLangNode(GeneralLanguage.BAN_INFO_PAST, "&8Ban-ID: &f%id\n" +
                        "&8Target: &f%target\n" +
                        "&8Reason: &f%reason\n" +
                        "&8Details: &f%details\n" +
                        "&8Duration: &f%duration\n" +
                        "&8Punish Date: &f%date\n" +
                        "&8Punisher: &f%punisher\n" +
                        "&8Pardoner: &f%pardoner\n" +
                        "&8Pardon Date: &f%pardondate");

                lang.addLangNode(GeneralLanguage.INFO_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/checkinfo <player>");

                lang.addLangNode(GeneralLanguage.UNBAN_UNBANNED, "&7%z has been unbanned.");
                lang.addLangNode(GeneralLanguage.UNBAN_NOT_BANNED, "&c%z is not banned.");
                lang.addLangNode(GeneralLanguage.UNBAN_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/unban <player>");

                lang.addLangNode(GeneralLanguage.IP_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/ip <IP>");

                lang.addLangNode(GeneralLanguage.KICK_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/kick <player> <reason>");
                lang.addLangNode(GeneralLanguage.KICK_KICKED, "%z has been kicked by %p.");
                lang.addLangNode(GeneralLanguage.KICK_SCREEN, "&cYou were kicked from the network.\n\n" +
                        "&8Reason: &f%reason");
                lang.addLangNode(GeneralLanguage.KICK_NOPERM, "&cYou cannot kick this player.");

                lang.addLangNode(GeneralLanguage.MUTE_MUTED, "&7%z has been muted.");
                lang.addLangNode(GeneralLanguage.MUTE_MUTED_ON_TALK, "&cYOU ARE MUTED!");
                lang.addLangNode(GeneralLanguage.MUTE_DISPLAY, "&8Mute-ID: &f%id\n" +
                        "&8Reason: &f%reason\n" +
                        "&8Duration: &f%duration\n" +
                        "&8Remaining: &f%remaining");
                lang.addLangNode(GeneralLanguage.MUTE_INFO, "&8Mute-ID: &f%id\n" +
                        "&8Target: &f%target\n" +
                        "&8Reason: &f%reason\n" +
                        "&8Details: &f%details\n" +
                        "&8Duration: &f%duration\n" +
                        "&8Remaining: &f%remaining\n" +
                        "&8Punish Date: &f%date\n" +
                        "&8Punisher: &f%punisher");
                lang.addLangNode(GeneralLanguage.MUTE_INFO_PAST, "&8Mute-ID: &f%id\n" +
                        "&8Target: &f%target\n" +
                        "&8Reason: &f%reason\n" +
                        "&8Details: &f%details\n" +
                        "&8Duration: &f%duration\n" +
                        "&8Punish Date: &f%date\n" +
                        "&8Punisher: &f%punisher\n" +
                        "&8Pardoner: &f%pardoner\n" +
                        "&8Pardon Date: &f%pardondate");

                lang.addLangNode(GeneralLanguage.UNMUTE_UNMUTED, "&7%z has been unmuted.");
                lang.addLangNode(GeneralLanguage.UNMUTE_UNMUTED_YOU, "&aYou have been unmuted.");
                lang.addLangNode(GeneralLanguage.UNMUTE_NOT_MUTED, "&c%z is not muted!");
                lang.addLangNode(GeneralLanguage.UNMUTE_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/unmute <player>");

                lang.addLangNode(GeneralLanguage.REPORT_STAFF, "&6%hacker &bhas been reported for &6%reason &bby %reporter");
                lang.addLangNode(GeneralLanguage.REPORT_CONFIRMED, "&aYour report has been sent to the staff team.");
                lang.addLangNode(GeneralLanguage.REPORT_NOT_ONLINE, "&cThis player is not online!");
                lang.addLangNode(GeneralLanguage.REPORT_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/report <player>");
                lang.addLangNode(GeneralLanguage.REPORT_COOLDOWN, "&cYou can only report the same player every 5 minutes.");
                lang.addLangNode(GeneralLanguage.REPORT_ERROR_SELF, "&cYou cannot report yourself!");
                lang.addLangNode(GeneralLanguage.REPORT_ERROR_STAFF, "&cYou cannot report a staff member.");
                lang.addLangNode(GeneralLanguage.REPORT_ERROR_NONE, "&cNo reports found.");

                lang.addLangNode(GeneralLanguage.REPORT_LIST, "&cReport List:\n%list");

                lang.addLangNode(GeneralLanguage.REPORT_CLEAR, "&aAll reports targeting %p have been cleared.");
                lang.addLangNode(GeneralLanguage.REPORT_CLEAR_EMPTY, "&cThis player hasn't been reported.");
                lang.addLangNode(GeneralLanguage.REPORT_CLEAR_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/reportclear <player>");

                lang.addLangNode(GeneralLanguage.REPORT_GUI_TITLE, "&cReport %z for:");
                lang.addLangNode(GeneralLanguage.REPORT_GUI_HACKING, "&6Hacking\n" +
                        "&7(Using an illegal client-mod)");
                lang.addLangNode(GeneralLanguage.REPORT_GUI_BUG, "&6Bug Abuse\n" +
                        "&7(Abusing a bug for personal benefit)");
                lang.addLangNode(GeneralLanguage.REPORT_GUI_TEAM, "&6Too big team\n" +
                        "&7(Team consists of more players than allowed)");
                lang.addLangNode(GeneralLanguage.REPORT_GUI_STATS, "&6Stats manipulation\n" +
                        "&7(Example: Kill Farming)");

                lang.addLangNode(GeneralLanguage.INFO_GUI_ERROR, "&cInternal Error.\n&cCheck console");
                lang.addLangNode(GeneralLanguage.INFO_GUI_ADMIN, "&cIP: &r%ip\n" +
                        "&cRank: &r%rank\n" +
                        "&cUUID: &r%uuid\n" +
                        "&cSent Reports: &r%sentreports\n" +
                        "&cTrue Sent Reports: &r%truereports");
                lang.addLangNode(GeneralLanguage.INFO_GUI_TIME, "&bFirst join date: &r%joindate\n" +
                        "&bLast join date: &r%lastonline\n" +
                        "&bPlay time: &r%playtime");
                lang.addLangNode(GeneralLanguage.INFO_GUI_STATS, "&aLevel: &r%level\n" +
                        "&aPrestige: &r%prestige\n" +
                        "&aTokens: &r%tokens\n" +
                        "&aClan: &r%clan\n" +
                        "&aKills: &r%kills\n" +
                        "&aDeaths: &r%deaths\n" +
                        "&aKDR: &r%kdr\n" +
                        "&aELO: &r%elo");
                lang.addLangNode(GeneralLanguage.INFO_GUI_MUTES, "&cMutes");
                lang.addLangNode(GeneralLanguage.INFO_GUI_BANS, "&cBans");
                lang.addLangNode(GeneralLanguage.INFO_GUI_REPORTS, "&6Reports");
                lang.addLangNode(GeneralLanguage.INFO_GUI_ALTS, "&eRelated accounts");
                lang.addLangNode(GeneralLanguage.INFO_GUI_REPORTS_REPORT, "&bTarget: &6%target\n" +
                        "&bReason: &6%reason\n" +
                        "&bReporter: &6%reporter\n" +
                        "&bReport date: &6%date");

                lang.addLangNode(GeneralLanguage.SILENT_HIDDEN, "&cAll alerts are now hidden from you.");
                lang.addLangNode(GeneralLanguage.SILENT_VIS, "&cAll alerts are now visible for you.");

                lang.addLangNode(GeneralLanguage.SPAMCHECK_STAFF, "&cYou would have been muted for spam!");

                lang.addLangNode(GeneralLanguage.COMBATLOG_SAFE, "&aLost combatlog protection! You can now logout safely again.");

                lang.addLangNode(GeneralLanguage.ALERT_HACKS, "&6%p might be using %hack (%chance)!");
                lang.addLangNode(GeneralLanguage.ALERT_NONE, "&cThis player does not have any alerts.");
                lang.addLangNode(GeneralLanguage.ALERT_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/alerts <player>");
                lang.addLangNode(GeneralLanguage.ALERT_LIST, "&6Detected alerts: %alerts");

                lang.addLangNode(GeneralLanguage.CLAN_CREATED, "&aClan successfully created.");
                lang.addLangNode(GeneralLanguage.CLAN_EXIST, "&cThis clan already exists!");
                lang.addLangNode(GeneralLanguage.CLAN_EXIST_NOT, "&cThis clan does not exist!");
                lang.addLangNode(GeneralLanguage.CLAN_NAME_MAX_SIZE, "Your clan name cannot have more than 5 characters!");
                lang.addLangNode(GeneralLanguage.CLAN_HAS_CLAN, "&cPlayer already has a clan.");
                lang.addLangNode(GeneralLanguage.CLAN_DELETED, "&6Your clan has been deleted.");
                lang.addLangNode(GeneralLanguage.CLAN_RANK_ERROR, "&cYour clan rank isn't high enough for you to perform this action.");
                lang.addLangNode(GeneralLanguage.CLAN_INVITE_SENT, "&aYou have invited &r%p &ato join your clan.");
                lang.addLangNode(GeneralLanguage.CLAN_INVITE_RECEIVED, "&aYou have been invited to join the &6%clan&a!");
                lang.addLangNode(GeneralLanguage.CLAN_INVITE_RECEIVED_CLICK_ACCEPT, "&aDo &b/clan accept %clan &ato accept th invite.");
                lang.addLangNode(GeneralLanguage.CLAN_INVITE_RECEIVED_CLICK_DENY, "&aDo &b/clan deny %clan &ato deny th invite.");
                lang.addLangNode(GeneralLanguage.CLAN_INVITE_ALREADY, "&cYour clan has already invited this player.");
                lang.addLangNode(GeneralLanguage.CLAN_INVITE_NOT_OTHER, "&cThis player hasn't been invited to your clan!");
                lang.addLangNode(GeneralLanguage.CLAN_INVITE_NOT_YOU, "&cYou are not invited to this clan!");
                lang.addLangNode(GeneralLanguage.CLAN_INVITE_REVOKED, "&aYou revoked the invite of %p");
                lang.addLangNode(GeneralLanguage.CLAN_INVITE_ACCEPT, "&aYou are now in the &6%clan &aclan!");
                lang.addLangNode(GeneralLanguage.CLAN_INVITE_DENY, "&aYou have denied the invite for the %clan clan.");
                lang.addLangNode(GeneralLanguage.CLAN_PROMOTED, "%p &6is now a &5Moderator &6of your clan.");
                lang.addLangNode(GeneralLanguage.CLAN_PROMOTED_ALREADY, "&cThis player has already been promoted!");
                lang.addLangNode(GeneralLanguage.CLAN_DEMOTED, "%p &6is no longer a Moderator in your clan.");
                lang.addLangNode(GeneralLanguage.CLAN_DEMOTED_ALREADY, "&cThis player has already been demoted!");
                lang.addLangNode(GeneralLanguage.CLAN_NOT_SAME, "This player is not in your clan!");
                lang.addLangNode(GeneralLanguage.CLAN_HIGHER_RANK, "&aThis players clan rank is higher/equal than/to yours!");
                lang.addLangNode(GeneralLanguage.CLAN_KICKED, "&6You kicked %p from the clan.");
                lang.addLangNode(GeneralLanguage.CLAN_LEAVE, "&aYou have left the &6%clan &aclan");
                lang.addLangNode(GeneralLanguage.CLAN_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/clan help");
                lang.addLangNode(GeneralLanguage.CLAN_CHAT_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/clanchat <message>");
                lang.addLangNode(GeneralLanguage.CLAN_NONE, "&cYou have no clan!");
                lang.addLangNode(GeneralLanguage.CLAN_HELP, "&6" + General.SPACER + "\n" +
                        "&6Clan commands:\n" +
                        "&6/clan create <clan> &7- Create a new clan.\n" +
                        "&6/clan delete <clan> &7- Delete a clan.\n" +
                        "&6/clan info <clan> &7- Get all information about a certain clan.\n" +
                        "&6/clan invite <player> &7- Invite a player to your clan.\n" +
                        "&6/clan uninvite <player> &7- Revoke the invite of a player to your clan.\n" +
                        "&6/clan accept <clan> &7- Accept a clan invite.\n" +
                        "&6/clan deny <clan> &7- Deny a clan invite.\n" +
                        "&6/clan promote <player> &7- Promote a player to clan moderator.\n" +
                        "&6/clan demote <player> &7- Demote a clan moderator.\n" +
                        "&6/clan kick <player> &7- Kick a player from your clan.\n" +
                        "&6/clan leave &7- Leave your current clan.\n" +
                        "&6/clanchat <message> &7- Send a message to all online clan members\n" +
                        "&6" + General.SPACER);

                lang.addLangNode(GeneralLanguage.SKIT_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/skit <create : apply : delete> <skit name> [radius]");
                lang.addLangNode(GeneralLanguage.SKIT_APPLY, "&aAll players within &6%radius &ablocks now have the &6%skit &aSKit.");
                lang.addLangNode(GeneralLanguage.SKIT_CREATE, "&aYou created the &6%skit &aSKit.");
                lang.addLangNode(GeneralLanguage.SKIT_DELETE, "&cYou deleted the &6%skit &cSKit");
                lang.addLangNode(GeneralLanguage.SKIT_NULL, "&4SKit does not exist!");
                lang.addLangNode(GeneralLanguage.SKIT_EXISTS, "&cThis SKit already exists!");

                lang.addLangNode(GeneralLanguage.AFK_TRUE, "&7You are now AFK.");
                lang.addLangNode(GeneralLanguage.AFK_FALSE, "&7You are no longer AFK.");

                lang.addLangNode(GeneralLanguage.RULES_AGREED_NOT, "\n\n\n&6&k" + General.SPACER + "\n\n" +
                        "&aYou have not yet agreed to our &rTerms & Conditions &aor they might have changed!\n" +
                        "&aIf you want to play on our server you must first read and agree with them.\n" +
                        "&bwww.%domain/terms_and_conditions\n" +
                        "&aIn order ot agree with them, type &r/agree\n\n" +
                        "&6&k" + General.SPACER);
                lang.addLangNode(GeneralLanguage.RULES_TITLE, "&a&lREAD CHAT TO PLAY!");
                lang.addLangNode(GeneralLanguage.RULES_AGREED, "\n\n&aYou have now agreed to our &rTerms & Conditions&a.\n&aHave fun on our network!");
                lang.addLangNode(GeneralLanguage.RULES_AGREED_ALREADY, "&cYou have already agreed to our Terms & Conditions!");

                lang.addLangNode(GeneralLanguage.MSG_USAGE_TELL, lang.get(GeneralLanguage.SYNTAX) + "/tell <player> <message>");
                lang.addLangNode(GeneralLanguage.MSG_USAGE_REPLY, lang.get(GeneralLanguage.SYNTAX) + "/reply <message>");
                lang.addLangNode(GeneralLanguage.MSG_AFK, "&c%z is currently AFK and probably won't respond.");
                lang.addLangNode(GeneralLanguage.MSG_PARTNER_LEFT, "&4Your chat partner is no longer online.");
                lang.addLangNode(GeneralLanguage.MSG_REPLY_NULL, "&4You have nobody to reply too.");

                lang.addLangNode(GeneralLanguage.SAY_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/say <message>");

                lang.addLangNode(GeneralLanguage.CHAT_LINKS, "&cYou are not allowed to send links in the public chat.");
                lang.addLangNode(GeneralLanguage.CHAT_SWEAR, "&cYou are not allowed to swear in the public chat.");
                lang.addLangNode(GeneralLanguage.CHAT_CLEAR, "&7* Chat has been cleared by %p&7!");
                lang.addLangNode(GeneralLanguage.CHAT_ENABLED, "&7* Chat is now &aENABLED&7!");
                lang.addLangNode(GeneralLanguage.CHAT_DISABLED, "&7* Chat is now &cDISABLED&7!");
                lang.addLangNode(GeneralLanguage.CHAT_DISABLED_ON_TALK, "&cCHAT IS DISABLED");
                lang.addLangNode(GeneralLanguage.CHAT_LOG_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/chatlog <player>");

                lang.addLangNode(GeneralLanguage.PARTY_CREATED, "&aYou have started a party!");
                lang.addLangNode(GeneralLanguage.PARTY_ALREADY, "&cYou already are in a party!");
                lang.addLangNode(GeneralLanguage.PARTY_DELETED, "&6Your party has been deleted.");
                lang.addLangNode(GeneralLanguage.PARTY_NOT_CREATOR, "&cOnly the party creator can do this.");
                lang.addLangNode(GeneralLanguage.PARTY_INVITE, "&aYou invited %p&a to join the party.");
                lang.addLangNode(GeneralLanguage.PARTY_INVITE_RECEIVED, "%p &ahas invited you to their party.");
                lang.addLangNode(GeneralLanguage.PARTY_INVITE_RECEIVED_CLICK_ACCEPT, "&aDo &e/party accept %p &a to accept the invite.");
                lang.addLangNode(GeneralLanguage.PARTY_INVITE_RECEIVED_CLICK_DENY, "&aDo &e/party deny %p &a to deny the invite.");
                lang.addLangNode(GeneralLanguage.PARTY_INVITE_ALREADY, "&cThis player has already been invited to your party.");
                lang.addLangNode(GeneralLanguage.PARTY_INVITE_NULL, "&cThis player has not been invited to your party.");
                lang.addLangNode(GeneralLanguage.PARTY_INVITE_NOT_YOU, "&cYou are not invited to this party.");
                lang.addLangNode(GeneralLanguage.PARTY_INVITE_REVOKED, "&aYou revoked the invite of %p&a.");
                lang.addLangNode(GeneralLanguage.PARTY_INVITE_ACCEPT, "&aYou have accepted and joined the party of %p&a.");
                lang.addLangNode(GeneralLanguage.PARTY_INVITE_DENY, "&aYou denied the party invite of %p&a.");
                lang.addLangNode(GeneralLanguage.PARTY_NOT_SAME, "&cThis player is not in your party!");
                lang.addLangNode(GeneralLanguage.PARTY_KICKED, "%p &6has been kicked from the party!");
                lang.addLangNode(GeneralLanguage.PARTY_LEAVE, "&aYou left the party of %p&a.");
                lang.addLangNode(GeneralLanguage.PARTY_ALREADY_OTHER, "&cThis player is already part of a party.");
                lang.addLangNode(GeneralLanguage.PARTY_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/party help");
                lang.addLangNode(GeneralLanguage.PARTY_NONE_YOU, "&cYou are not in a party!");
                lang.addLangNode(GeneralLanguage.PARTY_CHAT_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/partychat <message>");
                lang.addLangNode(GeneralLanguage.PARTY_HELP, "&e" + General.SPACER + "\n" +
                        "&eParty commands:\n" +
                        "&e/party create <party> &7- Start a new party.\n" +
                        "&e/party delete <party> &7- Delete a party.\n" +
                        "&e/party invite <player> &7- Invite a player to your party.\n" +
                        "&e/party uninvite <player> &7- Revoke the invite of a player to your party.\n" +
                        "&e/party accept <player> &7- Accept a party invite.\n" +
                        "&e/party deny <player> &7- Deny a party invite.\n" +
                        "&e/party kick <player> &7- Kick a player from your party.\n" +
                        "&e/party leave &7- Leave your current party.\n" +
                        "&e/partychat <message> &7- Send a message to all online party members\n" +
                        "&e" + General.SPACER);

                lang.addLangNode(GeneralLanguage.ACHIEVEMENT_UNLOCKED, "&7You unlocked the &6%achievement &7achievement!");
                lang.addLangNode(GeneralLanguage.ACHIEVEMENT_FIRST_MSG, "First message\n&7Write a message in chat for the first time.");
                lang.addLangNode(GeneralLanguage.ACHIEVEMENT_KILLER, "Killer\n&7Kill a player.");
                lang.addLangNode(GeneralLanguage.ACHIEVEMENT_PRESTIGE, "Prestige\n&7Prestige for the first time.");
                lang.addLangNode(GeneralLanguage.ACHIEVEMENT_MAX_PRESTIGE, "Veteran\n&7Finish all prestiges.");
                lang.addLangNode(GeneralLanguage.ACHIEVEMENT_STAFF_KILL, "Staff slayer\n&7Kill a staff member for the first time.");
                lang.addLangNode(GeneralLanguage.ACHIEVEMENT_ADMIN_KIll, "Admin slayer\n&7Kill an Admin.");
                lang.addLangNode(GeneralLanguage.ACHIEVEMENT_MEDIA_KILL, "Notice Me Senpai\n&7Kill a player with the Media rank.");
                lang.addLangNode(GeneralLanguage.ACHIEVEMENT_MAX_PRESTIGE_KILL, "Beginners Luck\n&7Kill a player who is master prestige.");
                lang.addLangNode(GeneralLanguage.ACHIEVEMENT_BLOODTHIRSTY, "Bloodthirsty\n&7Kill 5 players without dying.");
                lang.addLangNode(GeneralLanguage.ACHIEVEMENT_INVINCIBLE, "Invincible\n&7Kill 10 players without dying.");
                lang.addLangNode(GeneralLanguage.ACHIEVEMENT_NUCLEAR, "Nuclear\n&7Kill 25 players without dying.");
                lang.addLangNode(GeneralLanguage.ACHIEVEMENT_GOD, "God\n&7Kill 50 player without dying.");
                lang.addLangNode(GeneralLanguage.ACHIEVEMENT_LAST_RESORT, "Last Resort\n&7Kill a player with half a heart left and no soups.");
                lang.addLangNode(GeneralLanguage.ACHIEVEMENT_LONG_SHOT, "Hail Marie\n&7Kill someone with an arrow shot.");
                lang.addLangNode(GeneralLanguage.ACHIEVEMENT_BACKSTAB, "Backstabber\n&7Kill a player who is not looking at you.");

                lang.addLangNode(GeneralLanguage.SPY_ENABLED, "&cSPY &7is now active.");
                lang.addLangNode(GeneralLanguage.SPY_DISABLED, "&cSPY &7is no longer active.");

                lang.addLangNode(GeneralLanguage.APPLICATION_LINK, "&6Apply at: &b&nhttps://www.%domain/apply");

                lang.addLangNode(GeneralLanguage.SUPPORT_REQUEST_STAFF, "&e%p is trying to contact a staff member!");
                lang.addLangNode(GeneralLanguage.SUPPORT_REQUEST_SENT, "&eYour support request has been submitted.\n" +
                        "&ePlease wait until a staff member answers you.");
                lang.addLangNode(GeneralLanguage.SUPPORT_CONFIRMED_YOU, "&aA staff member has accepted your support request!\n" +
                        "&aYou are now talking with %staff&a.\n" +
                        "&aSimply write normally in chat to talk with %staff&a.\n" +
                        "&aYou can do &6/support end &ato leave the support chat.");
                lang.addLangNode(GeneralLanguage.SUPPORT_CONFIRMED_STAFF, "&aYou are now supporting %z&a.");
                lang.addLangNode(GeneralLanguage.SUPPORT_END, "&cSupport chat has been ended!");
                lang.addLangNode(GeneralLanguage.SUPPORT_SELF, "&cYou cannot add yourself to a support chat.");
                lang.addLangNode(GeneralLanguage.SUPPORT_ALREADY, "&c%z &cis already being supported.");
                lang.addLangNode(GeneralLanguage.SUPPORT_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/support [end : add]");
                lang.addLangNode(GeneralLanguage.SUPPORT_NO_REQUEST, "&c%z &cdid not make a support request.");
                lang.addLangNode(GeneralLanguage.SUPPORT_COOLDOWN, "&cYou can only make a support request every 5 minutes.");
                lang.addLangNode(GeneralLanguage.SUPPORT_CHAT_NULL, "&cYou are not in a support chat!");

                lang.addLangNode(GeneralLanguage.MENU_GUI_STAFF, "&aStaff list");
                lang.addLangNode(GeneralLanguage.MENU_GUI_ACHIEVEMENTS, "&9Achievements");
                lang.addLangNode(GeneralLanguage.MENU_GUI_FAQ, "&eF.A.Q");
                lang.addLangNode(GeneralLanguage.MENU_GUI_APPLICATION, "&6Applications");
                lang.addLangNode(GeneralLanguage.MENU_GUI_STATS, "&dStats");
                lang.addLangNode(GeneralLanguage.MENU_GUI_UPDATE, "&7Update log");

                lang.addLangNode(GeneralLanguage.MENU_GUI_BACK, "&aBack");
                lang.addLangNode(GeneralLanguage.MENU_GUI_TITLE, "&aMenu");

                lang.addLangNode(GeneralLanguage.MENU_GUI_APPLICATION_APPLY, "&6Apply\n" +
                        "&7Click this to get the application link.");
                lang.addLangNode(GeneralLanguage.MENU_GUI_APPLICATION_MOD, Prefix.MOD.getColor() + "Moderator qualifications:\n" +
                        " \n" +
                        "&6- Skype\n" +
                        "&6- Teamspeak 3\n" +
                        "&6- Speak english &9&kAA&r&c&oWITHOUT GOOGLE TRANSLATOR&r&9&kAA\n" +
                        "&6- Previous moderator experience\n" +
                        "&6- Maturity\n" +
                        "&6- Patience and examination skills\n" +
                        "&6- Able to record/upload videos at a minimum of 30fps and 360p");
                lang.addLangNode(GeneralLanguage.MENU_GUI_APPLICATION_SUPPORTER, Prefix.SUPPORTER.getColor() + "Supporter qualifications:\n" +
                        " \n" +
                        "&6- Skype\n" +
                        "&6- Teamspeak 3\n" +
                        "&6- Speak english &9&kAA&r&c&oWITHOUT GOOGLE TRANSLATOR&r&9&kAA\n" +
                        "&6- Maturity\n" +
                        "&6- Patience\n" +
                        "&6- Polite");
                lang.addLangNode(GeneralLanguage.MENU_GUI_APPLICATION_BUILDER, Prefix.BUILDER.getColor()+ "Builder qualifications:\n" +
                        " \n" +
                        "&6- Skype\n" +
                        "&6- Teamspeak 3\n" +
                        "&6- Speak english &9&kAA&r&c&oWITHOUT GOOGLE TRANSLATOR&r&9&kAA\n" +
                        "&6- Excellent WorldEdit knowledge\n" +
                        "&6- Maturity");

                lang.addLangNode(GeneralLanguage.MENU_GUI_STATS_GENERAL, "&cRank: %rank\n" +
                        "&cClan: &6%clan\n" +
                        "&cPrestige: &6%prestige\n" +
                        "&cLevel: &6%level\n" +
                        "&cTokens: &6%tokens\n" +
                        "&cExp: %exp");
                lang.addLangNode(GeneralLanguage.MENU_GUI_STATS_ACTIVITY, "&aKills: &f%kills\n" +
                        "&aDeaths: &f%deaths\n" +
                        "&aKDR: &f%kdr\n" +
                        "&aELO: &f%elo");
                lang.addLangNode(GeneralLanguage.MENU_GUI_STATS_TIME, "&bFirst join: &f%firstjoin\n" +
                        "&bLast join: &f%lastjoin\n" +
                        "&bPlay time: &f%playtime");
                lang.addLangNode(GeneralLanguage.MENU_GUI_STATS_PRESTIGE_SET, "&aPrestige mode\n" +
                        "&7Click to prestige\n" +
                        " \n" +
                        "&6&kAA&cWARNING&6&kAA\n" +
                        " \n" +
                        "&e&nOnce you prestige you will not be able to go back!\n\n" +
                        "&e- Items which were bought with normal tokens\n" +
                        "  &ewill be locked until a certain level!\n" +
                        "&e- You will receive 1 prestige token.");
                lang.addLangNode(GeneralLanguage.MENU_GUI_STATS_PRESTIGE_LOCKED, "&4LOCKED\n" +
                        "&8Available at level 55");

                lang.addLangNode(GeneralLanguage.MENU_GUI_UPDATE_1, "&e&nKitPvP: 17 new Kits\n" +
                        "&717/11/2016");
                lang.addLangNode(GeneralLanguage.MENU_GUI_UPDATE_2, "&e&nAchievements\n" +
                        "&702/11/2016");
                lang.addLangNode(GeneralLanguage.MENU_GUI_UPDATE_3, "&e&nMulti-Language\n" +
                        "&720/10/2016");
                lang.addLangNode(GeneralLanguage.MENU_GUI_UPDATE_4, "&e&nSupporter rank\n" +
                        "&712/09/2016");
                lang.addLangNode(GeneralLanguage.MENU_GUI_UPDATE_5, "&e&n--");
                lang.addLangNode(GeneralLanguage.MENU_GUI_UPDATE_6, "&e&n--");
                lang.addLangNode(GeneralLanguage.MENU_GUI_UPDATE_7, "&e&n--");

                lang.addLangNode(GeneralLanguage.MENU_GUI_FAQ_RULES, "&eRules");
                lang.addLangNode(GeneralLanguage.MENU_GUI_FAQ_LEVELS, "&eLevels/Prestige");
                lang.addLangNode(GeneralLanguage.MENU_GUI_FAQ_STORE, "&eStore");
                lang.addLangNode(GeneralLanguage.MENU_GUI_FAQ_TEAMSPEAK, "&eTeamspeak");
                lang.addLangNode(GeneralLanguage.MENU_GUI_FAQ_UNBAN, "&eUnban/Unmute");
                lang.addLangNode(GeneralLanguage.MENU_GUI_FAQ_SUPPORT, "&eSupport\n" +
                        "&aClick here to contact\n" +
                        "&aa staff member for help.");

                lang.addLangNode(GeneralLanguage.MENU_GUI_FAQ_RULES_MODS, "&aWhat mods am I allowed to use?");
                lang.addLangNode(GeneralLanguage.MENU_GUI_FAQ_RULES_CHAT, "&aWhat are the chat rules?");
                lang.addLangNode(GeneralLanguage.MENU_GUI_FAQ_RULES_TEAMS, "&aAre teams allowed?");
                lang.addLangNode(GeneralLanguage.MENU_GUI_FAQ_RULES_TERMS, "&aWhere can I see the complete rule list?");

                lang.addLangNode(GeneralLanguage.MENU_GUI_FAQ_LEVELS_LEVELS, "&aWhat are levels?");
                lang.addLangNode(GeneralLanguage.MENU_GUI_FAQ_LEVELS_PRESTIGE, "&aWhat is a Prestige?");
                lang.addLangNode(GeneralLanguage.MENU_GUI_FAQ_LEVELS_TOKENS, "&aWhat are tokens?");
                lang.addLangNode(GeneralLanguage.MENU_GUI_FAQ_LEVELS_PRESTIGE_CHANGE, "&aHow can I change Prestige?");
                lang.addLangNode(GeneralLanguage.MENU_GUI_FAQ_LEVELS_LEVEL_MAX, "&aWhat is the maximum level?");
                lang.addLangNode(GeneralLanguage.MENU_GUI_FAQ_LEVELS_PRESTIGE_MAX, "&aWhat is the maximum prestige?");
                lang.addLangNode(GeneralLanguage.MENU_GUI_FAQ_LEVELS_COLORS, "&aWhy do some levels have different colors?");

                lang.addLangNode(GeneralLanguage.MENU_GUI_FAQ_STORE_KITS, "&aWhere can I buy ranks/kits?");

                lang.addLangNode(GeneralLanguage.MENU_GUI_FAQ_TEAMSPEAK_WHAT, "&aWhat is Teamspeak 3?");
                lang.addLangNode(GeneralLanguage.MENU_GUI_FAQ_TEAMSPEAK_WHERE, "&aWhere can I download Teamspeak 3?");
                lang.addLangNode(GeneralLanguage.MENU_GUI_FAQ_TEAMSPEAK_IP, "&aWhat is the Teamspeak IP of %name?");

                lang.addLangNode(GeneralLanguage.MENU_GUI_FAQ_UNBAN_UNBAN, "&aHow can I get unbanned?");
                lang.addLangNode(GeneralLanguage.MENU_GUI_FAQ_UNBAN_UNMUTE, "&aHow can I get unmuted?");
                lang.addLangNode(GeneralLanguage.MENU_GUI_FAQ_UNBAN_WHERE, "&aHow can I buy an unban/unmute?");

                lang.addLangNode(GeneralLanguage.MENU_GUI_STAFF_PREFIX, "&6&nPrefix List\n ");
                lang.addLangNode(GeneralLanguage.MENU_GUI_STAFF_ADMINS, Prefix.ADMIN.getColor() + "Admins");
                lang.addLangNode(GeneralLanguage.MENU_GUI_STAFF_MODS, Prefix.MOD.getColor() + "Moderators");
                lang.addLangNode(GeneralLanguage.MENU_GUI_STAFF_SUPPORTERS, Prefix.SUPPORTER.getColor() + "Supporters");
                lang.addLangNode(GeneralLanguage.MENU_GUI_STAFF_BUILDERS, Prefix.BUILDER.getColor() + "Builders");
                lang.addLangNode(GeneralLanguage.MENU_GUI_STAFF_YOU, "You?");
                break;
            case GERMAN:
                break;
        }
    }
}
