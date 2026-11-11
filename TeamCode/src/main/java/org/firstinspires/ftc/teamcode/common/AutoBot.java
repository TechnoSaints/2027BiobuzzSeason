package org.firstinspires.ftc.teamcode.common;

import com.pedropathing.follower.Follower;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.components.Drivetrain;
import org.firstinspires.ftc.teamcode.common.components.PollenTurret;
import org.firstinspires.ftc.teamcode.common.constants.RobotConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.common.constants.FieldLocations;


public class AutoBot extends Bot {
    private Follower follower;

    public AutoBot(OpMode opMode, Telemetry telemetry, PollenTurret.Alliance alliance) {
        super(opMode, telemetry, alliance);
        follower = Constants.createFollower(opMode.hardwareMap);
        telemetry.addLine(FieldLocations.startPose.toString());
        telemetry.update();
        follower.setStartingPose(FieldLocations.startPose);
    }

    public void followPath(PathChain path, double maxSpeed) {
        followPath(path, maxSpeed, true);
    }

    public void followPath(PathChain path, double maxPower, boolean holdEnd)
    {
        follower.followPath(path, maxPower, holdEnd);
    }

    public Follower getFollower() {
        return (follower);
    }

    //    public void moveManualInches(double axial, double strafe, double heading) {
//        Pose targetPose = new Pose(getFollower().getPose().getX() - axial, getFollower().getPose().getY() - strafe, getFollower().getPose().getHeading() + Math.toRadians(heading));
//
//        PathChain targetPath = follower.pathBuilder()
//                .addPath(new BezierLine(getFollower().getPose(), targetPose))
//                .setLinearHeadingInterpolation(getFollower().getPose().getHeading(), targetPose.getHeading())
//                .build();
//
//        followPath(targetPath, 0.8);
//    }
    public boolean followerIsBusy()
    {
        return (follower.isBusy());
    }

    public boolean isBusy()
    {
        return (super.isBusy() || followerIsBusy());
    }

    public void update() {
        super.update();
        //launcher.distanceFromLauncher(follower.getPose(), red);
        follower.update();
    }
}
