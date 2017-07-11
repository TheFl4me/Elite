package com.minecraft.plugin.elite.survivalgames.manager.arena;

import com.minecraft.plugin.elite.general.api.GeneralPlayer;
import com.minecraft.plugin.elite.general.database.Database;
import com.minecraft.plugin.elite.survivalgames.SurvivalGames;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArenaManager {

    private static Map<String, Arena> arenas = new HashMap<>();

    public static Arena get(World world) {
        return get(world.getName());
    }

    public static Arena get(String worldName) {
        return arenas.get(worldName.toLowerCase());
    }

    public static Arena get(GeneralPlayer p) {
        for(Arena arena : getAll()) {
            if(arena.getPlayers().contains(p))
                return arena;
        }
        return null;
    }

    public static List<Arena> getAll() {
        List<Arena> list = new ArrayList<>();
        list.addAll(arenas.values());
        return list;
    }

    public static void loadAll() {
        List<String> maps = Arrays.asList("Downstream", "Breeze2", "HolidayResort", "SG6", "Zone85", "SG3", "FutureCity", "SG4", "Turbulence");
        maps.forEach(ArenaManager::create);
        Bukkit.getWorlds().stream().filter(world -> maps.contains(world.getName())).forEach(ArenaManager::load);
    }

    public static void create(String worldName) {
        if(Bukkit.getWorld(worldName) == null) {
            WorldCreator creator = new WorldCreator(worldName);
            creator.environment(World.Environment.NORMAL);
            World world = creator.createWorld();
            world.setSpawnLocation(0, 150, 0);
            world.setAutoSave(false);
        }
    }

    public static void load(World world) {
        Database db = SurvivalGames.getDB();
        if(!db.containsValue(SurvivalGames.DB_ARENAS, "name", world.getName())) {
            db.execute("INSERT INTO " + SurvivalGames.DB_ARENAS + " (name, maxsize, minsize,"
                            + " centerx, centery, centerz,"
                            + " pody,"
                            + " pod1x, pod1z,"
                            + " pod2x, pod2z,"
                            + " pod3x, pod3z,"
                            + " pod4x, pod4z,"
                            + " pod5x, pod5z,"
                            + " pod6x, pod6z,"
                            + " pod7x, pod7z,"
                            + " pod8x, pod8z,"
                            + " pod9x, pod9z,"
                            + " pod10x, pod10z,"
                            + " pod11x, pod11z,"
                            + " pod12x, pod12z,"
                            + " pod13x, pod13z,"
                            + " pod14x, pod14z,"
                            + " pod15x, pod15z,"
                            + " pod16x, pod16z,"
                            + " pod17x, pod17z,"
                            + " pod18x, pod18z,"
                            + " pod19x, pod19z,"
                            + " pod20x, pod20z,"
                            + " pod21x, pod21z,"
                            + " pod22x, pod22z,"
                            + " pod23x, pod23z,"
                            + " pod24x, pod24z) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                    world.getName(), 0, 0,
                    0, 150, 0,
                    150,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0);
        }
        Arena arena = new Arena(world);
        arenas.put(world.getName().toLowerCase(), arena);
        if(arena.getMaxSize() > 0) {
            WorldBorder border = world.getWorldBorder();
            border.setSize(arena.getMaxSize());
            border.setCenter(arena.getCenter());
            border.setDamageBuffer(0);
            border.setDamageAmount(1);
            border.setWarningDistance(20);
        }
    }
}
