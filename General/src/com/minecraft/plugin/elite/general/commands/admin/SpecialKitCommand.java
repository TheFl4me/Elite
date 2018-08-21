package com.minecraft.plugin.elite.general.commands.admin;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.api.special.skit.SKit;
import com.minecraft.plugin.elite.general.api.special.skit.SKitManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class SpecialKitCommand extends GeneralCommand implements TabCompleter {

    public SpecialKitCommand() {
        super("skit", GeneralPermission.ADMIN_SPECIAL_KIT, false);
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String lbl, String[] args) {
        if (args.length == 1)
            return Arrays.asList("apply", "create", "delete", "list");
        return null;
    }

    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        if (args.length > 2 && args[0].equalsIgnoreCase("apply"))
            try {
                SKit skit = SKitManager.get(args[1]);
                if (skit != null) {
                    List<Entity> nearby = p.getPlayer().getNearbyEntities(Double.parseDouble(args[2]), Double.parseDouble(args[2]), Double.parseDouble(args[2]));
                    nearby.forEach((entity) -> {
                        if (entity instanceof Player) {
                            GeneralPlayer all = GeneralPlayer.get((Player) entity);
                            if (!all.isAdminMode() && !all.isInvis()) {
                                all.clear();
                                all.getPlayer().getInventory().setContents(skit.getContents());
                            }
                        }
                    });
                    p.getPlayer().sendMessage(p.getLanguage().get(com.minecraft.plugin.elite.general.GeneralLanguage.SKIT_APPLY)
                            .replaceAll("%skit", skit.getName()));
                    return true;
                } else {
                    p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.SKIT_NULL);
                    return true;
                }
            } catch (IllegalArgumentException e) {
                p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.SKIT_USAGE);
                return true;
            }
        else if (args.length > 1)
            if (args[0].equalsIgnoreCase("create")) {
                if (SKitManager.get(args[1]) == null) {
                    new SKit(args[1], p.getPlayer().getInventory().getContents());
                    p.getPlayer().sendMessage(p.getLanguage().get(com.minecraft.plugin.elite.general.GeneralLanguage.SKIT_CREATE)
                            .replaceAll("%skit", args[1]));
                    return true;
                } else {
                    p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.SKIT_EXISTS);
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("delete")) {
                SKit skit = SKitManager.get(args[1]);
                if (skit != null) {
                    p.getPlayer().sendMessage(p.getLanguage().get(com.minecraft.plugin.elite.general.GeneralLanguage.SKIT_DELETE)
                            .replaceAll("%skit", skit.getName()));
                    skit.delete();
                    return true;
                } else {
                    p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.SKIT_NULL);
                    return true;
                }
            } else {
                p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.SKIT_USAGE);
                return true;
            }
        else if (args.length > 0 && args[0].equalsIgnoreCase("list")) {
            StringBuilder list = new StringBuilder();
            list.append(ChatColor.GREEN + General.SPACER + "\n");

            StringBuilder skits = new StringBuilder();
            for (SKit skit : SKitManager.getAll()) {
                skits.append(skit.getName() + ", ");
            }

            list.append(ChatColor.GREEN + "Skit List:\n");
            list.append(ChatColor.GOLD + skits.toString().substring(0, skits.toString().length() - 2));
            list.append(ChatColor.GREEN + General.SPACER);

            p.getPlayer().sendMessage(list.toString());
            return true;
        } else {
            p.sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.SKIT_USAGE);
            return true;
        }
    }
}