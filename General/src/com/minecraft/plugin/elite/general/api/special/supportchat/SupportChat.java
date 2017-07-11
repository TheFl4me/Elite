package com.minecraft.plugin.elite.general.api.special.supportchat;

import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;

import java.util.UUID;

public class SupportChat {
	
	private UUID staff;
	private UUID user;
	
	public SupportChat(GeneralPlayer staff, GeneralPlayer user) {
		this.staff = staff.getUniqueId();
		this.user = user.getUniqueId();
		SupportChatManager.add(this);
	}
	
	public void start() {
		SupportChatManager.removeRequest(this.getPlayer());
		this.getStaff().getPlayer().sendMessage(this.getStaff().getLanguage().get(GeneralLanguage.SUPPORT_CONFIRMED_STAFF)
				.replaceAll("%z", this.getPlayer().getChatName()));
		this.getPlayer().getPlayer().sendMessage(this.getPlayer().getLanguage().get(GeneralLanguage.SUPPORT_CONFIRMED_YOU)
				.replaceAll("%staff", this.getStaff().getChatName()));
	}
	
	public void end() {
		this.getStaff().sendMessage(GeneralLanguage.SUPPORT_END);
		this.getPlayer().sendMessage(GeneralLanguage.SUPPORT_END);
		SupportChatManager.remove(this);
	}
	
	public GeneralPlayer getStaff() {
		return GeneralPlayer.get(this.staff);
	}
	
	public GeneralPlayer getPlayer() {
		return GeneralPlayer.get(this.user);
	}
	
	public void sendMessage(String msg) {
		this.getStaff().getPlayer().sendMessage(msg);
		this.getPlayer().getPlayer().sendMessage(msg);
	}
}