package gg.kite.core;

import gg.kite.core.utils.BedBoostInjector;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * The BedBoost class serves as the main entry point for the BedBoost plugin.
 * It extends the JavaPlugin class, providing lifecycle methods to initialize
 * and terminate the plugin within a Bukkit/Spigot environment. The plugin is
 * designed to enhance gameplay by applying configurable effects and features
 * tied to interactions such as player behavior or world events.
 */
public final class BedBoost extends JavaPlugin {

    private static final Logger LOGGER = Logger.getLogger("BedBoost");
    private BedBoostInjector injector;

    @Override
    public void onEnable() {
        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            injector = new BedBoostInjector(this);
            getServer().getPluginManager().registerEvents(injector.getListenerManager(), this);
            LOGGER.info("BedBoost enabled successfully.");
        });
    }

    @Override
    public void onDisable() {
        injector.getAdventureAPI().close();
        LOGGER.info("BedBoost disabled successfully.");
    }

    public BedBoostInjector getInjector() {
        return injector;
    }
}