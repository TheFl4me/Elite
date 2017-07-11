package com.minecraft.plugin.elite.general.punish.info;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.abstracts.GUI;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.api.enums.Rank;
import com.minecraft.plugin.elite.general.api.special.clan.Clan;
import com.minecraft.plugin.elite.general.api.special.clan.ClanManager;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.general.punish.PunishManager;
import com.minecraft.plugin.elite.general.punish.ban.Ban;
import com.minecraft.plugin.elite.general.punish.ban.BanManager;
import com.minecraft.plugin.elite.general.punish.ban.PastBan;
import com.minecraft.plugin.elite.general.punish.mute.Mute;
import com.minecraft.plugin.elite.general.punish.mute.MuteManager;
import com.minecraft.plugin.elite.general.punish.mute.PastMute;
import com.minecraft.plugin.elite.general.punish.report.Report;
import com.minecraft.plugin.elite.general.punish.report.ReportManager;
import org.bukkit.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class InfoGUI extends GUI {

    private UUID uuid;
    private List<UUID> banList;
    private List<UUID> muteList;
    private List<UUID> altList;
    private List<Report> reportList;

    public InfoGUI(Language lang, UUID uuid) {
        super(lang);
        this.uuid = uuid;

        List<UUID> banIds = new ArrayList<>();
        Ban ban = BanManager.getBan(this.getPlayer().getUniqueId());
        Collection<PastBan> pastBans = BanManager.getPastBans(this.getPlayer().getUniqueId());
        if(ban != null)
            banIds.add(ban.getUniqueId());
        if(pastBans != null)
            for(PastBan pastBan : pastBans)
                banIds.add(pastBan.getUniqueId());
        this.banList = banIds;

        List<UUID> muteIds = new ArrayList<>();
        Mute mute = MuteManager.getMute(this.getPlayer().getUniqueId());
        Collection<PastMute> pastMutes = MuteManager.getPastMutes(this.getPlayer().getUniqueId());
        if(mute != null)
            muteIds.add(mute.getUniqueId());
        if(pastMutes != null)
            for(PastMute pastMute : pastMutes)
                muteIds.add(pastMute.getUniqueId());
        this.muteList = muteIds;

        Database db = General.getDB();
        List<UUID> altList = new ArrayList<>();
        try {
            String ip = null;
            ResultSet res1 = db.select(General.DB_PLAYERS, "uuid", this.getPlayer().getUniqueId().toString());
            if(res1.next())
                ip = res1.getString("ip");

            ResultSet res = db.select(General.DB_PLAYERS, "ip", ip);
            while(res.next()) {
                UUID altUuid = UUID.fromString(res.getString("uuid"));
                OfflinePlayer p = Bukkit.getOfflinePlayer(this.getPlayer().getUniqueId());
                if(!altUuid.toString().equalsIgnoreCase(p.getUniqueId().toString()))
                    altList.add(altUuid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.altList = altList;

        this.reportList = ReportManager.getReports(uuid);
    }

    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(this.uuid);
    }

    public List<UUID> getBanList() {
        return this.banList;
    }

    public List<UUID> getMuteList() {
        return this.muteList;
    }

    public List<UUID> getAltList() {
        return this.altList;
    }

    public List<Report> getReportList() {
        return this.reportList;
    }

    private ItemStack glass() {
        Server server = Server.get();
        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        server.rename(glass, " ");
        return glass;
    }

    public Inventory info() throws SQLException {
        this.build(54, Rank.get(this.getPlayer()).getPrefix().getColor() + this.getPlayer().getName());

        Server server = Server.get();
        Database db = General.getDB();

        ItemStack head = server.playerHead(this.getPlayer().getName());
        if(this.getPlayer().isOnline() && !GeneralPlayer.get(this.getPlayer().getUniqueId()).isInvis())
            server.rename(head, ChatColor.GREEN + "ONLINE");
        else
            server.rename(head, ChatColor.RED + "OFFLINE");

        ItemStack admin = new ItemStack(Material.DIAMOND_BLOCK);
        ItemStack time = new ItemStack(Material.WATCH);
        ItemStack stats = new ItemStack(Material.IRON_SWORD);

        ResultSet res = db.select(General.DB_PLAYERS, "uuid", this.getPlayer().getUniqueId().toString());
        if(res.next()) {
            server.rename(admin, this.getLanguage().get(GeneralLanguage.INFO_GUI_ADMIN)
                    .replaceAll("%ip", res.getString("ip"))
                    .replaceAll("%rank", Rank.get(this.getPlayer()).getDisplayName())
                    .replaceAll("%uuid", this.getPlayer().getUniqueId().toString())
                    .replaceAll("%sentreports", Long.toString(PunishManager.getSentReports(this.getPlayer().getUniqueId())))
                    .replaceAll("%truereports", Long.toString(PunishManager.getTrueSentReports(this.getPlayer().getUniqueId()))));

            server.rename(time, this.getLanguage().get(GeneralLanguage.INFO_GUI_TIME)
                    .replaceAll("%joindate", server.getDate(res.getLong("firstjoin")))
                    .replaceAll("%lastonline", server.getDate(res.getLong("lastjoin")))
                    .replaceAll("%playtime", server.getTime(res.getLong("playtime"), this.getLanguage())));

            String kdr;
            Clan clan = ClanManager.get(this.getPlayer().getUniqueId());
            DecimalFormat df = new DecimalFormat("0.00");
            if (res.getInt("deaths") == 0)
                kdr = Double.toString((double) res.getInt("kills"));
            else if (res.getInt("kills") == 0)
                kdr = Double.toString(0.0);
            else
                kdr = df.format((double) res.getInt("kills") / (double) res.getInt("deaths"));

            server.rename(stats, this.getLanguage().get(GeneralLanguage.INFO_GUI_STATS)
                    .replaceAll("%level", Integer.toString(res.getInt("level")))
                    .replaceAll("%elo", Long.toString(res.getLong("elo")))
                    .replaceAll("%prestige", Integer.toString(res.getInt("prestige")))
                    .replaceAll("%tokens", Long.toString(res.getLong("tokens")))
                    .replaceAll("%clan", (clan == null ? "" : clan.getName()))
                    .replaceAll("%kills", Integer.toString(res.getInt("kills")))
                    .replaceAll("%deaths", Integer.toString(res.getInt("deaths")))
                    .replaceAll("%kdr", kdr));
        }

        ItemStack mutes = new ItemStack(Material.BOOK);
        server.rename(mutes, this.getLanguage().get(GeneralLanguage.INFO_GUI_MUTES) + "\n" + ChatColor.GRAY + "[" + Integer.toString(this.getMuteList().size()) + "]");

        ItemStack bans = new ItemStack(Material.IRON_FENCE);
        server.rename(bans, this.getLanguage().get(GeneralLanguage.INFO_GUI_BANS) + "\n" + ChatColor.GRAY + "[" + Integer.toString(this.getBanList().size()) + "]");

        ItemStack reports = new ItemStack(Material.LAVA_BUCKET);
        server.rename(reports, this.getLanguage().get(GeneralLanguage.INFO_GUI_REPORTS) + "\n" + ChatColor.GRAY + "[" + Integer.toString(this.getReportList().size()) + "]");

        ItemStack alts = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        server.rename(alts, this.getLanguage().get(GeneralLanguage.INFO_GUI_ALTS) + "\n" + ChatColor.GRAY + "[" + Integer.toString(this.getAltList().size()) + "]");

        this.fill(this.glass());
        this.getInventory().setItem(12, head);
        this.getInventory().setItem(14, alts);
        this.getInventory().setItem(20, admin);
        this.getInventory().setItem(22, time);
        this.getInventory().setItem(24, stats);
        this.getInventory().setItem(38, mutes);
        this.getInventory().setItem(40, reports);
        this.getInventory().setItem(42, bans);
        return this.getInventory();
    }

    public Inventory bans(int page) {
        this.buildPageType(this.getLanguage().get(GeneralLanguage.INFO_GUI_BANS) + " - " + Rank.get(this.getPlayer()).getPrefix().getColor() + this.getPlayer().getName(), page, this.getBanList().size(), this.glass());

        Server server = Server.get();
        Ban ban = BanManager.getBan(this.getPlayer().getUniqueId());

        for(int i = this.getLastSlot(page); i < this.getNextSlot(page); i++) {
            if(i >= this.getBanList().size())
                break;
            UUID id = this.getBanList().get(i);
            PastBan pastBan = BanManager.getPastBan(id);
            ItemStack block = new ItemStack(Material.BARRIER);
            server.rename(block, this.getLanguage().get(GeneralLanguage.INFO_GUI_ERROR));
            try {
                if(ban != null && id.equals(ban.getUniqueId())) {
                    block = new ItemStack(Material.GOLD_BLOCK);
                    server.rename(block, ban.getInfo(GeneralLanguage.BAN_INFO, this.getLanguage()));
                } else if(pastBan != null){
                    if(pastBan.isTemp())
                        block = new ItemStack(Material.EMERALD_BLOCK);
                    else
                        block = new ItemStack(Material.DIAMOND_BLOCK);
                    server.rename(block, pastBan.getInfo(GeneralLanguage.BAN_INFO_PAST, this.getLanguage()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.getInventory().addItem(block);
        }
        return this.getInventory();
    }

    public Inventory mutes(int page) {
        this.buildPageType(this.getLanguage().get(GeneralLanguage.INFO_GUI_MUTES) + " - " + Rank.get(this.getPlayer()).getPrefix().getColor() + this.getPlayer().getName(), page, this.getMuteList().size() , this.glass());

        Server server = Server.get();
        Mute mute = MuteManager.getMute(this.getPlayer().getUniqueId());

        for(int i = this.getLastSlot(page); i < this.getNextSlot(page); i++) {
            if(i >= this.getMuteList().size())
                break;
            UUID id = this.getMuteList().get(i);
            PastMute pastMute = MuteManager.getPastMute(id);
            ItemStack block = new ItemStack(Material.BARRIER);
            server.rename(block, this.getLanguage().get(GeneralLanguage.INFO_GUI_ERROR));
            try {
                if(mute != null && id.equals(mute.getUniqueId())) {
                    block = new ItemStack(Material.GOLD_BLOCK);
                    server.rename(block, mute.getInfo(GeneralLanguage.MUTE_INFO, this.getLanguage()));
                } else if(pastMute != null){
                    if(pastMute.isTemp())
                        block = new ItemStack(Material.EMERALD_BLOCK);
                    else
                        block = new ItemStack(Material.DIAMOND_BLOCK);
                    server.rename(block, pastMute.getInfo(GeneralLanguage.MUTE_INFO_PAST, this.getLanguage()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.getInventory().addItem(block);
        }
        return this.getInventory();
    }

    public Inventory reports(int page) {
        this.buildPageType(this.getLanguage().get(GeneralLanguage.INFO_GUI_REPORTS) + " - " + Rank.get(this.getPlayer()).getPrefix().getColor() + this.getPlayer().getName(), page, this.getReportList().size(), this.glass());

        Server server = Server.get();

        for(int i = this.getLastSlot(page); i < this.getNextSlot(page); i++) {
            if(i >= this.getReportList().size())
                break;
            Report report = this.getReportList().get(i);
            Material mat;
            switch((GeneralLanguage) report.getReason()) {
                case REPORT_GUI_HACKING: mat = Material.DIAMOND_SWORD;
                    break;
                case REPORT_GUI_BUG: mat = Material.TNT;
                    break;
                case REPORT_GUI_STATS: mat = Material.LAVA_BUCKET;
                    break;
                case REPORT_GUI_TEAM: mat = Material.IRON_CHESTPLATE;
                    break;
                default: mat = Material.BARRIER;
            }
            ItemStack rep = new ItemStack(mat);
            server.rename(rep, this.getLanguage().get(GeneralLanguage.INFO_GUI_REPORTS_REPORT)
                    .replaceAll("%target", report.getTarget().getName())
                    .replaceAll("%reason", this.getLanguage().getOnlyFirstLine(report.getReason()))
                    .replaceAll("%reporter", report.getReporter().getName())
                    .replaceAll("%date", server.getDate(report.getDate())));
            this.getInventory().addItem(rep);
        }
        return this.getInventory();

    }

    public Inventory alts(int page) {
        this.buildPageType(this.getLanguage().get(GeneralLanguage.INFO_GUI_ALTS) + " - " + Rank.get(this.getPlayer()).getPrefix().getColor() + this.getPlayer().getName(), page, this.getAltList().size(), this.glass());

        Server server = Server.get();

        for(int i = this.getLastSlot(page); i < this.getNextSlot(page); i++) {
            if(i >= this.getAltList().size())
                break;
            OfflinePlayer alt = Bukkit.getOfflinePlayer(this.getAltList().get(i));
            ItemStack head = server.playerHead(alt.getName());
            server.rename(head, Rank.get(alt).getPrefix().getColor() + alt.getName() + "\n" + ChatColor.GRAY + alt.getUniqueId().toString());
            this.getInventory().addItem(head);
        }
        return this.getInventory();
    }
}
