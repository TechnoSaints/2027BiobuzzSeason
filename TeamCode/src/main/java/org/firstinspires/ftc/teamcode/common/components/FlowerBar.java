package org.firstinspires.ftc.teamcode.common.components;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.constants.RobotConstants;

/** Controls the Axon Max configured as a standard positional servo. */
public class FlowerBar extends Component {
    private final Servo servo;

    public FlowerBar(HardwareMap hardwareMap, Telemetry telemetry) {
        super(telemetry);
        servo = hardwareMap.get(
                Servo.class, RobotConstants.Intake.Flower_Servo_Name);
    }

    /** Sets the servo position from 0.0 to 1.0. */
    public void setPosition(double position) {
        if (!Double.isFinite(position)) {
            throw new IllegalArgumentException("Servo position must be finite.");
        }
        servo.setPosition(Range.clip(position, 0.0, 1.0));
    }

    public double getPosition() {
        return servo.getPosition();
    }

    @Override
    protected void update() {
        // The servo is controlled directly by setPosition.
    }
}
