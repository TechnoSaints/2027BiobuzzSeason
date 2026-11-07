package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.components.Drivetrain;
import org.firstinspires.ftc.teamcode.common.components.PollenTurret;
import org.firstinspires.ftc.teamcode.common.constants.RobotConstants;

/**
 * TeleopBot is specialized for TeleOp (driver controlled) mode.
 * It provides methods for manual driving and subsystem control.
 */
public class TeleopBot extends Bot {
    private final AccelerationLimiter forwardLimiter = new AccelerationLimiter();
    private final AccelerationLimiter strafeLimiter = new AccelerationLimiter();
    private final AccelerationLimiter turnLimiter = new AccelerationLimiter();
    private static Drivetrain drivetrain;
    private long lastDriveUpdateNanos = System.nanoTime();

    public TeleopBot(OpMode opMode, Telemetry telemetry, PollenTurret.Alliance alliance) {
        super(opMode, telemetry, alliance);
        drivetrain = new Drivetrain(telemetry, opMode.hardwareMap);
//        if (follower != null) {
//            follower.startTeleopDrive();
//        }
    }

    @Override
    public boolean isBusy() {
        return false;
    }

    /**
     * Sets the drive powers for the robot.
     * @param forward The forward/backward speed (-1 to 1).
     * @param strafe The left/right speed (-1 to 1).
     * @param turn The rotation speed (-1 to 1).
     * @param powerScale A multiplier for the overall speed (e.g. 0.3 for slow mode).
     */
    public void setPedroDrivePowers(double forward, double strafe, double turn, double powerScale) {
         if (follower != null) {
            long now = System.nanoTime();
            double deltaTimeSeconds = (now - lastDriveUpdateNanos) / 1_000_000_000.0;
            lastDriveUpdateNanos = now;

            double scaledForward = forward * powerScale;
            double scaledStrafe = strafe * powerScale;
            double scaledTurn = turn * powerScale;

            // Sends the movement vectors to the drivetrain
             // Untested AI code; disabled for now
            follower.setTeleOpDrive(scaledForward, scaledStrafe, scaledTurn,
//                    forwardLimiter.calculate(
//                            scaledForward, RobotConstants.Drivetrain.maxDriveAcceleration,
//                            deltaTimeSeconds
//                    ),
//                    strafeLimiter.calculate(
//                            scaledStrafe, RobotConstants.Drivetrain.maxDriveAcceleration,
//                            deltaTimeSeconds
//                    ),
//                    turnLimiter.calculate(
//                            scaledTurn, RobotConstants.Drivetrain.maxDriveAcceleration,
//                            deltaTimeSeconds
//                    ),
                    true
            );
        }
    }

    public void setManualDrivePowers(double axial, double strafe, double yaw){
        drivetrain.setDrivetrainPowers(axial, strafe, yaw);
    }

    public void stopDrive() {
        if (follower != null) {
            forwardLimiter.reset(0);
            strafeLimiter.reset(0);
            turnLimiter.reset(0);
            lastDriveUpdateNanos = System.nanoTime();
            follower.setTeleOpDrive(0, 0, 0, true);
            follower.update();
        }
    }

    /** Runs the intake forward */
    public void forward() {
        if (intake != null) {
            intake.forward();
        }
    }

    /** Runs the intake in reverse */
    public void reverse() {
        if (intake != null) {
            intake.reverse();
        }
    }

    /** Stops the intake */
    @Override
    public void stop() {
        super.stop();
    }

    /** Resets the robot's heading to zero. Use this if the robot's orientation gets drift. */
    public void resetHeading() {
        if (follower != null) {
            follower.setHeading(0);
        }
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void log(){
        super.log();
        drivetrain.log();
    }
}
