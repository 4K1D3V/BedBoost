package gg.kite.core.utils;

import gg.kite.core.BedBoost;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AdventureAPI is responsible for managing graphical, auditory, and gameplay
 * effects for players within the BedBoost plugin. This includes title messages,
 * sound effects, particle animations, and the application of potion effects.
 * It uses the Adventure library for message delivery and title display, and
 * integrates with Bukkit for scheduled tasks and particle management.
 *
 * This class serves as an intermediary for configuring the user experience,
 * applying the following features whenever invoked:
 * - Potion effects tied to configured durations and levels.
 * - Titles displayed to players with a custom message and timing.
 * - Sound effects tied to configured sounds in the server configuration.
 * - Animated particle effects rendered at the player's location.
 *
 * Key functionalities include:
 * - Applying speed boosts and other potion effects to players.
 * - Managing scheduled tasks for animations.
 * - Handling cleanup when the plugin is disabled.
 *
 * The class relies on configuration values retrieved from the Config class
 * and interacts directly with the Bukkit API for particle management and
 * scheduling tasks on the server.
 *
 * This class is not intended to be instantiated multiple times and is tightly
 * bound to the lifecycle of the BedBoost plugin.
 */
public final class AdventureAPI {
    private final BedBoost plugin;
    private final Config config;
    private final BukkitAudiences adventure;
    private static final Component WAKE_UP_MESSAGE = MiniMessage.miniMessage()
            .deserialize("<green>You feel <bold>refreshed</bold> after a good night's sleep!");
    private static final Component TITLE = MiniMessage.miniMessage().deserialize("<gold>Good Morning!");
    private static final Component SUBTITLE = MiniMessage.miniMessage().deserialize("<yellow>Rise and shine!");
    private static final int ANIMATION_DURATION_TICKS = 20;
    private static final int ANIMATION_INTERVAL_TICKS = 4;
    private final List<double[]> particleOffsets; // Precomputed offsets

    public AdventureAPI(BedBoost plugin, Config config) {
        this.plugin = plugin;
        this.config = config;
        this.adventure = BukkitAudiences.create(plugin);
        this.particleOffsets = precomputeParticleOffsets();
    }

    private List<double[]> precomputeParticleOffsets() {
        List<double[]> offsets = new ArrayList<>();
        for (int i = 0; i < config.getParticleCount(); i++) {
            double angle = i * 2 * Math.PI / config.getParticleCount();
            offsets.add(new double[]{
                    Math.cos(angle) * config.getParticleOffsetX(),
                    i * config.getParticleOffsetY() / config.getParticleCount(),
                    Math.sin(angle) * config.getParticleOffsetZ()
            });
        }
        return offsets;
    }

    public void applySpeedBoost(Player player) {
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            for (Map.Entry<PotionEffectType, Config.EffectData> entry : config.getEffects().entrySet()) {
                player.addPotionEffect(new PotionEffect(entry.getKey(), entry.getValue().durationTicks, entry.getValue().amplifier, true, false));
            }
            adventure.player(player).sendMessage(WAKE_UP_MESSAGE);
            showTitle(adventure.player(player), TITLE, SUBTITLE);
            player.playSound(player.getLocation(), config.getSound(), 1.0f, 1.0f);
            playSparkleAnimation(player);
        }, 2L);
    }

    private void playSparkleAnimation(Player player) {
        Location baseLocation = player.getLocation();
        int runs = ANIMATION_DURATION_TICKS / ANIMATION_INTERVAL_TICKS;
        double[] angle = {0};

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, task -> {
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                double height = angle[0] / (2 * Math.PI);
                for (double[] offset : particleOffsets) {
                    baseLocation.getWorld().spawnParticle(
                            Particle.valueOf(config.getParticleType()),
                            baseLocation.clone().add(offset[0], height + offset[1], offset[2]),
                            1,
                            0, 0, 0, 0
                    );
                }
            });
            angle[0] += Math.PI / 4;
            if (angle[0] >= runs * Math.PI / 4) task.cancel();
        }, 0L, ANIMATION_INTERVAL_TICKS);
    }


    public void showTitle(final net.kyori.adventure.audience.Audience audience, final Component title, final Component subtitle) {
        Title.Times times = Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(2000), Duration.ofMillis(500));
        audience.showTitle(Title.title(title, subtitle, times));
    }

    public void close() {
        adventure.close();
    }
}
