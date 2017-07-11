package com.minecraft.plugin.elite.kitpvp.listeners;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.events.stats.LevelChangeEvent;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import com.minecraft.plugin.elite.kitpvp.manager.kits.Kit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LevelUpEventListener implements Listener {

    @EventHandler
    public void onLevelUp(LevelChangeEvent e) {
        GeneralPlayer p = e.getPlayer();
        for(Kit kit : Kit.values())
            if(kit.getLevel() == e.getNewLevel())
                p.getPlayer().sendMessage(p.getLanguage().get(KitPvPLanguage.KIT_UNLOCKED)
                        .replaceAll("%kit", kit.getName()));
    }
}
