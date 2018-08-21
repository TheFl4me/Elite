package com.minecraft.plugin.elite.general;

import com.minecraft.plugin.elite.general.api.interfaces.PermissionNode;

public enum GeneralPermission implements PermissionNode {

    MODE_INVIS("mode.invis"),
    MODE_ADMIN("mode.admin"),
    MODE_WATCH("mode.watch"),
    MODE_BUILD("mode.build"),
    MODE_GAME("mode.game"),
    MODE_GAME_OTHER("mode.game.other"),
    MODE_SPY("mode.spy"),
    MODE_AFK("mode.afk"),

    JOIN_FULL("join"),

    ADMIN_SET_RANK("admin.rank"),
    ADMIN_LAG_CLEAR("admin.lag.clear"),
    ADMIN_HEAD("admin.head"),
    ADMIN_INVENTORY_SEE("admin.inventory"),
    ADMIN_LAG("admin.lag"),
    ADMIN_PING("admin.ping"),
    ADMIN_PVP_TOGGLE("admin.toggle.pvp"),
    ADMIN_SPECIAL_KIT("admin.skit"),
    ADMIN_SPEED("admin.speed"),
    ADMIN_STATUS("admin.status"),

    ANTI_HACK_BYPASS("antihack.bypass"),
    ANTI_HACK_ALERTS("antihack.alerts"),

    CHAT_TELL("chat.tell"),
    CHAT_CLEAR("chat.clear"),
    CHAT_CLEAR_BYPASS("chat.clear.bypass"),
    CHAT_LOG("chat.log"),
    CHAT_TOGGLE("chat.toggle"),
    CHAT_TOGGLE_BYPASS("chat.toggle.bypass"),
    CHAT_SAY("chat.say"),
    CHAT_SUPPORT("chat.support"),
    CHAT_SUPPORT_EXTRA("chat.support.extra"),
    CHAT_COLOR("chat.color"),
    CHAT_STAFF("chat.staff"),

    CLAN("clan"),
    CLAN_ADMIN("clan.admin"),

    PARTY("party"),

    PUNISH("punish"),
    PUNISH_INFO_CHECK("punish.info.check"),
    PUNISH_INFO_IP("punish.info.ip"),
    PUNISH_REPORT_CLEAR("punish.report.clear"),
    PUNISH_REPORT("punish,report"),
    PUNISH_REPORT_LIST("punish.list"),
    PUNISH_KICK("punish.kick"),
    PUNISH_BAN("punish.ban"),
    PUNISH_UNBAN("punish.unban"),
    PUNISH_UNMUTE("punish.unmute"),

    SPAWN("spawn"),
    SPAWN_SET("spawn.set"),

    TIME_SET("time"),

    AGREE("agree"),
    CLEAR("clear"),
    CLEAR_OTHER("clear.other"),
    HELP("help"),
    LANGUAGE("language"),
    STATS("stats"),
    SUICIDE("suicide"),

    KIT_FREE("kit.free"),
    KIT("kit"),
    KIT_INFO("kit.info");

    private String string;

    GeneralPermission(String string) {
        this.string = "general." + string;
    }

    public String toString() {
        return this.string;
    }
}
