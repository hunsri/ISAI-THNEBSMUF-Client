## Student Project 2 for Interactive Systems AI (SoSe 2025)
Projekt **THNEBSMUF** - **T**ron**H**as**NE**ver**B**een**S**o**MU**ch**F**un
*Project based on TRON*

### Running the application

**To run a single Client**, run the `AClient` class and replace the `serverAddress` field with your desired target.<br>
To demo run the application under Windows, you can use the [`start.bat`](./start.bat) file.

### Overview
This project is an application developed in the semester of summer 2025 in the span of one week for the Interactive Systems AI course.
It features a simple BFS Algorithm to find optimal paths for individual bots. Each bot has different capabilities:

### Bots and Their Capabilities

**Bots can only move forward, left or right.**
Each tick a bot has to choose one of these actions.
Once a bot hits a wall or the border of the map it gets elminated.

| Bot Type    | Capabilities                                                      |
|------------|--------------------------------------------------------------------|
| DEFAULT    | Standard bot. Cannot cross borders or move through its own trail.  |
| BORDERLESS | Ignores border checks: can move outside field boundaries.          |
| CLIPPING   | Ignores own trail checks: can move through its own and other friendly bots trails.          |

### BFS Pathfinding
The program uses straight forward **B**readth-**f**irst **s**earch Pathfinding.
The core functionality of the BFS is offered through the `TileNodeTree` and `TileNode` classes. The tree is build as a tertiary tree, growing in respect to the bots movements. Other invalid moves that would result in a KO get also pruned.

### Individual Bot Behavior

The individual behaviors are reflected in the `Pathfinder` and `MoveChecker` classes. For the `CLIPPING` bot the pathfinder will allow searching for paths through bots left by own trails as well. Furthermore it is not obstructed by the planned routes of other own bots. <br> For the `BORDERLESS` bot safechecks that disallow crossing the map border are disabled. This allows the `BORDERLESS` bot to break out of pockets if trapped at the edge of the map.

### How it looks
<img width="2559" height="1079" alt="Screenshot 2025-07-14 070425" src="https://github.com/user-attachments/assets/fbf7eb06-e545-4557-868f-555289c0fa4a" />
Shown on the left side is the debug view of the client. <br> The trails of blue represent the own bots, the ones in red enemy bots. The orange trail represents the calculated path to a targeted position. <br> 
On the right side is the server view. 
