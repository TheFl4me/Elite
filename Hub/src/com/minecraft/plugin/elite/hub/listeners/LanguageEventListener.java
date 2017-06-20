package com.minecraft.plugin.elite.hub.listeners;

import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.api.events.LanguageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LanguageEventListener implements Listener {

    @EventHandler
    public void onLangChange(LanguageEvent e) {
        Language lang = e.getLanguage();
        switch (lang) {
            case ENGLISH:
                break;
            case GERMAN:
                break;
        }
    }
}
