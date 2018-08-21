package com.minecraft.plugin.elite.survivalgames.listeners;

import com.minecraft.plugin.elite.survivalgames.manager.AirDrop;
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
                        Random r = new Random();
                        for(BlockFace face : faces) {
                            BlockState blocks = chest.getBlock().getRelative(face).getState();
                            if(blocks instanceof Chest) {
                                nextChest = (Chest) blocks;
                                break;
                            }
                        }
                        if (!arena.getLoadedChests().contains(chest)) {
                            for (AirDrop drop : arena.getAirDrops()) {
                                if(drop.getChestBlocks().contains(e.getClickedBlock())) {
                                    arena.getLoadedChests().add(chest);
                                    chest.getInventory().clear();
                                    for(int i = 0; i < 1 + r.nextInt(2); i++)
                                        ChestItem.setRandom(chest.getInventory(), true);
                                    return;
                                }
                            }
                            if(nextChest != null) {
                                if(!arena.getLoadedChests().contains(nextChest) && chest.getInventory().getHolder() instanceof DoubleChest) {
                                    DoubleChest doubleChest = (DoubleChest) chest.getInventory().getHolder();
                                    arena.getLoadedChests().add(chest);
                                    arena.getLoadedChests().add(nextChest);
                                    doubleChest.getInventory().clear();
                                    for(int i = 0; i < 7 + r.nextInt(3); i++)
                                        ChestItem.setRandom(doubleChest.getInventory(), false);
                                }
                            } else {
                                arena.getLoadedChests().add(chest);
                                chest.getInventory().clear();
                                for(int i = 0; i < 3 + r.nextInt(2); i++)
                                    ChestItem.setRandom(chest.getInventory(), false);
                            }
                        }
                    }
                }
            }
        }
    }
}
