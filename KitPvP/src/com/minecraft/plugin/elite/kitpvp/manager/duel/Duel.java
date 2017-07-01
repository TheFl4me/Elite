package com.minecraft.plugin.elite.kitpvp.manager.duel;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.kitpvp.KitPvP;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import com.minecraft.plugin.elite.kitpvp.manager.KitPlayer;
import com.minecraft.plugin.elite.kitpvp.manager.duel.custom.DuelGUI;
import com.minecraft.plugin.elite.kitpvp.manager.duel.custom.DuelSetup;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Duel {
	
	private List<UUID> players;
	private DuelType type;
	private boolean started;
	
	public Duel(DuelRequest request) {
		this.players = new ArrayList<>();
		this.players.add(request.getInviter().getUniqueId());
		this.players.add(request.getInvited().getUniqueId());
		this.type = request.getType();
		this.started = false;
		DuelManager.add(this);
	}

	public Duel(ePlayer inviter, ePlayer invited, DuelType type) {
		this.players = new ArrayList<>();
		this.players.add(inviter.getUniqueId());
		this.players.add(invited.getUniqueId());
		this.type = type;
		DuelManager.add(this);
	}

	public void delete() {
		DuelManager.remove(this);
	}
	
	public List<UUID> getPlayers() {
		return this.players;
	}

	public boolean hasStarted() {
		return this.started;
	}

	public void accepted() {
		ePlayer p1 = ePlayer.get(this.getPlayers().get(0));
		ePlayer p2 = ePlayer.get(this.getPlayers().get(1));
		DuelRequest request = DuelManager.getRequest(p1, p2);
		for(UUID uuid : this.getPlayers()) {
			ePlayer p = ePlayer.get(uuid);
			p.getPlayer().sendMessage(p.getLanguage().get(KitPvPLanguage.DUEL_REQUEST_ACCEPTED)
					.replaceAll("%z", request.getOther(p).getName()));
		}
		if(request.getType() == DuelType.CUSTOM) {
			DuelSetup setup = new DuelSetup(p1, p2);
			for(UUID uuid : this.getPlayers()) {
				ePlayer p = ePlayer.get(uuid);
				DuelGUI gui = new DuelGUI(p.getLanguage(), setup);
				p.openGUI(gui, gui.main(p));
				setup.playSound(Sound.ANVIL_USE);
			}
		} else if(request.getType() == DuelType.NORMAL) {
			for(UUID uuid : this.getPlayers()) {
				ePlayer p = ePlayer.get(uuid);
				p.getPlayer().closeInventory();
				p.getPlayer().setGameMode(GameMode.SURVIVAL);
				p.clear();
				p.clearTools();
				KitPlayer kp = KitPlayer.get(uuid);
				kp.addDefaults(true);
				kp.getPlayer().getInventory().setItem(0, new ItemStack(Material.DIAMOND_SWORD));
			}
			this.start();
		}
		request.delete();

	}

	public void queueStart() {
		ePlayer p1 = ePlayer.get(this.getPlayers().get(0));
		ePlayer p2 = ePlayer.get(this.getPlayers().get(1));
		p1.getPlayer().sendMessage(p1.getLanguage().get(KitPvPLanguage.DUEL_REQUEST_ACCEPTED)
				.replaceAll("%z", p2.getName()));
		p2.getPlayer().sendMessage(p2.getLanguage().get(KitPvPLanguage.DUEL_REQUEST_ACCEPTED)
				.replaceAll("%z", p1.getName()));

		for(UUID uuid : this.getPlayers()) {
			ePlayer p = ePlayer.get(uuid);
			p.getPlayer().closeInventory();
			p.getPlayer().setGameMode(GameMode.SURVIVAL);
			p.clear();
			p.clearTools();
			KitPlayer kp = KitPlayer.get(uuid);
			kp.addDefaults(true);
			kp.getPlayer().getInventory().setItem(kp.getSlotID(KitPlayer.SlotType.SWORD), new ItemStack(Material.DIAMOND_SWORD));

			DuelRequest request = DuelManager.getRequest(p);
			if(request != null)
				request.delete();
		}
		this.start();
	}

	public void start() {
		Database db = General.getDB();
		for(Player players : Bukkit.getOnlinePlayers()) {
			if(!this.getPlayers().contains(players.getUniqueId()))
				for(UUID uuid : this.getPlayers())
					ePlayer.get(uuid).getPlayer().hidePlayer(players.getPlayer());
		}
		for(int i = 0; i < this.getPlayers().size(); i++) {
			ePlayer p = ePlayer.get(this.getPlayers().get(i));
			DuelManager.removeFromQueue(p.getUniqueId());
			Location loc = null;
			try {
				ResultSet res = db.select(KitPvP.DB_DUEL, "location", "loc" + Integer.toString(i++));
				if(res.next())
					loc = new Location(Bukkit.getWorld("world"), res.getDouble("locx"), res.getDouble("locy"), res.getDouble("locz"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if(loc != null)
				p.getPlayer().teleport(loc);
		}
		this.started = true;
	}
	
	public void end(ePlayer winner, ePlayer loser) {
		Server server = Server.get();
		server.computeELO(winner, loser, 1 , 0, 32);

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

		Location loc = DuelManager.getDuelSpawn();
		if(loc != null)
			winner.getPlayer().teleport(loc);

		winner.clearHits();
		this.started = false;
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
