package org.firstinspires.ftc.teamcode.common;

import com.pedropathing.math.Pose;

/**
 * FieldConstants holds the coordinates for various important locations on the field.
 * All coordinates are in inches. 
 * Pedro Pathing uses (0,0) as a reference point on the field.
 */
public class FieldConstants {
    // Poses define a position (x, y) and an orientation (heading)
    
    // Starting Poses: Where the robot begins the match
    public static final Pose RED_LEFT_START = new Pose(9, 111, Math.toRadians(0));
    public static final Pose RED_RIGHT_START = new Pose(9, 33, Math.toRadians(0));
    public static final Pose BLUE_LEFT_START = new Pose(135, 33, Math.toRadians(180));
    public static final Pose BLUE_RIGHT_START = new Pose(135, 111, Math.toRadians(180));

    // Scoring Locations: Where the robot needs to go to score
    public static final Pose RED_SCORING_POSITION = new Pose(48, 48, Math.toRadians(45));
    public static final Pose BLUE_SCORING_POSITION = new Pose(96, 96, Math.toRadians(225));

    // Intake Locations: Where the robot can pick up game pieces
    public static final Pose RED_INTAKE_POSITION = new Pose(24, 24, Math.toRadians(0));
    public static final Pose BLUE_INTAKE_POSITION = new Pose(120, 120, Math.toRadians(180));
    
    // Waypoints: Points the robot passes through to avoid obstacles
    public static final Pose RED_CENTER_WAYPOINT = new Pose(72, 48, Math.toRadians(0));
    public static final Pose BLUE_CENTER_WAYPOINT = new Pose(72, 96, Math.toRadians(180));

    // Circuit Points for testing complex movement
    public static final Pose CIRCUIT_POINT_1 = new Pose(60, 120, Math.toRadians(0));
    public static final Pose CIRCUIT_POINT_2 = new Pose(120, 60, Math.toRadians(-90));
    public static final Pose CIRCUIT_POINT_3 = new Pose(60, 20, Math.toRadians(180));
    public static final Pose CIRCUIT_POINT_4 = new Pose(20, 60, Math.toRadians(90));

    // Intake Test Points: a simple box pattern used to test drivetrain movement and the intake together
    public static final Pose INTAKE_TEST_START = new Pose(0, 0, 0);
    public static final Pose INTAKE_TEST_FORWARD = new Pose(36, 0, 0);
    public static final Pose INTAKE_TEST_LEFT = new Pose(36, 24, 0);
    public static final Pose INTAKE_TEST_BACKWARD = new Pose(12, 24, 0);
    public static final Pose INTAKE_TEST_RIGHT = new Pose(12, -24, 0);
}
