package com.minecraft.plugin.elite.general.api.special.supportchat;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;

public class SupportChat {
	
	private GeneralPlayer staff;
	private GeneralPlayer user;
	
	public SupportChat(GeneralPlayer staff, GeneralPlayer user) {
		this.staff = staff;
		this.user = user;
		SupportChatManager.add(this);
	}
	
	public void start() {
		SupportChatManager.removeRequest(this.getPlayer());
		this.getStaff().getPlayer().sendMessage(this.getStaff().getLanguage().get(com.minecraft.plugin.elite.general.GeneralLanguage.SUPPORT_CONFIRMED_STAFF)
				.replaceAll("%z", this.getPlayer().getChatName()));
		this.getPlayer().getPlayer().sendMessage(this.getPlayer().getLanguage().get(com.minecraft.plugin.elite.general.GeneralLanguage.SUPPORT_CONFIRMED_YOU)
				.replaceAll("%staff", this.getStaff().getChatName()));
	}
	
	public void end() {
		this.getStaff().sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.SUPPORT_END);
		this.getPlayer().sendMessage(com.minecraft.plugin.elite.general.GeneralLanguage.SUPPORT_END);
		SupportChatManager.remove(this);
	}
	
	public GeneralPlayer getStaff() {
		return this.staff;
	}
	
	public GeneralPlayer getPlayer() {
		return this.user;
	}
	
	public void sendMessage(String msg) {
		this.getStaff().getPlayer().sendMessage(msg);
		this.getPlayer().getPlayer().sendMessage(msg);
	}
}