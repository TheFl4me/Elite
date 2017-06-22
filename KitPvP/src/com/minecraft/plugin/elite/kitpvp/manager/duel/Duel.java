package com.minecraft.plugin.elite.kitpvp.manager.duel;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import com.minecraft.plugin.elite.kitpvp.manager.duel.custom.DuelGUI;
import com.minecraft.plugin.elite.kitpvp.manager.duel.custom.DuelSetup;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Duel {
	
	private List<UUID> players;
	private DuelType type;
	
	public Duel(DuelRequest request) {
		this.players = new ArrayList<>();
		this.players.add(request.getInviter().getUniqueId());
		this.players.add(request.getInvited().getUniqueId());
		this.type = request.getType();
		DuelManager.add(this);
	}

	public void delete() {
		DuelManager.remove(this);
	}
	
	public List<UUID> getPlayers() {
		return this.players;
	}
	
	public void openGUI() {
		ePlayer p1 = ePlayer.get(this.getPlayers().get(0));
		ePlayer p2 = ePlayer.get(this.getPlayers().get(1));
		DuelRequest request = DuelManager.getRequest(p1, p2);
		DuelSetup setup = new DuelSetup(p1, p2);
		for(UUID uuid : this.getPlayers()) {
			ePlayer p = ePlayer.get(uuid);
			p.getPlayer().sendMessage(p.getLanguage().get(KitPvPLanguage.DUEL_REQUEST_ACCEPTED)
					.replaceAll("%z", request.getOther(p).getName()));
			DuelGUI gui = new DuelGUI(p.getLanguage(), setup);
			p.openGUI(gui, gui.main(p));
			setup.playSound(Sound.ANVIL_USE);
		}
		request.delete();
	}
	
	public void start() {
		for(Player players : Bukkit.getOnlinePlayers()) {
			ePlayer all = ePlayer.get(players);
			if(!this.getPlayers().contains(all.getUniqueId())) {
				for(UUID uuid : this.getPlayers())
					ePlayer.get(uuid).getPlayer().hidePlayer(all.getPlayer());
			}
		}
	}
	
	public void end(ePlayer winner, ePlayer loser) {
		loser.getPlayer().sendMessage(loser.getLanguage().get(KitPvPLanguage.DUEL_LOSE)
				.replaceAll("%z", winner.getName()));
		winner.getPlayer().sendMessage(winner.getLanguage().get(KitPvPLanguage.DUEL_WIN)
				.replaceAll("%z", loser.getName()));
		winner.clear();
		for(Player players : Bukkit.getOnlinePlayers()) {
			ePlayer all = ePlayer.get(players);
			if(!all.isInvis()) {
				winner.getPlayer().showPlayer(players);
				loser.getPlayer().showPlayer(players);
			} else {
				if(all.getInvisTo().ordinal() < winner.getRank().ordinal()) {
					winner.getPlayer().showPlayer(players);
				}
				if(all.getInvisTo().ordinal() < loser.getRank().ordinal()) {
					loser.getPlayer().showPlayer(players);
				}
			}
		}
		this.delete();
	}
	
	public ePlayer getOpponent(ePlayer p) {
		for(UUID uuid : this.getPlayers()) {
			if(!uuid.equals(p.getUniqueId()))
				return ePlayer.get(uuid);
		}
		return null;
	}

	public DuelType getType() {
		return this.type;
	}

	public enum DuelType {

		NORMAL,
		CUSTOM
	}
}
