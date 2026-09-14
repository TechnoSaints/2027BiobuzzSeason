package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Intake is the robot part that sucks up game pieces.
 * It's really just one motor - this class gives the rest of the code easy
 * on/off/reverse commands so nobody has to remember raw power numbers.
 */
public class Intake extends Component {
    private DcMotorEx intakeMotor;

    public Intake(HardwareMap hardwareMap, Telemetry telemetry) {
        super(telemetry);
        try {
            // This name has to exactly match what you typed into the Driver Station's Hardware Config.
            intakeMotor = hardwareMap.get(DcMotorEx.class, RobotConstants.Intake.INTAKE_MOTOR_NAME);
            intakeMotor.setDirection(RobotConstants.Intake.intakeMotorDirection);
        } catch (Exception e) {
            telemetry.addLine("Warning: Intake motor not found! Check your Hardware Config.");
        }
    }

    /**
     * Sets the intake motor's power directly.
     * @param power How hard to spin the motor, from -1.0 (full reverse) to 1.0 (full forward).
     */
    public void setIntakePower(double power) {
        if (intakeMotor != null) {
            intakeMotor.setPower(power);
        }
    }

    /** Spins the intake forward to pick up game pieces. */
    public void forward() {
        setIntakePower(RobotConstants.Intake.maxMovePower);
    }

    /** Spins the intake backward to spit game pieces back out. */
    public void reverse() {
        setIntakePower(-RobotConstants.Intake.maxMovePower);
    }

    /** Turns the intake motor off. */
    public void stop() {
        setIntakePower(RobotConstants.Intake.stopPower);
    }

    @Override
    public boolean isBusy() {
        return false;
    }

    @Override
    public void update() {
        // Nothing needs to happen here every loop yet - if you add a sensor
        // to the intake later (like a color sensor), check it here.
    }
}
