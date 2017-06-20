package com.minecraft.plugin.elite.kitpvp.manager.duel;

import com.minecraft.plugin.elite.general.api.ePlayer;

import java.util.UUID;

public class DuelRequest {
	
	private UUID inviter;
	private UUID invited;
	
	public DuelRequest(ePlayer inviter, ePlayer invited) {
		this.inviter = inviter.getUniqueId();
		this.invited = invited.getUniqueId();
		DuelManager.addRequest(this);
	}

	public void delete() {
		DuelManager.removeRequest(this);
	}
	
	public ePlayer getInviter() {
		return ePlayer.get(this.inviter);
	}
	
	public ePlayer getInvited() {
		return ePlayer.get(this.invited);
	}
	
	public ePlayer getOther(ePlayer p) {
		if(p.getUniqueId().equals(this.getInviter().getUniqueId()))
			return this.getInvited();
		else if(p.getUniqueId().equals(this.getInvited().getUniqueId()))
			return this.getInviter();
		else
			return null;
	}

}
