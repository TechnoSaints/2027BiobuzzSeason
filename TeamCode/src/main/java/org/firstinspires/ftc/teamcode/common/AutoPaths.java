package org.firstinspires.ftc.teamcode.common;

import com.pedropathing.api.Paths;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

/**
 * AutoPaths provides pre-defined paths for autonomous routines.
 */
public class AutoPaths {

    /**
     * Generates a path from the starting position to the scoring position via a waypoint.
     */
    public static Path getScorePath(boolean isRed) {
        if (isRed) {
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
     * Generates a simple line path between two poses.
     */
    public static Path getSimplePath(Pose start, Pose end) {
        return Paths.line(start, end);
    }
}
