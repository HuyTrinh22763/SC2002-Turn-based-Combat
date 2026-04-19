# SC2002-Turn-based-Combat

A robust, Java-based implementation of a turn-based combat game designed to demonstrate advanced Object-Oriented Design principles and the Entity-Control-Boundary (ECB) architectural pattern.

## Overview

This project simulates a battle between a Player (with various classes like Warrior or Wizard) and multiple waves of enemies (Goblins, Wolves, etc.). It emphasizes clean separation of concerns, scalability, and adherence to SOLID principles.

## Architecture (ECB Pattern)

The system is organized into three primary layers to ensure maintainability:

*   **Boundary**: Handles all interactions between the user and the system.
    *   `boundary.GameApplication`: The entry point of the application.
    *   `boundary.CliGameSession`: Manages the CLI input/output loop.
*   **Control**: Orchestrates the game logic and flow.
    *   `control.TurnManager`: Handles round progression, turn order, and battle states.
    *   `control.ActionExecutor`: Validates and executes combat actions using specialized processors.
*   **Entity**: Represent the core domain data and rules.
    *   `entity.combatants`: Abstract and concrete definitions for players and enemies.
    *   `entity.items`: Logic for consumable items like Potions and Power Stones.
    *   `entity.battlerules`: Fundamental rules for damage calculation and action types.

## Key Features

*   **ECB Architecture**: Strict separation of concerns between game logic (Control), data models (Entity), and user interface (Boundary).
*   **Dynamic Combat Mechanics**: Implementation of diverse actions including standard attacks, defensive stances, and class-specific special skills.
*   **Character Specialization**: Unique skillsets for different classes, such as the Warrior's stunning 'Shield Bash' or the Wizard's slowing 'Caltrops'.
*   **Strategic Turn Ordering**: A speed-based initiative system that determines turn priority, allowing for strategic wait-actions and turn manipulation.
*   **Advanced Attribute Management**: Centralized logic for damage calculation, stat modifiers, and status effect processing.
*   **Status Effect System**: Robust implementation of combat states like Stunned or Hinder, which programmatically skip or delay turns.
*   **Inventory & Consumables**: Strategic use of items (e.g., Potions, Smoke Bombs) with specialized effects on combatants.
*   **Level & Wave Progression**: Support for multiple enemy waves and dynamic difficulty levels that scale combatant attributes.
*   **Strategy Pattern Implementation**: Pluggable turn-order strategies and action processors for high codebase extensibility.

## Getting Started

### Prerequisites
*   Java Development Kit (JDK) 8 or higher.

### Running the Application
To compile and run the game from the terminal:

1. Compile the source files:
   ```bash
   javac boundary/*.java control/*.java entity/*.java
   ```

2. Run the main application:
   ```bash
   java boundary.GameApplication
   ```
