package org.firstinspires.ftc.teamcode.common;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Component is the pattern every robot part (like the Intake) has to follow.
 * It's like a checklist: any new subsystem you build must be able to say
 * whether it's busy, and must be updated every loop to keep working.
 */
public abstract class Component {
    protected Telemetry telemetry;

    protected Component(Telemetry telemetry)
    {
        this.telemetry = telemetry;
    }

    /** True if this part of the robot is still in the middle of doing something. */
    protected abstract boolean isBusy();

    /** Called once per loop so this part of the robot can keep doing its job. */
    protected abstract void update();
}
