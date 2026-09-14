package org.firstinspires.ftc.teamcode.common;

import com.pedropathing.drivetrain.DrivePowers;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * TeleopBot is the robot used during driver-controlled play.
 * On top of everything Bot already does, it adds the ability to drive
 * with the joysticks instead of following a pre-planned path.
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
     * Drives the robot using joystick-style inputs.
     * @param forward how fast to move forward/backward, from -1 to 1.
     * @param strafe how fast to move left/right, from -1 to 1.
     * @param turn how fast to spin, from -1 to 1.
     * @param powerScale a multiplier on all of the above, e.g. 0.3 for a slow/precise mode.
     */
    public void setDrivePowers(double forward, double strafe, double turn, double powerScale) {
        if (follower != null && follower.drivetrain != null) {
            double scaledForward = forward * powerScale;
            double scaledStrafe = strafe * powerScale;
            double scaledTurn = turn * powerScale;

            // Hand the scaled speeds off to the drivetrain to actually move the wheels
            follower.drivetrain.drive(new DrivePowers(scaledForward, scaledStrafe, scaledTurn), true);
        }
    }

    /** Tells the robot "you are currently facing forward." Use this if the robot's sense of direction has drifted. */
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
