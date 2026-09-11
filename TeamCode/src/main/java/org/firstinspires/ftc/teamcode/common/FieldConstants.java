package org.firstinspires.ftc.teamcode.common;

import com.pedropathing.math.Pose;

/**
 * FieldConstants holds the coordinates for various important locations on the field.
 * All coordinates are in inches.
 */
public class FieldConstants {
    // Starting Poses
    public static final Pose RED_LEFT_START = new Pose(9, 111, Math.toRadians(0));
    public static final Pose RED_RIGHT_START = new Pose(9, 33, Math.toRadians(0));
    public static final Pose BLUE_LEFT_START = new Pose(135, 33, Math.toRadians(180));
    public static final Pose BLUE_RIGHT_START = new Pose(135, 111, Math.toRadians(180));

    // Sample Scoring Locations (Adjust based on actual game)
    public static final Pose RED_SCORING_POSITION = new Pose(48, 48, Math.toRadians(45));
    public static final Pose BLUE_SCORING_POSITION = new Pose(96, 96, Math.toRadians(225));

    // Sample Intake Locations
    public static final Pose RED_INTAKE_POSITION = new Pose(24, 24, Math.toRadians(0));
    public static final Pose BLUE_INTAKE_POSITION = new Pose(120, 120, Math.toRadians(180));
    
    // Intermediate Waypoints
    public static final Pose RED_CENTER_WAYPOINT = new Pose(72, 48, Math.toRadians(0));
    public static final Pose BLUE_CENTER_WAYPOINT = new Pose(72, 96, Math.toRadians(180));
}
