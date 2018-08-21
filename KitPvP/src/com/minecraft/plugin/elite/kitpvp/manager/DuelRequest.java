package com.minecraft.plugin.elite.kitpvp.manager;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;

public class DuelRequest {
	
	private GeneralPlayer inviter;
	private GeneralPlayer invited;
	private Duel.DuelType type;
	
	public DuelRequest(GeneralPlayer inviter, GeneralPlayer invited, Duel.DuelType type) {
		this.inviter = inviter;
		this.invited = invited;
		this.type = type;
		DuelManager.addRequest(this);
	}

	public void delete() {
		DuelManager.removeRequest(this);
	}
	
	public GeneralPlayer getInviter() {
		return this.inviter;
	}
	
	public GeneralPlayer getInvited() {
		return this.invited;
	}
	
	public GeneralPlayer getOther(GeneralPlayer p) {
		if(p.getUniqueId().equals(this.getInviter().getUniqueId()))
			return this.getInvited();
		else if(p.getUniqueId().equals(this.getInvited().getUniqueId()))
			return this.getInviter();
		else
			return null;
	}

	public Duel.DuelType getType() {
		return this.type;
	}

}
