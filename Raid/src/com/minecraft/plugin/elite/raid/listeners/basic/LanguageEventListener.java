package com.minecraft.plugin.elite.raid.listeners.basic;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.api.events.LanguageEvent;
import com.minecraft.plugin.elite.raid.RaidLanguage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LanguageEventListener implements Listener {

    @EventHandler
    public void onLangChange(LanguageEvent e) {
        Language lang = e.getLanguage();
        switch (lang) {
            case ENGLISH:
                lang.addLangNode(RaidLanguage.WARP_NULL, "&4Warp not found!");
                lang.addLangNode(RaidLanguage.WARP_NOT_MOVE, "&aStand still for 5 seconds.");
                lang.addLangNode(RaidLanguage.WARP_ANNOUNCE, "&aWarping you to %warp.");
                lang.addLangNode(RaidLanguage.WARP_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/warp <warp name>");
                lang.addLangNode(RaidLanguage.WARP_CREATE_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/setwarp <warp name>");
                lang.addLangNode(RaidLanguage.WARP_DELETE_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/delwarp <warp name>");
                lang.addLangNode(RaidLanguage.WARP_CLAN_CREATE_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/setclanwarp <warp name>");
                lang.addLangNode(RaidLanguage.WARP_CLAN_DELETE_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/delclanwarp <warp name>");
                lang.addLangNode(RaidLanguage.WARP_CONFIRMED, "&aWarp request received. &a&oPrepare for warp!");
                lang.addLangNode(RaidLanguage.WARP_CANCELLED, "&cYou moved! &c&oWarp cancelled.");
                lang.addLangNode(RaidLanguage.WARP_CREATED, "&7You &acreated &7the &6%warp &7warp.");
                lang.addLangNode(RaidLanguage.WARP_DELETED, "&7You &cdeleted &7the &6%warp &7warp.");
                lang.addLangNode(RaidLanguage.WARP_ALREADY_EXIST, "&cThat warp already exists.");
                lang.addLangNode(RaidLanguage.WARP_CLAN_MAX, "&cYour clan cannot make anymore warps!\n(Max. 4 warps per clan)");
                lang.addLangNode(RaidLanguage.WARP_PLAYER_MAX, "&cYou cannot make anymore private warps!\n&cIf you want to make more warps you can buy a rank at:\n&e&nhttps://www.%domain/shop");
                lang.addLangNode(RaidLanguage.WARP_LIST_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/warplist [clan : private]");
                lang.addLangNode(RaidLanguage.WARP_LIST, "&6Warp list:\n &7%list\n");
                lang.addLangNode(RaidLanguage.WARP_LIST_CLAN, "&6Clan warp list:\n &7%list\n");
                lang.addLangNode(RaidLanguage.WARP_LIST_PRIVATE, "&6Private warp list:\n &7%list\n");
                lang.addLangNode(RaidLanguage.WARP_ERROR_TOO_MANY, "&cYou have more private warps than your rank allows you to have!\nIf you want to warp again you must delete &e%amount warps &cfirst.\n&cYou can see all your private warps with: &c&o/warplist private");
                lang.addLangNode(RaidLanguage.WARP_ERROR_SPAWN, "&cYou cannot warp at spawn!");

                lang.addLangNode(RaidLanguage.TRACK_NULL, "&cYou are not standing on a tracker!");
                lang.addLangNode(RaidLanguage.TRACK_NO_COMPASS, "&cYou do not have a compass in your inventory!");
                lang.addLangNode(RaidLanguage.TRACK_NO_ENTITY_FOUND, "&cThere are no enemy players within a %radius block radius of your current location.");
                lang.addLangNode(RaidLanguage.TRACK_NOT_EQUAL, "&cThe tracker must have the same range in all 4 directions!");
                lang.addLangNode(RaidLanguage.TRACK_CONFIRMED, "&eCompass pointing at last location of %player.");

                lang.addLangNode(RaidLanguage.SPAWN_EXPIRED, "&cYou have used all your available random spawn.\n&cYou can buy a rank on our store to get more:\n&e&nhttps://www.%domain/shop");
                lang.addLangNode(RaidLanguage.SPAWN_CONFIRMED, "&a%remaining random spawns remaining!");

                lang.addLangNode(RaidLanguage.DEATH, "&b%z killed you with &c%health hearts &band &6%soups soups &bremaining.");
                break;
            case GERMAN:
                break;
        }
    }
}
