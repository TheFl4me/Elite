package com.minecraft.plugin.elite.general.listeners;

import com.minecraft.plugin.elite.general.api.ePlayer;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenSignEditor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.lang.reflect.Field;

public class SignEventsListener implements Listener {

    @EventHandler
    public void addSignColor(SignChangeEvent e) {
        ePlayer p = ePlayer.get(e.getPlayer());
        if (p.getPlayer().hasPermission("egeneral.signcolor"))
            for (int i = 0; i < 4; i++)
                e.setLine(i, ChatColor.translateAlternateColorCodes('&', e.getLine(i)));
    }

    @EventHandler
    public void clickOnSign(PlayerInteractEvent e) {
        ePlayer p = ePlayer.get(e.getPlayer());
        if((p.isBuilding())) {
            if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Block block = e.getClickedBlock();
                if(block.getType() == Material.SIGN || block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
                    Sign s = (Sign) e.getClickedBlock().getState();
                    try {
                        Field field = s.getClass().getDeclaredField("sign");
                        field.setAccessible(true);

                        Object tileEntity = field.get(s);
                        Field editable = tileEntity.getClass().getDeclaredField("isEditable");
                        editable.set(tileEntity, true);

                        Field owner = tileEntity.getClass().getDeclaredField("h");
                        owner.setAccessible(true);
                        owner.set(tileEntity, ((CraftPlayer) p.getPlayer()).getHandle());

                        BlockPosition pos = new BlockPosition(s.getX(), s.getY(), s.getZ());
                        Packet packet = new PacketPlayOutOpenSignEditor(pos);
                        ((CraftPlayer) p.getPlayer()).getHandle().playerConnection.sendPacket(packet);
                    } catch(Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }
}