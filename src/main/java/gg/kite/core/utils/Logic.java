package gg.kite.core.utils;

import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * The Logic class provides utility methods for determining
 * conditions under which specific actions should be applied
 * in the game environment.
 *
 * This class is designed to evaluate the state of the Minecraft
 * world and players to determine suitability for certain gameplay effects.
 * It is immutable and contains only static timing thresholds for evaluation.
 */
public final class Logic {
    private static final long MORNING_START = 0L;
    private static final long MORNING_END = 1000L;

    public boolean shouldApplyBoost(Player player) {
        World world = player.getWorld();
        if (world.getEnvironment() != World.Environment.NORMAL) return false;
        long time = world.getTime();
        return time >= MORNING_START && time <= MORNING_END;
    }
}