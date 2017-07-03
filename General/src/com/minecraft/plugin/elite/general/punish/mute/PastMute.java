package com.minecraft.plugin.elite.general.punish.mute;

import com.minecraft.plugin.elite.general.punish.PastPunishment;
import com.minecraft.plugin.elite.general.punish.PunishReason;

import java.util.UUID;

public class PastMute extends PastPunishment {

    public PastMute(UUID id, UUID target, PunishReason reason, String details, long date, long time, String punisher, String pardoner, long pardonDate) {
        super(id, target, reason, details, date, time, punisher, pardoner, pardonDate);
    }
}
