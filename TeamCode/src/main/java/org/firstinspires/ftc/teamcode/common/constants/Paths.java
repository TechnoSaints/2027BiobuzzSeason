package org.firstinspires.ftc.teamcode.common.constants;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;

import java.util.FormatFlagsConversionMismatchException;

public class Paths {
    public static PathChain startToCorner, cornerToSideFlower, sideFlowerToFarFlower, farFlowerToPushStart, pushStartToEnd;

    public static void buildPaths (Follower follower) {
        // Implementation for building paths
        startToCorner = follower.pathBuilder()
                .addPath(new BezierLine(FieldConstants.startPose, FieldConstants.cornerPose))
                .setLinearHeadingInterpolation(FieldConstants.startPose.getHeading(), FieldConstants.cornerPose.getHeading())
                .build();
        cornerToSideFlower = follower.pathBuilder()
                .addPath(new BezierLine(FieldConstants.cornerPose, FieldConstants.sideFlowerPickupPose))
                .setLinearHeadingInterpolation(FieldConstants.cornerPose.getHeading(), FieldConstants.sideFlowerPickupPose.getHeading())
                .build();
        sideFlowerToFarFlower = follower.pathBuilder()
                .addPath(new BezierCurve(FieldConstants.sideFlowerPickupPose, FieldConstants.farCornerControlPose, FieldConstants.farFlowerPickupPose))
                .setLinearHeadingInterpolation(FieldConstants.sideFlowerPickupPose.getHeading(), FieldConstants.farFlowerPickupPose.getHeading())
                .build();
        farFlowerToPushStart = follower.pathBuilder()
                .addPath(new BezierCurve(FieldConstants.farFlowerPickupPose, FieldConstants.farCornerControlPose, FieldConstants.pushStartPose))
                .setLinearHeadingInterpolation(FieldConstants.farFlowerPickupPose.getHeading(), FieldConstants.pushStartPose.getHeading())
                .build();
        pushStartToEnd = follower.pathBuilder()
                .addPath(new BezierLine(FieldConstants.pushStartPose, FieldConstants.endPose))
                .setLinearHeadingInterpolation(FieldConstants.pushStartPose.getHeading(), FieldConstants.endPose.getHeading())
                .build();
    }
}
