package gg.kite.core.utils;

import gg.kite.core.BedBoost;
import gg.kite.core.listener.ListenerManager;

/**
 * The BedBoostInjector class serves as the central dependency injector
 * for the BedBoost plugin. It initializes and provides instances of
 * key components required for the plugin's functionality, ensuring
 * proper configuration and dependency management.
 *
 * This class is instantiated during the plugin's lifecycle and provides
 * access to various services like configuration handling, event listening,
 * and interaction with the Adventure API.
 *
 * Responsibilities:
 * - Initialize and manage the Config class for handling plugin configuration.
 * - Create and manage the AdventureAPI for advanced messaging and effects.
 * - Provide the Logic class for gameplay condition evaluation.
 * - Generate and supply a ListenerManager for handling plugin-specific events.
 */
public final class BedBoostInjector {
    private final BedBoost plugin;
    private final Config config;
    private final AdventureAPI adventureAPI;
    private final Logic logic;

    public BedBoostInjector(BedBoost plugin) {
        this.plugin = plugin;
        this.config = new Config(plugin);
        this.adventureAPI = new AdventureAPI(plugin, config);
        this.logic = new Logic();
    }

    public ListenerManager getListenerManager() {
        return new ListenerManager(plugin, adventureAPI, config, logic);
    }

    public AdventureAPI getAdventureAPI() {
        return adventureAPI;
    }
}