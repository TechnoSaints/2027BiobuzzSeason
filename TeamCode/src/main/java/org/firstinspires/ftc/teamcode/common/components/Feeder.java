package org.firstinspires.ftc.teamcode.common.components;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.constants.RobotConstants;

/**
 * Feeder is the robot part that sucks up game pieces.
 * It's really just one motor - this class gives the rest of the code easy
 * on/off/reverse commands so nobody has to remember raw power numbers.
 */
public class Feeder extends Component {
    private DcMotorEx feederMotor;
    private CRServo gate;

    public Feeder(HardwareMap hardwareMap, Telemetry telemetry) {
        super(telemetry);
        try {
            // This name has to exactly match what you typed into the Driver Station's Hardware Config.
            feederMotor = hardwareMap.get(DcMotorEx.class, RobotConstants.Feeder.FEEDER_MOTOR_NAME);
            feederMotor.setDirection(RobotConstants.Feeder.feederMotorDirection);
            gate = hardwareMap.get(CRServo.class, RobotConstants.Feeder.FEEDER_GATE_NAME);
        } catch (Exception e) {
            telemetry.addData(
                    "Warning: Failed to initialize feeder motor",
                    "%s: %s", e.getClass().getSimpleName(), String.valueOf(e.getMessage())
            );
        }
    }

    /**
     * Sets the feeder motor's power directly.
     * @param power How hard to spin the motor, from -1.0 (full reverse) to 1.0 (full forward).
     */
    public void setFeederPower(double power) {
        if (feederMotor != null) {
            feederMotor.setPower(power);
        }
    }

    /** Spins the feeder forward to pick up game pieces. */
    public void forward() {
        setFeederPower(RobotConstants.Feeder.maxMovePower);
    }

    /** Spins the feeder backward to spit game pieces back out. */
    public void reverse() {
        setFeederPower(-RobotConstants.Feeder.maxMovePower);
    }

    /** Turns the feeder motor off. */
    public void stop() {
        setFeederPower(RobotConstants.Feeder.stopPower);
    }

    public void openGate(){
        gate.setPower(1);
        telemetry.addLine("Continuous Servo Running");
    }

    public void closeGate() {
        gate.setPower(-1);
    }

    @Override
    public boolean isBusy() {
        return false;
    }

    @Override
    public void update() {
        // Nothing needs to happen here every loop yet - if you add a sensor
        // to the feeder later (like a color sensor), check it here.
    }
}
