package com.minecraft.plugin.elite.general.punish;

import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.api.interfaces.LanguageNode;
import org.bukkit.Bukkit;

import java.util.UUID;

public abstract class PastPunishment extends Punishment {

    private String pardoner;
    private long pardonDate;
    private long time;

    public PastPunishment(UUID id, UUID target, PunishReason reason, String details, long date, long time, String punisher, String pardoner, long pardonDate) {
        super(target, punisher, reason, details, date, id);
        this.pardoner = pardoner;
        this.pardonDate = pardonDate;
        this.time = time;
    }

    public String getPardoner() {
        return this.pardoner;
    }

    public long getPardonDate() {
        return this.pardonDate;
    }

    public long getDuration() {
        return this.time;
    }

    public boolean isTemp() {
        return this.getDuration() > 0;
    }

    public String getInfo(LanguageNode node, Language lang) {
        Server server = Server.get();
        return lang.get(node)
                .replaceAll("%id", this.getUniqueId().toString())
                .replaceAll("%target", Bukkit.getOfflinePlayer(this.getTarget()).getName())
                .replaceAll("%reason", this.getReason().toString())
                .replaceAll("%details", this.getDetails())
                .replaceAll("%duration", (this.isTemp() ? server.getTime(this.getDuration(), lang) : "permanent"))
                .replaceAll("%date", server.getDate(this.getDate()))
                .replaceAll("%punisher", this.getPunisher())
                .replaceAll("%pardoner", this.getPardoner())
                .replaceAll("%pardondate", server.getDate(this.getPardonDate()));
    }
}
