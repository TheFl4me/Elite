package com.minecraft.plugin.elite.survivalgames.listeners.basic;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.api.events.LanguageEvent;
import com.minecraft.plugin.elite.survivalgames.SurvivalGamesLanguage;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LanguageEventListener implements Listener {

    @EventHandler
    public void onLangChange(LanguageEvent e) {
        Language lang = e.getLanguage();
        switch (lang) {
            case ENGLISH:
                lang.addLangNode(SurvivalGamesLanguage.ARENA_NULL, "&4Arena not found!");
                lang.addLangNode(SurvivalGamesLanguage.ARENA_COUNTDOWN, "&7The game will begin in &6%seconds &7seconds!");
                lang.addLangNode(SurvivalGamesLanguage.ARENA_START, "&7The game has &6started&7!");
                lang.addLangNode(SurvivalGamesLanguage.ARENA_WARNING, "&4&lNO Grace Period!");

                lang.addLangNode(SurvivalGamesLanguage.POD_SET, "&7Pod %pod set.");
                lang.addLangNode(SurvivalGamesLanguage.POD_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/pod <index>");

                lang.addLangNode(SurvivalGamesLanguage.CENTER_SET, "&7Center set.");

                lang.addLangNode(SurvivalGamesLanguage.SIZE_SET, "&7Border size has been set.");
                lang.addLangNode(SurvivalGamesLanguage.SIZE_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/border <min> <max>");
                lang.addLangNode(SurvivalGamesLanguage.SIZE_INVALID, "&cYou must enter a valid number.");

                lang.addLangNode(SurvivalGamesLanguage.SHRINK_START, "&aThe forcefield is now starting to move!");
                lang.addLangNode(SurvivalGamesLanguage.SHRINK_END, "&cThe forcefield has stopped moving. Chests have been refilled.");

                lang.addLangNode(SurvivalGamesLanguage.KICK_DEATH, "&bYou died.\n&r%remaining &btribute(s) remaining.\n&bBetter luck next time!");
                lang.addLangNode(SurvivalGamesLanguage.KICK_ALREADY, "&cThe game has already started.\n&cTry joining the next game.");
                lang.addLangNode(SurvivalGamesLanguage.KICK_FULL, "&cThe game is full!\n&cYou can buy &6Premium &cto join anyway!\n\n" +
                                "&6Store: &b&uwww.%domain/store");
                lang.addLangNode(SurvivalGamesLanguage.KICK_FULL_VIP, "&cThe game is full!\n&cWe were unable to find a non-premium player for you to replace.");
                lang.addLangNode(SurvivalGamesLanguage.KICK_END, "&bNobody won the game.\n&bServer is now restarting.");
                lang.addLangNode(SurvivalGamesLanguage.KICK_END_WINNER, "%winner &bwon the game!\n&bServer is now restarting.");
                lang.addLangNode(SurvivalGamesLanguage.KICK_REPLACED, "&cYou have been kicked to make space for a premium or higher!\n&cTo avoid this in the future you can buy &6Premium&c!\n\n" +
                        "&6Store: &b&uwww.%domain/store");

                lang.addLangNode(SurvivalGamesLanguage.START_ALREADY, "&cThe game has already started.");

                lang.addLangNode(SurvivalGamesLanguage.MAP_SET_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/setmap <map>");
                lang.addLangNode(SurvivalGamesLanguage.MAP_SET, "&aThe map has been changed to &6%arena&a.");

                lang.addLangNode(SurvivalGamesLanguage.MAP_TELEPORT_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/maptp <map>");
                lang.addLangNode(SurvivalGamesLanguage.MAP_TELEPORT_CONFIRM, "&bYou have been teleported to &6%arena&b.");

                lang.addLangNode(SurvivalGamesLanguage.WIN, "&6%player wins!!!");

                lang.addLangNode(SurvivalGamesLanguage.REMAINING, "&f%remaining &btribute(s) remaining.");
                lang.addLangNode(SurvivalGamesLanguage.DEATH, "&b%player has fallen.\n" + lang.get(SurvivalGamesLanguage.REMAINING));
                lang.addLangNode(SurvivalGamesLanguage.DISCONNECT, "&b%player disconnected.\n" + lang.get(SurvivalGamesLanguage.REMAINING));

                lang.addLangNode(SurvivalGamesLanguage.LOBBY_COUNTDOWN, "&7You will be teleported in &6%seconds &7seconds!");
                lang.addLangNode(SurvivalGamesLanguage.LOBBY_STATUS, ChatColor.AQUA + General.SPACER +"\n" +
                        "&6%minimum &bPlayers needed to start!\n" +
                        "&bPlayers: &6%players/24\n" +
                        "&bMap: &6%arena\n" +
                        ChatColor.AQUA + General.SPACER);

                lang.addLangNode(SurvivalGamesLanguage.TRACKING, "&eCompass pointing at %player.");
                lang.addLangNode(SurvivalGamesLanguage.TRACKING_NONE, "&4No target found!");

                lang.addLangNode(SurvivalGamesLanguage.PLAYERS_GUI_HEAD, "%p\n&6Kills: %kills\n&7(Click to teleport)");
                lang.addLangNode(SurvivalGamesLanguage.PLAYERS_GUI_TITLE, "&6Alive players");

                lang.addLangNode(SurvivalGamesLanguage.SCOREBOARD_TITLE, "&7%domain");
                lang.addLangNode(SurvivalGamesLanguage.SCOREBOARD_MAP, "&bArena:");
                lang.addLangNode(SurvivalGamesLanguage.SCOREBOARD_TIME, "&bStarts in:");
                lang.addLangNode(SurvivalGamesLanguage.SCOREBOARD_PLAYTIME, "&bPlay time:");
                lang.addLangNode(SurvivalGamesLanguage.SCOREBOARD_PLAYERS, "&bPlayers:");
                lang.addLangNode(SurvivalGamesLanguage.SCOREBOARD_KILLS, "&bKills:");
                break;
            case GERMAN:
                break;
        }
    }
}
