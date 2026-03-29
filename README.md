# MinerZombie Mod

A simple Minecraft Fabric mod that extends the `ZombieEntity` class to create an automated miner. The miner zombie searches for ores, navigates to them, mines them, and delivers the drops to the nearest player.

---

## Features

- Custom AI using a simple state machine with 3 states: `IDLE`, `MOVING_TO_TARGET`, `MINING`
- Searches for nearby coal, iron, and diamond ores
- Automatically moves to the target block using Minecraft's default pathfinding
- Mines blocks and delivers drops to the player
- Prevents getting stuck and skips unreachable blocks
- Safe in daylight (does not burn)

---

## Tech Stack

- **Minecraft Version:** 1.19+  
- **API:** Fabric API  
- **Language:** Java  

---

## How It Works

The MinerZombie uses a simple AI loop:

1. **IDLE** – Searches for nearby ores within a radius of 10 blocks  
2. **MOVING_TO_TARGET** – Uses Minecraft’s navigation system to walk toward the target block  
3. **MINING** – Breaks the block, collects the drops, and delivers them to the nearest player  

Stuck detection ensures the zombie skips blocks it cannot reach.  

---

## Installation

1. Install Fabric Loader and Fabric API for your Minecraft version  
2. Place the compiled JAR in your `mods` folder  
3. Launch Minecraft with the Fabric profile  

---

## Notes from Development

- Reading Minecraft’s source code was fascinating — it’s amazing how much is happening behind the scenes!  
- Following documentation and tutorials was a bit of a nightmare at times, which made this project challenging.  
- This mod was mainly a learning exercise; future projects will focus more on Python and AI rather than Minecraft modding.

---

## License

MIT License 
