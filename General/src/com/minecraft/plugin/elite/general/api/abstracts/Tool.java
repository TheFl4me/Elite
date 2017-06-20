package com.minecraft.plugin.elite.general.api.abstracts;

import com.minecraft.plugin.elite.general.api.Server;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class Tool {

    private String name;
    private ItemStack item;
    private int slot;

    public Tool(String name, Material mat, int slot) {
        this.name = name;
        this.slot = slot;

        Server server = Server.get();
        ItemStack item = new ItemStack(mat);
        server.rename(item, this.getName());
        this.item = item;
    }

    public String getName() {
        return this.name;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public int getSlot() {
        return this.slot;
    }
}