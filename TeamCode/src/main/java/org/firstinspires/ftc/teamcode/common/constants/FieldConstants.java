package org.firstinspires.ftc.teamcode.common.constants;

import com.pedropathing.geometry.Pose;

public class FieldConstants {
    public static Pose startPose = new Pose(0, 0, 0);
    public static Pose cornerPose = new Pose(-72,0,3 * Math.PI/4);
    public static Pose farFlowerPickupPose = new Pose(0, 144, Math.PI);
    public static Pose sideFlowerPickupPose = new Pose(-72, 60, Math.PI/2);
    public static Pose pushStartPose = new Pose(-72, 72, 0);
    public static Pose endPose = new Pose(-72, 84, 0);
    public static Pose farCornerControlPose = new Pose(-66, 132, 0);
}
