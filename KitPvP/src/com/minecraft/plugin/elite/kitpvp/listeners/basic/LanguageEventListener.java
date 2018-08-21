package com.minecraft.plugin.elite.kitpvp.listeners.basic;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.api.events.LanguageEvent;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LanguageEventListener implements Listener {

    @EventHandler
    public void onLangChange(LanguageEvent e) {
        Language lang = e.getLanguage();
        switch (lang) {
            case ENGLISH:
                lang.addLangNode(KitPvPLanguage.SCOREBOARD_TITLE, "&7%domain");
                lang.addLangNode(KitPvPLanguage.SCOREBOARD_KIT, "&cKit:");
                lang.addLangNode(KitPvPLanguage.SCOREBOARD_KILLS, "&cKills:");
                lang.addLangNode(KitPvPLanguage.SCOREBOARD_DEATHS, "&cDeaths:");
                lang.addLangNode(KitPvPLanguage.SCOREBOARD_ELO, "&cELO:");

                lang.addLangNode(KitPvPLanguage.REGION_SPAWN_LEAVE, "&7You no longer have spawn protection!");

                lang.addLangNode(KitPvPLanguage.HOLOGRAM_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/sethologram <feast : ehg>");
                lang.addLangNode(KitPvPLanguage.HOLOGRAM_SET, "&7%holo hologram set.");
                lang.addLangNode(KitPvPLanguage.HOLOGRAM_FEAST, "&aFeast");
                lang.addLangNode(KitPvPLanguage.HOLOGRAM_EHG, "&eEarlyHG");

                lang.addLangNode(KitPvPLanguage.DUEL_SET_SPAWN, "&7Duel spawn set.");
                lang.addLangNode(KitPvPLanguage.DUEL_SET_LOCATION_SET, "&7Duel location %loc set.");
                lang.addLangNode(KitPvPLanguage.DUEL_SET_LOCATION_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/setduelloc <1 : 2>");

                lang.addLangNode(KitPvPLanguage.SPAWN_TOOL, "&cBack to Spawn");

                lang.addLangNode(KitPvPLanguage.DUEL_REQUEST_RECEIVED, "&a%p has asked you for a %type duel!\nRight click him with your &bDuel Selector &ato accept the request.");
                lang.addLangNode(KitPvPLanguage.DUEL_REQUEST_SENT, "&aYou asked %z for a %type duel!");
                lang.addLangNode(KitPvPLanguage.DUEL_REQUEST_ACCEPTED, "&aYou are now dueling %z");
                lang.addLangNode(KitPvPLanguage.DUEL_REQUEST_COOLDOWN, "&cYou must wait 10 seconds before asking the same player for a duel again.");
                lang.addLangNode(KitPvPLanguage.DUEL_ALREADY, "&c%z1 is already dueling %z2!");
                lang.addLangNode(KitPvPLanguage.DUEL_WIN, "&6You won the duel against %z!");
                lang.addLangNode(KitPvPLanguage.DUEL_LOSE, "&cYou lost the duel again %z.");

                lang.addLangNode(KitPvPLanguage.DUEL_GUI_TITLE, "&cCustomize the duel setup");
                lang.addLangNode(KitPvPLanguage.DUEL_GUI_WAIT, "&c%z is still deciding!");
                lang.addLangNode(KitPvPLanguage.DUEL_GUI_EDIT, "&eEDIT");
                lang.addLangNode(KitPvPLanguage.DUEL_GUI_ACCEPT, "&bACCEPT");
                lang.addLangNode(KitPvPLanguage.DUEL_GUI_DONE, "&aDONE");
                lang.addLangNode(KitPvPLanguage.DUEL_GUI_CANCEL, "&cCANCEL");
                lang.addLangNode(KitPvPLanguage.DUEL_GUI_EDITOR, "&a%p is EDITING");
                lang.addLangNode(KitPvPLanguage.DUEL_GUI_STANDBY, "&a%p is WAITING");
                lang.addLangNode(KitPvPLanguage.DUEL_GUI_RE_CRAFT, "Recraft");

                lang.addLangNode(KitPvPLanguage.DUEL_TOOL, "&bDuel");
                lang.addLangNode(KitPvPLanguage.DUEL_TOOL_DEFAULT, "&bDefault Duel Selector");
                lang.addLangNode(KitPvPLanguage.DUEL_TOOL_CUSTOM, "&6Custom Duel Selector");
                lang.addLangNode(KitPvPLanguage.DUEL_TOOL_QUEUE, "&5Duel Queue");
                lang.addLangNode(KitPvPLanguage.DUEL_TOOL_QUEUE_CANCEL, "&cExit Duel Queue");

                lang.addLangNode(KitPvPLanguage.DEATH, "&b%z[&7%kit&b] killed you with &c%health hearts &band &6%soups soups &bremaining.");
                lang.addLangNode(KitPvPLanguage.KILL_STREAK, "&c%p is now on a &6%streak &ckillstreak!");
                break;
            case GERMAN:
                break;
        }
    }
}
