package org.firstinspires.ftc.teamcode.common.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.constants.RobotConstants;

/** Launcher turret with a 6000 RPM motor and an aim servo. */
public class Turret extends Component {
    private final DcMotorEx launcherMotor;
    private final DcMotorEx kickerMotor;
    private final Servo aimServo;

    public Turret(HardwareMap hardwareMap, Telemetry telemetry) {
        super(telemetry);
        launcherMotor = hardwareMap.get(
                DcMotorEx.class, RobotConstants.TurretConstants.LAUNCHER_MOTOR_NAME);
        launcherMotor.setDirection(RobotConstants.TurretConstants.LAUNCHER_MOTOR_DIRECTION);
        launcherMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        launcherMotor.setVelocityPIDFCoefficients(
                RobotConstants.TurretConstants.LAUNCHER_VELOCITY_P,
                RobotConstants.TurretConstants.LAUNCHER_VELOCITY_I,
                RobotConstants.TurretConstants.LAUNCHER_VELOCITY_D,
                RobotConstants.TurretConstants.LAUNCHER_VELOCITY_F);

        kickerMotor = hardwareMap.get(
                DcMotorEx.class, RobotConstants.TurretConstants.KICKER_MOTOR_NAME);
        kickerMotor.setDirection(RobotConstants.TurretConstants.KICKER_MOTOR_DIRECTION);
        kickerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        aimServo = hardwareMap.get(
                Servo.class, RobotConstants.TurretConstants.AIM_SERVO_NAME);
    }

    /** Sets launcher output from -1.0 to 1.0. */
    public void setLauncherPower(double power) {
        if (!Double.isFinite(power)) {
            throw new IllegalArgumentException("Launcher power must be finite.");
        }
        launcherMotor.setPower(Range.clip(power, -1.0, 1.0));
    }

    /** Sets launcher velocity in the motor encoder's ticks per second. */
    public void setLauncherVelocity(double ticksPerSecond) {
        launcherMotor.setVelocity(ticksPerSecond);
    }

    public double getLauncherVelocity() {
        return launcherMotor.getVelocity();
    }

    public double getMaxLauncherVelocity() {
        return launcherMotor.getMotorType().getAchieveableMaxTicksPerSecond();
    }

    public void stopLauncher() {
        launcherMotor.setPower(0.0);
    }

    /** Runs the kicker wheel at full power to feed balls into the launcher. */
    public void runKickerForward() {
        kickerMotor.setPower(1.0);
    }

    /** Runs the kicker wheel at full power in reverse. */
    public void runKickerReverse() {
        kickerMotor.setPower(-1.0);
    }

    /** Stops the kicker wheel. */
    public void stopKicker() {
        kickerMotor.setPower(0.0);
    }

    /** Sets turret aim-servo position from 0.0 to 1.0. */
    public void setAimPosition(double position) {
        aimServo.setPosition(clampServoPosition(position));
    }

    public double getAimPosition() {
        return aimServo.getPosition();
    }

    private double clampServoPosition(double position) {
        if (!Double.isFinite(position)) {
            throw new IllegalArgumentException("Servo position must be finite.");
        }
        return Range.clip(position, 0.0, 1.0);
    }

    @Override
    protected boolean isBusy() {
        return false;
    }

    @Override
    protected void update() {
        // The launcher and positional servos are controlled directly by their setters.
    }
}
