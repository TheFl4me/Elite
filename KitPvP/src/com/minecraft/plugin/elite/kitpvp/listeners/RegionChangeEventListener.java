package com.minecraft.plugin.elite.kitpvp.listeners;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.events.region.RegionEnterEvent;
import com.minecraft.plugin.elite.general.api.events.region.RegionLeaveEvent;
import com.minecraft.plugin.elite.general.api.special.menu.MenuTool;
import com.minecraft.plugin.elite.kitpvp.KitPvP;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import com.minecraft.plugin.elite.kitpvp.manager.KitPlayer;
import com.minecraft.plugin.elite.kitpvp.manager.duel.custom.CustomDuelSelector;
import com.minecraft.plugin.elite.kitpvp.manager.duel.normal.NormalDuelSelector;
import com.minecraft.plugin.elite.kitpvp.manager.duel.tools.DuelTool;
import com.minecraft.plugin.elite.kitpvp.manager.duel.tools.SpawnTool;
import com.minecraft.plugin.elite.kitpvp.manager.kits.KitSelectorTool;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RegionChangeEventListener implements Listener {

    @EventHandler
    public void onSpawnLeave(RegionLeaveEvent e) {
        if(e.getRegion().getId().equalsIgnoreCase(KitPvP.REGION_SPAWN)) {
            ePlayer p = e.getPlayer();
            if(!p.isAdminMode() && !p.isWatching()) {
                if(p.hasTool())
                    p.clearTools();
                p.sendMessage(KitPvPLanguage.REGION_SPAWN_LEAVE);
            }
        }
    }

    @EventHandler
    public void onSpawnEnter(RegionEnterEvent e) {
        if(e.getRegion().getId().equalsIgnoreCase(KitPvP.REGION_SPAWN)) {
            ePlayer p = e.getPlayer();
            p.getPlayer().setFoodLevel(20);
            if(p.hasTool())
                p.clearTools();
            p.giveTool(new MenuTool(p.getLanguage()));
            p.giveTool(new DuelTool(p.getLanguage()));
            if(!p.isAdminMode() && !p.isWatching()) {
                p.giveTool(new KitSelectorTool(p.getLanguage()));
            }
        }
    }

    @EventHandler
    public void onDuelEnter(RegionEnterEvent e) {
        if(e.getRegion().getId().equalsIgnoreCase(KitPvP.REGION_DUEL)) {
            ePlayer p = e.getPlayer();
            p.getPlayer().setFoodLevel(20);
            if(p.hasTool())
                p.clearTools();
            p.giveTool(new MenuTool(p.getLanguage()));
            p.giveTool(new SpawnTool(p.getLanguage()));
            if(!p.isAdminMode() && !p.isWatching()) {
                p.giveTool(new CustomDuelSelector(p.getLanguage()));
                p.giveTool(new NormalDuelSelector(p.getLanguage()));
            }
        }
    }

    @EventHandler
    public void onEnterArena(RegionEnterEvent e) {
        if(!e.getPlayer().isAdminMode() && !e.getPlayer().isWatching()) {
            KitPlayer kp = KitPlayer.get(e.getPlayer().getUniqueId());
            if(e.getRegion().getId().equalsIgnoreCase(KitPvP.REGION_EHG)) {
                kp.giveKitInv(false);
            } else if(e.getRegion().getId().equalsIgnoreCase(KitPvP.REGION_FEAST)) {
                kp.giveKitInv(true);
            }
        }
    }
}
