package com.minecraft.plugin.elite.general.punish.mute;

import com.minecraft.plugin.elite.general.punish.PastPunishment;
import com.minecraft.plugin.elite.general.punish.PunishReason;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class PastMute extends PastPunishment {

    public PastMute(UUID id, OfflinePlayer target, PunishReason reason, String details, long date, long time, String punisher, String pardoner, long pardonDate) {
        super(id, target, reason, details, date, time, punisher, pardoner, pardonDate);
    }
}
