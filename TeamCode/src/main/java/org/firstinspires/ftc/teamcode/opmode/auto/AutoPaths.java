package org.firstinspires.ftc.teamcode.opmode.auto;

import com.pedropathing.api.Paths;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.common.FieldConstants;

/**
 * AutoPaths is where we build reusable paths for Autonomous.
 * Building a path once here (instead of inside an OpMode) means multiple
 * routines can share the exact same path without copy-pasting it.
 */
public class AutoPaths {

    /**
     * Builds a smooth path from the starting corner to the scoring spot.
     * @param isRed true for the red alliance's path, false for blue.
     * @return the path to follow.
     */
    public static Path getScorePath(boolean isRed) {
        if (isRed) {
            // curve() draws a smooth, rounded path through these points instead of sharp turns
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
     * Builds a path that loops through 4 test points and back to the start.
     * Handy for checking that the robot can follow a complicated path accurately.
     */
    public static Path getCircuitPath() {
        // through() makes the path pass exactly through every point given, in order
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
