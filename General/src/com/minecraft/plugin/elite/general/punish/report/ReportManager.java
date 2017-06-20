package com.minecraft.plugin.elite.general.punish.report;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.api.interfaces.LanguageNode;
import com.minecraft.plugin.elite.general.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ReportManager {

    private static List<Report> reports = new ArrayList<>();

    public static Collection<Report> getAll() {
        //remove after 3 days
        Collection<Report> invalid = new HashSet<>();
        for(Report report : reports.toArray(new Report[reports.size()])) {
            if(report.getDate() + 259200000L < System.currentTimeMillis())
                invalid.add(report);
        }

        invalid.forEach(Report::remove);
        return reports;
    }

    public static Report getReport(UUID hacker, GeneralLanguage reason, long date, UUID reporter) {
        for(Report report : getAll())
            if(report.getTarget().getUniqueId().equals(hacker) && report.getReason().equals(reason) && report.getDate() == date && report.getReporter().getUniqueId().equals(reporter))
                return report;
        return null;
    }

    public static List<Report> getReports(UUID hacker) {
        return getAll().stream().filter(report -> report.getTarget().getUniqueId().equals(hacker)).collect(Collectors.toList());
    }

    public static Collection<Report> getAllReports(boolean offlineReports) {
        if(offlineReports)
            return getAll();
        return getAll().stream().filter(report -> report.getTarget().isOnline()).collect(Collectors.toList());
    }

    public static String getReportList(boolean offlineReports, Language lang) {

        Map<UUID, List<LanguageNode>> reasons = new HashMap<>();
        if(!getAllReports(offlineReports).isEmpty()) {
            for(Report report : getAllReports(offlineReports)) {
                if(reasons.containsKey(report.getTarget().getUniqueId())) {
                    if(!reasons.get(report.getTarget().getUniqueId()).contains(report.getReason()))
                        reasons.get(report.getTarget().getUniqueId()).add(report.getReason());
                } else {
                    List<LanguageNode> list = new ArrayList<>();
                    list.add(report.getReason());
                    reasons.put(report.getTarget().getUniqueId(), list);
                }
            }
        } else {
            return null;
        }
        StringBuilder finalList = new StringBuilder();
        finalList.append(ChatColor.RED + General.SPACER + "\n");
        StringBuilder playerReports = new StringBuilder();
        for(UUID uuid : reasons.keySet()) {
            for(LanguageNode reason : reasons.get(uuid)) {
                playerReports.append((playerReports.length() > 0 ? ", " : "") + lang.getOnlyFirstLine(reason));
            }
            finalList.append(ChatColor.YELLOW + Bukkit.getOfflinePlayer(uuid).getName() + ChatColor.RED + " -> " + ChatColor.YELLOW + playerReports.toString() + "\n");
        }
        finalList.append(ChatColor.RED + General.SPACER);
        return finalList.toString();
    }

    public static void clearReportsOnBan(UUID hacker) {
        Database db = General.getDB();
        Collection<UUID> list = new ArrayList<>();
        if(!getReports(hacker).isEmpty()) {
            for(Report report : getReports(hacker)) {
                UUID uuid = report.getReporter().getUniqueId();
                if(!list.contains(uuid))
                    list.add(uuid);
            }
            clearReports(hacker);
        }
        for(UUID uuid : list) {
            try {
                ResultSet res = db.select(General.DB_PLAYERS, "uuid", uuid.toString());
                if(res.next())
                    db.update(General.DB_PLAYERS, "truereports", res.getInt("truereports") + 1, "uuid", uuid);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void clearReports(UUID hacker) {
        Database db = General.getDB();
        List<Report> list = new ArrayList<>();
        for(Report report : getAll()) {
            list.add(report);
            if(report.getTarget().getUniqueId().equals(hacker))
                list.remove(report);
        }
        reports = list;
        db.delete(General.DB_REPORTS, "target", hacker);
    }

    public static void add(Report report) {
        reports.add(report);
    }

    public static void remove(Report report) {
        reports.remove(report);
    }

    public static void reload() {
        Database db = General.getDB();
        try {
            ResultSet reportRes = db.select(General.DB_REPORTS);
            while(reportRes.next()) {
                UUID reporter = UUID.fromString(reportRes.getString("reporter"));
                UUID hacker = UUID.fromString(reportRes.getString("target"));
                GeneralLanguage reason = GeneralLanguage.valueOf(reportRes.getString("reason").toUpperCase());
                long date = reportRes.getLong("date");
                new Report(hacker, reporter, reason, date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
