package org.firstinspires.ftc.teamcode.common;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class Component {
    protected Telemetry telemetry;
    protected Component(Telemetry telemetry)
    {
        this.telemetry = telemetry;
    }

    protected abstract boolean isBusy();

    protected abstract void update();
}
