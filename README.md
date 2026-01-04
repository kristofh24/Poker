# Texas Hold 'em Poker

## Overview

**Project Timeline**: May 31, 2025 - October 30, 2025 (5 months)  
**Approach**: Agile - incremental implementation with refinements and improvements added along the way

This is a graphical implementation of Texas Hold 'em Poker using Java Swing. The game focuses on smooth UX and engaging UI through the use of custom animation and components. Animation is achieved using timer events and linear interpolation. It is a component that I am very proud of because it pushed me to think differently. Project development has currently been paused to focus on school and other work, but I plan to resume development later in 2026. Below is a detailed development flow to aid with understanding my process.

---

## Development Flow

### Phase 1: Foundation (May 31 - June 8)
**Goal**: Establish basic systems, entities, and interactions, and core game logic

**Components Built**:
1. **Deck** - Shuffling logic
2. **Player** - Player entity with actions (fold, call, raise)
3. **Lambda** - Custom event callback system
4. **Card** - Card entity and visual representation
5. **Evaluator** - Hand evaluation logic
6. **Interpolation** - Animation system
7. **Rank Enum** - Type definition for card ranking
8. **Game** - Implement basic game logic

---

### Phase 2: GUI Infrastructure (June 9 - July 12)
**Goal**: Design and create custom user interface

**Components Built**:
1. **TextButton & TextBox** - Basic UI controls used to control game
2. **Table** - Data display component used to display player list in menu
3. **Frame** - Custom styling component
4. **Restriction Enum** - Type definition for textbox input restriction
5. **Property Enum** - Type definition for custom property interaction

---

### Phase 3: Assembly (July 20 - October 30)
**Goal**: Create additional components, polish existing ones, assemble solution and identify points for further improvement

**Components Built**:
1. **Hand** - Object used to store 5-card hands
2. **Utility** - Shared constants and helper methods
3. **TextLabel** - Flexible text rendering
4. **Game** - Refine game logic and state management
5. **Main** - Finalzie application entry point and menu

---

## Development Strategy

### Bottom-Up Architecture
```
Foundation Layer (Core Entities, Systems & Logic)
    ↓
GUI Components
    ↓
Application Assembly
```

### Key Principles
- **Incremental Building**: Test components as they're built
- **Separation of Concerns**: Game logic separate from UI
- **Modular Design**: Components work independently

---

## Timeline

| Period | Focus | Outcome |
|--------|-------|---------|
| **May 31** | Foundation start | Deck system, player entity |
| **June 1-8** | Core systems | Cards, evaluation, animation, basic inputs |
| **June 9 - July 12** | Visual components | UI components |
| **July 20** | Abstraction | Hand object, utility file |
| **October 26-30** | MVP | Playable game |

---

## Development Decisions

1. **String-based cards** - Simple representation ("AS", "10H")
2. **Static game state** - Global access for simplicity
3. **Timer-based animation** - Seamlessly integrates with Swing framework
4. **Combination evaluation** - Tries all possibilities for accuracy
5. **Custom GUI components** - Full control over styling and behavior