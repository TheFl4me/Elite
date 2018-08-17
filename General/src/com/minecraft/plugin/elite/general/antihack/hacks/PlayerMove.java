package com.minecraft.plugin.elite.general.antihack.hacks;

import com.minecraft.plugin.elite.general.antihack.hacks.movement.AutoCritsHack;
import com.minecraft.plugin.elite.general.antihack.hacks.movement.FastLadderHack;
import com.minecraft.plugin.elite.general.antihack.hacks.movement.FlyHack;
import com.minecraft.plugin.elite.general.antihack.hacks.movement.SpeedHack;
import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.api.Server;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove {

    private boolean isValid;
    private boolean isUnderBlock;
    private boolean isOnGround;
    private boolean isOnIce;
    private boolean isOnLava;
    private boolean isOnBlockEdge;
    private boolean isOnSlab;
    private boolean isNextToBlock;
    private boolean isOnWater;
    private boolean isInWeb;
    private boolean isInLadder;
    private boolean isInVine;
    private boolean isInVehicle;
    private boolean isInLava;
    private boolean isInWater;
    private boolean isSprinting;
    private boolean isCanFly;
    private boolean isCanSpeed;

    private GeneralPlayer player;
    private Location location;
    private Location eyeLocation;
    private double verticalDistance;
    private double horizontalDistance;
    private double jumpHeight;
    private double heightAboveGround;
    private double moveSpeed;
    private Block legs;
    private Block head;

    public static void store(PlayerMoveEvent e) {
        if (e.isCancelled())
            return;

        PlayerMove move = new PlayerMove(e);

        if(!move.isValid())
            return;

        FlyHack.check(move);
        SpeedHack.check(move);
        FastLadderHack.check(move);
        AutoCritsHack.check(move);

        if (move.isOnGround()) {
            move.getPlayer().setLastOnGround(move);
            move.getPlayer().setLastOnGroundMovesAgo(0);
        } else {
            move.getPlayer().setLastOnGroundMovesAgo(move.getPlayer().getLastOnGroundMovesAgo() + 1);
        }
    }

    public PlayerMove(PlayerMoveEvent e) {
        GeneralPlayer p = GeneralPlayer.get(e.getPlayer());
        this.player = p;
        p.getMoves()[p.getMoveCount() % p.getMoves().length] = this;
        p.setLastMove(this);
        p.setMoveCount(p.getMoveCount() + 1);
        this.isValid = true;
        Location to = e.getTo().clone();
        Location from = e.getFrom().clone();

        this.location = p.getPlayer().getLocation();
        this.eyeLocation = p.getPlayer().getEyeLocation().clone();

        this.legs = this.getLocation().getBlock();

        switch(this.getLegs().getType()) {
            case STEP:
            case BED_BLOCK:
            case STONE_SLAB2:
            case WOOD_STEP:
            case TRAP_DOOR:
            case IRON_TRAPDOOR:
            case ENCHANTMENT_TABLE:
            case SOUL_SAND:
            case BREWING_STAND:
            case ENDER_PORTAL_FRAME:
            case DRAGON_EGG:
            case COCOA:
            case CHEST:
            case ENDER_CHEST:
            case TRAPPED_CHEST:
            case FLOWER_POT:
            case REDSTONE_COMPARATOR_ON:
            case REDSTONE_COMPARATOR_OFF:
            case DIODE_BLOCK_OFF:
            case DIODE_BLOCK_ON:
            case DAYLIGHT_DETECTOR:
            case DAYLIGHT_DETECTOR_INVERTED:
            case HOPPER:
            case SLIME_BLOCK:
            case CARPET:
            case WOOD_STAIRS:
            case ACACIA_STAIRS:
            case BIRCH_WOOD_STAIRS:
            case SPRUCE_WOOD_STAIRS:
            case DARK_OAK_STAIRS:
            case JUNGLE_WOOD_STAIRS:
            case RED_SANDSTONE_STAIRS:
            case NETHER_BRICK_STAIRS:
            case BRICK_STAIRS:
            case SANDSTONE_STAIRS:
            case COBBLESTONE_STAIRS:
            case SMOOTH_STAIRS:
            case QUARTZ_STAIRS:
            case SKULL:
                this.isOnSlab = true;
                break;
            default: this.isOnSlab = false;
        }

        this.head = this.getLegs().getRelative(BlockFace.UP);
        final Block below = (this.isOnSlab() ? this.getLegs() : this.getLegs().getRelative(BlockFace.DOWN));
        final Block over = this.getHead().getRelative(BlockFace.UP);

        this.moveSpeed = 0;
        this.isUnderBlock = over.getType().isSolid();
        this.isOnIce = below.getType() == Material.ICE || this.isNextTo(below, Material.ICE) || below.getType() == Material.PACKED_ICE || this.isNextTo(below, Material.PACKED_ICE);
        this.isOnGround = below.getType().isSolid() && !this.isInWater() && !this.isInLava() && !this.isOnWater() && !this.isOnLava();
        this.isInLadder = this.isInside(Material.LADDER) || this.isNextTo(Material.LADDER);
        this.isInVine = this.isInside(Material.VINE) || this.isNextTo(Material.VINE);
        this.isInWeb = this.isInside(Material.WEB) || this.isNextTo(Material.WEB);
        this.isInVehicle = p.getPlayer().getVehicle() != null || p.getPlayer().isInsideVehicle();
        this.isInLava = this.isInside(Material.LAVA) || this.isInside(Material.STATIONARY_LAVA)|| this.isNextTo(Material.LAVA) || this.isNextTo(Material.STATIONARY_LAVA);
        this.isInWater = this.isInside(Material.WATER) || this.isInside(Material.STATIONARY_WATER) || this.isNextTo(Material.WATER) || this.isNextTo(Material.STATIONARY_WATER);
        this.isSprinting = p.getPlayer().isSprinting();
        this.isNextToBlock = this.isNextToSolid(this.getLegs()) || this.isNextToSolid(this.getHead());
        this.isOnBlockEdge = !below.getType().isSolid() && (this.isNextToSolid(below));
        this.isCanFly = p.isCanFly();
        this.isCanSpeed = p.isCanSpeed();

        this.isOnLava = below.getType() == Material.LAVA || below.getType() == Material.STATIONARY_LAVA || this.isNextTo(below, Material.LAVA) || this.isNextTo(below, Material.STATIONARY_LAVA);
        this.isOnWater  = below.getType() == Material.WATER || below.getType() == Material.STATIONARY_WATER || this.isNextTo(below, Material.WATER) || this.isNextTo(below, Material.STATIONARY_WATER);

        if (p.getLastOnGround() == null && this.isOnGround())
            p.setLastOnGround(this);

        final double x = to.getX() - from.getX();
        final double y = to.getY() - from.getY();
        final double z = to.getZ() - from.getZ();
        this.horizontalDistance = Math.sqrt(x * x + z * z);
        this.verticalDistance = y;

        this.jumpHeight = (p.getLastOnGround() == null ? 0 : this.getLocation().getY() - p.getLastOnGround().getLocation().getY());
        this.heightAboveGround = this.getLocation().getY() - this.getLocation().getWorld().getHighestBlockYAt(this.getLocation());

        this.moveSpeed = e.getFrom().distance(e.getTo());

        Server server = Server.get();
        if (!p.isValid() || p.isCanFly() || p.isKnockbacked() || p.isLagging() || server.isLagging() || p.canBypassChecks() || this.isInWater() || this.isOnWater() || this.isInVehicle() || this.isInLava() || this.isOnLava()|| this.isOnBlockEdge() || p.getLastOnGround() == null || this.isInVine() || this.isInWeb())
            this.isValid = false;
    }

    public boolean isOnGround() {
        return this.isOnGround;
    }
    public boolean isValid() {
        return this.isValid;
    }
    public boolean isUnderBlock() {
        return this.isUnderBlock;
    }
    public boolean isOnIce() {
        return this.isOnIce;
    }
    public boolean isOnLava() {
        return this.isOnLava;
    }
    public boolean isOnBlockEdge() {
        return this.isOnBlockEdge;
    }
    public boolean isOnSlab() {
        return this.isOnSlab;
    }
    public boolean isNextToBlock() {
        return this.isNextToBlock;
    }
    public boolean isOnWater() {
        return this.isOnWater;
    }
    public boolean isInWeb() {
        return this.isInWeb;
    }
    public boolean isInLadder() {
        return this.isInLadder;
    }
    public boolean isInVine() {
        return this.isInVine;
    }
    public boolean isInVehicle() {
        return this.isInVehicle;
    }
    public boolean isInLava() {
        return this.isInLava;
    }
    public boolean isInWater() {
        return this.isInWater;
    }
    public boolean isSprinting() {
        return this.isSprinting;
    }
    public boolean isCanFly() {
        return this.isCanFly;
    }
    public boolean isCanSpeed() {
        return this.isCanSpeed;
    }

    public Location getLocation() {
        return this.location;
    }
    public Location getEyeLocation() {
        return this.eyeLocation;
    }
    public GeneralPlayer getPlayer() {
        return this.player;
    }
    public double getVerticalDistance() {
        return this.verticalDistance;
    }
    public double getHorizontalDistance() {
        return this.horizontalDistance;
    }
    public double getJumpHeight() {
        return this.jumpHeight;
    }
    public double getHeightAboveGround() {
        return this.heightAboveGround;
    }
    public double getSpeed() {
        return this.moveSpeed;
    }
    public Block getLegs() {
        return this.legs;
    }
    public Block getHead() {
        return this.head;
    }

    public boolean isNextTo(Block block, Material material) {
        return block.getRelative(BlockFace.NORTH).getType() == material
                || block.getRelative(BlockFace.EAST).getType() == material
                || block.getRelative(BlockFace.SOUTH).getType() == material
                || block.getRelative(BlockFace.WEST).getType() == material;
    }
    public boolean isNextTo(Material material) {
        return this.getHead().getRelative(BlockFace.NORTH).getType() == material
                || this.getHead().getRelative(BlockFace.EAST).getType() == material
                || this.getHead().getRelative(BlockFace.SOUTH).getType() == material
                || this.getHead().getRelative(BlockFace.WEST).getType() == material
                || this.getLegs().getRelative(BlockFace.NORTH).getType() == material
                || this.getLegs().getRelative(BlockFace.EAST).getType() == material
                || this.getLegs().getRelative(BlockFace.SOUTH).getType() == material
                || this.getLegs().getRelative(BlockFace.WEST).getType() == material;
    }
    public boolean isNextToSolid(Block block) {
        return block.getRelative(BlockFace.NORTH).getType().isSolid()
                || block.getRelative(BlockFace.EAST).getType().isSolid()
                || block.getRelative(BlockFace.SOUTH).getType().isSolid()
                || block.getRelative(BlockFace.WEST).getType().isSolid();
    }

    public boolean isInside(Material material) {
        return this.getLegs().getType() == material || this.getHead().getType() == material;
    }
}