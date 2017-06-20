package com.minecraft.plugin.elite.general.punish;

import java.util.UUID;

public abstract class Punishment {
	
	protected UUID target;
	protected String punisher;
	protected String reason;
	protected String details;
	protected long date;
	protected UUID id;
	
	public Punishment(UUID targetUuid, String pName, String pReason, String pDetails, long pDate, UUID id) {
		this.target = targetUuid;
		this.punisher = pName;
		this.reason = pReason;
		this.details = pDetails;
		this.date = pDate;
		this.id = id;
	}
	
	public UUID getTarget() {
		return this.target;
	}
	public String getPunisher() {
		return this.punisher;
	}
	public String getReason() {
		return this.reason;
	}
	public String getDetails() {
		return this.details;
	}
	public long getDate() {
		return this.date;
	}
	public UUID getUniqueId() {
		return this.id;
	}
}

