# Donkey Kong-Inspired 2D Java Game

This project is a Donkey Kong inspired 2D platformer developed as part of my Object-Oriented Programming coursework at ISCTE.  
It was built entirely in Java, using a modular architecture, game loop logic, and GUI components to simulate enemy movement, collisions, and player progression through multiple rooms.

---

## Gameplay Overview
- The player navigates through multiple rooms while avoiding enemies and obstacles.  
- The game includes environment interactions, hazards, collectible items, and win/lose conditions.  
- All entities (player, enemies, items, obstacles, goals) were implemented as independent and extensible objects.

---

## Key Object-Oriented Concepts Used

### Inheritance  
Shared behavior between entities is defined in parent classes, allowing flexible extensions and cleaner design.

### Polymorphism  
Game objects implement shared interfaces, enabling the engine to handle them uniformly while invoking the correct behavior at runtime.

### Interfaces  
Common behaviors (movement, drawing, interaction) are defined through interfaces to enforce consistent contracts across multiple classes.

### Encapsulation  
State and logic for each entity are contained within dedicated classes, reducing side-effects and improving maintainability.

### Lambda Expressions  
Used for filtering collections, event-based logic, and simplifying smaller operations inside the game loop.

### Program Structure and Architecture  
This project strengthened my understanding of structuring a mid-sized Java application, including:  
- Separating GUI from core logic  
- Organizing packages by responsibility  
- Building a game loop  
- Managing state transitions  
- Handling input and events cleanly  

---

## Project Structure

```
src/
 ├── objects/               # Core entities in the game (player, enemies, obstacles, items)
 ├── pt.iscte.poo.game/     # Main game logic, game loop, state transitions
 ├── pt.iscte.poo.gui/      # GUI components and rendering
 ├── pt.iscte.poo.observer/ # Observer pattern implementation (events, notifications)
 └── pt.iscte.poo.utils/    # Helper utilities and shared classes

images/                     # Sprites and assets used in the game
rooms/                      # Room configuration files
```

---

Feel free to explore the code. A runnable version will be added soon.
