package com.minecraft.plugin.elite.general.api.enums;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.interfaces.LanguageNode;
import org.bukkit.ChatColor;

public enum Achievement {

    FIRST_MSG(GeneralLanguage.ACHIEVEMENT_FIRST_MSG, Difficulty.EASY),
    KILLER(GeneralLanguage.ACHIEVEMENT_KILLER, Difficulty.EASY),
    PRESTIGE(GeneralLanguage.ACHIEVEMENT_PRESTIGE, Difficulty.MEDIUM),
    MAX_PRESTIGE(GeneralLanguage.ACHIEVEMENT_MAX_PRESTIGE, Difficulty.EXTREME),
    STAFF_KILL(GeneralLanguage.ACHIEVEMENT_STAFF_KILL, Difficulty.MEDIUM),
    ADMIN_KIll(GeneralLanguage.ACHIEVEMENT_ADMIN_KIll, Difficulty.HARD),
    MEDIA_KILL(GeneralLanguage.ACHIEVEMENT_MEDIA_KILL, Difficulty.MEDIUM),
    MAX_PRESTIGE_KILL(GeneralLanguage.ACHIEVEMENT_MAX_PRESTIGE_KILL, Difficulty.HARD),
    BLOODTHIRSTY(GeneralLanguage.ACHIEVEMENT_BLOODTHIRSTY, Difficulty.EASY),
    INVINCIBLE(GeneralLanguage.ACHIEVEMENT_INVINCIBLE, Difficulty.MEDIUM),
    NUCLEAR(GeneralLanguage.ACHIEVEMENT_NUCLEAR, Difficulty.MEDIUM),
    GOD(GeneralLanguage.ACHIEVEMENT_GOD, Difficulty.HARD),
    LAST_RESORT(GeneralLanguage.ACHIEVEMENT_LAST_RESORT, Difficulty.HARD),
    LONG_SHOT(GeneralLanguage.ACHIEVEMENT_LONG_SHOT, Difficulty.MEDIUM),
    BACK_STAB(GeneralLanguage.ACHIEVEMENT_BACK_STAB, Difficulty.MEDIUM);

    private final LanguageNode name;
    private final Difficulty difficulty;

    Achievement(LanguageNode name, Difficulty difficulty) {
        this.name = name;
        this.difficulty = difficulty;
    }

    public String getName(Language lang) {
        return lang.getOnlyFirstLine(this.name);
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public String getDisplay(Language lang) {
        return lang.get(this.name) + "\n\n" + ChatColor.DARK_GRAY + "+" + this.getTokens() + " Tokens\n" + ChatColor.DARK_GRAY + "+" + this.getExp() + " Exp";
    }

    public long getExp() {
        return (long) this.getDifficulty().getModifier() * 1000L;
    }

    public long getTokens() {
        return this.getDifficulty().getModifier();
    }

    public enum Difficulty {

        EASY(1),
        MEDIUM(5),
        HARD(10),
        EXTREME(50);

        private int index;

        Difficulty(int i) {
            this.index = i;
        }

        public int getModifier() {
            return this.index;
        }
    }
}