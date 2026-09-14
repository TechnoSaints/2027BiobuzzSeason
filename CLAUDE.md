# 2027BiobuzzSeason — FTC Robot Controller

Team Biobuzz's *FIRST* Tech Challenge robot code for the **DECODE (2025-2026)** season, built on the official FTC SDK 11.2.1.

## Project layout

This is a multi-module Gradle/Android Studio project (standard FTC SDK layout):

- `FtcRobotController/` — the stock FTC SDK app module. Don't edit unless updating the SDK itself.
- `TeamCode/` — **all team code lives here.** This is what you'll almost always be editing.
- `doc/ftc-knowledge/` — reference notes on the libraries and concepts this codebase uses (see below). Read these before making non-trivial changes to drivetrain, paths, or tuning code.
- `build.dependencies.gradle` — shared Maven dependencies (FTC SDK, Pedro Pathing, FTCLib, panels). Add new libraries here, not in `TeamCode/build.gradle`.

### TeamCode package structure (`org.firstinspires.ftc.teamcode`)

- `common/` — shared robot classes used by every OpMode:
  - `Component` — abstract base for any subsystem (`isBusy()` + `update()`).
  - `Bot` — abstract base robot; owns the Pedro `Follower` (drivetrain/localization) and `Intake`. Subclassed by `TeleopBot` and `AutoBot`.
  - `TeleopBot` — driver-controlled robot: `setDrivePowers()`, intake `forward()/reverse()/stop()`, `resetHeading()`.
  - `AutoBot` — autonomous robot: `followPath()`, `holdPose()`, `setPose()`, plus the same intake controls.
  - `Intake` — single-motor subsystem wrapping a `DcMotorEx`.
  - `RobotConstants` — **all hardware config names and tuning numbers.** Motor names must match the Driver Station Hardware Config exactly.
  - `FieldConstants` — named `Pose` locations on the field (start poses, scoring positions, waypoints), in inches.
  - `AutoPaths` — reusable `Path` factories built from `FieldConstants` poses.
- `opmode/auto/` — `@Autonomous` OpModes (e.g. `SampleAuto`, a state-machine example).
- `opmode/teleop/` — `@TeleOp` OpModes (e.g. `PedroTeleOp`, the main driver control).
- `opmode/test/` — test/experimental autonomous routines (`CircuitAutoTest`, `IntakeAuto`).
- `opmode/tuning/` — manual tuning OpModes not tied to Pedro's tuner system (`ServoTuner`).
- `pedro/` — Pedro Pathing hardware wiring: `Constants.java` (drivetrain/localizer config, builds the `Follower`), `Tuning.java` (registers `@Tuner` procedures for the Pedro Pathing tuning app), `procedures/` (per-hardware tuning procedures — Mecanum, Pinpoint, OTOS, OctoQuad, three/two-wheel odometry).

## Key libraries in use

- **FTC SDK 11.2.1** — core robot control (`RobotCore`, `Hardware`, `Vision`, etc).
- **Pedro Pathing** (`com.pedropathing:*`) — drivetrain abstraction, path following, and localization. This robot uses a `Mecanum` drivetrain with a **GoBILDA Pinpoint** localizer and the `Foresight` following algorithm. See `doc/ftc-knowledge/pedro-pathing.md`.
- **FTCLib** (`org.ftclib.ftclib:core`) — general utility library (currently a dependency; not yet used in the subsystem code above).
- **bylazar `fullpanels`** — telemetry/dashboard panels.

See `doc/ftc-knowledge/` for deeper notes on each of these plus the FTC SDK's OpMode lifecycle and hardware config conventions.

## Conventions to follow

- Motor/servo hardware names live in `RobotConstants` — never hardcode a hardware-map name string inside an OpMode or subsystem.
- Field positions live in `FieldConstants` as `Pose` objects — don't inline raw coordinates in an OpMode.
- Every subsystem extends `Component` and implements `isBusy()`/`update()`; every robot type extends `Bot`.
- Call `bot.update()` every loop iteration (TeleOp `loop()` or the `while (opModeIsActive())` loop in autonomous) — it drives the Pedro `Follower` and all subsystems.
- Hardware initialization in `Bot` is wrapped in try/catch with a telemetry warning rather than throwing — a missing sensor/motor degrades gracefully instead of crashing the OpMode. Follow this pattern for new subsystems.
- Code comments in this repo are intentionally student-friendly/explanatory (see commit `8920c1a`) — keep that style when editing existing files, since students read this code to learn.

## Building

Standard Android Studio/Gradle project — open the repo root in Android Studio, or run `./gradlew build` (`gradlew.bat` on Windows). No local emulator; OpModes deploy to and run on the Driver Hub / Robot Controller phone via the Driver Station app.
