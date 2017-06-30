package com.minecraft.plugin.elite.general.commands.punish;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.abstracts.eCommand;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.api.enums.Rank;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.general.punish.PunishManager;
import com.minecraft.plugin.elite.general.punish.PunishReason;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class PunishCommand extends eCommand implements TabCompleter {

    public PunishCommand() {
        super("punish", "eban.punish", true);
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String lbl, String[] args) {
        if(args.length == 2)
            return PunishReason.getAllReasons();
        return null;
    }

    @SuppressWarnings("deprecation")
    public boolean execute(CommandSender cs, Command cmd, String[] args) {

        Language lang = Language.ENGLISH;
        if(cs instanceof Player)
            lang = ePlayer.get((Player) cs).getLanguage();

        if(args.length > 2) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            boolean canBan = true;
            Database db = General.getDB();
            if(!db.containsValue(General.DB_PLAYERS, "uuid", target.getUniqueId().toString())) {
                cs.sendMessage(lang.get(GeneralLanguage.NEVER_JOINED));
                return true;
            }
            if(cs instanceof Player) {
                ePlayer p = ePlayer.get((Player) cs);
                if(p.getRank().ordinal() <= Rank.get(target).ordinal())
                    canBan = false;
            }
            if(canBan) {
                PunishReason reason = PunishReason.get(args[1]);
                if(reason != null) {
                    StringBuilder details = new StringBuilder();
                    for(int i = 2; i < args.length; i++)
                        details.append(args[i]).append(" ");
                    PunishManager.punish(cs.getName(), target, reason, details.toString());
                    return true;
                } else {
                    cs.sendMessage(lang.get(GeneralLanguage.ARG_INVALID)
                            .replaceAll("%arg", args[1]));
                    return true;
                }
            } else {
                cs.sendMessage(lang.get(GeneralLanguage.PUNISH_NOPERM));
                return true;
            }
        } else {
            cs.sendMessage(lang.get(GeneralLanguage.PUNISH_USAGE));
            return true;
        }
    }
}
