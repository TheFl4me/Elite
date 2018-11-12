package com.minecraft.plugin.elite.general.commands.chat;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.GeneralPermission;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.abstracts.GeneralCommand;
import com.minecraft.plugin.elite.general.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatLogCommand extends GeneralCommand {

    public ChatLogCommand() {
        super("chatlog", GeneralPermission.CHAT_LOG, false);
    }

    @SuppressWarnings("deprecation")
    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        GeneralPlayer p = GeneralPlayer.get((Player) cs);
        if (args.length > 0) {
            Server server = Server.get();
            OfflinePlayer z = Bukkit.getOfflinePlayer(args[0]);
            StringBuilder logs = new StringBuilder();
            List<String> messages = new ArrayList<>();
            Database db = General.getDB();
            db.execute("DELETE FROM " + General.DB_CHAT_LOGS + " WHERE date < ?", System.currentTimeMillis() - 604800000L);
            p.sendMessage(GeneralLanguage.DB_CHECK);
            try {
                ResultSet res = db.select(General.DB_CHAT_LOGS, "uuid", z.getUniqueId().toString());
                logs.append(ChatColor.RED + General.SPACER + "\n");
                logs.append(ChatColor.RED + "ChatLog " + z.getName() + ":\n");
                while (res.next()) {
                    if (res.getString("type").equalsIgnoreCase("public")) {
                        if (messages.size() > 14)
                            messages.remove(messages.get(0));
                        messages.add(ChatColor.GRAY + "[" + server.getDate(res.getLong("date")) + "] " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', res.getString("message")) + "\n");
                    }
                }
                messages.forEach(logs::append);
                logs.append(ChatColor.RED + General.SPACER);
                cs.sendMessage(logs.toString());
                return true;
            } catch (SQLException e) {
                p.sendMessage(GeneralLanguage.DB_CHECK_FAIL);
                e.printStackTrace();
                return true;
            }
        } else {
            p.sendMessage(GeneralLanguage.CHAT_LOG_USAGE);
            return true;
        }
    }
}