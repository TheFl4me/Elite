package com.minecraft.plugin.elite.survivalgames.listeners;

import com.minecraft.plugin.elite.survivalgames.manager.ChestItem;
import com.minecraft.plugin.elite.survivalgames.manager.Lobby;
import com.minecraft.plugin.elite.survivalgames.manager.arena.Arena;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ChestFillEventListener implements Listener {

    @EventHandler
    public void onOpenChest(PlayerInteractEvent e) {
        Lobby lobby = Lobby.get();
        if(!lobby.isActive()) {
            Arena arena = lobby.getArena();
            if(arena != null) {
                if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    BlockState block = e.getClickedBlock().getState();
                    if(block instanceof Chest) {
                        List<BlockFace> faces = Arrays.asList(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);
                        Chest chest = (Chest) block;
                        Chest nextChest = null;
                        for(BlockFace face : faces) {
                            BlockState blocks = chest.getBlock().getRelative(face).getState();
                            if(blocks instanceof Chest) {
                                nextChest = (Chest) blocks;
                                break;
                            }
                        }
                        Random r = new Random();
                        if(nextChest != null) {
                            if(!arena.getLoadedChests().contains(chest) && !arena.getLoadedChests().contains(nextChest) && chest.getInventory().getHolder() instanceof DoubleChest) {
                                DoubleChest doubleChest = (DoubleChest) chest.getInventory().getHolder();
                                arena.addChest(chest);
                                arena.addChest(nextChest);
                                doubleChest.getInventory().clear();
                                for(int i = 0; i < 10 - r.nextInt(3); i++)
                                    ChestItem.setRandom(doubleChest.getInventory());
                            }
                        } else if(!arena.getLoadedChests().contains(chest)) {
                            arena.addChest(chest);
                            chest.getInventory().clear();
                            for(int i = 0; i < 5 - r.nextInt(2); i++)
                                ChestItem.setRandom(chest.getInventory());
                        }
                    }
                }
            }
        }
    }
}
