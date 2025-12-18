This project is an editor for CRRCsim, a radio-controlled aircraft simulator, and integrates with AVL for aerodynamic analysis.
It allows users to modify aircraft parameters and configurations for simulation.
Developed in Java and Scala, it provides a GUI for streamlined editing and analysis.

## Building and Running

### Compilation

To compile the project, run the following command:

```bash
sbt compile
```

### Running the application

To run the application in the foreground, use the following command:

```bash
sbt run
```

The application will start in full-screen mode.

To run the application in the background while keeping the graphical user interface visible, use the `nohup` command:

```bash
nohup sbt run &
```

This will free up your terminal, and the application will continue running even if you close the terminal.

### Commit Message Guidelines

Commit messages should be concise, clear, and a maximum of two lines. The first line should be a brief summary, and the second line can optionally provide more context if needed.

### Recent UI Changes

- The button to add a new section to a surface has been changed to a `+` button for a more intuitive user experience.
