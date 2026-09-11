package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Intake represents the robot's mechanism for picking up pollen balls.
 * It encapsulates a single motor and provides simple commands to control it.
 */
public class Intake extends Component {
    private DcMotorEx intakeMotor;

    public Intake(HardwareMap hardwareMap, Telemetry telemetry) {
        super(telemetry);
        try {
            // "intakeMotor" must match the name in your Hardware Config on the Driver Station
            intakeMotor = hardwareMap.get(DcMotorEx.class, RobotConstants.Intake.INTAKE_MOTOR_NAME);
            intakeMotor.setDirection(RobotConstants.Intake.intakeMotorDirection);
        } catch (Exception e) {
            telemetry.addLine("Warning: Intake motor not found! Check your Hardware Config.");
        }
    }

    /**
     * Sets the raw power of the intake motor.
     * @param power The power to set (-1.0 to 1.0).
     */
    public void setIntakePower(double power) {
        if (intakeMotor != null) {
            intakeMotor.setPower(power);
        }
    }

    /** Collects pollen balls by spinning the intake forward. */
    public void forward() {
        setIntakePower(RobotConstants.Intake.maxMovePower);
    }

    /** Ejects pollen balls by spinning the intake in reverse. */
    public void reverse() {
        setIntakePower(-RobotConstants.Intake.maxMovePower);
    }

    /** Stops the intake motor. */
    public void stop() {
        setIntakePower(RobotConstants.Intake.stopPower);
    }

    @Override
    public boolean isBusy() {
        return false;
    }

    @Override
    public void update() {
        // Periodic logic for the intake (e.g. sensor checks) can be added here.
    }
}
