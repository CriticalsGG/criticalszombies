# CRITICALS ZOMBIES (Fabric, Minecraft 1.21.11)

Version 0.5.0 - renamed to CRITICALS ZOMBIES and the whole theme is now red (#BF1919).

Version 0.4.0 - the lobby characters are now real 3D player models (yours uses your real skin).
If the 3D code fails to compile or crashes, set `USE_3D = false` in `ui/Theme.java`, or make `Player3D.draw` just `return false;`.

Version 0.3.0 - main menu buttons run along the bottom (left to right) under a full-width lobby; everything sizes itself to the window.

Milestone 1: a replacement title screen with a lobby scene (blocky players walking),
a map select screen, and a loadout screen. All data is sample data. No gameplay yet.

## Build with GitHub Actions
1. Create a new GitHub repo and upload everything in this folder (keep `.github/workflows/build.yml`).
2. Open the **Actions** tab -> **Build mod** -> the latest run.
3. When it finishes, download the **zombiesmod-jar** artifact at the bottom and unzip it.
4. Use the jar that does NOT end in `-sources.jar`.

## Install (everyone who plays)
- Minecraft 1.21.11 with Fabric Loader and Fabric API for 1.21.11
- Put the jar in `.minecraft/mods`

## Rename the mod
Change `archives_base_name` and `maven_group` in `gradle.properties`, the `id`/`name` in
`src/main/resources/fabric.mod.json`, and the package `com.example.zombiesmod`.

## If the window looks cramped
In the Minecraft launcher: Installations -> your Fabric profile -> More options -> Resolution, e.g. 1280 x 720.
In-game, Options -> Video Settings -> GUI Scale (Auto is fine) also changes how much space the menu has.
