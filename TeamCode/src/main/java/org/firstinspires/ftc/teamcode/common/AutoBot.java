package org.firstinspires.ftc.teamcode.common;

import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * AutoBot is specialized for Autonomous routines.
 * It provides methods for path following and holding poses.
 */
public class AutoBot extends Bot {

    public AutoBot(OpMode opMode, Telemetry telemetry) {
        super(opMode, telemetry);
    }

    /**
     * Checks if the robot is currently following a path.
     * @return true if the robot is moving or busy.
     */
    @Override
    public boolean isBusy() {
        return follower != null && follower.isBusy();
    }

    @Override
    public void update() {
        super.update();
    }

    /**
     * Tells the robot to follow a specific path.
     * @param path The path to follow.
     */
    public void followPath(Path path) {
        if (follower != null) {
            follower.follow(path);
        }
    }

    /**
     * Tells the robot to hold a specific position (Pose).
     * @param pose The location and heading to hold.
     */
    public void holdPose(Pose pose) {
        if (follower != null) {
            follower.hold(pose);
        }
    }

    /**
     * Manually updates the robot's current position on the field.
     * @param pose The new position.
     */
    public void setPose(Pose pose) {
        if (follower != null) {
            follower.setPose(pose);
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
}
