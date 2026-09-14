package org.firstinspires.ftc.teamcode.opmode.auto;

import com.pedropathing.api.Paths;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.common.FieldConstants;

/**
 * AutoPaths provides pre-defined paths for autonomous routines.
 * Use this to build complex movements once and reuse them in multiple OpModes.
 */
public class AutoPaths {

    /**
     * Generates a smooth curve path from the starting position to the scoring position.
     * @param isRed true if the robot is on the red alliance.
     * @return A Path object representing the journey.
     */
    public static Path getScorePath(boolean isRed) {
        if (isRed) {
            // curve() creates a smooth Bezier path through multiple points
            return Paths.curve(
                FieldConstants.RED_LEFT_START,
                FieldConstants.RED_CENTER_WAYPOINT,
                FieldConstants.RED_SCORING_POSITION
            );
        } else {
            return Paths.curve(
                FieldConstants.BLUE_RIGHT_START,
                FieldConstants.BLUE_CENTER_WAYPOINT,
                FieldConstants.BLUE_SCORING_POSITION
            );
        }
    }

    /**
     * Generates a straight line path between two points.
     * @param start The starting Pose.
     * @param end The ending Pose.
     * @return A straight Line Path.
     */
    public static Path getSimplePath(Pose start, Pose end) {
        return Paths.line(start, end);
    }

    /**
     * Generates a complex circuit path that loops through 4 waypoints.
     * Useful for testing localization and smooth transitions.
     */
    public static Path getCircuitPath() {
        // through() creates a path that passes exactly through each specified point
        return Paths.through(
            FieldConstants.RED_LEFT_START,
            FieldConstants.CIRCUIT_POINT_1,
            FieldConstants.CIRCUIT_POINT_2,
            FieldConstants.CIRCUIT_POINT_3,
            FieldConstants.CIRCUIT_POINT_4,
            FieldConstants.RED_LEFT_START
        );
    }
}
