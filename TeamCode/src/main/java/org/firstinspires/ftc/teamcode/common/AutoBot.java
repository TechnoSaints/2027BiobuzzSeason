package org.firstinspires.ftc.teamcode.common;

import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class AutoBot extends Bot {

    public AutoBot(OpMode opMode, Telemetry telemetry) {
        super(opMode, telemetry);
    }

    @Override
    public boolean isBusy() {
        return follower != null && follower.isBusy();
    }

    @Override
    public void update() {
        super.update();
    }

    public void followPath(Path path) {
        if (follower != null) {
            follower.follow(path);
        }
    }

    public void holdPose(Pose pose) {
        if (follower != null) {
            follower.hold(pose);
        }
    }

    public void setPose(Pose pose) {
        if (follower != null) {
            follower.setPose(pose);
        }
    }

    public void forward() {
        if (intake != null) {
            intake.forward();
        }
    }

    public void reverse() {
        if (intake != null) {
            intake.reverse();
        }
    }

    public void stop() {
        if (intake != null) {
            intake.stop();
        }
    }
}
