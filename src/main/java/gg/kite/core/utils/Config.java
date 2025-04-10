package gg.kite.core.utils;

import gg.kite.core.BedBoost;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

/**
 * The Config class is responsible for reading and storing configuration data
 * defined in the plugin's configuration file. This includes the setup for
 * potion effects, particle effects, and sound effects used by the BedBoost plugin.
 *
 * This class initializes configuration values during instantiation and ensures
 * that the plugin's default configuration file is saved if not already present.
 * It retrieves the following configurations:
 *
 * - Potion effects: Types, durations, and amplification levels for gameplay effects.
 * - Particle effects: Particle type and display settings (e.g., count, offsets).
 * - Sound effects: The sound type played during specific events.
 *
 * It provides access to this data through getter methods for use throughout
 * the plugin functionality.
 */
public final class Config {
    private final BedBoost plugin;
    private final Map<PotionEffectType, EffectData> effects;
    private final String particleType;
    private final int particleCount;
    private final double particleOffsetX;
    private final double particleOffsetY;
    private final double particleOffsetZ;
    private final String sound;

    public static class EffectData {
        public final int durationTicks;
        final int amplifier;

        EffectData(int durationTicks, int amplifier) {
            this.durationTicks = durationTicks;
            this.amplifier = amplifier;
        }
    }

    public Config(BedBoost plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();

        effects = new HashMap<>();
        ConfigurationSection effectsSection = config.getConfigurationSection("effects");
        for (String key : effectsSection.getKeys(false)) {
            PotionEffectType type = PotionEffectType.getByName(key.toUpperCase());
            if (type != null) {
                effects.put(type, new EffectData(
                        effectsSection.getInt(key + ".duration-ticks", 200),
                        effectsSection.getInt(key + ".amplifier", 0)
                ));
            }
        }

        this.particleType = config.getString("particle.type", "VILLAGER_HAPPY");
        this.particleCount = config.getInt("particle.count", 3);
        this.particleOffsetX = config.getDouble("particle.offset-x", 0.5);
        this.particleOffsetY = config.getDouble("particle.offset-y", 1.5);
        this.particleOffsetZ = config.getDouble("particle.offset-z", 0.5);
        this.sound = config.getString("sound", "BLOCK_NOTE_BLOCK_CHIME");
    }

    public Map<PotionEffectType, EffectData> getEffects() {
        return effects;
    }

    public String getParticleType() {
        return particleType;
    }

    public int getParticleCount() {
        return particleCount;
    }

    public double getParticleOffsetX() {
        return particleOffsetX;
    }

    public double getParticleOffsetY() {
        return particleOffsetY;
    }

    public double getParticleOffsetZ() {
        return particleOffsetZ;
    }

    public Sound getSound() {
        return Sound.valueOf(sound.toUpperCase());
    }
}