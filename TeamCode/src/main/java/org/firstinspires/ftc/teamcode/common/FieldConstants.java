package org.firstinspires.ftc.teamcode.common;

import com.pedropathing.math.Pose;

/**
 * FieldConstants is a list of important spots on the field, written as (x, y, heading).
 * Distances are in inches, and (0, 0) is one corner of the field.
 * Writing coordinates here once - instead of typing them into every OpMode -
 * means everyone agrees on where things are and it's easy to fix a spot later.
 */
public class FieldConstants {
    // A Pose is just an (x, y) spot on the field plus which way the robot is facing (heading)

    // Where the robot starts sitting before the match begins
    public static final Pose RED_LEFT_START = new Pose(9, 111, Math.toRadians(0));
    public static final Pose BLUE_RIGHT_START = new Pose(135, 111, Math.toRadians(180));

    // Where the robot needs to drive to score
    public static final Pose RED_SCORING_POSITION = new Pose(48, 48, Math.toRadians(45));
    public static final Pose BLUE_SCORING_POSITION = new Pose(96, 96, Math.toRadians(225));

    // Points along the way, used so a path can curve around instead of driving straight through something
    public static final Pose RED_CENTER_WAYPOINT = new Pose(72, 48, Math.toRadians(0));
    public static final Pose BLUE_CENTER_WAYPOINT = new Pose(72, 96, Math.toRadians(180));

    // A loop of 4 points used to test that the robot can follow a complicated path correctly
    public static final Pose CIRCUIT_POINT_1 = new Pose(60, 120, Math.toRadians(0));
    public static final Pose CIRCUIT_POINT_2 = new Pose(120, 60, Math.toRadians(-90));
    public static final Pose CIRCUIT_POINT_3 = new Pose(60, 20, Math.toRadians(180));
    public static final Pose CIRCUIT_POINT_4 = new Pose(20, 60, Math.toRadians(90));

    // A simple box-shaped path (forward, left, backward, right) used to test driving and the intake together
    public static final Pose INTAKE_TEST_START = new Pose(0, 0, 0);
    public static final Pose INTAKE_TEST_FORWARD = new Pose(36, 0, 0);
    public static final Pose INTAKE_TEST_LEFT = new Pose(36, 24, 0);
    public static final Pose INTAKE_TEST_BACKWARD = new Pose(12, 24, 0);
    public static final Pose INTAKE_TEST_RIGHT = new Pose(12, -24, 0);
}
