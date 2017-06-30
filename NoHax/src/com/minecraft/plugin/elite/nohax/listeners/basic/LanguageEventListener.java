package com.minecraft.plugin.elite.nohax.listeners.basic;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.api.events.LanguageEvent;
import com.minecraft.plugin.elite.nohax.NoHaxLanguage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LanguageEventListener implements Listener {

    @EventHandler
    public void onLangChange(LanguageEvent e) {
        Language lang = e.getLanguage();
        switch (lang) {
            case ENGLISH:
                lang.addLangNode(NoHaxLanguage.SILENT_HIDDEN, "&cAll alerts are now hidden from you.");
                lang.addLangNode(NoHaxLanguage.SILENT_VIS, "&cAll alerts are now visible for you.");

                lang.addLangNode(NoHaxLanguage.SPAMCHECK_STAFF, "&cYou would have been muted for spam!");

                lang.addLangNode(NoHaxLanguage.COMBATLOG_SAFE, "&aLost combatlog protection! You can now logout safely again.");

                lang.addLangNode(NoHaxLanguage.ALERT_HACKS, "&6%p might be using %hack (%chance)!");
                lang.addLangNode(NoHaxLanguage.ALERT_NONE, "&cThis player does not have any alerts.");
                lang.addLangNode(NoHaxLanguage.ALERT_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/alerts <player>");
                lang.addLangNode(NoHaxLanguage.ALERT_LIST, "&6Detected alerts: %alerts");
                break;
            case GERMAN:
                break;
        }
    }
}
