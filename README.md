# PhysicsSimulatorTP
La práctica de TP2

# Physics Simulator

Physics Simulator is an interactive Java application designed to simulate the movement of bodies in space. The project allows users to explore how objects behave under different physical conditions, helping to visualize concepts such as gravity, mass, velocity, position, and force interaction.

## Overview

Understanding physics can be challenging when concepts are explained only through formulas. This simulator provides a visual and practical way to observe how celestial bodies move and interact with each other. By changing parameters and applying different force laws, users can experiment with physical systems and better understand the behavior of objects in motion.

The application is especially useful for students, teachers, and anyone interested in astronomy, mechanics, or computational simulations.

## Features

- **Interactive Simulation**  
  Visualize the movement of bodies in a simulated space environment.

- **Custom Body Configuration**  
  Define bodies with different properties such as mass, position, velocity, and acceleration.

- **Force Law Selection**  
  Apply different force laws to groups of bodies and observe how their motion changes.

- **JSON Input Support**  
  Load simulation scenarios from JSON files, making it easy to create and test different configurations.

- **JSON Output Generation**  
  Save simulation results in JSON format for later analysis or reuse.

- **Graphical User Interface**  
  Use a visual interface to interact with the simulation in a more intuitive way.

- **Educational Purpose**  
  Helps users understand physics through experimentation and visualization instead of only theoretical formulas.

## Technologies Used

- Java
- Java Swing
- JSON
- Apache Commons CLI

## Project Structure

```text
PhysicsSimulatorTP-II-main/
├── out/                 # Compiled Java classes
├── src/                 # Source code of the application
│   └── simulator/
│       ├── control/     # Simulation controller logic
│       ├── factories/   # Object and force law builders/factories
│       ├── launcher/    # Main class used to run the application
│       ├── misc/        # Utility classes
│       ├── model/       # Core physics simulation model
│       └── view/        # Graphical user interface
├── resources/           # Application resources
│   ├── viewer/          # Viewer configuration/resources
│   ├── uml/             # UML diagrams
│   ├── other/           # Additional resources
│   ├── icons/           # Icons used by the GUI
│   └── examples/        # Example JSON simulation files
├── README.md
└── lib/                 # External libraries