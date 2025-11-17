# Donkey Kong-Inspired 2D Java Game

This project is a Donkey Kong–inspired 2D platformer developed as part of my Object-Oriented Programming coursework at ISCTE.  
It was built entirely in **Java**, using a modular architecture, game loop logic, and GUI components to simulate enemy movement, collisions, and player progression through multiple rooms.

---

## 🎮 Gameplay Overview
- The player moves through a set of rooms while avoiding obstacles and enemies.  
- The game includes dynamic interactions, environment objects, and clear win/lose conditions.  
- All game elements (player, enemies, barrels, items, walls, goals) were implemented as **independent, extensible objects**.

---

## 🧠 Key Object-Oriented Concepts Used

### ✔ **Inheritance**
Many entities share common behavior through parent classes, allowing clean extension of features and reduced code duplication.

### ✔ **Polymorphism**
Game objects implement shared interfaces, enabling the engine to treat them uniformly while still calling the correct method per object type.

### ✔ **Interfaces**
Shared behaviors (such as movable objects, drawable entities, and interactive elements) were defined through interfaces to enforce clean contracts across multiple classes.

### ✔ **Encapsulation**
State and logic for each game object is isolated inside its class, preventing unwanted side-effects and keeping the code modular.

### ✔ **Lambda Expressions**
Used for filtering collections, event-based logic, and simplifying small operations inside the game loop.

### ✔ **OOP Architecture & Program Structure**
This project taught me how to structure a mid-sized Java application:
- Separating GUI from logic  
- Organizing packages by responsibility  
- Structuring a game loop  
- Managing state transitions  
- Handling input and events cleanly  

---

## 📁 Project Structure

