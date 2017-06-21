package com.minecraft.plugin.elite.general.api.events.region;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RegionEnterEvent extends RegionChangeEvent {

    public RegionEnterEvent(ePlayer p, ProtectedRegion newRegion) {
        super(p, newRegion);
    }
}
