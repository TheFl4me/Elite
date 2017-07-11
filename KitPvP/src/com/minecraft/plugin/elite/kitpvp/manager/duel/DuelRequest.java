package com.minecraft.plugin.elite.kitpvp.manager.duel;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;

import java.util.UUID;

public class DuelRequest {
	
	private UUID inviter;
	private UUID invited;
	private Duel.DuelType type;
	
	public DuelRequest(GeneralPlayer inviter, GeneralPlayer invited, Duel.DuelType type) {
		this.inviter = inviter.getUniqueId();
		this.invited = invited.getUniqueId();
		this.type = type;
		DuelManager.addRequest(this);
	}

	public void delete() {
		DuelManager.removeRequest(this);
	}
	
	public GeneralPlayer getInviter() {
		return GeneralPlayer.get(this.inviter);
	}
	
	public GeneralPlayer getInvited() {
		return GeneralPlayer.get(this.invited);
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
