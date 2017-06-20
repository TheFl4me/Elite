package com.minecraft.plugin.elite.general.api.enums;

import com.minecraft.plugin.elite.general.api.events.LanguageEvent;
import com.minecraft.plugin.elite.general.api.interfaces.LanguageNode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public enum Language {

    ENGLISH,
    GERMAN;

    private HashMap<LanguageNode, String> strings;

    Language() {
        this.strings = new HashMap<>();
    }

    public void loadStrings() {
        LanguageEvent event = new LanguageEvent(this);
        Bukkit.getServer().getPluginManager().callEvent(event);
    }

    public Map<LanguageNode, String> getStrings() {
        return this.strings;
    }

    public String get(LanguageNode node) {
        String string = this.getStrings().get(node);
        if(string == null)
            return ChatColor.DARK_RED + "Error whilst loading this message.";
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public LanguageNode getNode(String string, boolean gui) {
        for(LanguageNode node : this.getStrings().keySet()) {
            if(gui)
                if(this.getOnlyFirstLine(node).equalsIgnoreCase(string))
                return node;
            else
                if(this.get(node).equalsIgnoreCase(string))
                    return node;
        }
        return null;
    }

    public String getOnlyFirstLine(LanguageNode node) {
        String[] strings = this.get(node).split("\n");
        return strings[0];
    }

    public void addLangNode(LanguageNode node, String string) {
        this.strings.put(node, string);
    }
}
