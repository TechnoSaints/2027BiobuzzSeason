package org.firstinspires.ftc.teamcode.common.constants;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;

public class Paths {
    public static PathChain startToCornerIntake;
    public static PathChain cornerIntakeToSideFlowerSetup;
    public static PathChain sideFlowerSetupToSideFlowerPickup;
    public static PathChain sideFlowerPickupToFarFlowerSetup;
    public static PathChain farFlowerSetupToFarFlowerPickup;
    public static PathChain farFlowerPickupToTeamSetup;
    public static PathChain teamSetupToTeamPickup;
    public static PathChain teamPickupToTeamShoot;
    public static PathChain teamShootToParkStrafe;
    public static PathChain parkStrafeToParkSetup;
    public static PathChain parkSetupToPark;

    public static void buildPaths(Follower follower) {
        startToCornerIntake = buildLine(follower, FieldConstants.startPose, FieldConstants.cornerIntakePose);
        cornerIntakeToSideFlowerSetup = buildLine(follower, FieldConstants.cornerIntakePose, FieldConstants.sideFlowerSetupPose);
        sideFlowerSetupToSideFlowerPickup = buildLine(follower, FieldConstants.sideFlowerSetupPose, FieldConstants.sideFlowerPickupPose);
        sideFlowerPickupToFarFlowerSetup = follower.pathBuilder()
                .addPath(new BezierCurve(
                        FieldConstants.sideFlowerPickupPose,
                        FieldConstants.controlPoint1,
                        FieldConstants.controlPoint2,
                        FieldConstants.controlPoint3,
                        FieldConstants.controlPose4,
                        FieldConstants.controlPoint5,
                        FieldConstants.controlPoint6,
                        FieldConstants.controlPoint7,
                        FieldConstants.controlPoint8,
                        FieldConstants.controlPoint9,
                        FieldConstants.controlPoint10,
                        FieldConstants.farFlowerSetupPose
                ))
                .setLinearHeadingInterpolation(
                        FieldConstants.sideFlowerPickupPose.getHeading(),
                        FieldConstants.farFlowerSetupPose.getHeading()
                )
                .build();
        farFlowerSetupToFarFlowerPickup = buildLine(follower, FieldConstants.farFlowerSetupPose, FieldConstants.farFlowerPickupPose);
        farFlowerPickupToTeamSetup = buildLine(follower, FieldConstants.farFlowerPickupPose, FieldConstants.teamSetupPose);
        teamSetupToTeamPickup = buildLine(follower, FieldConstants.teamSetupPose, FieldConstants.teamPickupPose);
        teamPickupToTeamShoot = buildLine(follower, FieldConstants.teamPickupPose, FieldConstants.teamShootPose);
        teamShootToParkStrafe = buildLine(follower, FieldConstants.teamShootPose, FieldConstants.parkStrafePose);
        parkStrafeToParkSetup = buildLine(follower, FieldConstants.parkStrafePose, FieldConstants.parkSetupPose);
        parkSetupToPark = buildLine(follower, FieldConstants.parkSetupPose, FieldConstants.parkPose);
    }

    private static PathChain buildLine(Follower follower, Pose start, Pose end) {
        return follower.pathBuilder()
                .addPath(new BezierLine(start, end))
                .setLinearHeadingInterpolation(start.getHeading(), end.getHeading())
                .build();
    }
}