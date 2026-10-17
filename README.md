# Time To Rest

![Time To Rest Logo](data/src/main/resources/logo.png)

## Description
A client-side Minecraft mod that reminds you to take a rest after you play for a long while.
Supports NeoForge and Fabric on Minecraft 1.21.1.

## Configuration

| Loader | Where to open | Config file |
| --- | --- | --- |
| NeoForge | Mods list → Time To Rest → Config | `config/time_to_rest-client.toml` |
| Fabric | Mods list → Time To Rest → Config (requires [Mod Menu](https://modrinth.com/mod/modmenu); Cloth Config is bundled) | `config/time_to_rest.json` |

Options:

- `enabled` — whether the reminder is active (default `true`).
- `reminder_period` — reminder interval in seconds (default `1800`, minimum `10`).

Changes take effect immediately on both loaders; no world restart is needed.
When the period is shortened, the timer is rescheduled from the moment of the change,
so already-passed intervals do not trigger a burst of reminders.

## Download

### From release
You can download this mod from [GitHub Releases](https://github.com/lzm222/TimeToRest/releases).

### Build from source code
1. Make sure you have Java version 21 or above installed.
2. Clone this repository

    ```shell
    git clone github.com/lzm222/TimeToRest
    ```

3. Run Gradle build command

    ```shell
    cd TimeToRest
    ./gradlew build
    ```

The loader jars are written to `neoforge/build/libs/` and `fabric/build/libs/`.

## License
[MIT License](LICENSE)
