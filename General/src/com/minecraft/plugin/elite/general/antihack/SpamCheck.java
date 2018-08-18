package com.minecraft.plugin.elite.general.antihack;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.punish.PunishManager;
import com.minecraft.plugin.elite.general.punish.PunishReason;
import org.bukkit.Bukkit;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.*;

public class SpamCheck {

    public static Map<UUID, SpamCheck> players = new Hashtable<>();

    public ArrayList<ChatMessage> messages;
    public GeneralPlayer player;

    public static void onPlayerChat(AsyncPlayerChatEvent e) {
        GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
        SpamCheck check = players.computeIfAbsent(p.getUniqueId(), k -> new SpamCheck());
        check.addMessage(e);
    }

    public static void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
        SpamCheck check = players.computeIfAbsent(p.getUniqueId(), k -> new SpamCheck());
        check.addMessage(e);
    }

    public SpamCheck() {
        this.messages = new ArrayList<>();
    }

    public void addMessage(AsyncPlayerChatEvent e) {
        this.player = GeneralPlayer.get(e.getPlayer());
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
        this.player = GeneralPlayer.get(e.getPlayer());
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
        GeneralPlayer p = this.getPlayer();
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
            // if they sent 3 messages in 1.5 seconds, they are spamming
            isSpamming = time < 1500;
            // if they sent the same message 3 times in a row within 5 seconds, they are spamming
            isSpamming = isSpamming || (time < 5000 && same);
        }
        if (isSpamming) {
            if (GeneralPlayer.get(p.getUniqueId()).canBypassChecks()) {
                p.sendMessage(GeneralLanguage.SPAM_CHECK_STAFF);
            } else {
                PunishManager.punish("System - SpamCheck", Bukkit.getOfflinePlayer(this.getPlayer().getUniqueId()), PunishReason.CHAT_SPAM, "Detected spamming chat by SpamCheck.");
                this.clear();
            }
        }
    }

    public void clear() {
        this.messages.clear();
    }

    public GeneralPlayer getPlayer() {
        return this.player;
    }

    public List<ChatMessage> getMessages() {
        return this.messages;
    }

    public class ChatMessage {
        public GeneralPlayer player;
        public long when;
        public String message;

        public ChatMessage(GeneralPlayer p, long w, String msg) {
            this.player = p;
            this.when = w;
            this.message = msg;
        }

        public GeneralPlayer getPlayer() {
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