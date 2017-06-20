package com.minecraft.plugin.elite.general.api.abstracts;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.ePlayer;
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

public abstract class eCommand implements CommandExecutor {

    protected String perm;
    protected PluginCommand cmd;
    protected boolean consoleCmd;

    private static HashSet<eCommand> commands = new HashSet<>();

    public static eCommand[] getCommands() {
        return commands.toArray(new eCommand[commands.size()]);
    }

    public eCommand(String command, String perm, boolean console) {
        this.perm = perm;
        this.cmd = Bukkit.getPluginCommand(command);
        this.consoleCmd = console;
        if (this.cmd == null) {
            System.out.println(ChatColor.RED + "Failed to register the " + command + " command!");
        } else {
            this.cmd.setExecutor(this);
        }
        commands.add(this);
    }

    public boolean hasPermission(CommandSender sender) {
        return !(this.perm == null || this.perm.isEmpty()) && sender.hasPermission(this.perm);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof ConsoleCommandSender) {
            if(this.consoleCmd) {
                return execute(sender, cmd, args);
            } else {
                sender.sendMessage(Language.ENGLISH.get(GeneralLanguage.ONLY_PLAYER));
                return true;
            }
        } else if(sender instanceof Player) {
            if (hasPermission(sender)) {
                return execute(sender, cmd, args);
            } else {
                sender.sendMessage(ePlayer.get((Player) sender).getLanguage().get(GeneralLanguage.NOPERM));
                return true;
            }
        } else {
            return false;
        }
    }

    public abstract boolean execute(CommandSender cs, Command cmd, String[] args);
}
