package com.minecraft.plugin.elite.general.api.special.supportchat;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

public class SupportChatManager {

    private static Collection<SupportChat> chat = new HashSet<>();
    private static Collection<UUID> requests = new ArrayList<>();

    public static SupportChat get(GeneralPlayer p) {
        for(SupportChat c : chat) {
            if(c.getPlayer().getUniqueId().equals(p.getUniqueId()) || c.getStaff().getUniqueId().equals(p.getUniqueId()))
                return c;
        }
        return null;
    }

    public static void sendRequest(GeneralPlayer p) {
        requests.add(p.getUniqueId());
        p.sendMessage(GeneralLanguage.SUPPORT_REQUEST_SENT);
        for(Player staffs : Bukkit.getOnlinePlayers()) {
            GeneralPlayer staff = GeneralPlayer.get(staffs);
            if(staff.getPlayer().hasPermission("egeneral.support.extra"))
                staff.getPlayer().sendMessage(staff.getLanguage().get(GeneralLanguage.SUPPORT_REQUEST_STAFF).replaceAll("%p", p.getName()));
        }
        Bukkit.getScheduler().runTaskLater(General.getPlugin(), () -> removeRequest(p), 6000L);
    }

    public static Collection<GeneralPlayer> getRequests() {
        return requests.stream().map(GeneralPlayer::get).collect(Collectors.toList());
    }

    public static boolean hasSentRequest(GeneralPlayer p) {
        return requests.contains(p.getUniqueId());
    }

    public static void removeRequest(GeneralPlayer p) {
        requests.remove(p.getUniqueId());
    }

    public static void add(SupportChat sChat) {
        chat.add(sChat);
    }

    public static void remove(SupportChat sChat) {
        chat.remove(sChat);
    }
}
