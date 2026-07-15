# CPU and Memory Monitor

A desktop dashboard built with Java Swing that displays live CPU (per-core and overall) and RAM usage, updating once per second.

## Overview

This is a self-directed learning project built to get hands-on with Java concurrency and GUI development. It polls live system stats via [OSHI](https://github.com/oshi/oshi) on a background thread and safely reflects them onto a Swing UI, without ever blocking the interface.

## Features

- Live overall CPU usage (large progress bar)
- Live per-core CPU usage (12 individual progress bars)
- Live RAM usage (used / available / total, in MB)
- Clean shutdown of background polling when the window is closed

## Tech Stack

- **Java** — core language
- **Swing** — GUI toolkit
- **OSHI 7.3.1** — cross-platform hardware/OS data (CPU ticks, memory)
- **Maven** — dependency management and build
- **Eclipse** — IDE

## Architecture

```
App            → entry point; wires everything together
SystemStats    → wraps OSHI; exposes clean CPU/RAM data, no OSHI details leak out
DashboardFrame → Swing GUI; exposes a single thread-safe updateBars() method
StatsPoller    → polls SystemStats on a background thread on a fixed schedule,
                  then safely pushes results to the GUI
```

Each class has one job and knows nothing about the internals of the others — `StatsPoller` doesn't know a `JProgressBar` exists, and `DashboardFrame` doesn't know OSHI exists.

## Key Concepts Learned

- **Encapsulation** — keeping OSHI details confined to `SystemStats`, and Swing details confined to `DashboardFrame`, so other classes only interact through clean method calls.
- **CPU load via tick deltas** — OSHI reports raw cumulative tick counters, not percentages; load must be derived by comparing two readings over an interval (`getProcessorCpuLoadBetweenTicks`).
- **The Event Dispatch Thread (EDT)** — Swing is single-threaded by design. Blocking the EDT freezes the UI; mutating Swing components from another thread risks corrupting it.
- **`ScheduledExecutorService`** — running background polling on a dedicated thread, away from the EDT.
- **`scheduleAtFixedRate` vs. `scheduleWithFixedDelay`** — fixed-rate scheduling keeps updates anchored to real time instead of drifting if a poll takes longer than expected.
- **`SwingUtilities.invokeLater()`** — the safe handoff mechanism from a background thread back to the EDT, placed *inside* the update method itself so it's safe regardless of which thread calls it.
- **Swing layout management** — nested `JPanel`s, `BorderLayout` (and the different behavior of `NORTH`/`SOUTH` vs. `CENTER`), `GridLayout` with gaps, and `BorderFactory` for padding.
- **Clean shutdown** — using a `WindowListener` to stop the executor service when the window closes, preventing orphaned background threads.

## Running the Project

```bash
mvn clean install
mvn exec:java -Dexec.mainClass="monitor.App"
```

Or run `App.java` directly from Eclipse.
