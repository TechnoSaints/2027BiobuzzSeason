package org.firstinspires.ftc.teamcode.common;

import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * AutoBot is the robot used during Autonomous.
 * On top of everything Bot already does, it adds the ability to follow a
 * pre-planned path and to hold still at a specific spot on the field.
 */
public class AutoBot extends Bot {

    public AutoBot(OpMode opMode, Telemetry telemetry) {
        super(opMode, telemetry);
    }

    /**
     * @return true while the robot is still driving along a path.
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
     * Starts driving the robot along the given path.
     * @param path the path to follow, usually built with AutoPaths.
     */
    public void followPath(Path path) {
        if (follower != null) {
            follower.follow(path);
        }
    }

    /**
     * Tells the robot to stay put at one exact spot and heading.
     * @param pose the location and heading to hold.
     */
    public void holdPose(Pose pose) {
        if (follower != null) {
            follower.hold(pose);
        }
    }

    /**
     * Tells the robot "you are here" - use this once at the start of Autonomous
     * to match the robot's actual starting spot on the field.
     * @param pose the robot's starting position.
     */
    public void setPose(Pose pose) {
        if (follower != null) {
            follower.setPose(pose);
        }
    }

    /**
     * @return the robot's current field position as text, ready to print
     *         with telemetry, or "unknown" if the localizer never started up.
     */
    public String getPoseString() {
        return follower != null ? follower.pose().toString() : "unknown";
    }
}
