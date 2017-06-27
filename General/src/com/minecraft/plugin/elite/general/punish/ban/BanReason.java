package com.minecraft.plugin.elite.general.punish.ban;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum BanReason {
	
	CHEATING("cheating", "Cheating", 0, false),
	PAYMENT_FRAUD("payment-fraud", "Payment fraud", 0, false),
	RANK_ABUSE("rank-abuse", "Rank abuse", 0, false),
	SECURITY_RISK("security-risk", "Security risk", 0, false),
	DDOS("ddos", "Ddos threat", 0, false),

	THREAT("threat", "Threat", 0.5, true),
	MALICIOUS_LINK("malicious-link", "Sending a malicious link/url", 2.0, true),
	BUG_ABUSE("bug-abuse", "Bug abuse", 0.7, true),
	TEAM("big-team", "More than 2 players in team", 0.7, true),
	STATS_MANIPULATION("stats-manipulation", "Manipulating stats", 0.7, true),
	ADVERTISING("advertising", "Advertising", 3.0, true),
	INFO_LEAK("information-leak", "Leaking private information", 2.0, true),
	FALSE_APPLICATION("falsified-application", "Falsifying application", 1.5, true);
	
	private double modifier;
	private String reason;
	private String displayReason;
	private boolean tempban;
	
	public static BanReason get(String args) {
		for(BanReason reason : values()) {
			if(reason.toString().equalsIgnoreCase(args))
				return reason;
		}
		return null;
	}
	
	public static List<String> getAllReasons() {
		List<String> list = new ArrayList<>();
		Arrays.stream(values()).forEach((reason) -> list.add(reason.toString()));
		return list;
	}
	
	BanReason(String reason, String displayReason, double modifier, boolean tempban) {
		this.reason = reason;
		this.displayReason = displayReason;
		this.modifier = modifier;
		this.tempban = tempban;
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
		return this.tempban;
	}
}