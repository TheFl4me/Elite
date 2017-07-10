package com.minecraft.plugin.elite.kitpvp.manager.duel.custom;

import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import com.minecraft.plugin.elite.kitpvp.manager.KitPlayer;
import com.minecraft.plugin.elite.kitpvp.manager.duel.Duel;
import com.minecraft.plugin.elite.kitpvp.manager.duel.DuelManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.UUID;

public class DuelSetup {

    private HashMap<UUID, Phase> phases;
    private Duel duel;
    private boolean editing;

    public DuelSetup(ePlayer p1, ePlayer p2) {
        this.phases = new HashMap<>();
        this.duel = DuelManager.get(p1);
        this.editing = true;
        this.setPhase(p1, Phase.EDITING);
        this.setPhase(p2, Phase.STANDBY);
    }

    public boolean isEditing() {
        return this.editing;
    }

    public void setEditing(boolean edit) {
        this.editing = edit;
    }

    public ePlayer getEditor() {
        for(UUID uuid : phases.keySet()) {
            ePlayer p = ePlayer.get(uuid);
            if(this.getPhase(p) == Phase.EDITING || this.getPhase(p) == Phase.PENDING) {
                return p;
            }
        }
        return null;
    }

    public ePlayer getStandby() {
        for(UUID uuid : phases.keySet()) {
            ePlayer p = ePlayer.get(uuid);
            if(this.getPhase(p) == Phase.STANDBY) {
                return p;
            }
        }
        return null;
    }

    public Duel getDuel() {
        return this.duel;
    }

    public void setPhase(ePlayer p, Phase phase) {
        if(phases.containsKey(p.getUniqueId()))
            phases.remove(p.getUniqueId());
        phases.put(p.getUniqueId(), phase);
    }

    public Phase getPhase(ePlayer p) {
        return phases.get(p.getUniqueId());
    }

    public void switchRoles() {
        final ePlayer editor = this.getEditor();
        final ePlayer standby = this.getStandby();
        this.setPhase(standby, Phase.PENDING);
        this.setPhase(editor, Phase.STANDBY);

        Server server = Server.get();

        for(UUID uuid : phases.keySet()) {
            ePlayer p = ePlayer.get(uuid);

            ItemStack glass = new ItemStack(Material.THIN_GLASS);
            Server.get().rename(glass, " ");

            ItemStack edit;
            ItemStack accept;
            ItemStack cancel;
            if(phases.get(uuid) == Phase.STANDBY) {
                cancel = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
                server.rename(cancel, p.getLanguage().get(KitPvPLanguage.DUEL_GUI_WAIT)
                        .replaceAll("%z", this.getEditor().getName()));
                edit = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
                server.rename(edit, p.getLanguage().get(KitPvPLanguage.DUEL_GUI_WAIT)
                        .replaceAll("%z", this.getEditor().getName()));
                accept = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
                server.rename(accept, p.getLanguage().get(KitPvPLanguage.DUEL_GUI_WAIT)
                        .replaceAll("%z", this.getEditor().getName()));
            } else {
                edit = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
                server.rename(edit, p.getLanguage().get(KitPvPLanguage.DUEL_GUI_EDIT));
                accept = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 9);
                server.rename(accept, p.getLanguage().get(KitPvPLanguage.DUEL_GUI_ACCEPT));
                cancel = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
                server.rename(cancel, p.getLanguage().get(KitPvPLanguage.DUEL_GUI_CANCEL));
            }

            ItemStack head1 = server.playerHead(this.getEditor().getName());
            server.rename(head1, p.getLanguage().get(KitPvPLanguage.DUEL_GUI_EDITOR)
                    .replaceAll("%p", this.getEditor().getName()));

            ItemStack head2 = server.playerHead(this.getStandby().getName());
            server.rename(head2, p.getLanguage().get(KitPvPLanguage.DUEL_GUI_STANDBY)
                    .replaceAll("%p", this.getStandby().getName()));

            Inventory inv = p.getGUI().getInventory();
            for(int i = 45; i < 54; i++)
                inv.setItem(i, edit);

            inv.setItem(3, head1);
            inv.setItem(5, head2);

            for(int i = 1; i <= 4; i++ )
                inv.setItem(9 * i, cancel);

            for(int i = 1; i <= 4; i++ )
                inv.setItem((9 * (i+1)) - 1, accept);
        }

        this.playSound(Sound.NOTE_PLING);
    }

    public void playSound(Sound sound) {
        this.getEditor().getPlayer().playSound(this.getEditor().getPlayer().getLocation(), sound, 1, 1);
        this.getStandby().getPlayer().playSound(this.getStandby().getPlayer().getLocation(), sound, 1, 1);
    }

    public void edit() {
        this.setPhase(this.getEditor(), Phase.EDITING);

        Server server = Server.get();

        ItemStack glass = new ItemStack(Material.THIN_GLASS);
        Server.get().rename(glass, " ");

        for(UUID uuid : phases.keySet()) {
            ePlayer p = ePlayer.get(uuid);
            if(phases.get(uuid) == Phase.STANDBY) {
                Inventory inv = p.getGUI().getInventory();
                for(int i = 0; i < inv.getContents().length; i++)
                    if(inv.getItem(i).getType() == Material.STAINED_GLASS_PANE)
                        inv.setItem(i, glass);
            } else {
                ItemStack done = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 13);
                server.rename(done, p.getLanguage().get(KitPvPLanguage.DUEL_GUI_DONE));

                Inventory inv = p.getGUI().getInventory();
                for(int i = 45; i < 54; i++)
                    inv.setItem(i, glass);

                for(int i = 1; i <= 4; i++ )
                    inv.setItem((9 * (i+1)) - 1, done);
            }
        }

        this.playSound(Sound.ANVIL_USE);
    }

    public void accept() {

        this.playSound(Sound.FIREWORK_LARGE_BLAST);

        Inventory editInv = this.getEditor().getGUI().getInventory();
        final ItemStack helmet = editInv.getItem(Slot.HELMET.getSlot());
        final ItemStack chest = editInv.getItem(Slot.CHESTPLATE.getSlot());
        final ItemStack leg = editInv.getItem(Slot.LEGGINGS.getSlot());
        final ItemStack boots = editInv.getItem(Slot.BOOTS.getSlot());

        final ItemStack sword = editInv.getItem(Slot.SWORD.getSlot());
        final ItemStack soup = editInv.getItem(Slot.SOUP.getSlot());
        final ItemStack fishing = editInv.getItem(Slot.FISHING_ROD.getSlot());
        final boolean recraft = editInv.getItem(Slot.RECRAFT.getSlot()).getType() != Material.BARRIER;

        this.setEditing(false);
        for(UUID uuid : phases.keySet()) {
            ePlayer p = ePlayer.get(uuid);
            p.getPlayer().closeInventory();
            p.getPlayer().setGameMode(GameMode.SURVIVAL);
            p.clear();
            p.clearTools();
            PlayerInventory inv = p.getPlayer().getInventory();
            inv.setArmorContents(null);
            inv.setHelmet((helmet.getType() == Material.BARRIER ? null : helmet));
            inv.setChestplate((chest.getType() == Material.BARRIER ? null : chest));
            inv.setLeggings((leg.getType() == Material.BARRIER ? null : leg));
            inv.setBoots((boots.getType() == Material.BARRIER ? null : boots));

            KitPlayer kp = KitPlayer.get(p.getUniqueId());

            if(sword.getType() != Material.BARRIER)
                kp.setItem(KitPlayer.SlotType.SWORD, sword);
            if(fishing.getType() != Material.BARRIER)
                kp.setItem(KitPlayer.SlotType.KIT_ITEM, fishing);

            if(soup.getType() != Material.BARRIER) {
                if(recraft) {
                    kp.setItem(KitPlayer.SlotType.BOWL, (new ItemStack(Material.BOWL, 32)));
                    kp.setItem(KitPlayer.SlotType.BROWN_MUSHROOM, (new ItemStack(Material.BROWN_MUSHROOM, 32)));
                    kp.setItem(KitPlayer.SlotType.RED_MUSHROOM, (new ItemStack(Material.RED_MUSHROOM, 32)));
                }
                for (int i = 0; i < inv.getSize(); i++)
                    inv.addItem(soup);

            }
        }
        this.getDuel().start();
    }

    public void abort() {
        this.playSound(Sound.ANVIL_BREAK);
        if(this.isEditing())
            this.getDuel().delete();
        this.getEditor().getPlayer().closeInventory();
        this.getStandby().getPlayer().closeInventory();
    }

    public Material next(ItemStack item, int slot) {
        if(slot == Slot.HELMET.getSlot()) {
            switch(item.getType()) {
                case BARRIER : return Material.LEATHER_HELMET;
                case LEATHER_HELMET : return Material.GOLD_HELMET;
                case GOLD_HELMET : return Material.CHAINMAIL_HELMET;
                case CHAINMAIL_HELMET : return Material.IRON_HELMET;
                case IRON_HELMET : return Material.DIAMOND_HELMET;
                case DIAMOND_HELMET : return Material.BARRIER;
                default: return null;
            }
        } else if(slot == Slot.CHESTPLATE.getSlot()) {
            switch(item.getType()) {
                case BARRIER : return Material.LEATHER_CHESTPLATE;
                case LEATHER_CHESTPLATE : return Material.GOLD_CHESTPLATE;
                case GOLD_CHESTPLATE : return Material.CHAINMAIL_CHESTPLATE;
                case CHAINMAIL_CHESTPLATE : return Material.IRON_CHESTPLATE;
                case IRON_CHESTPLATE : return Material.DIAMOND_CHESTPLATE;
                case DIAMOND_CHESTPLATE : return Material.BARRIER;
                default: return null;
            }
        } else if(slot == Slot.LEGGINGS.getSlot()) {
            switch(item.getType()) {
                case BARRIER : return Material.LEATHER_LEGGINGS;
                case LEATHER_LEGGINGS : return Material.GOLD_LEGGINGS;
                case GOLD_LEGGINGS : return Material.CHAINMAIL_LEGGINGS;
                case CHAINMAIL_LEGGINGS : return Material.IRON_LEGGINGS;
                case IRON_LEGGINGS : return Material.DIAMOND_LEGGINGS;
                case DIAMOND_LEGGINGS : return Material.BARRIER;
                default: return null;
            }
        } else if(slot == Slot.BOOTS.getSlot()) {
            switch(item.getType()) {
                case BARRIER : return Material.LEATHER_BOOTS;
                case LEATHER_BOOTS : return Material.GOLD_BOOTS;
                case GOLD_BOOTS : return Material.CHAINMAIL_BOOTS;
                case CHAINMAIL_BOOTS : return Material.IRON_BOOTS;
                case IRON_BOOTS : return Material.DIAMOND_BOOTS;
                case DIAMOND_BOOTS : return Material.BARRIER;
                default: return null;
            }
        } else if(slot == Slot.SWORD.getSlot()) {
            switch(item.getType()) {
                case BARRIER : return Material.WOOD_SWORD;
                case WOOD_SWORD : return Material.GOLD_SWORD;
                case GOLD_SWORD : return Material.STONE_SWORD;
                case STONE_SWORD: return Material.IRON_SWORD;
                case IRON_SWORD : return Material.DIAMOND_SWORD;
                case DIAMOND_SWORD : return Material.BARRIER;
                default: return null;
            }
        } else if(slot == Slot.SOUP.getSlot()) {
            switch(item.getType()) {
                case BARRIER : return Material.MUSHROOM_SOUP;
                case MUSHROOM_SOUP : return Material.BARRIER;
                default: return null;
            }
        } else if(slot == Slot.RECRAFT.getSlot()) {
            switch(item.getType()) {
                case BARRIER : return Material.BROWN_MUSHROOM;
                case BROWN_MUSHROOM : return Material.BARRIER;
                default: return null;
            }
        } else if(slot == Slot.FISHING_ROD.getSlot()) {
            switch(item.getType()) {
                case BARRIER : return Material.FISHING_ROD;
                case FISHING_ROD : return Material.BARRIER;
                default: return null;
            }
        }
        return null;
    }

    public void change(int slot, ItemStack item) {
        for(UUID uuid : phases.keySet()) {
            ePlayer p = ePlayer.get(uuid);
            p.getGUI().getInventory().setItem(slot, item);
        }
    }

    public enum Slot {

        HELMET(13),
        CHESTPLATE(22),
        LEGGINGS(31),
        BOOTS(40),
        SWORD(15),
        SOUP(11),
        RECRAFT(38),
        FISHING_ROD(42);

        private int slot;

        Slot(int slot) {
            this.slot = slot;
        }

        public int getSlot() {
            return this.slot;
        }
    }

    public enum Phase {

        EDITING,
        PENDING,
        STANDBY
    }
}
