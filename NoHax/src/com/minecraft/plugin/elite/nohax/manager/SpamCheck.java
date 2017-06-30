package com.minecraft.plugin.elite.nohax.manager;

import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.punish.PunishManager;
import com.minecraft.plugin.elite.general.punish.mute.MuteManager;
import com.minecraft.plugin.elite.general.punish.mute.MuteReason;
import com.minecraft.plugin.elite.nohax.NoHaxLanguage;
import org.bukkit.Bukkit;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SpamCheck {

    public static Map<UUID, SpamCheck> players = new Hashtable<>();

    public ArrayList<ChatMessage> messages;
    public ePlayer player;

    public static void onPlayerChat(AsyncPlayerChatEvent e) {
        ePlayer p = ePlayer.get(e.getPlayer());
        SpamCheck check = players.computeIfAbsent(p.getUniqueId(), k -> new SpamCheck());
        check.addMessage(e);
    }

    public static void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        ePlayer p = ePlayer.get(e.getPlayer());
        SpamCheck check = players.computeIfAbsent(p.getUniqueId(), k -> new SpamCheck());
        check.addMessage(e);
    }

    public SpamCheck() {
        this.messages = new ArrayList<>();
    }

    public void addMessage(AsyncPlayerChatEvent e) {
        this.player = ePlayer.get(e.getPlayer());
        ChatMessage message = new ChatMessage(this.getPlayer(), System.currentTimeMillis(), e.getMessage());
        this.messages.add(0, message);
        if (e.isCancelled())
            return;
        if (!PunishManager.isMuted(this.getPlayer().getUniqueId())) {
            this.checkSpamming();
            if(PunishManager.isMuted(this.getPlayer().getUniqueId()))
                e.setCancelled(true);
        }
        if (this.getMessages().size() > 10) {
            this.messages.remove(this.getMessages().size() - 1);
        }
    }

    public void addMessage(PlayerCommandPreprocessEvent e) {
        this.player = ePlayer.get(e.getPlayer());
        ChatMessage message = new ChatMessage(this.getPlayer(), System.currentTimeMillis(), e.getMessage());
        this.messages.add(0, message);
        if (e.isCancelled())
            return;
        if (!PunishManager.isMuted(this.getPlayer().getUniqueId())) {
            this.checkSpamming();
            if(PunishManager.isMuted(this.getPlayer().getUniqueId()))
                e.setCancelled(true);
        }
        if (this.getMessages().size() > 10) {
            this.messages.remove(this.getMessages().size() - 1);
        }
    }

    public void checkSpamming() {
        ePlayer p = this.getPlayer();
        Server server = Server.get();
        if(server.isLagging() || p.isLagging())
            return;
        boolean isSpamming = false;
        if (this.getMessages().size() >= 3) {
            String msg = this.getMessages().get(0).getMessage();
            boolean same = true;
            for (int i = 1; i < 3; i++)
                same = same && msg.equalsIgnoreCase(this.getMessages().get(i).getMessage());
            long time = this.getMessages().get(0).getWhen() - this.getMessages().get(2).getWhen();
            // if they sent 3 msgs in 1.5 seconds, they are spamming
            isSpamming = time < 1500;
            // if they sent the same message 3 times in a row within 3 seconds, they are spamming
            isSpamming = isSpamming || (time < 3000 && same);
        }
        if (isSpamming) {
            if (HaxPlayer.get(p.getUniqueId()).canBypassChecks()) {
                p.sendMessage(NoHaxLanguage.SPAMCHECK_STAFF);
            } else {
                MuteManager.mute("System - SpamCheck", Bukkit.getOfflinePlayer(this.getPlayer().getUniqueId()), MuteReason.SPAM, "Detected spamming chat by SpamCheck.");
                this.clear();
            }
        }
    }

    public void clear() {
        this.messages.clear();
    }

    public ePlayer getPlayer() {
        return this.player;
    }

    public List<ChatMessage> getMessages() {
        return this.messages;
    }

    public class ChatMessage {
        public ePlayer player;
        public long when;
        public String message;

        public ChatMessage(ePlayer p, long w, String msg) {
            this.player = p;
            this.when = w;
            this.message = msg;
        }

        public ePlayer getPlayer() {
            return this.player;
        }

        public long getWhen() {
            return this.when;
        }

        public String getMessage() {
            return this.message;
        }
    }
}