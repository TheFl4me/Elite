package com.minecraft.plugin.elite.general.api.events;

import com.minecraft.plugin.elite.general.api.enums.Language;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LanguageEvent extends Event {

    private Language lang;

    private static final HandlerList handlers = new HandlerList();

    public LanguageEvent(Language lang) {
        this.lang = lang;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Language getLanguage() {
        return this.lang;
    }
}
