package com.minecraft.plugin.elite.general.punish.mute;

import java.util.ArrayList;
import java.util.List;

public enum MuteReason {
	
	SPAM("spam", "Spam", 1, true),
	INSULT("insult", "Insult", 2, true),
	RACISM("racism", "Racism", 3, true),
	NAZISM("nazism", "Nazism", 3, true),
	ADULT_CONTENT("adult-content", "Sharing adult content", 5, true),
	PROVOCATION("provocation", "Provocation", 3, true);
	
	public int modifier;
	public String reason;
	public String displayReason;
	public boolean temp;
	
	public static MuteReason get(String args) {
		for(MuteReason reason : MuteReason.values()) {
			if(reason.toString().equalsIgnoreCase(args))
				return reason;
		}
		return null;
	}
	
	public static List<String> getAllReasons() {
		List<String> list = new ArrayList<>();
		for(MuteReason reason : MuteReason.values())
			list.add(reason.toString());
		return list;
	}
	
	MuteReason(String reason, String displayReason, int modifier, boolean temp) {
		this.reason = reason;
		this.displayReason = displayReason;
		this.modifier = modifier;
		this.temp = temp;
	}
	
	public String toString() {
		return this.reason;
	}
	
	public String toDisplayString() {
		return this.displayReason;
	}
	
	public int getModifier() {
		return this.modifier;
	}
	
	public boolean isTemp() {
		return this.temp;
	}
}