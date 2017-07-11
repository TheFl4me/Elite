package com.minecraft.plugin.elite.general.api.abstracts;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.enums.Language;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import java.util.HashSet;

public abstract class GeneralCommand implements CommandExecutor {

    private PluginCommand cmd;
    private String perm;
    private boolean consoleCmd;
    private Language lang;

    private static HashSet<GeneralCommand> commands = new HashSet<>();

    public static GeneralCommand[] getCommands() {
        return commands.toArray(new GeneralCommand[commands.size()]);
    }

    public GeneralCommand(String command, String perm, boolean console) {
        this.perm = perm;
        this.cmd = Bukkit.getPluginCommand(command);
        this.consoleCmd = console;
        this.lang = Language.ENGLISH;
        if (this.getCommand() == null)
            System.out.println(ChatColor.RED + "Failed to register the " + command + " command!");
        else
            this.getCommand().setExecutor(this);
        commands.add(this);
    }

    public PluginCommand getCommand() {
        return this.cmd;
    }

    public String getPermission() {
        return this.perm;
    }

    public boolean isConsoleCmd() {
        return this.consoleCmd;
    }

    public Language getLanguage() {
        return this.lang;
    }

    public void setLanguage(Language lang) {
        this.lang = lang;
    }

    public boolean hasPermission(CommandSender sender) {
        return !(this.getPermission() == null || this.getPermission().isEmpty()) && sender.hasPermission(this.getPermission());
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof ConsoleCommandSender) {
            if(this.isConsoleCmd()) {
                return this.execute(sender, cmd, args);
            } else {
                sender.sendMessage(this.getLanguage().get(GeneralLanguage.ONLY_PLAYER));
                return true;
            }
        } else if(sender instanceof Player) {
            this.setLanguage(GeneralPlayer.get((Player) sender).getLanguage());
            if (this.hasPermission(sender)) {
                return this.execute(sender, cmd, args);
            } else {
                sender.sendMessage(this.getLanguage().get(GeneralLanguage.NOPERM));
                return true;
            }
        } else {
            return false;
        }
    }

    public abstract boolean execute(CommandSender cs, Command cmd, String[] args);
}
