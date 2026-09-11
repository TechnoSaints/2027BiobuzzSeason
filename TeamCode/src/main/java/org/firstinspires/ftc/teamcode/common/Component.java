package org.firstinspires.ftc.teamcode.common;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Component is an abstract base class for all robot subsystems (like Intake, Lift, etc.).
 * It ensures every subsystem has access to telemetry and follows the same update structure.
 */
public abstract class Component {
    protected Telemetry telemetry;
    
    protected Component(Telemetry telemetry)
    {
        this.telemetry = telemetry;
    }

    /** Should return true if the component is currently performing an action. */
    protected abstract boolean isBusy();

    /** Should be called repeatedly to update the state of the component. */
    protected abstract void update();
}
