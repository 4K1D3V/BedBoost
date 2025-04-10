package gg.kite.core.listener;

import gg.kite.core.BedBoost;
import gg.kite.core.utils.AdventureAPI;
import gg.kite.core.utils.Config;
import gg.kite.core.utils.Logic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The ListenerManager class serves as an event listener and manager responsible for
 * handling player interactions with beds in the Minecraft game, specifically within
 * the BedBoost plugin ecosystem. It integrates with the Bukkit/Spigot API to
 * manage event-driven behavior and execute scheduled tasks.
 *
 * Key responsibilities of this class include:
 * - Listening for the PlayerBedLeaveEvent and checking if conditions are met
 *   for applying gameplay boosts, such as speed effects, to players.
 * - Managing a queue of players who have interacted with beds and are eligible
 *   for boost effects.
 * - Using a scheduled task to process players in the queue and apply boosts
 *   asynchronously to avoid performance bottlenecks.
 *
 * This class interacts with several key components to provide its functionality:
 * - AdventureAPI: Utilized to apply graphical and gameplay effects, such as
 *   speed boosts or animations, to players.
 * - Logic: Used to determine if specific conditions (e.g., world time or player permissions)
 *   are right for a player to receive a boost.
 * - Config: Provides configurable values for determining the duration and type of effects
 *   applied to players.
 *
 * The ListenerManager ensures that boosted players are tracked and removes their
 * boost status after a certain duration to avoid stacking effects or re-applying
 * them unnecessarily. All tasks are processed efficiently to maintain server performance.
 */
public final class ListenerManager implements Listener {
    private final BedBoost plugin;
    private final Logic logic;
    private final AdventureAPI adventureAPI;
    private final Config config;
    private final Set<UUID> boostedPlayers = ConcurrentHashMap.newKeySet();
    private final Queue<Player> boostQueue = new ConcurrentLinkedQueue<>();

    public ListenerManager(BedBoost plugin, AdventureAPI adventureAPI, Config config, Logic logic) {
        this.plugin = plugin;
        this.logic = logic;
        this.adventureAPI = adventureAPI;
        this.config = config;
        startBatchProcessor();
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerLeaveBed(PlayerBedLeaveEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("bedboost.effect") || boostedPlayers.contains(player.getUniqueId()) || !logic.shouldApplyBoost(player)) return;
        boostQueue.offer(player);
    }

    private void startBatchProcessor() {
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            while (!boostQueue.isEmpty()) {
                Player player = boostQueue.poll();
                if (player != null && player.isOnline()) {
                    boostedPlayers.add(player.getUniqueId());
                    adventureAPI.applySpeedBoost(player);

                    plugin.getServer().getScheduler().runTaskLater(plugin, () -> boostedPlayers.remove(player.getUniqueId()),
                            config.getEffects().values().stream().mapToInt(e -> e.durationTicks).max().orElse(200));
                }
            }
        }, 0L, 2L);
    }
}