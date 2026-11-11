package org.firstinspires.ftc.teamcode.common.constants;

import com.pedropathing.geometry.Pose;

public class FieldLocations {
    public static Pose startPose = new Pose(132.5, 128, Math.toRadians(90));
    public static Pose cornerIntakePose = new Pose(132.5,129,Math.toRadians(90));
    public static Pose sideFlowerSetupPose = new Pose(119, 95, Math.toRadians(0));
    public static Pose sideFlowerPickupPose = new Pose(9.5,48,Math.toRadians(180));
    public static Pose farFlowerSetupPose = new Pose(48, 120, Math.toRadians(90));
    public static Pose farFlowerPickupPose = new Pose(48, 131, Math.toRadians(90));
    public static Pose teamSetupPose = new Pose(34, 83, Math.toRadians(180));
    public static Pose teamPickupPose = new Pose(25, 83, Math.toRadians(180));
    public static Pose teamShootPose = new Pose(35, 118, Math.toRadians(180));
    public static Pose parkStrafePose = new Pose(35, 63, Math.toRadians(270));
    public static Pose parkSetupPose = new Pose(9, 63, Math.toRadians(270));
    public static Pose parkPose = new Pose(9, 93, Math.toRadians(270));


    // Control points for sideFlowerPickupPose to farFlowerSetupPose
    public static Pose controlPoint1 = new Pose(17, 47);
    public static Pose controlPoint2 = new Pose(34, 53);
    public static Pose controlPoint3 = new Pose(-9, 37);
    public static Pose controlPose4 = new Pose(178, 77);
    public static Pose controlPoint5 = new Pose(-214, 94);
    public static Pose controlPoint6 = new Pose(314,27);
    public static Pose controlPoint7 = new Pose(-192, 154);
    public static Pose controlPoint8 = new Pose(145, 59);
    public static Pose controlPoint9 = new Pose(6, 118);
    public static Pose controlPoint10 = new Pose(46, 111);
}
