package com.minecraft.plugin.elite.nohax.listeners;

import com.minecraft.plugin.elite.general.api.abstracts.Tool;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.enums.Rank;
import com.minecraft.plugin.elite.general.api.events.tool.ToolClickEvent;
import com.minecraft.plugin.elite.nohax.NoHax;
import com.minecraft.plugin.elite.nohax.manager.ForceFieldCheckTool;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class ForceFieldCheckToolEventListener implements Listener {

    HashMap<UUID, BukkitRunnable> checking = new HashMap<>();

    @EventHandler
    public void onToolClick(ToolClickEvent e) {
        ePlayer p = e.getPlayer();
        Tool tool = e.getTool();
        if(tool instanceof ForceFieldCheckTool) {
            if(p.isAdminMode()) {
                final Rank invisTo = p.getInvisTo();
                p.setInvis(false);
                checking.put(p.getUniqueId(), new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(p != null)
                            p.setInvisTo(invisTo);
                        cancel();
                        checking.remove(p.getUniqueId());
                    }
                });
                checking.get(p.getUniqueId()).runTaskLater(NoHax.getPlugin(), 10);
            }
        }
    }
}
