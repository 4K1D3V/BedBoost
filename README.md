# BedBoost

![GitHub release (latest by date)](https://img.shields.io/github/v/release/4K1D3V/bedboost?color=green)
![GitHub issues](https://img.shields.io/github/issues/4K1D3V/bedboost)
![GitHub license](https://img.shields.io/github/license/4K1D3V/bedboost)

**BedBoost** is a lightweight Minecraft plugin for Spigot 1.21.1 that enhances the bed mechanics by granting players configurable potion effects, particle animations, sounds, and titles when they wake up after skipping the night. Designed with performance and scalability in mind, it’s perfect for both small servers and large-scale networks.

## Features

- **Configurable Potion Effects**: Apply multiple effects (e.g., Speed, Jump Boost) with customizable duration and amplifier.
- **Dynamic Particle Animations**: Displays a rising spiral of configurable particles (e.g., `HAPPY_VILLAGER`) with adjustable offsets and count.
- **Sound Effects**: Plays a sound (e.g., `BLOCK_NOTE_BLOCK_CHIME`) when waking up.
- **Adventure Titles**: Shows a stylish "Good Morning!" title and subtitle using the Adventure API.
- **Permission Support**: Restrict boosts to players with the `bedboost.effect` permission.
- **Performance Optimizations**:
    - Asynchronous config loading and particle scheduling.
    - Precomputed particle offsets for efficient animations.
    - Batch event processing for multi-player scenarios.
- **Event Debouncing**: Prevents rapid boosting with a cooldown system.

## Installation

1. **Download**: Grab the latest `.jar` from the [Releases](https://github.com/4K1D3V/BedBoost/releases) page.
2. **Place in Plugins Folder**: Drop `BedBoost.jar` into your Spigot server’s `plugins` folder.
3. **Restart Server**: Start or restart your server to load the plugin.
4. **Configure**: Edit `plugins/BedBoost/config.yml` to customize settings (see [Configuration](#configuration)).

### Requirements
- **Spigot**: Version 1.21.1
- **Java**: 17 or higher
- **Dependencies**:
    - [Adventure Platform Bukkit](https://mvnrepository.com/artifact/net.kyori/adventure-platform-bukkit) (bundled or installed separately)

## Configuration

The plugin generates a `config.yml` file in `plugins/BedBoost/` on first run. Here’s the default configuration:

```yaml
effects:
  speed:
    duration-ticks: 200    # 10 seconds
    amplifier: 0          # Speed I
  jump:
    duration-ticks: 100   # 5 seconds
    amplifier: 1          # Jump Boost II
particle:
  type: "HAPPY_VILLAGER"  # Particle type (e.g., HAPPY_VILLAGER, DUST)
  count: 3               # Particles per tick
  offset-x: 0.5          # Horizontal spread
  offset-y: 1.5          # Vertical height
  offset-z: 0.5          # Depth spread
sound: "BLOCK_NOTE_BLOCK_CHIME"  # Sound played on wake-up
```

### Options
- **`effects`**: Add any valid potion effect (e.g., `regeneration`, `strength`).
- **`particle.type`**: Use any Spigot particle name (see [Particle Enum](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Particle.html)).
- **`sound`**: Use any Spigot sound name (see [Sound Enum](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Sound.html)).

## Permissions

- **`bedboost.effect`**
    - **Description**: Allows players to receive the wake-up boost.
    - **Default**: `true` (all players have it by default unless restricted).

Example with LuckPerms:
```
/lp group default permission set bedboost.effect true
```

## Usage

1. Place a bed in the Overworld.
2. Sleep to skip the night.
3. Wake up to enjoy:
    - Configured potion effects (e.g., Speed, Jump Boost).
    - A spiral particle animation.
    - A wake-up sound.
    - A "Good Morning!" title.

## Building from Source

### Prerequisites
- **Maven**: For dependency management.
- **JDK**: 17 or higher.

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/4K1D3V/BedBoost.git
   cd bedboost
   ```
2. Build the plugin:
   ```bash
   mvn clean package
   ```
3. Find the `.jar` in `target/BedBoost-1.0.0.jar`.

### Dependencies
- `spigot-api:1.21.1-R0.1-SNAPSHOT` (provided)
- `adventure-platform-bukkit:4.3.2` (compile)

## Performance

- **Async Operations**: Config loading and particle scheduling run off the main thread.
- **Particle Pooling**: Precomputed offsets reduce runtime calculations.
- **Batch Processing**: Multiple players waking up are handled efficiently in batches.
- **Thread Safety**: Uses `ConcurrentHashMap` and `ConcurrentLinkedQueue` for multi-threaded environments.

## Contributing

Contributions are welcome! Here’s how to get started:

1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/awesome-idea`).
3. Commit your changes (`git commit -m "Add awesome idea"`).
4. Push to your branch (`git push origin feature/awesome-idea`).
5. Open a Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Credits

- **Author**: [KiteGG](https://github.com/4K1D3V)