package com.minecraft.plugin.elite.general.punish.mute;

import java.util.ArrayList;
import java.util.List;

public enum MuteReason {
	
	SPAM("spam", "Spam", 0.001, true),
	INSULT("insult", "Insult", 0.001, true),
	RACISM("racism", "Racism", 0.02, true),
	NAZISM("nazism", "Nazism", 0.03, true),
	ADULT_CONTENT("adult-content", "Sharing adult content", 0.5, true),
	PROVOCATION("provocation", "Provocation", 0.01, true);

	private double modifier;
	private String reason;
	private String displayReason;
	private boolean temp;
	
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
	
	MuteReason(String reason, String displayReason, double modifier, boolean temp) {
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
	
	public double getModifier() {
		return this.modifier;
	}
	
	public boolean isTemp() {
		return this.temp;
	}
}