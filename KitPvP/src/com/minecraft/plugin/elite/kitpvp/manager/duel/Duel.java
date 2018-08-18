package com.minecraft.plugin.elite.kitpvp.manager.duel;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.kitpvp.KitPvP;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import com.minecraft.plugin.elite.kitpvp.manager.KitPlayer;
import com.minecraft.plugin.elite.kitpvp.manager.duel.custom.DuelGUI;
import com.minecraft.plugin.elite.kitpvp.manager.duel.custom.DuelSetup;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Duel {
	
	private List<GeneralPlayer> players;
	private DuelType type;
	private boolean started;
	
	public Duel(DuelRequest request) {
		this.players = new ArrayList<>();
		this.players.add(request.getInviter());
		this.players.add(request.getInvited());
		this.type = request.getType();
		this.started = false;
		DuelManager.add(this);
	}

	public Duel(GeneralPlayer inviter, GeneralPlayer invited, DuelType type) {
		this.players = new ArrayList<>();
		this.players.add(inviter);
		this.players.add(invited);
		this.type = type;
		DuelManager.add(this);
	}

	public void delete() {
		DuelManager.remove(this);
	}
	
	public List<GeneralPlayer> getPlayers() {
		return this.players;
	}

	public boolean hasStarted() {
		return this.started;
	}

	public void accepted() {
		GeneralPlayer p1 = this.getPlayers().get(0);
		GeneralPlayer p2 = this.getPlayers().get(1);
		DuelRequest request = DuelManager.getRequest(p1, p2);
		if (request != null) {
			for(GeneralPlayer p : this.getPlayers())
				p.getPlayer().sendMessage(p.getLanguage().get(KitPvPLanguage.DUEL_REQUEST_ACCEPTED)
						.replaceAll("%z", request.getOther(p).getName()));
			if(request.getType() == DuelType.CUSTOM) {
				DuelSetup setup = new DuelSetup(p1, p2);
				for(GeneralPlayer p : this.getPlayers()) {
					DuelGUI gui = new DuelGUI(p.getLanguage(), setup);
					p.openGUI(gui, gui.main(p));
					setup.playSound(Sound.ANVIL_USE);
				}
			} else if(request.getType() == DuelType.NORMAL) {
				for(GeneralPlayer p : this.getPlayers()) {
					p.getPlayer().closeInventory();
					p.getPlayer().setGameMode(GameMode.SURVIVAL);
					p.clear();
					p.clearTools();
					KitPlayer kp = KitPlayer.get(p.getUniqueId());
					if (kp != null) {
						kp.addDefaults(true);
						kp.setItem(KitPlayer.SlotType.SWORD, new ItemStack(Material.DIAMOND_SWORD));
					}
				}
				this.start();
			}
			request.delete();
		}
	}

	public void queueStart() {
		GeneralPlayer p1 = this.getPlayers().get(0);
		GeneralPlayer p2 = this.getPlayers().get(1);
		p1.getPlayer().sendMessage(p1.getLanguage().get(KitPvPLanguage.DUEL_REQUEST_ACCEPTED)
				.replaceAll("%z", p2.getName()));
		p2.getPlayer().sendMessage(p2.getLanguage().get(KitPvPLanguage.DUEL_REQUEST_ACCEPTED)
				.replaceAll("%z", p1.getName()));

		for(GeneralPlayer p : this.getPlayers()) {
			p.getPlayer().closeInventory();
			p.getPlayer().setGameMode(GameMode.SURVIVAL);
			p.clear();
			p.clearTools();
			KitPlayer kp = KitPlayer.get(p.getUniqueId());
			if (kp != null) {
				kp.addDefaults(true);
				kp.setItem(KitPlayer.SlotType.SWORD, new ItemStack(Material.DIAMOND_SWORD));
			}

			DuelRequest request = DuelManager.getRequest(p);
			if(request != null)
				request.delete();
		}
		this.start();
	}

	public void start() {
		for(Player players : Bukkit.getOnlinePlayers()) {
			if(!this.getPlayers().contains(GeneralPlayer.get(players)))
				for(GeneralPlayer p : this.getPlayers())
					p.getPlayer().hidePlayer(players.getPlayer());
		}
		for(int i = 0; i < this.getPlayers().size(); i++) {
			GeneralPlayer p = this.getPlayers().get(i);
			DuelManager.removeFromQueue(p.getUniqueId());
			Location loc = this.getLocation(i + 1);
			if(loc != null)
				p.getPlayer().teleport(loc);
		}
		for(GeneralPlayer p : this.getPlayers()) {
			GeneralPlayer z = this.getOpponent(p);

			Location p1 = p.getPlayer().getLocation();
			Location z1 = z.getPlayer().getLocation();

			p1.setDirection(z1.toVector().subtract(p1.toVector()));
			p.getPlayer().teleport(p1);
		}
		this.started = true;
	}
	
	public void end(GeneralPlayer winner, GeneralPlayer loser) {
		Server server = Server.get();
		server.computeELO(winner, loser, 1 , 0, 32);

		loser.getPlayer().sendMessage(loser.getLanguage().get(KitPvPLanguage.DUEL_LOSE)
				.replaceAll("%z", winner.getName()));
		winner.getPlayer().sendMessage(winner.getLanguage().get(KitPvPLanguage.DUEL_WIN)
				.replaceAll("%z", loser.getName()));

		winner.clear();

		for(Player players : Bukkit.getOnlinePlayers()) {
			GeneralPlayer all = GeneralPlayer.get(players);
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
	
	public GeneralPlayer getOpponent(GeneralPlayer p) {
		for(GeneralPlayer z : this.getPlayers()) {
			if(!z.getUniqueId().equals(p.getUniqueId()))
				return z;
		}
		return null;
	}

	public Location getLocation(int x) {
		Database db = KitPvP.getDB();
		try {
			ResultSet res = db.select(KitPvP.DB_DUEL, "location", "loc" + Integer.toString(x));
			if(res.next())
				return new Location(Bukkit.getWorld("world"), res.getDouble("locx"), res.getDouble("locy"), res.getDouble("locz"));
		} catch (SQLException e) {
			e.printStackTrace();
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
