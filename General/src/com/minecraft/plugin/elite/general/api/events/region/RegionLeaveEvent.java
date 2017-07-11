package com.minecraft.plugin.elite.general.api.events.region;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RegionLeaveEvent extends RegionChangeEvent {

    public RegionLeaveEvent(GeneralPlayer p, ProtectedRegion oldRegion) {
        super(p, oldRegion);
    }
}
