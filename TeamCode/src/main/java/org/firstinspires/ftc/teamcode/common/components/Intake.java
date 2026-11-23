package org.firstinspires.ftc.teamcode.common.components;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.constants.RobotConstants;

/**
 * Combines the intake and feeder into one game-piece handling subsystem.
 */
public class Intake extends Component {
    private DcMotorEx intakeMotor;
    private Servo flowerServo;
    private Feeder feeder;

    public Intake(HardwareMap hardwareMap, Telemetry telemetry) {
        super(telemetry);
        try {
            // This name has to exactly match what you typed into the Driver Station's Hardware Config.
            intakeMotor = hardwareMap.get(DcMotorEx.class, RobotConstants.Intake.INTAKE_MOTOR_NAME);
            intakeMotor.setDirection(RobotConstants.Intake.intakeMotorDirection);
            flowerServo = hardwareMap.get(Servo.class, RobotConstants.Intake.Flower_Servo_Name);
        } catch (Exception e) {
            telemetry.addData(
                    "Warning: Failed to initialize intake motor",
                    "%s: %s", e.getClass().getSimpleName(), String.valueOf(e.getMessage())
            );
        }
        feeder = new Feeder(hardwareMap, telemetry);
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

    public void feederForward() {
        feeder.forward();
    }

    public void feederReverse() {
        feeder.reverse();
    }

    public void stopFeeder() {
        feeder.stop();
    }

    public void openGate() {
        feeder.openGate();
    }

    public void closeGate() {
        feeder.closeGate();
    }

    @Override
    public boolean isBusy() {
        return feeder.isBusy();
    }

    @Override
    public void update() {
        feeder.update();
    }

    public void lowerFlowerServo(){
        flowerServo.setPosition(RobotConstants.Intake.FLOWER_INTAKE_POSITION);
    }

    public void raiseFlowerServo(){
        flowerServo.setPosition(RobotConstants.Intake.FLOWER_STORE_POSITION);
    }
}
