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
        return follower.isBusy();
    }

    @Override
    public void update() {
        super.update();
    }

    public void followPath(Path path) {
        follower.follow(path);
    }

    public void holdPose(Pose pose) {
        follower.hold(pose);
    }

    public void setPose(Pose pose) {
        follower.setPose(pose);
    }
}
