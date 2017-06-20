package com.minecraft.plugin.elite.survivalgames.manager.alive;

import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.abstracts.GUI;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.survivalgames.SurvivalGamesLanguage;
import com.minecraft.plugin.elite.survivalgames.manager.Lobby;
import com.minecraft.plugin.elite.survivalgames.manager.arena.Arena;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerGUI extends GUI {

    public PlayerGUI(Language lang) {
        super(lang);
    }

    private ItemStack glass() {
        Server server = Server.get();
        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE);
        server.rename(glass, " ");
        return glass;
    }

    public Inventory main(int page) {
        List<UUID> players = new ArrayList<>();
        HashMap<UUID, Integer> kills = new HashMap<>();
        Lobby lobby = Lobby.get();
        if(!lobby.isActive()) {
            Arena arena = lobby.getArena();
            for(ePlayer p : arena.getPlayers()) {
                players.add(p.getUniqueId());
                kills.put(p.getUniqueId(), arena.getKills(p));
            }
        } else {
            lobby.getPlayers().forEach((p) -> players.add(p.getUniqueId()));
        }
        Server server = Server.get();
        this.buildPageType(SurvivalGamesLanguage.PLAYERS_GUI_TITLE, page, players.size(), this.glass());
        for(int i = this.getLastSlot(page); i < this.getNextSlot(page); i++) {
            if(i >= players.size())
                break;
            UUID uuid = players.get(i);
            ePlayer p = ePlayer.get(uuid);
            ItemStack head = server.playerHead(p.getName());
            server.rename(head, this.getLanguage().get(SurvivalGamesLanguage.PLAYERS_GUI_HEAD).replaceAll("%p", p.getChatName()).replaceAll("%kills", (lobby.isActive() ? "0" : Integer.toString(kills.get(p.getUniqueId())))));
            this.getInventory().addItem(head);
        }
        this.getInventory().setItem(4, this.glass());
        return this.getInventory();
    }
}
