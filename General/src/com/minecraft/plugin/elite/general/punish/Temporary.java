package com.minecraft.plugin.elite.general.punish;

public interface Temporary {
	
	long getExpireDate();
	long getTime();
	boolean hasExpired();
}
