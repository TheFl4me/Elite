package com.minecraft.plugin.elite.general.api.special.skit;

import org.bukkit.inventory.ItemStack;

public class SKit {

    private String name;
    private ItemStack[] contents;

    public SKit(String name, ItemStack[] contents) {
        this.name = name;
        this.contents = contents;
        SKitManager.add(this);
    }

    public void delete() {
        SKitManager.remove(this);
    }

    public String getName() {
        return this.name;
    }

    public ItemStack[] getContents() {
        return this.contents;
    }
}