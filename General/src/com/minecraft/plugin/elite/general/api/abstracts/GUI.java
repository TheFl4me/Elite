package com.minecraft.plugin.elite.general.api.abstracts;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.api.interfaces.LanguageNode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class GUI {

    private Inventory inv;
    private Language lang;
    private int pages;
    private int availableSlots;

    public GUI(Language lang) {
        this.lang = lang;
        this.pages = 1;
    }

    public void fill(ItemStack content) {
        for(int i = 0; i < this.getInventory().getSize(); i++)
            this.getInventory().setItem(i, content);
    }

    private ItemStack back() {
        Server server = Server.get();
        ItemStack back = new ItemStack(Material.SUGAR);
        server.rename(back, this.getLanguage().get(GeneralLanguage.GUI_BACK));
        return back;
    }

    private ItemStack page(int page) {
        Server server = Server.get();
        ItemStack carpet = new ItemStack(Material.PAPER);
        server.rename(carpet, ChatColor.GOLD + Integer.toString(page));
        return carpet;
    }

    public void setInventory(Inventory inv) {
        this.inv = inv;
    }

    public Inventory getInventory() {
        return this.inv;
    }

    public void setLanguage(Language lang) {
        this.lang = lang;
    }

    public Language getLanguage() {
        return this.lang;
    }

    public int getPages() {
        return this.pages;
    }

    public int getAvailableSlots() {
        return this.availableSlots;
    }

    public int getLastSlot(int page) {
        return this.getAvailableSlots() * (page - 1);
    }

    public int getNextSlot(int page) {
        return this.getAvailableSlots() * page;
    }

    public void buildPageType(LanguageNode title, int page, int pageSize, ItemStack filler) {
        this.buildPageType(this.getLanguage().get(title), page, pageSize, filler);
    }

    public void buildPageType(String title, int page, int pageSize, ItemStack filler) {
        int size = 54;
        this.availableSlots = size - 9;
        if(pageSize > 0)
            this.pages = (pageSize / this.getAvailableSlots()) + 1;
        else
            this.pages = 1;
        this.build(size, title, this.getPages());

        for(int i = 0; i < 9; i++)
            this.getInventory().setItem(i, filler);

        this.getInventory().setItem(4, this.back());
        this.getInventory().setItem(0, (page - 1 == 0 ? filler : this.page(page - 1)));
        this.getInventory().setItem(8, (page >= this.getPages() ? filler : this.page(page + 1)));
    }

    public void build(int size, LanguageNode title) {
        this.build(size, this.getLanguage().get(title), 0);
    }

    public void build(int size, String title) {
        this.build(size, title, 0);
    }

    public void build(int size, LanguageNode title, int pages) {
        this.build(size, this.getLanguage().get(title), pages);
    }

    public void build(int size, String title, int pages) {
        this.pages = pages;
        this.setInventory(Bukkit.createInventory(null, size, title));
    }
}
