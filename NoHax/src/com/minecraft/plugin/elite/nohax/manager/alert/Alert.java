package com.minecraft.plugin.elite.nohax.manager.alert;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;

public class Alert {

    private GeneralPlayer hacker;
    private long date;
    private AlertType type;

    public Alert(GeneralPlayer p, AlertType alert) {
        this.hacker = p;
        this.type = alert;
        this.date = System.currentTimeMillis();
    }

    public GeneralPlayer getHacker() {
        return this.hacker;
    }

    public AlertType getType() {
        return this.type;
    }

    public String toString() {
        return this.getType().toString();
    }

    public int getDetections() {
        int detected = 0;
        for (Alert alert : AlertManager.getAll(this.getHacker()))
            if (alert.getType() == this.getType())
                detected++;
        return detected - 1;
    }

    public long getDate() {
        return this.date;
    }
}
