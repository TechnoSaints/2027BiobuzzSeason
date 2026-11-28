package org.firstinspires.ftc.teamcode.common.components;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.constants.RobotConstants;

/** Controls the 435 RPM intake feeder motor. */
public class IntakeFeeder extends Component {
    private final DcMotorEx motor;

    public IntakeFeeder(HardwareMap hardwareMap, Telemetry telemetry) {
        super(telemetry);
        motor = hardwareMap.get(
                DcMotorEx.class, RobotConstants.Feeder.FEEDER_MOTOR_NAME);
        motor.setDirection(RobotConstants.Feeder.feederMotorDirection);
    }

    /** Runs the feeder forward at a proportion of maximum power from 0.0 to 1.0. */
    public void runForward(double powerProportion) {
        motor.setPower(validatePowerProportion(powerProportion));
    }

    /** Runs the feeder in reverse at a proportion of maximum power from 0.0 to 1.0. */
    public void runReverse(double powerProportion) {
        motor.setPower(-validatePowerProportion(powerProportion));
    }

    public void stop() {
        motor.setPower(0.0);
    }

    private double validatePowerProportion(double powerProportion) {
        if (!Double.isFinite(powerProportion)) {
            throw new IllegalArgumentException("Power proportion must be finite.");
        }
        return Range.clip(powerProportion, 0.0, 1.0);
    }

    @Override
    protected void update() {
        // The feeder motor is controlled directly by its run and stop methods.
    }
}
