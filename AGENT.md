# AVL Editor

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

### Git Push Policy

**IMPORTANT:** Never push to remote without explicit user permission. Always ask before running `git push`.

### Recent UI Changes

- The button to add a new section to a surface has been changed to a `+` button for a more intuitive user experience.

## Feature Planning

### PLAN.md

**IMPORTANT:** Before starting any task, the agent MUST first create or update the plan in `PLAN.md`. No implementation work should begin until the plan is written and reviewed.

The project uses a local `PLAN.md` file (not tracked in git) to maintain context across sessions for the current feature being developed.

**Usage:**
- Plans must be written in English
- This file should be rewritten/reset when starting a new feature
- It contains the feature description, current status, next steps, and relevant context
- Helps the agent continue work after context cleanup
- Should include task lists, important decisions, and files being modified
