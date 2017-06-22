package com.minecraft.plugin.elite.kitpvp.listeners;

import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.events.ClearPlayerEvent;
import com.minecraft.plugin.elite.kitpvp.KitPvP;
import com.minecraft.plugin.elite.kitpvp.KitPvPLanguage;
import com.minecraft.plugin.elite.kitpvp.manager.KitPlayer;
import com.minecraft.plugin.elite.kitpvp.manager.kits.Kit;
import com.minecraft.plugin.elite.kitpvp.manager.kits.KitGUI;
import com.minecraft.plugin.elite.kitpvp.manager.kits.events.KitChangeEvent;
import com.minecraft.plugin.elite.nohax.manager.HaxPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class KitEventListener implements Listener {


	public void cleanUp(ePlayer p ) {
		if(chargeTask.containsKey(p.getUniqueId())) {
			chargeTask.get(p.getUniqueId()).cancel();
			chargeTask.remove(p.getUniqueId());
		}
		if(invincibleTask.containsKey(p.getUniqueId())) {
			invincibleTask.get(p.getUniqueId()).cancel();
			invincibleTask.remove(p.getUniqueId());
		}
		if(empty.contains(p.getUniqueId()))
			empty.remove(p.getUniqueId());
		if(frostyTask.containsKey(p.getUniqueId())) {
			frostyTask.get(p.getUniqueId()).cancel();
			frostyTask.remove(p.getUniqueId());
		}
		if(saveTask.containsKey(p.getUniqueId())) {
			saveTask.get(p.getUniqueId()).cancel();
			saveTask.remove(p.getUniqueId());
		}
		if(tpTo.containsKey(p.getUniqueId())) {
			tpTo.remove(p.getUniqueId());
		}
		if(poseidonTask.containsKey(p.getUniqueId())) {
			poseidonTask.get(p.getUniqueId()).cancel();
			poseidonTask.remove(p.getUniqueId());
		}
		if(rogueTask.containsKey(p.getUniqueId())) {
			rogueTask.get(p.getUniqueId()).cancel();
			rogueTask.remove(p.getUniqueId());
		}
		if(teleportScanTask.containsKey(p.getUniqueId())) {
			teleportScanTask.get(p.getUniqueId()).cancel();
			teleportScanTask.remove(p.getUniqueId());
		}
		if(tpedTask.containsKey(p.getUniqueId())) {
			tpedTask.get(p.getUniqueId()).cancel();
			tpedTask.remove(p.getUniqueId());
		}
		if(teleportScanTimer.containsKey(p.getUniqueId())) {
			teleportScanTimer.remove(p.getUniqueId());
		}
		if(oldBlock.containsKey(p.getUniqueId())) {
			BlockState state = oldBlock.get(p.getUniqueId());
			Block block = p.getPlayer().getWorld().getBlockAt(state.getLocation());
			block.setType(state.getType());
			state.setData(state.getData());
			state.update();
			oldBlock.remove(p.getUniqueId());
		}
		if(jump.contains(p.getUniqueId())) {
			jump.remove(p.getUniqueId());
		}
		if(flyTimer.containsKey(p.getUniqueId())) {
			flyTimer.remove(p.getUniqueId());
		}
		if(flyTask.containsKey(p.getUniqueId())) {
			flyTask.get(p.getUniqueId()).cancel();
			flyTask.remove(p.getUniqueId());
		}
		if(locs.containsKey(p.getUniqueId())) {
			locs.remove(p.getUniqueId());
		}
		if(levels.containsKey(p.getUniqueId())) {
			levels.remove(p.getUniqueId());
		}
		cleanUpGladiator(p);
	}

	@EventHandler
	public void cleanOnClear(ClearPlayerEvent e) {
		ePlayer p = e.getPlayer();
		cleanUp(p);
	}
	
	//Anchor
	@EventHandler
	public void AnchorKnockback(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			final KitPlayer target = KitPlayer.get((Player) e.getEntity());
			KitPlayer hitter = KitPlayer.get((Player) e.getDamager());
			if((hitter.hasKit(Kit.ANCHOR) || target.hasKit(Kit.ANCHOR)) && (!hitter.isNearRogue() && !target.isNearRogue())) {
				if(hitter.hasSpawnProtection() || target.hasSpawnProtection()) {
					return;
				}
				target.getPlayer().setVelocity(new Vector());
				Bukkit.getScheduler().runTaskLater(KitPvP.getPlugin(), () -> target.getPlayer().setVelocity(new Vector()), 1L);
				hitter.getPlayer().getWorld().playSound(hitter.getPlayer().getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
				target.getPlayer().getWorld().playSound(target.getPlayer().getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
			}
		}
	}
	
	//Archer
	@EventHandler
	public void giveArrow(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) e.getDamager();
			if(arrow.getShooter() instanceof Player && e.getEntity() instanceof Player) {
				ePlayer z = ePlayer.get(e.getEntity().getUniqueId());
				if(z.isAdminMode() || z.isWatching())
					return;
				KitPlayer shooter = KitPlayer.get((Player) arrow.getShooter());
				if(shooter.hasKit(Kit.ARCHER) && !shooter.isNearRogue()) {
					shooter.getPlayer().getInventory().addItem(new ItemStack(Material.ARROW));
				}
			}		
		}
	}
	
	//Backup
	@EventHandler
	public void onBackup(PlayerInteractEvent e) {
		ePlayer p = ePlayer.get(e.getPlayer());
		KitPlayer kp = KitPlayer.get(p.getUniqueId());
		ItemStack item = p.getPlayer().getItemInHand();
		if(item == null || item.getType() == Material.AIR)
			return;
		if(item.getItemMeta().hasDisplayName()) {
			if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && (item.getItemMeta().getDisplayName().equalsIgnoreCase(p.getLanguage().get(KitPvPLanguage.KIT_BACKUP_ITEM))) && (kp.hasKit(Kit.BACKUP)) && !kp.isNearRogue()) {
				KitGUI gui = new KitGUI(p.getLanguage());
				p.openGUI(gui, gui.selector(kp, 1));
			}
		}
	}
	
	//Fisherman
	@EventHandler
    public void fisherman(PlayerFishEvent e) {
    	KitPlayer p = KitPlayer.get(e.getPlayer());
    	if ((e.getCaught() instanceof Player)) {
    		final KitPlayer caught = KitPlayer.get((Player) e.getCaught());
			ePlayer cp = ePlayer.get(caught.getUniqueId());
    		if(p.hasKit(Kit.FISHERMAN) && !p.isNearRogue() && !cp.isAdminMode() && !cp.isWatching()) {
				if(p.hasSpawnProtection() || caught.hasSpawnProtection())
					return;
    			caught.getPlayer().teleport(p.getPlayer().getLocation());
				if(fight.containsKey(cp.getUniqueId()) && !fight.containsKey(p.getUniqueId()))
					cleanUpGladiator(cp);
    		}
    	}
    }
	
	//Kangaroo
    private Collection<UUID> jump = new HashSet<>();

	@EventHandler
    public void kangaroo(PlayerInteractEvent e) {
    	KitPlayer p = KitPlayer.get(e.getPlayer());
		ItemStack item = p.getPlayer().getItemInHand();
		if(item == null || item.getType() == Material.AIR)
			return;
		if(item.getItemMeta().hasDisplayName()) {
			if(item.getItemMeta().getDisplayName().equalsIgnoreCase(p.getLanguage().get(KitPvPLanguage.KIT_KANGAROO_ITEM)) && p.hasKit(Kit.KANGAROO)) {
				e.setCancelled(true);
				if (!jump.contains(p.getUniqueId()) && !p.isNearRogue()) {
					if(p.hasSpawnProtection())
						return;
					Location loc = p.getPlayer().getLocation();
					Vector jumpVec = loc.getDirection();
					loc.setPitch(0.0F);
					jump.add(p.getUniqueId());
					boolean nearby = false;
					for(Entity entity : p.getPlayer().getNearbyEntities(5, 5, 5)) {
						if(entity instanceof Player) {
							ePlayer z = ePlayer.get(entity.getUniqueId());
							if(!z.isAdminMode() && !z.isWatching()) {
								nearby = true;
								break;
							}
						}
					}
					if(!nearby) {
						if (p.getPlayer().isSneaking()) {
							jumpVec.setY(0.4D);
							jumpVec.multiply(1.25D);
						} else {
							jumpVec.setY(1.25D);
							jumpVec.multiply(0.8D);
						}
					} else {
						if (p.getPlayer().isSneaking()) {
							jumpVec.setY(0.2D);
							jumpVec.multiply(0.6D);
						} else {
							jumpVec.setY(0.6D);
							jumpVec.multiply(0.4D);
						}
					}
					p.getPlayer().setVelocity(jumpVec);
				}
			}
		}
    }

	@SuppressWarnings("deprecation")
	@EventHandler
    public void kangaRemove(PlayerMoveEvent e) {
		KitPlayer p = KitPlayer.get(e.getPlayer());
    	if (jump.contains(p.getUniqueId()) && p.hasKit(Kit.KANGAROO) && p.getPlayer().isOnGround()) {
			jump.remove(p.getUniqueId());
    	}
    }
	
	//Phantom
	private Map<UUID, BukkitRunnable> flyTask = new HashMap<>();
	private HashMap<UUID, Integer> flyTimer = new HashMap<>();

	@EventHandler
    public void PhantomFly(PlayerInteractEvent e) {
		final KitPlayer p = KitPlayer.get(e.getPlayer());
    	if ((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
    		ItemStack item = p.getPlayer().getItemInHand();
			if(item == null)
				return;
    		if(item.getType() == Material.FEATHER && p.hasKit(Kit.PHANTOM) && !p.isNearRogue()) {
    			if(p.hasSpawnProtection())
    				return;
    			if(p.isCooldowned()) {
    				e.getPlayer().sendMessage(p.getLanguage().get(KitPvPLanguage.KIT_COOLDOWN)
							.replaceAll("%seconds", Integer.toString(p.getCooldownTime())));
    				e.setCancelled(true);
    			} else if(!flyTask.containsKey(p.getUniqueId())){
    				p.getPlayer().setAllowFlight(true);
    				p.getPlayer().setFlying(true);
    				PlayerInventory inv = p.getPlayer().getInventory();
    				final ItemStack boots = p.getPlayer().getInventory().getBoots();
    				final ItemStack helm = p.getPlayer().getInventory().getHelmet();
    				final ItemStack chest = p.getPlayer().getInventory().getChestplate();
    				final ItemStack legs = p.getPlayer().getInventory().getLeggings();
    				inv.setHelmet(new ItemStack(Material.LEATHER_HELMET));
    				inv.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
    				inv.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
    				inv.setBoots(new ItemStack(Material.LEATHER_BOOTS));
    				p.getPlayer().getWorld().playSound(p.getPlayer().getLocation(), Sound.WITHER_DEATH, 20F, 20F);
					p.getPlayer().getNearbyEntities(20F, 20F, 20F).forEach((entity) -> {
						if(entity instanceof Player) {
							ePlayer all = ePlayer.get((Player) entity);
							all.getPlayer().sendMessage(all.getLanguage().get(KitPvPLanguage.PHANTOM_WARNING).replaceAll("%p", p.getName()));
						}
					});
					flyTimer.put(p.getUniqueId(), 5);
					flyTask.put(p.getUniqueId(), new BukkitRunnable() {
						@Override
						public void run() {
							final int index = flyTimer.get(p.getUniqueId());
							flyTimer.remove(p.getUniqueId());
							flyTimer.put(p.getUniqueId(), index - 1);
							p.getPlayer().sendMessage(p.getLanguage().get(KitPvPLanguage.PHANTOM_FLIGHT_TIME)
									.replaceAll("%seconds", Integer.toString(flyTimer.get(p.getUniqueId()))));
							if(flyTimer.get(p.getUniqueId()) <= 0) {
								p.getPlayer().setAllowFlight(false);
								p.getPlayer().setFlying(false);
								p.getPlayer().getInventory().setHelmet(helm);
								p.getPlayer().getInventory().setChestplate(chest);
								p.getPlayer().getInventory().setLeggings(legs);
								p.getPlayer().getInventory().setBoots(boots);
								p.getPlayer().getWorld().playSound(p.getPlayer().getLocation(), Sound.WITHER_SPAWN, 20F, 20F);
								p.startCooldown(60);
								flyTimer.remove(p.getUniqueId());
								flyTask.get(p.getUniqueId()).cancel();
								flyTask.remove(p.getUniqueId());
							}
						}
					});
					flyTask.get(p.getUniqueId()).runTaskTimer(KitPvP.getPlugin(), 20, 20);
    			}
    		}
    	}
    }
    
    @EventHandler
    public void PhantomFall(EntityDamageEvent e) {
    	if(e.getEntity() instanceof Player && e.getCause() == DamageCause.FALL) {
    		KitPlayer p = KitPlayer.get((Player) e.getEntity());
    		if(p.hasKit(Kit.PHANTOM) && p.isCooldowned())
    			e.setDamage(e.getDamage() * 2);
    	}
    }

    @EventHandler
	public void dontMoveArmor(InventoryClickEvent e) {
    	ePlayer p = ePlayer.get((Player) e.getWhoClicked());
		if(e.getSlotType() == InventoryType.SlotType.ARMOR && flyTask.containsKey(p.getUniqueId()))
			e.setCancelled(true);
	}

	@EventHandler
	public void dontDropArmor(PlayerDropItemEvent e) {
		ePlayer p = ePlayer.get(e.getPlayer());
		if(flyTask.containsKey(p.getUniqueId())) {
			switch(e.getItemDrop().getItemStack().getType()) {
				case LEATHER_BOOTS: p.getPlayer().getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
					e.getItemDrop().remove();
					break;
				case LEATHER_LEGGINGS: p.getPlayer().getInventory().setBoots(new ItemStack(Material.LEATHER_LEGGINGS));
					e.getItemDrop().remove();
					break;
				case LEATHER_CHESTPLATE: p.getPlayer().getInventory().setBoots(new ItemStack(Material.LEATHER_CHESTPLATE));
					e.getItemDrop().remove();
					break;
				case LEATHER_HELMET: p.getPlayer().getInventory().setBoots(new ItemStack(Material.LEATHER_HELMET));
					e.getItemDrop().remove();
					break;
			}
		}
	}

	@EventHandler
	public void clearArmorOnDeath(PlayerDeathEvent e) {
		ePlayer p = ePlayer.get(e.getEntity());
		if(flyTask.containsKey(p.getUniqueId())) {
			List<ItemStack> list = e.getDrops();
			Iterator<ItemStack> i = list.iterator();
			while(i.hasNext()) {
				ItemStack item = i.next();
				switch(item.getType()) {
					case LEATHER_BOOTS:
					case LEATHER_LEGGINGS:
					case LEATHER_CHESTPLATE:
					case LEATHER_HELMET:
						i.remove();
				}
			}
		}
	}
    
    //Snail
    @EventHandler
	public void SnailHit(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			KitPlayer target = KitPlayer.get((Player) e.getEntity());
			KitPlayer hitter = KitPlayer.get((Player) e.getDamager());
			ePlayer et = ePlayer.get(target.getUniqueId());
			if(hitter.hasKit(Kit.SNAIL) && !hitter.isNearRogue() && !et.isWatching() && !et.isAdminMode()) {
				if(hitter.hasSpawnProtection() || target.hasSpawnProtection())
					return;
				Random r = new Random();
				int per = r.nextInt(100) + 1;
				if (per <= 33) 
					target.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
			}
		}
	}
    
    //Stomper
    @EventHandler
	public void Stomp(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			KitPlayer p = KitPlayer.get((Player) e.getEntity());
			if(p.hasKit(Kit.STOMPER) && e.getCause() == DamageCause.FALL && !p.isNearRogue()) {
				if(p.hasSpawnProtection())
					return;
				p.getPlayer().getWorld().playSound(p.getPlayer().getLocation(), Sound.ANVIL_LAND, 1F, 1F);
				List<Entity> nearby = p.getPlayer().getNearbyEntities(4, 4, 4);
				for (Entity entity : nearby) {
					if(entity instanceof Player) {
						KitPlayer target = KitPlayer.get((Player) entity);
						if(target.hasSpawnProtection())
							continue;
						double damage = e.getDamage();
						if(target.getPlayer().isSneaking())
							damage = 8;		
						target.getPlayer().damage(damage);
					}
				}
				if(p.getPlayer().getFallDistance() > 6) {
					p.getPlayer().damage(4);
					e.setCancelled(true);
				}
			}
		}
	}
    
    //Thor
	@EventHandler
	public void thor(PlayerInteractEvent e) {
		final KitPlayer p = KitPlayer.get(e.getPlayer());
		ItemStack item = p.getPlayer().getItemInHand();
		if(item == null || item.getType() == Material.AIR)
			return;
		if(item.getItemMeta().hasDisplayName()) {
			if(p.hasKit(Kit.THOR) && (e.getAction() == Action.RIGHT_CLICK_BLOCK) && item.getItemMeta().getDisplayName().equalsIgnoreCase(p.getLanguage().get(KitPvPLanguage.KIT_THOR_ITEM)) && !p.isNearRogue()) {
				if(p.hasSpawnProtection())
					return;
				if(p.isCooldowned()) {
					e.getPlayer().sendMessage(p.getLanguage().get(KitPvPLanguage.KIT_COOLDOWN)
							.replaceAll("%seconds", Integer.toString(p.getCooldownTime())));
				} else {
					e.getPlayer().getWorld().strikeLightning(p.getPlayer().getWorld().getHighestBlockAt(e.getClickedBlock().getLocation()).getLocation()).setFireTicks(100);
					p.startCooldown(5);
				}
			}
		}
	}
	
	
	@EventHandler
	public void onStrike(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			KitPlayer p = KitPlayer.get((Player) e.getEntity());
			if(p.hasKit(Kit.THOR) && (e.getCause() == DamageCause.LIGHTNING) && !p.isNearRogue())
				e.setCancelled(true);
		}
	}
	
	//Turtle
	@EventHandler
	public void TurtleDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {
			KitPlayer hitter = KitPlayer.get((Player) e.getDamager());
			if(hitter.hasKit(Kit.TURTLE)) {
				if(hitter.getPlayer().isSneaking()) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void TurtleDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			KitPlayer target = KitPlayer.get((Player) e.getEntity());
			if(target.hasKit(Kit.TURTLE) && !target.isNearRogue()) {
				if(target.getPlayer().isSneaking()) {
					e.setDamage(2.0);
					return;
				}
				if(target.getPlayer().isSneaking() && target.getPlayer().isBlocking()) {
					e.setDamage(1.0);
				}
			}
		}
	}
	
	//Viper
	@EventHandler
	public void ViperHit(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			KitPlayer target = KitPlayer.get((Player) e.getEntity());
			KitPlayer hitter = KitPlayer.get((Player) e.getDamager());
			ePlayer et = ePlayer.get(target.getUniqueId());
			if(hitter.hasKit(Kit.VIPER) && !hitter.isNearRogue() && !et.isAdminMode() && !et.isWatching()) {
				if(hitter.hasSpawnProtection() || target.hasSpawnProtection())
					return;
				Random r = new Random();
				int per = r.nextInt(100) + 1;
				if (per <= 33) {
					target.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 1));
				}
			}
		}
	}

	private Map<UUID, BukkitRunnable> teleportScanTask = new HashMap<>();
	private HashMap<UUID, Integer> teleportScanTimer = new HashMap<>();
	private Map<UUID, BukkitRunnable> tpedTask = new HashMap<>();
	private Map<UUID, BlockState> oldBlock = new HashMap<>();

	//Endermage
	@EventHandler
	public void PortalPlace(PlayerInteractEvent e) {
		final KitPlayer p = KitPlayer.get(e.getPlayer());
		ItemStack item = p.getPlayer().getItemInHand();
		if(item == null || item.getType() == Material.AIR)
			return;
		if(item.getItemMeta().hasDisplayName()) {
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK && item.getItemMeta().getDisplayName().equalsIgnoreCase(p.getLanguage().get(KitPvPLanguage.KIT_ENDERMAGE_ITEM)) && !p.isCooldowned() && !teleportScanTimer.containsKey(p.getUniqueId()) && p.hasKit(Kit.ENDERMAGE) && !p.isNearRogue()) {
				if(p.hasSpawnProtection())
					return;
				Block above = e.getClickedBlock().getRelative(BlockFace.UP);
				if(!above.getType().isSolid() && !above.getRelative(BlockFace.UP).getType().isSolid()) {
					Block block = e.getClickedBlock();
					oldBlock.put(p.getUniqueId(), block.getState());
					Location loc = above.getLocation();
					block.setType(Material.ENDER_PORTAL_FRAME);
					teleportScanTimer.put(p.getUniqueId(), 0);
					teleportScanTask.put(p.getUniqueId(), new BukkitRunnable() {
						@Override
						public void run() {
							BlockState state = oldBlock.get(p.getUniqueId());
							Collection<UUID> targets = new ArrayList<>();
							Location pLoc = p.getPlayer().getLocation();
							for(Entity ent : p.getPlayer().getNearbyEntities(5.0, block.getWorld().getMaxHeight(), 5.0)) {
								if(ent instanceof Player) {
									Location zLoc = ent.getLocation();
									ePlayer z = ePlayer.get(ent.getUniqueId());
									int zY = zLoc.getBlockY();
									int pY = pLoc.getBlockY();
									if(zY - pY < 3 && zY - pY > -3)
										continue;
									if(!targets.contains(ent.getUniqueId()) && !z.isAdminMode() && !z.isWatching() && !z.getUniqueId().equals(p.getUniqueId()))
										targets.add(ent.getUniqueId());
								}
							}

							final int timer = teleportScanTimer.get(p.getUniqueId());
							teleportScanTimer.remove(p.getUniqueId());
							teleportScanTimer.put(p.getUniqueId(), timer + 1);
							if(!targets.isEmpty() || teleportScanTimer.get(p.getUniqueId()) >= 3) {
								if(!targets.isEmpty()) {
									targets.add(p.getUniqueId());
									for(UUID uuid : targets) {
										KitPlayer t = KitPlayer.get(uuid);
										t.getPlayer().teleport(loc);
										t.sendMessage(KitPvPLanguage.ENDERMAGE_WARNING);
										if(fight.containsKey(t.getUniqueId()) && !fight.containsKey(p.getUniqueId()))
											cleanUpGladiator(t);
										tpedTask.put(t.getUniqueId(), new BukkitRunnable() {
											@Override
											public void run() {
												if(tpedTask.containsKey(t.getUniqueId())) {
													tpedTask.get(t.getUniqueId()).cancel();
													tpedTask.remove(t.getUniqueId());
												}
											}
										});
										tpedTask.get(t.getUniqueId()).runTaskLater(KitPvP.getPlugin(), 100);
									}
								}
								teleportScanTask.remove(p.getUniqueId());
								teleportScanTimer.remove(p.getUniqueId());
								block.setType(state.getType());
								state.setData(state.getData());
								state.update();
								oldBlock.remove(p.getUniqueId());
								p.startCooldown(5);
								cancel();
							}
						}
					});
					teleportScanTask.get(p.getUniqueId()).runTaskTimer(KitPvP.getPlugin(), 0, 20);
				}
			}
		}
	}

	@EventHandler
	public void onMagedHit(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			if(tpedTask.containsKey(e.getEntity().getUniqueId()))
				e.setCancelled(true);
		}
	}

	//Neo
	@EventHandler
	public void cancelNeoDamage(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Projectile) {
			KitPlayer p = KitPlayer.get((Player) e.getEntity());
			Projectile proj = (Projectile) e.getDamager();
			if(p.hasKit(Kit.NEO) && !p.isNearRogue()) {
				if(p.hasSpawnProtection())
					return;
				e.setCancelled(true);
				final Vector v = proj.getVelocity().multiply(-1);
				Projectile newProj = p.getPlayer().launchProjectile(proj.getClass());
				proj.remove();
				newProj.setVelocity(v);
			}
		}
	}

	//Monk
	@EventHandler
	public void onMonk(PlayerInteractEntityEvent e) {
		if(e.getRightClicked() instanceof Player) {
			KitPlayer hitter = KitPlayer.get(e.getPlayer());
			KitPlayer target = KitPlayer.get((Player) e.getRightClicked());
			ePlayer et = ePlayer.get(target.getUniqueId());
			if(hitter.hasKit(Kit.MONK) && hitter.getPlayer().getItemInHand().getType() == Material.BLAZE_ROD && !hitter.isNearRogue() && !et.isAdminMode() && !et.isWatching()) {
				if(hitter.hasSpawnProtection() || target.hasSpawnProtection())
					return;
				if(hitter.isCooldowned()) {
					hitter.getPlayer().sendMessage(hitter.getLanguage().get(KitPvPLanguage.KIT_COOLDOWN)
							.replaceAll("%seconds", Integer.toString(hitter.getCooldownTime())));
					return;
				}
				PlayerInventory inv = target.getPlayer().getInventory();
				final ItemStack inHand = target.getPlayer().getItemInHand();
				Random r = new Random();
				int i = r.nextInt(35);

				inv.setItem(inv.getHeldItemSlot(), inv.getItem(i));
				inv.setItem(i, inHand);

				hitter.startCooldown(10);
			}
		}
	}

	private Map<UUID, BukkitRunnable> rogueTask = new HashMap<>();

	//Rogue
	@EventHandler
	public void onRogue(KitChangeEvent e) {
		KitPlayer p = e.getPlayer();
		if(e.getKit() == Kit.ROGUE) {
			rogueTask.put(p.getUniqueId(), new BukkitRunnable() {
				@Override
				public void run() {
					KitPlayer kp = KitPlayer.get(p.getUniqueId());
					if(kp != null && kp.hasKit(Kit.ROGUE)) {
						if(kp.hasSpawnProtection())
							return;
						p.getPlayer().getNearbyEntities(10, 10, 10).stream().filter(entity -> entity instanceof Player).forEach(entity -> {
							KitPlayer all = KitPlayer.get((Player) entity);
							if(!all.isAdminMode() && !all.isWatching() && all.hasKit() && !all.hasSpawnProtection()) {
								all.sendMessage(KitPvPLanguage.ROGUE_BLOCKING);
							}
						});
					} else {
						cancel();
						rogueTask.remove(p.getUniqueId());
					}
				}
			});
			rogueTask.get(p.getUniqueId()).runTaskTimer(KitPvP.getPlugin(), 0, 100);
		}
	}


	private Map<UUID, BukkitRunnable> poseidonTask = new HashMap<>();

	//Poseidon
	@EventHandler
	public void onPoseidon(KitChangeEvent e) {
		KitPlayer p = e.getPlayer();
		if(e.getKit() == Kit.POSEIDON) {
			poseidonTask.put(p.getUniqueId(), new BukkitRunnable() {
				@Override
				public void run() {
					KitPlayer kp = KitPlayer.get(p.getUniqueId());
					if(kp != null && kp.hasKit(Kit.POSEIDON)) {
						if(kp.hasSpawnProtection())
							return;
						if(!kp.isNearRogue() &&  (kp.getPlayer().getLocation().getBlock().getType() == Material.WATER || kp.getPlayer().getLocation().getBlock().getType() == Material.STATIONARY_WATER)) {
							kp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40, 0));
							kp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 2));
							kp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40, 1));
							kp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 40, 1));
						}
					} else {
						cancel();
						poseidonTask.remove(p.getUniqueId());
					}
				}
			});
			poseidonTask.get(p.getUniqueId()).runTaskTimer(KitPvP.getPlugin(), 0, 20);
		}
	}

	private Map<UUID, UUID> tpTo = new HashMap<>();
	private Map<UUID, BukkitRunnable> saveTask = new HashMap<>();

	//Ninja
	@EventHandler
	public void saveNinjaOnHit(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			final KitPlayer target = KitPlayer.get((Player) e.getEntity());
			final KitPlayer hitter = KitPlayer.get((Player) e.getDamager());
			ePlayer et = ePlayer.get(target.getUniqueId());
			if(hitter.hasKit(Kit.NINJA) && !et.isWatching() && !et.isAdminMode() && target.hasKit()) {
				if(hitter.hasSpawnProtection() || target.hasSpawnProtection())
					return;
				if(tpTo.containsKey(hitter.getUniqueId()))
					tpTo.remove(hitter.getUniqueId());
				tpTo.put(hitter.getUniqueId(), target.getUniqueId());
				if(saveTask.containsKey(hitter.getUniqueId())) {
					saveTask.get(hitter.getUniqueId()).cancel();
					saveTask.remove(hitter.getUniqueId());
				}
				saveTask.put(hitter.getUniqueId(), new BukkitRunnable() {
					@Override
					public void run() {
						tpTo.remove(hitter.getUniqueId());
						cancel();
						saveTask.remove(hitter.getUniqueId());
					}
				});
				saveTask.get(hitter.getUniqueId()).runTaskLater(KitPvP.getPlugin(), 100);
			}
		}
	}

	@EventHandler
	public void onShift(PlayerToggleSneakEvent e) {
		KitPlayer p = KitPlayer.get(e.getPlayer());
		if(e.isSneaking() && p.hasKit(Kit.NINJA) && !p.isNearRogue()) {
			if(tpTo.containsKey(p.getUniqueId())) {
				KitPlayer z = KitPlayer.get(tpTo.get(p.getUniqueId()));
				if(!z.isWatching() && !z.isAdminMode() && z.hasKit()) {
					p.getPlayer().teleport(z.getPlayer().getLocation());
					if(fight.containsKey(p.getUniqueId()))
						if(!fight.get(p.getUniqueId()).equals(z.getUniqueId()))
							cleanUpGladiator(p);
				}
				p.startCooldown(10);
				tpTo.remove(p.getUniqueId());
				if(saveTask.containsKey(p.getUniqueId())) {
					saveTask.get(p.getUniqueId()).cancel();
					saveTask.remove(p.getUniqueId());
				}
			}
		}
	}

	@EventHandler
	public void clearOnDeath(PlayerDeathEvent e) {
		if(tpTo.values().contains(e.getEntity().getUniqueId())) {
			for(UUID uuid : tpTo.keySet()) {
				if(tpTo.get(uuid).equals(e.getEntity().getUniqueId())) {
					tpTo.remove(uuid);
					if(saveTask.containsKey(uuid)) {
						saveTask.get(uuid).cancel();
						saveTask.remove(uuid);
					}
					break;
				}
			}
		}
	}

	//Switcher
	@EventHandler
	public void onSnowball(ProjectileLaunchEvent e) {
		if(e.getEntity() instanceof Snowball && e.getEntity().getShooter() instanceof Player) {
			KitPlayer p = KitPlayer.get((Player) e.getEntity().getShooter());
			if(p.hasKit(Kit.SWITCHER)) {
				if(p.hasSpawnProtection()) {
					e.setCancelled(true);
					p.getPlayer().getInventory().addItem(new ItemStack(Material.SNOW_BALL));
					return;
				}
				if(p.isCooldowned()) {
					p.getPlayer().sendMessage(p.getLanguage().get(KitPvPLanguage.KIT_COOLDOWN)
							.replaceAll("%seconds", Integer.toString(p.getCooldownTime())));
					e.setCancelled(true);
					p.getPlayer().getInventory().addItem(new ItemStack(Material.SNOW_BALL));
				} else
					p.startCooldown(10);
			}
		}
	}

	@EventHandler
	public void onSwitcherHit(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Projectile) {
			Projectile proj = (Projectile) e.getDamager();
			KitPlayer target = KitPlayer.get((Player) e.getEntity());
			ePlayer et = ePlayer.get(target.getUniqueId());
			if(proj.getShooter() instanceof Player && proj instanceof Snowball) {
				KitPlayer thrower = KitPlayer.get((Player) proj.getShooter());
				if(thrower.hasKit(Kit.SWITCHER) && !thrower.isNearRogue() && !et.isAdminMode() && !et.isWatching() && !thrower.hasSpawnProtection()) {
					final Location switcherLoc = thrower.getPlayer().getLocation();
					final Location targetLoc = target.getPlayer().getLocation();
					thrower.getPlayer().teleport(targetLoc);
					target.getPlayer().teleport(switcherLoc);
				}
			}
		}
	}

	//Magma
	@EventHandler
	public void MagmaHit(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			KitPlayer target = KitPlayer.get((Player) e.getEntity());
			KitPlayer hitter = KitPlayer.get((Player) e.getDamager());
			ePlayer et = ePlayer.get(target.getUniqueId());
			if(hitter.hasKit(Kit.MAGMA) && !hitter.isNearRogue() && !et.isWatching() && !et.isAdminMode()) {
				if(hitter.hasSpawnProtection() || target.hasSpawnProtection())
					return;
				Random r = new Random();
				int per = r.nextInt(100) + 1;
				if (per <= 33) {
					target.getPlayer().setFireTicks(100);
				}
			}
		}
	}

	//Reaper
	@EventHandler
	public void ReaperHit(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			KitPlayer target = KitPlayer.get((Player) e.getEntity());
			KitPlayer hitter = KitPlayer.get((Player) e.getDamager());
			ePlayer et = ePlayer.get(target.getUniqueId());
			ItemStack item = hitter.getPlayer().getItemInHand();
			if(item == null || item.getType() == Material.AIR)
				return;
			if(item.getItemMeta().hasDisplayName()) {
				if(hitter.hasKit(Kit.REAPER) && !hitter.isNearRogue() && item.getItemMeta().getDisplayName().equalsIgnoreCase(hitter.getLanguage().get(KitPvPLanguage.KIT_REAPER_ITEM)) && !et.isAdminMode() && !et.isWatching()) {
					if(hitter.hasSpawnProtection())
						return;
					target.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 1));
				}
			}
		}
	}

	private Map<UUID, BukkitRunnable> frostyTask = new HashMap<>();

	//Frosty
	@EventHandler
	public void onFrosty(KitChangeEvent e) {
		KitPlayer p = e.getPlayer();
		if(e.getKit() == Kit.FROSTY) {
			frostyTask.put(p.getUniqueId(), new BukkitRunnable() {
				@Override
				public void run() {
					KitPlayer kp = KitPlayer.get(p.getUniqueId());
					if(kp != null && kp.hasKit(Kit.FROSTY)) {
						if(kp.hasSpawnProtection())
							return;
						Material block_below = kp.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
						Material block = kp.getPlayer().getLocation().getBlock().getType();
						if(!kp.isNearRogue() &&  (block == Material.SNOW || block_below == Material.SNOW_BLOCK || block_below == Material.ICE || block_below == Material.PACKED_ICE)) {
							kp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 3));
							kp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 0));
						}
					} else {
						cancel();
						frostyTask.remove(p.getUniqueId());
					}
				}
			});
			frostyTask.get(p.getUniqueId()).runTaskTimer(KitPvP.getPlugin(), 0, 20);
		}
	}

	private Map<UUID, BukkitRunnable> chargeTask = new HashMap<>();
	private Map<UUID, BukkitRunnable> invincibleTask = new HashMap<>();
	private Collection<UUID> empty = new ArrayList<>();


	//Titan
	@EventHandler
	public void onTitanClick(PlayerInteractEvent e) {
		KitPlayer p = KitPlayer.get(e.getPlayer());
		ItemStack item = p.getPlayer().getItemInHand();
		if(item == null || item.getType() == Material.AIR)
			return;
		if(item.getItemMeta().hasDisplayName()) {
			if(p.hasKit(Kit.TITAN) && item.getItemMeta().getDisplayName().equalsIgnoreCase(p.getLanguage().get(KitPvPLanguage.KIT_TITAN_ITEM)) && !p.isNearRogue() && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
				if(invincibleTask.containsKey(p.getUniqueId())) {
					p.sendMessage(KitPvPLanguage.TITAN_ALREADY);
					return;
				}
				if(p.hasSpawnProtection())
					return;
				if(empty.contains(p.getUniqueId())) {
					p.sendMessage(KitPvPLanguage.TITAN_NEED_CHARGE);
				} else {
					p.sendMessage(KitPvPLanguage.TITAN_ENABLE);
					p.getPlayer().getWorld().playEffect(p.getPlayer().getLocation(), Effect.MOBSPAWNER_FLAMES, 100);
					p.getPlayer().getWorld().playSound(p.getPlayer().getLocation(), Sound.IRONGOLEM_DEATH, 1, 1);
					invincibleTask.put(p.getUniqueId(), new BukkitRunnable() {
						@Override
						public void run() {
							if(invincibleTask.containsKey(p.getUniqueId())) {
								p.sendMessage(KitPvPLanguage.TITAN_DISABLE);
								invincibleTask.get(p.getUniqueId()).cancel();
								invincibleTask.remove(p.getUniqueId());
								empty.add(p.getUniqueId());
							}
						}
					});
					invincibleTask.get(p.getUniqueId()).runTaskLater(KitPvP.getPlugin(), 200);
				}
			}
		}
	}

	@EventHandler
	public void onHitTitan(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			KitPlayer p = KitPlayer.get((Player) e.getEntity());
			if(p.hasKit(Kit.TITAN) && !p.isNearRogue() && invincibleTask.containsKey(p.getUniqueId())) {
				if(p.hasSpawnProtection())
					return;
				e.setCancelled(true);
				if(e instanceof EntityDamageByEntityEvent) {
					EntityDamageByEntityEvent ee = (EntityDamageByEntityEvent) e;
					if(ee.getDamager() instanceof Player) {
						KitPlayer z = KitPlayer.get((Player) ee.getDamager());
						z.getPlayer().playSound(p.getPlayer().getLocation(), Sound.IRONGOLEM_HIT, 1, 1);
						z.sendMessage(KitPvPLanguage.TITAN_HIT);
					}
				}
			}
		}
	}

	@EventHandler
	public void startTitanCharge(PlayerToggleSneakEvent e) {
		KitPlayer p = KitPlayer.get(e.getPlayer());
		if(p.hasKit(Kit.TITAN)) {
			if(p.hasSpawnProtection())
				return;
			if(e.isSneaking()) {
				if(empty.contains(p.getUniqueId())) {
					p.sendMessage(KitPvPLanguage.TITAN_CHARGE_START);
					p.getPlayer().getWorld().playEffect(p.getPlayer().getLocation(), Effect.ENDER_SIGNAL, 100);
					chargeTask.put(p.getUniqueId(), new BukkitRunnable() {
						@Override
						public void run() {
							if(chargeTask.containsKey(p.getUniqueId())) {
								empty.remove(p.getUniqueId());
								p.sendMessage(KitPvPLanguage.TITAN_CHARGE_COMPLETE);
								chargeTask.get(p.getUniqueId()).cancel();
								chargeTask.remove(p.getUniqueId());
							}
						}
					});
					chargeTask.get(p.getUniqueId()).runTaskLater(KitPvP.getPlugin(), 600);
				}
			} else {
				if(chargeTask.containsKey(p.getUniqueId())) {
					p.sendMessage(KitPvPLanguage.TITAN_CHARGE_ABORT);
					chargeTask.get(p.getUniqueId()).cancel();
					chargeTask.remove(p.getUniqueId());
				}
			}
		}
	}

	@EventHandler
	public void titanHitWhileCharge(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {
			KitPlayer hitter = KitPlayer.get((Player) e.getDamager());
			if(hitter.hasKit(Kit.TITAN))
				if(chargeTask.containsKey(hitter.getUniqueId()))
					e.setCancelled(true);
		}
	}

	//Tank
	@EventHandler
	public void onTankKill(PlayerDeathEvent e) {
		if(e.getEntity().getKiller() instanceof Player) {
			final KitPlayer killer = KitPlayer.get(e.getEntity().getKiller());
			final KitPlayer target = KitPlayer.get(e.getEntity().getPlayer());
			ePlayer et = ePlayer.get(target.getUniqueId());
			if((killer.hasKit(Kit.TANK) || target.hasKit(Kit.TANK)) && !killer.isNearRogue() && !et.isAdminMode() && !et.isWatching()) {
				if(killer.hasSpawnProtection() || target.hasSpawnProtection())
					return;
				Location loc = e.getEntity().getPlayer().getLocation();
				killer.getPlayer().getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 5, false, false);
			}
		}
	}

	@EventHandler
	public void cancelExplosionDamageForTank(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			KitPlayer p = KitPlayer.get((Player) e.getEntity());
			if(p.hasKit(Kit.TANK) && !p.isNearRogue()) {
				if(p.hasSpawnProtection())
					return;
				if(e.getCause() == DamageCause.BLOCK_EXPLOSION || e.getCause() == DamageCause.ENTITY_EXPLOSION)
					e.setCancelled(true);
			}
		}
	}

	//Berserker
	@EventHandler
	public void onBerserkerKill(PlayerDeathEvent e) {
		if(e.getEntity().getKiller() instanceof Player) {
			KitPlayer killer = KitPlayer.get(e.getEntity().getKiller());
			KitPlayer target = KitPlayer.get(e.getEntity().getPlayer());
			ePlayer et = ePlayer.get(target.getUniqueId());
			if(killer.hasKit(Kit.BERSERKER) && !killer.isNearRogue() && !et.isAdminMode() && !et.isWatching()) {
				if(killer.hasSpawnProtection())
					return;
				killer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 50, 1));
				killer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 80, 3));
			}
		}
	}

	//Hulk
	@EventHandler
	public void Hulk(PlayerInteractEntityEvent e) {
		if(e.getRightClicked() instanceof Player) {
			ItemStack item = e.getPlayer().getItemInHand();
			if(item.getType() == Material.AIR || item == null) {
				KitPlayer p = KitPlayer.get(e.getPlayer());
				KitPlayer z = KitPlayer.get((Player) e.getRightClicked());
				ePlayer ez = ePlayer.get(z.getUniqueId());
				if(p.hasKit(Kit.HULK) && !p.isNearRogue() && p.getPlayer().getPassenger() != null && z.getPlayer().getPassenger() != null && !ez.isAdminMode() && !ez.isWatching() && !z.getPlayer().isSneaking()) {
					if(p.hasSpawnProtection() || z.hasSpawnProtection())
						return;
					HaxPlayer.get(z.getPlayer()).setCanFly(true);
					p.getPlayer().setPassenger(z.getPlayer());
				}
			}
		}
	}

	@EventHandler
	public void HulkThrow(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {
			KitPlayer p = KitPlayer.get((Player) e.getDamager());
			ItemStack item = p.getPlayer().getItemInHand();
			if(item.getType() == Material.AIR || item == null) {
				if(p.getPlayer().getPassenger() != null && !p.isNearRogue()) {
					e.setCancelled(true);
					Vector v = p.getPlayer().getEyeLocation().getDirection();
					v.setY(1);
					v.multiply(1);
					Entity ent = p.getPlayer().getPassenger();
					if(ent instanceof Player) {
						ent.leaveVehicle();
						HaxPlayer.get(ent.getUniqueId()).invalidate(5000);
						ent.setVelocity(v);
						HaxPlayer.get(ent.getUniqueId()).setCanFly(false);
					}
				}
			}
		}
	}

	private Map<UUID, Arrow> locs = new HashMap<>();

	//Raijin
	@EventHandler
	public void ThrowArrow(PlayerInteractEvent e) {
		KitPlayer p = KitPlayer.get(e.getPlayer());
		if(e.getAction() == Action.RIGHT_CLICK_AIR) {
			ItemStack item = p.getPlayer().getItemInHand();
			if(item != null) {
				if(item.getType() == Material.ARROW && p.hasKit(Kit.RAIJIN)) {
					if(p.hasSpawnProtection())
						return;
					if(p.isCooldowned()) {
						p.getPlayer().sendMessage(p.getLanguage().get(KitPvPLanguage.KIT_COOLDOWN)
								.replaceAll("%seconds", Integer.toString(p.getCooldownTime())));
						return;
					}
					Arrow arrow = p.getPlayer().launchProjectile(Arrow.class);
					PlayerInventory inv = p.getPlayer().getInventory();
					arrow.setVelocity(arrow.getVelocity().multiply(0.5));
					inv.setItem(inv.getHeldItemSlot(), new ItemStack(item.getType(), item.getAmount() - 1));
					if(locs.containsKey(p.getUniqueId()))
						locs.remove(p.getUniqueId());
					locs.put(p.getUniqueId(), arrow);
					final UUID uuid = p.getUniqueId();
					final UUID arrowUuid = arrow.getUniqueId();
					p.startCooldown(10);
					Bukkit.getScheduler().runTaskLater(KitPvP.getPlugin(), () -> {
                        if(locs.containsKey(uuid))
                            if(locs.get(uuid).getUniqueId().equals(arrowUuid))
                                locs.remove(uuid);
                    }, 600);
				}
			}
		}
	}

	@EventHandler
	public void onPickUp(PlayerPickupItemEvent e) {
		if(e.getItem().getItemStack().getType() == Material.ARROW) {
			for(UUID uuid : locs.keySet()) {
				if(locs.get(uuid).getUniqueId().equals(e.getItem().getUniqueId())) {
					locs.remove(uuid);
					break;
				}
			}
		}
	}

	@EventHandler
	public void onHitThrower(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Arrow && e.getEntity() instanceof Player) {
			KitPlayer p = KitPlayer.get((Player) e.getEntity());
			Entity arrow = e.getDamager();
			if(locs.containsKey(p.getUniqueId())) {
				if(locs.get(p.getUniqueId()).getUniqueId().equals(arrow.getUniqueId())) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onHitEntity(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Arrow && !e.isCancelled()) {
			Entity arrow = e.getDamager();
			for(UUID uuid : locs.keySet()) {
				if(locs.get(uuid).getUniqueId().equals(arrow.getUniqueId())) {
					locs.remove(uuid);
					break;
				}
			}
		}
	}

	@EventHandler
	public void onSneak(PlayerToggleSneakEvent e) {
		KitPlayer p = KitPlayer.get(e.getPlayer());
		if(e.isSneaking() && p.hasKit(Kit.RAIJIN) && !p.isNearRogue() && locs.containsKey(p.getUniqueId())) {
			if(p.hasSpawnProtection())
				return;
			final float yaw = p.getPlayer().getLocation().getYaw();
			p.getPlayer().teleport(locs.get(p.getUniqueId()).getLocation());
			p.getPlayer().getLocation().setYaw(yaw);
			if(fight.containsKey(p.getUniqueId()))
				cleanUpGladiator(p);
		}
	}

	//Gladiator
	@EventHandler
	public void onPlaceFence(BlockPlaceEvent e) {
		KitPlayer p = KitPlayer.get(e.getPlayer());
		if(p.hasKit(Kit.GLADIATOR)) {
			ItemStack item = e.getItemInHand();
			if(item != null && item.getType() != Material.AIR && item.getItemMeta().hasDisplayName()) {
				if(item.getItemMeta().getDisplayName().equalsIgnoreCase(p.getLanguage().get(KitPvPLanguage.KIT_GLADIATOR_ITEM))) {
					e.setCancelled(true);
				}
			}
		}
	}

	private Map<UUID, UUID> fight = new HashMap<>();
	private Map<UUID, List<Location>> cubes = new HashMap<>();
	private Map<UUID, Location> oldLoc = new HashMap<>();
	private Map<UUID, BukkitRunnable> witherTask = new HashMap<>();

	@EventHandler
	public void clickGladiator(PlayerInteractEntityEvent e) {
		KitPlayer p = KitPlayer.get(e.getPlayer());
		if(p.hasKit(Kit.GLADIATOR)) {
			ItemStack item = p.getPlayer().getItemInHand();
			if(item != null && item.getType() != Material.AIR && item.getItemMeta().hasDisplayName()) {
				if(item.getItemMeta().getDisplayName().equalsIgnoreCase(p.getLanguage().get(KitPvPLanguage.KIT_GLADIATOR_ITEM))) {
					if(e.getRightClicked() instanceof Player) {
						ePlayer z = ePlayer.get((Player) e.getRightClicked());
						if(!z.isAdminMode() && !z.isWatching()) {
							if(p.hasSpawnProtection() || KitPlayer.get(z.getUniqueId()).hasSpawnProtection())
								return;
							if(p.isNearRogue())
								return;
							if(!fight.containsKey(p.getUniqueId()) && !fight.containsKey(z.getUniqueId())) {
								World world = p.getPlayer().getWorld();
								final Location loc = p.getPlayer().getLocation();
								boolean allEmpty = false;
								int max = 15;
								int min = 0;
								loc.setY(130);
								int i = 0;
								List<Location> cube = new ArrayList<>();
								Collection<Location> cage = new ArrayList<>();
								Location p1 = null;
								Location p2 = null;
								while(!allEmpty) {
									i++;
									allEmpty = true;
									for(int bX = i; bX <= i + max; bX++) {
										for(int bZ = min; bZ <= max; bZ++) {
											for(int bY = min; bY <= max; bY++) {
												Location newLoc = loc.clone().add(bX, bY, bZ);
												cube.add(newLoc);
												if(bY == min || bY == max || bX == i || bX == i + max || bZ == min || bZ == max)
													cage.add(newLoc);
												if(bY == min + 1) {
													if(bX == i + 2 && bZ == min + 2)
														p1 = newLoc;
													if(bX == (i + max) - 2 && bZ == max - 2)
														p2 = newLoc;
												}
											}
										}
									}
									for(Location l : cube) {
										if(l.getBlock().getType() != Material.AIR) {
											allEmpty = false;
											break;
										}
									}
									if(!allEmpty) {
										cube.clear();
										cage.clear();
										p1 = null;
										p2 = null;
									}
								}
								cage.forEach(l -> l.getBlock().setType(Material.GLASS));

								oldLoc.put(p.getUniqueId(), p.getPlayer().getLocation());
								oldLoc.put(z.getUniqueId(), z.getPlayer().getLocation());

								p1.setDirection(p2.toVector().subtract(p1.toVector()));
								p2.setDirection(p1.toVector().subtract(p2.toVector()));
								p.getPlayer().teleport(p1);
								z.getPlayer().teleport(p2);

								z.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 255));
								p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 255));

								fight.put(p.getUniqueId(), z.getUniqueId());
								fight.put(z.getUniqueId(), p.getUniqueId());
								cubes.put(p.getUniqueId(), cube);
								cubes.put(z.getUniqueId(), cube);
								p.sendMessage(KitPvPLanguage.GLADIATOR_START);
								z.sendMessage(KitPvPLanguage.GLADIATOR_START);

								witherTask.put(p.getUniqueId(), new BukkitRunnable() {
									@Override
									public void run() {
										z.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 40, 2));
										p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 40, 2));
									}
								});
								witherTask.get(p.getUniqueId()).runTaskTimer(KitPvP.getPlugin(), 1200, 40);
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void breakCage(BlockBreakEvent e) {
		if(fight.containsKey(e.getPlayer().getUniqueId()) && e.getBlock().getType() == Material.GLASS) {
			e.setCancelled(true);
			e.getBlock().setType(Material.BEDROCK);
			Bukkit.getScheduler().runTaskLater(KitPvP.getPlugin(), () -> {
                if(e.getBlock().getType() == Material.BEDROCK)
                    e.getBlock().setType(Material.GLASS);
            }, 100);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void dropDeathItemsOnGround(PlayerDeathEvent e) {
		if(fight.containsKey(e.getEntity().getPlayer().getUniqueId())) {
			Collection<ItemStack> drops = new ArrayList<>();
			drops.addAll(e.getDrops());
			e.getDrops().clear();
			ePlayer killer = ePlayer.get(fight.get(e.getEntity().getPlayer().getUniqueId()));
			Bukkit.getScheduler().runTaskLater(KitPvP.getPlugin(), () -> {
                for(ItemStack item : drops)
                    killer.getPlayer().getWorld().dropItem(killer.getPlayer().getLocation(), item);
            }, 20);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void cleanOnDeath(PlayerDeathEvent e) {
		ePlayer p = ePlayer.get(e.getEntity().getPlayer());
		cleanUpGladiator(p);
	}

	private void cleanUpGladiator(ePlayer p) {
		if(cubes.containsKey(p.getUniqueId())) {
			for(Location loc : cubes.get(p.getUniqueId()))
				loc.getBlock().setType(Material.AIR);
			final UUID zuuid = fight.get(p.getUniqueId());
			cubes.remove(zuuid);
			cubes.remove(p.getUniqueId());
			fight.remove(zuuid);
			fight.remove(p.getUniqueId());
			KitPlayer z = KitPlayer.get(zuuid);

			z.getPlayer().teleport(oldLoc.get(z.getUniqueId()));
			oldLoc.remove(z.getUniqueId());
			oldLoc.remove(p.getUniqueId());
			z.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 255));
			if(witherTask.containsKey(p.getUniqueId())) {
				witherTask.get(p.getUniqueId()).cancel();
				witherTask.remove(p.getUniqueId());
				p.getPlayer().removePotionEffect(PotionEffectType.WITHER);
			}
			if(witherTask.containsKey(z.getUniqueId())) {
				witherTask.get(z.getUniqueId()).cancel();
				witherTask.remove(z.getUniqueId());
				z.getPlayer().removePotionEffect(PotionEffectType.WITHER);
			}
		}
	}

	private HashMap<UUID, Integer> levels = new HashMap<>();

	//Repulse
	@EventHandler
	public void repulse(PlayerInteractEvent e) {
		KitPlayer p = KitPlayer.get(e.getPlayer());
		ItemStack item = p.getPlayer().getItemInHand();
		if(item != null) {
			if(item.getType() != Material.AIR && item.getItemMeta().hasDisplayName() && p.hasKit(Kit.REPULSE)) {
				if(item.getItemMeta().getDisplayName().equalsIgnoreCase(p.getLanguage().get(KitPvPLanguage.KIT_REPULSE_ITEM))) {
					if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
						if(p.hasSpawnProtection())
							return;
						if(p.isNearRogue())
							return;
						if(p.isCooldowned()) {
							p.getPlayer().sendMessage(p.getLanguage().get(KitPvPLanguage.KIT_COOLDOWN)
									.replaceAll("%seconds", Integer.toString(p.getCooldownTime())));
							return;
						}
						int lvl = levels.get(p.getUniqueId());
						p.getPlayer().getWorld().playSound(p.getPlayer().getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
						for(Entity ent : p.getPlayer().getNearbyEntities(1.5 * lvl, 1.5 * lvl, 1.5 * lvl)) {
							if(ent instanceof Player) {
								ePlayer ep = ePlayer.get(ent.getUniqueId());
								if(ep.isAdminMode() || ep.isWatching() ||KitPlayer.get(ent.getUniqueId()).hasSpawnProtection())
									continue;
								HaxPlayer.get(ep.getUniqueId()).invalidate(10000);
								ep.sendMessage(KitPvPLanguage.REPULSE_REPULSED);
							}
							Vector direction = ent.getLocation().toVector().subtract(p.getPlayer().getLocation().toVector());
							if(ent instanceof Player && ((Player) ent).isSneaking())
								ent.setVelocity(direction.normalize().multiply(lvl / 1.5 / 2));
							else
								ent.setVelocity(direction.normalize().multiply(lvl / 1.5));
						}
						p.startCooldown(20 * lvl);

					} else if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
						final int lvl = levels.get(p.getUniqueId());
						int newLvl = lvl + 1;
						if(lvl + 1 > 5)
							newLvl = 1;
						levels.remove(p.getUniqueId());
						levels.put(p.getUniqueId(), newLvl);
						p.sendActionBar(p.getLanguage().get(KitPvPLanguage.REPULSE_LVL).replaceAll("%lvl", Integer.toString(newLvl)));
					}
				}
			}
		}
	}

	@EventHandler
	public void addToRepulseMap(KitChangeEvent e) {
		if(e.getKit() == Kit.REPULSE) {
			levels.put(e.getPlayer().getUniqueId(), 1);
			e.getPlayer().sendActionBar(e.getPlayer().getLanguage().get(KitPvPLanguage.REPULSE_LVL).replaceAll("%lvl", Integer.toString(1)));
		}
	}
}