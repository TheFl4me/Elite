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

                lang.addLangNode(KitPvPLanguage.DUEL_REQUEST_RECEIVED, "&a%p has asked you for a duel!\nRight click him with the &bDuel Selector &ato accept the request.");
                lang.addLangNode(KitPvPLanguage.DUEL_REQUEST_SENT, "&aYou asked %z for a duel!");
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
                lang.addLangNode(KitPvPLanguage.DUEL_GUI_RECRAFT, "Recraft");

                lang.addLangNode(KitPvPLanguage.DUEL_TOOL, "&bDuel Selector");

                lang.addLangNode(KitPvPLanguage.DEATH, "&b%z[&7%kit&b] killed you with &c%health hearts &band &6%soups soups &bremaining.");
                lang.addLangNode(KitPvPLanguage.KILL_STREAK, "&c%p is now on a &6%streak &ckillstreak!");

                lang.addLangNode(KitPvPLanguage.KIT_UNLOCKED, "&7You can now buy the &6%kit &7kit.");

                lang.addLangNode(KitPvPLanguage.KIT_GIVE, "&aYou are now a %kit.");
                lang.addLangNode(KitPvPLanguage.KIT_NOPERM, "&cYou do not own this kit!\n&cDo &f/kit &cto see all kits.");
                lang.addLangNode(KitPvPLanguage.KIT_ERROR_ALREADY, "&cYou already have a kit!");
                lang.addLangNode(KitPvPLanguage.KIT_ERROR_NULL, "&cThis kit does not exist.");
                lang.addLangNode(KitPvPLanguage.KIT_ERROR_MODE, "&4You cannot choose a kit whilst you are in ADMIN/WATCH/INVIS mode!");
                lang.addLangNode(KitPvPLanguage.KIT_ERROR_LOCKED, "&cThis kit is locked until you reach level %level!");
                lang.addLangNode(KitPvPLanguage.KIT_COOLDOWN, "&cYou are still on cooldown for another %seconds seconds!");

                lang.addLangNode(KitPvPLanguage.KIT_INFO, "&7%z is using the %kit kit.");
                lang.addLangNode(KitPvPLanguage.KIT_INFO_NONE, "&c%z is not using any kit.");
                lang.addLangNode(KitPvPLanguage.KIT_INFO_USAGE, lang.get(GeneralLanguage.SYNTAX) + "/kitinfo <player>");

                lang.addLangNode(KitPvPLanguage.KIT_FREE_TRUE, "&c&l&kAAA&r&bAll kits are now &b&oFREE&r&b!&r&c&l&kAAA");
                lang.addLangNode(KitPvPLanguage.KIT_FREE_FALSE, "&cAll kits are no longer free.");

                lang.addLangNode(KitPvPLanguage.PHANTOM_WARNING, "&l%p is not fly hacking!\n&lHe is using the Phantom kit!");
                lang.addLangNode(KitPvPLanguage.PHANTOM_FLIGHT_TIME, "&c%seconds seconds of flight remaining.");
                lang.addLangNode(KitPvPLanguage.ENDERMAGE_WARNING, "&cYou have been &c&lENDERMAGED&r&c!\n&cYou have 5 seconds of invincibility.");
                lang.addLangNode(KitPvPLanguage.ROGUE_BLOCKING, "&cIt seems that a nearby player is disabling your kit ability...");

                lang.addLangNode(KitPvPLanguage.TITAN_NEED_CHARGE, "&cTitan mode needs to be charged for 30 seconds before it can be used again.");
                lang.addLangNode(KitPvPLanguage.TITAN_CHARGE_START, "&aTitan mode is charging...");
                lang.addLangNode(KitPvPLanguage.TITAN_CHARGE_COMPLETE, "&aTitan mode is now fully charged again!");
                lang.addLangNode(KitPvPLanguage.TITAN_CHARGE_ABORT, "&cTitan mode charging has been aborted.");
                lang.addLangNode(KitPvPLanguage.TITAN_ENABLE, "&6You are now in Titan mode!");
                lang.addLangNode(KitPvPLanguage.TITAN_ALREADY, "&cYou are already in Titan mode!");
                lang.addLangNode(KitPvPLanguage.TITAN_DISABLE, "&cTitan mode has been used up...");
                lang.addLangNode(KitPvPLanguage.TITAN_HIT, "&cYou are fighting a Titan.. RUN AWAY!");

                lang.addLangNode(KitPvPLanguage.REPULSE_LVL, "&7Repulse Level: &6%lvl");
                lang.addLangNode(KitPvPLanguage.REPULSE_REPULSED, "&cAn invisible force has repulsed you!");

                lang.addLangNode(KitPvPLanguage.GLADIATOR_START, "&aYou have been forced into a shadow game by a Gladiator!");

                lang.addLangNode(KitPvPLanguage.KIT_GUI_BACK, "&aBack");
                lang.addLangNode(KitPvPLanguage.KIT_GUI_ABILITY, "&aAbility\n%ability");
                lang.addLangNode(KitPvPLanguage.KIT_GUI_DESCRIPTION, "&eDescription\n%description");
                lang.addLangNode(KitPvPLanguage.KIT_GUI_ITEMS, "&fStarting items");

                lang.addLangNode(KitPvPLanguage.KIT_GUI_SELECTOR_TITLE, "&cKit Selector");
                lang.addLangNode(KitPvPLanguage.KIT_GUI_SELECTOR_LOCKED, "&7Unlock at level %level");
                lang.addLangNode(KitPvPLanguage.KIT_GUI_SELECTOR_SELECT, "&cPlay with this kit");

                lang.addLangNode(KitPvPLanguage.KIT_GUI_SHOP_TITLE, "&6Shop");
                lang.addLangNode(KitPvPLanguage.KIT_GUI_SHOP_INFERIOR_LEVEL, "&4LEVEL TOO LOW\n" +
                        " \n" +
                        "&7This kit can only be bought\n" +
                        "&7at level &e%level &7if using normal tokens.");
                lang.addLangNode(KitPvPLanguage.KIT_GUI_SHOP_BUY_NORMAL_TOKEN, "&aBUY KIT\n" +
                        " \n" +
                        "&bPrice: &6%price &btoken(s)\n" +
                        " \n" +
                        "&bYou have &a%tokens &btoken(s)");
                lang.addLangNode(KitPvPLanguage.KIT_GUI_SHOP_INFERIOR_TOKENS, "&cNOT ENOUGH TOKENS\n" +
                        " \n" +
                        "&bPrice: &6%price &btoken(s)\n" +
                        " \n" +
                        "&bYou have &c%tokens &btoken(s)");
                lang.addLangNode(KitPvPLanguage.KIT_GUI_SHOP_BUY_PRESTIGE_TOKEN, "&aPermanently unlock\n" +
                        " \n" +
                        "&cPrice: &61 prestige token\n" +
                        " \n" +
                        "&cYou have &b%prestigetokens &cprestige token(s)");

                lang.addLangNode(KitPvPLanguage.KIT_BACKUP_ITEM, "&aBackup");
                lang.addLangNode(KitPvPLanguage.KIT_KANGAROO_ITEM, "&6Double Jump");
                lang.addLangNode(KitPvPLanguage.KIT_THOR_ITEM, "Mjolnir");
                lang.addLangNode(KitPvPLanguage.KIT_ENDERMAGE_ITEM, "&5Portal");
                lang.addLangNode(KitPvPLanguage.KIT_REAPER_ITEM, "&0Death Scythe");
                lang.addLangNode(KitPvPLanguage.KIT_GRANDPA_ITEM, "Big Bertha");
                lang.addLangNode(KitPvPLanguage.KIT_TITAN_ITEM, "&aTitan Mode");
                lang.addLangNode(KitPvPLanguage.KIT_GLADIATOR_ITEM, "&cShadow Game");
                lang.addLangNode(KitPvPLanguage.KIT_REPULSE_ITEM, "&cAlmighty Push");

                lang.addLangNode(KitPvPLanguage.KIT_PVP_DESCRIPTION, "Gain 20% more exp for every kill");
                lang.addLangNode(KitPvPLanguage.KIT_PVP_ABILITY, "- 20% more kill exp");
                lang.addLangNode(KitPvPLanguage.KIT_ARCHER_DESCRIPTION, "Whenever you hit someone with an arrow\nyou will get the arrow back.");
                lang.addLangNode(KitPvPLanguage.KIT_ARCHER_ABILITY, "- +1 Arrow for every arrow hit");
                lang.addLangNode(KitPvPLanguage.KIT_BACKUP_DESCRIPTION, "You can choose a kit whenever you want\nby clicking the book.");
                lang.addLangNode(KitPvPLanguage.KIT_BACKUP_ABILITY, "- Open another Kit GUI");
                lang.addLangNode(KitPvPLanguage.KIT_FISHERMAN_DESCRIPTION, "Whenever you wield someone in with your\nfishing rod they will be teleported to you");
                lang.addLangNode(KitPvPLanguage.KIT_FISHERMAN_ABILITY, "- Teleport other players to you");
                lang.addLangNode(KitPvPLanguage.KIT_KANGAROO_DESCRIPTION, "You can make really high jumps\nby clicking the rocket, or you can\nmake big jumps forward when sneaking\nand clicking the rocket at the same time.");
                lang.addLangNode(KitPvPLanguage.KIT_KANGAROO_ABILITY, "- Double jump upwards\n- Double jump forwards");
                lang.addLangNode(KitPvPLanguage.KIT_PHANTOM_DESCRIPTION, "You can fly for 5 seconds when clicking\nthe feather in your inventory.\nThis kit has a 60 second cooldown\nafter each use. You also have\nleather armor whilst flying");
                lang.addLangNode(KitPvPLanguage.KIT_PHANTOM_ABILITY,"- Temporary flying\n- Leather armor");
                lang.addLangNode(KitPvPLanguage.KIT_THOR_DESCRIPTION, "Whenever you click a block with your\nwooden axe, a lightning will appear\non the highest block of the\nclicked block location.");
                lang.addLangNode(KitPvPLanguage.KIT_THOR_ABILITY, "- Summon thunder\n- Immune to thunder");
                lang.addLangNode(KitPvPLanguage.KIT_TURTLE_DESCRIPTION, "While sneaking you can only take a\nmaximum amount of 1 heart per damage.\nIf you sneak and block with your\nsword at the same time you will\nonly take a maximum amount of\n0.5 hearts per damage.\nYou cannot hit whilst sneaking.");
                lang.addLangNode(KitPvPLanguage.KIT_TURTLE_ABILITY, "- Max 1 heart damage when sneaking\n- Max 1/2 heart damage when sneaking + blocking sword");
                lang.addLangNode(KitPvPLanguage.KIT_ANCHOR_DESCRIPTION, "You will not receive knockback\nwhen hit by another player\n, and they wont get any knockback\nether when you hit them");
                lang.addLangNode(KitPvPLanguage.KIT_ANCHOR_ABILITY, "- Receive no knockback\n- give no knockback");
                lang.addLangNode(KitPvPLanguage.KIT_VIPER_DESCRIPTION, "Whenever you hit a player you\nhave a 33% chance of giving them\npoison 2 for 3 seconds");
                lang.addLangNode(KitPvPLanguage.KIT_VIPER_ABILITY, "- Give poison 2 to other players");
                lang.addLangNode(KitPvPLanguage.KIT_SNAIL_DESCRIPTION, "Whenever you hit a player you\nhave a 33% chance of giving them\nslowness 2 for 5 seconds");
                lang.addLangNode(KitPvPLanguage.KIT_SNAIL_ABILITY,"- Give slowness 2 to other players");
                lang.addLangNode(KitPvPLanguage.KIT_STOMPER_DESCRIPTION, "Whenever you land close to another player,\nyour fall damage amount will be transferred\nto them.\nYou will receive max. 2 hearts fall damage");
                lang.addLangNode(KitPvPLanguage.KIT_STOMPER_ABILITY, "- Transfer fall damage\n- Max. 2 hearts fall damage");
                lang.addLangNode(KitPvPLanguage.KIT_ENDERMAGE_DESCRIPTION, "You can teleport player above and/or beneath\nyou to a specified location.");
                lang.addLangNode(KitPvPLanguage.KIT_ENDERMAGE_ABILITY, "- Teleport players & yourself to a location");
                lang.addLangNode(KitPvPLanguage.KIT_ROGUE_DESCRIPTION, "Cast a deadly sphere in which\nall kits will be disabled.");
                lang.addLangNode(KitPvPLanguage.KIT_ROGUE_ABILITY, "- Kits will not work within a 10 block radius of you.");
                lang.addLangNode(KitPvPLanguage.KIT_NEO_DESCRIPTION, "Projectiles have zero effect on you,\nwhoever dares throwing one at you\nwill be hit back by their own power.");
                lang.addLangNode(KitPvPLanguage.KIT_NEO_ABILITY, "- Projectiles have no effect on you\n- Projectiles will bounce of you at the same velocity with which they hit you");
                lang.addLangNode(KitPvPLanguage.KIT_MONK_DESCRIPTION, "Create panic and confusion amongst\nyour enemies to slay them.");
                lang.addLangNode(KitPvPLanguage.KIT_MONK_ABILITY, "- Switch the item which your enemy is holding in his hand\n- 5 second cooldown");
                lang.addLangNode(KitPvPLanguage.KIT_SURPRISE_DESCRIPTION, "Infinite and uncontrollable power is your motto.");
                lang.addLangNode(KitPvPLanguage.KIT_SURPRISE_ABILITY, "- Get a random kit (including kits which you do not own)");
                lang.addLangNode(KitPvPLanguage.KIT_POSEIDON_DESCRIPTION, "Fear the god of the 7 seas!\nAll whom dare attack you in your homeland\nwill be slain without mercy!");
                lang.addLangNode(KitPvPLanguage.KIT_POSEIDON_ABILITY, "- Strength 2 whilst in water\n- Speed 2 whilst in water\n- Regeneration 2 whilst in Water\n- Water breathing whilst in water");
                lang.addLangNode(KitPvPLanguage.KIT_NINJA_DESCRIPTION, "Attack your enemies from the shadows\nand they will never know what hit them");
                lang.addLangNode(KitPvPLanguage.KIT_NINJA_ABILITY, "- 10 second window after hitting an enemy\n  in which you can sneak to teleport to them.\n- 10 second cooldown after each teleport");
                lang.addLangNode(KitPvPLanguage.KIT_SWITCHER_DESCRIPTION, "The ultimate trapping kit!\nIf you like camping + sneaking on a tower\nto kill your enemy, then this\nkit was made for you!");
                lang.addLangNode(KitPvPLanguage.KIT_SWITCHER_ABILITY, "- Throw a snowball on a player to switch position with him\n- 10 second cooldown after every throw attempt");
                lang.addLangNode(KitPvPLanguage.KIT_MAGMA_DESCRIPTION, "Become the most feared pyro-technician!");
                lang.addLangNode(KitPvPLanguage.KIT_MAGMA_ABILITY, "- 33% chance of setting a player on fire");
                lang.addLangNode(KitPvPLanguage.KIT_REAPER_DESCRIPTION, "You literally become DEATH\nA single touch of your scythe is enough\nto make a player wither away.");
                lang.addLangNode(KitPvPLanguage.KIT_REAPER_ABILITY, "- Give Wither 2 effect to players with your scythe.");
                lang.addLangNode(KitPvPLanguage.KIT_FROSTY_DESCRIPTION, "(Song)\n\nFrosty the Snowman\nWas a jolly happy soul\nWith a corncob pipe and a button nose\nAnd two eyes made out of coal[..]");
                lang.addLangNode(KitPvPLanguage.KIT_FROSTY_ABILITY, "- Speed 4 when on snow\n- Regeneration 1 when on snow");
                lang.addLangNode(KitPvPLanguage.KIT_GRANDPA_DESCRIPTION, "Did you ever want to become a old bag of bones,\nyell at kits running on your lawn,\nand have a cane that can whoop some serious a**?\nThen this is THE kit for you.");
                lang.addLangNode(KitPvPLanguage.KIT_GRANDPA_ABILITY, "- Get a stick with Knockback 2");
                lang.addLangNode(KitPvPLanguage.KIT_TITAN_DESCRIPTION, "The overlord of all\nmystical creatures. Nothing can damage you\nwhen you go full on Titan.");
                lang.addLangNode(KitPvPLanguage.KIT_TITAN_ABILITY, "- Don't receive any damage for 10 seconds\n- Sneak for 30 seconds to charge up again\n- You cannot hit entities whilst charging up");
                lang.addLangNode(KitPvPLanguage.KIT_TANK_DESCRIPTION, "F**k those teaming a**h*les!\nSeriously, F*CK'EM UP!\nWith this Kit you can.");
                lang.addLangNode(KitPvPLanguage.KIT_TANK_ABILITY, "- Immune to all explosive damage\n- Whenever you kill someone they explode");
                lang.addLangNode(KitPvPLanguage.KIT_BERSERKER_DESCRIPTION, "The feeling of joy mixed with rage\nwhenever you kill a player in team\nhas never been this strong.");
                lang.addLangNode(KitPvPLanguage.KIT_BERSERKER_ABILITY, "- Get Strength 2 for 2 seconds after killing a player\n- Get Nausea 4 for 2 seconds after killing a player");
                lang.addLangNode(KitPvPLanguage.KIT_HULK_DESCRIPTION, "Take my word for it\nwhen I tell you that there isn't\na better trolling kit out there.");
                lang.addLangNode(KitPvPLanguage.KIT_HULK_ABILITY, "- Pick up a player and carry them around\n- Throw away players which you picked up");
                lang.addLangNode(KitPvPLanguage.KIT_RAIJIN_DESCRIPTION, "Throw you arrows wherever you want\nto teleport to in an instant!");
                lang.addLangNode(KitPvPLanguage.KIT_RAIJIN_ABILITY, "- Throw arrows\n- Sneak to teleport to your last thrown arrow\n- 10 second cooldown after throwing an arrow");
                lang.addLangNode(KitPvPLanguage.KIT_GLADIATOR_DESCRIPTION, "Divide to concur has never been this true.\nDestroy teams by taking them on one by one.");
                lang.addLangNode(KitPvPLanguage.KIT_GLADIATOR_ABILITY, "- Teleport your self and another player\n  into a glass cage at Y = 120\n- 5 seconds of damage resistance after you win the duel");
                break;
            case GERMAN:
                break;
        }
    }
}
