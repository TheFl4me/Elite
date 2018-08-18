package com.minecraft.plugin.elite.survivalgames.manager;

public enum GamePhase {
	
	WAITING(false),
	MAIN(true),
	FINAL(true),
	END(false);
	
	private boolean pvp;
	
	GamePhase(boolean pvp) {
		this.pvp = pvp;
	}
	
	public boolean isPvP() {
		return this.pvp;
	}
}