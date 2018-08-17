package com.minecraft.plugin.elite.general.antihack.hacks;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.antihack.hacks.combat.AutoClickHack;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerClick {

    private GeneralPlayer player;
    private double timestamp;
    private boolean isValid;

    public static void storeHitClick(PlayerInteractEvent e) {
        if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            GeneralPlayer p = GeneralPlayer.get(e.getPlayer().getUniqueId());

            PlayerClick click = new PlayerClick(p);

            if (click.isValid())
                AutoClickHack.check(click);
        }
    }

    public static void storeInvClick(InventoryClickEvent e) {
        if(e.getClick() == ClickType.LEFT || e.getClick() == ClickType.SHIFT_LEFT) {
            GeneralPlayer p = GeneralPlayer.get(e.getWhoClicked().getUniqueId());
            if(p.canBypassChecks())
                return;

            PlayerClick click = new PlayerClick(p);

            if (click.isValid())
                AutoClickHack.check(click);
        }
    }

    public PlayerClick(GeneralPlayer p) {
        this.player = p;
        this.timestamp = System.currentTimeMillis();
        this.isValid = true;
        this.getPlayer().getClicks()[this.getPlayer().getClickCount() % this.getPlayer().getClicks().length] = this;
        this.getPlayer().setClickCount(this.getPlayer().getClickCount() + 1);

        Server server = Server.get();
        if(server.isLagging() || p.isLagging() || !p.isValid() || p.canBypassChecks())
            this.isValid = false;
    }

    public GeneralPlayer getPlayer() {
        return this.player;
    }

    public double getTime() {
        return this.timestamp;
    }

    public boolean isValid() {
        return this.isValid;
    }
}
