package org.firstinspires.ftc.teamcode.common;

import com.pedropathing.geometry.Pose;

/**
 * Nominal BIOBUZZ field coordinates, based on the official 2026-2027 FTC
 * Competition Manual, section 9 and figures 9-4 through 9-5.
 *
 * The manual defines a 6-by-6 grid of nominal 24-inch tiles, but does not
 * prescribe a Cartesian origin or heading convention. This project uses the
 * audience-left inside corner as (0, 0), +X toward the audience's right,
 * +Y away from the audience, and headings in radians increasing
 * counterclockwise from +X.
 * In this frame, the red side occupies tile columns A-C and the blue side
 * occupies columns D-F. The manual does not give fixed field (x, y) values
 * for FLOWERS or the AprilTags; the tags move with the tipping HIVE cells.
 *
 * Official sources:
 * https://ftc-resources.firstinspires.org/ftc/game/cm-html
 * https://ftc-resources.firstinspires.org/ftc/archive/2027/field/field-cad
 */
public final class FieldConstants {
    public static final int TILE_COUNT = 6;
    public static final double TILE_SIZE_INCHES = 24.0;
    public static final double FIELD_SIZE_INCHES = TILE_COUNT * TILE_SIZE_INCHES;
    public static final double ALLIANCE_DIVIDER_X_INCHES = FIELD_SIZE_INCHES / 2.0;
    public static final double ROBOT_HALF_SIZE_INCHES = 9.0;

    /** Nominal center of the field and HIVE structure; not an approach pose. */
    public static final Pose FIELD_CENTER = new Pose(
            FIELD_SIZE_INCHES / 2.0,
            FIELD_SIZE_INCHES / 2.0,
            0.0
    );

    private FieldConstants() {
    }

    /**
     * Returns the center of a field tile. Columns are A-F and rows are 1-6,
     * as labeled in the official field diagram.
     *
     * @param column tile column, A through F
     * @param row tile row, 1 through 6
     * @param headingDegrees robot heading in the project field frame
     * @return the tile-center pose, with heading converted to radians
     */
    public static Pose tileCenter(char column, int row, double headingDegrees) {
        char normalizedColumn = Character.toUpperCase(column);
        if (normalizedColumn < 'A' || normalizedColumn >= 'A' + TILE_COUNT) {
            throw new IllegalArgumentException("Tile column must be A-F");
        }
        if (row < 1 || row > TILE_COUNT) {
            throw new IllegalArgumentException("Tile row must be 1-6");
        }

        double x = (normalizedColumn - 'A' + 0.5) * TILE_SIZE_INCHES;
        double y = (row - 0.5) * TILE_SIZE_INCHES;
        return pose(x, y, headingDegrees);
    }

    /**
     * Returns a pose on a tile seam intersection. Grid lines are numbered
     * 0-6 from the audience-left/near corner.
     */
    public static Pose gridIntersection(
            int xGridLine,
            int yGridLine,
            double headingDegrees) {
        if (xGridLine < 0 || xGridLine > TILE_COUNT
                || yGridLine < 0 || yGridLine > TILE_COUNT) {
            throw new IllegalArgumentException("Grid line indices must be 0-6");
        }

        return pose(
                xGridLine * TILE_SIZE_INCHES,
                yGridLine * TILE_SIZE_INCHES,
                headingDegrees
        );
    }

    public static Pose pose(double xInches, double yInches, double headingDegrees) {
        return new Pose(xInches, yInches, Math.toRadians(headingDegrees));
    }

    /**
     * Checks that the center of a nominal 18-inch robot remains inside the
     * field perimeter with its full starting-size footprint.
     */
    public static boolean isInsideFieldForStartingRobot(double xInches, double yInches) {
        return Double.isFinite(xInches)
                && Double.isFinite(yInches)
                && xInches >= ROBOT_HALF_SIZE_INCHES
                && xInches <= FIELD_SIZE_INCHES - ROBOT_HALF_SIZE_INCHES
                && yInches >= ROBOT_HALF_SIZE_INCHES
                && yInches <= FIELD_SIZE_INCHES - ROBOT_HALF_SIZE_INCHES;
    }
}
