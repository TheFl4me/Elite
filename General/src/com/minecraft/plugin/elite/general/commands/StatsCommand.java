package com.minecraft.plugin.elite.general.commands;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.text.DecimalFormat;

public class StatsCommand extends eCommand {

    public StatsCommand() {
        super("stats", "egeneral.stats", false);
    }

    @SuppressWarnings("deprecation")
    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        Database db = General.getDB();
        ePlayer p = ePlayer.get((Player) cs);
        p.sendMessage(GeneralLanguage.DB_CHECK);
        Server server = Server.get();
        DecimalFormat df = new DecimalFormat("0.00");
        if(args.length == 0) {
            p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.STATS)
                    .replaceAll("%player", p.getName())
                    .replaceAll("%level", Integer.toString(p.getLevel()))
                    .replaceAll("%prestige", Integer.toString(p.getPrestige()))
                    .replaceAll("%joindate", server.getDate(p.getFirstJoin()))
                    .replaceAll("%lastonline", server.getDate(p.getLastJoin()))
                    .replaceAll("%playtime", server.getTime(p.getPlayTime(), p.getLanguage()))
                    .replaceAll("%kills", Integer.toString(p.getKills()))
                    .replaceAll("%deaths", Integer.toString(p.getDeaths()))
                    .replaceAll("%kdr", df.format(p.getKDR()))
                    .replaceAll("%elo", Long.toString(p.getELO())));
            return true;
        } else {
            try {
                OfflinePlayer z = Bukkit.getOfflinePlayer(args[0]);
                if (db.containsValue(General.DB_PLAYERS, "uuid", z.getUniqueId().toString())) {
                    ResultSet res = db.select(General.DB_PLAYERS, "uuid", z.getUniqueId().toString());
                    if (res.next()) {
                        String kdr;
                        if (res.getInt("deaths") == 0)
                            kdr = Double.toString((double) res.getInt("kills"));
                        else if (res.getInt("kills") == 0)
                            kdr = Double.toString(0.0);
                        else
                            kdr = df.format((double) res.getInt("kills") / (double) res.getInt("deaths"));
                        p.getPlayer().sendMessage(p.getLanguage().get(GeneralLanguage.STATS)
                                .replaceAll("%player", z.getName())
                                .replaceAll("%level", Integer.toString(res.getInt("level")))
                                .replaceAll("%prestige", Integer.toString(res.getInt("prestige")))
                                .replaceAll("%joindate", server.getDate(res.getLong("firstjoin")))
                                .replaceAll("%lastonline", server.getDate(res.getLong("lastjoin")))
                                .replaceAll("%playtime", server.getTime(res.getLong("playtime"), p.getLanguage()))
                                .replaceAll("%kills", Integer.toString(res.getInt("kills")))
                                .replaceAll("%deaths", Integer.toString(res.getInt("deaths")))
                                .replaceAll("%kdr", kdr)
                                .replaceAll("%elo", Long.toString(res.getLong("elo"))));
                        return true;
                    }
                    return true;
                } else {
                    p.sendMessage(GeneralLanguage.NEVER_JOINED);
                    return true;
                }
            } catch (Exception e) {
                p.sendMessage(GeneralLanguage.DB_CHECK_FAIL);
                e.printStackTrace();
                return true;
            }
        }
    }
}