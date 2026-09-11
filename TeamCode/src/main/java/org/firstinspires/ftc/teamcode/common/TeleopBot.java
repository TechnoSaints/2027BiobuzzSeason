package org.firstinspires.ftc.teamcode.common;

import com.pedropathing.drivetrain.DrivePowers;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * TeleopBot is specialized for TeleOp (driver controlled) mode.
 * It provides methods for manual driving and subsystem control.
 */
public class TeleopBot extends Bot {

    public TeleopBot(OpMode opMode, Telemetry telemetry) {
        super(opMode, telemetry);
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
    public void setDrivePowers(double forward, double strafe, double turn, double powerScale) {
        if (follower != null && follower.drivetrain != null) {
            double scaledForward = forward * powerScale;
            double scaledStrafe = strafe * powerScale;
            double scaledTurn = turn * powerScale;

            // Sends the movement vectors to the drivetrain
            follower.drivetrain.drive(new DrivePowers(scaledForward, scaledStrafe, scaledTurn), true);
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
    public void stop() {
        if (intake != null) {
            intake.stop();
        }
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
}
