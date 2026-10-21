package org.firstinspires.ftc.teamcode.opmode.auto;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.common.FieldConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

/**
 * Runs a configurable straight-line field test and reports localization error.
 * Edit the public static settings in Panels Configurables before initializing
 * the OpMode. Translation and heading tests can be combined in one path.
 */
@Configurable
@Autonomous(name = "BIOBUZZ Path Calibration", group = "Pedro Pathing")
public class BioBuzzPathCalibration extends AutoOpMode {
    public static double startXInches = FieldConstants.tileCenter('A', 4, 0).getX();
    public static double startYInches = FieldConstants.tileCenter('A', 4, 0).getY();
    public static double startHeadingDegrees = 0.0;
    public static double travelDirectionDegrees = 0.0;
    public static double travelDistanceInches = 40.0;
    public static double targetHeadingDegrees = 0.0;

    @Override
    public void runOpMode() {
        String configurationError = getConfigurationError();
        if (configurationError != null) {
            telemetry.addData("Configuration error", configurationError);
            telemetry.update();
            return;
        }

        Pose startPose = FieldConstants.pose(
                startXInches, startYInches, startHeadingDegrees);
        double directionRadians = Math.toRadians(travelDirectionDegrees);
        double targetX = startXInches + travelDistanceInches * Math.cos(directionRadians);
        double targetY = startYInches + travelDistanceInches * Math.sin(directionRadians);
        double startHeadingRadians = Math.toRadians(startHeadingDegrees);
        double targetHeadingRadians = startHeadingRadians
                + wrapRadians(Math.toRadians(targetHeadingDegrees - startHeadingDegrees));
        Pose targetPose = new Pose(targetX, targetY, targetHeadingRadians);

        if (!FieldConstants.isInsideFieldForStartingRobot(targetX, targetY)) {
            telemetry.addLine("Calibration endpoint is outside the safe field boundary.");
            telemetry.addData("Endpoint", "(%.1f, %.1f) in", targetX, targetY);
            telemetry.update();
            return;
        }

        initBot(startPose);
        if (bot.follower == null) {
            telemetry.addLine("Cannot run calibration: Pedro follower failed to initialize.");
            telemetry.update();
            return;
        }

        telemetry.addLine("BIOBUZZ field-frame calibration");
        telemetry.addData("Start", "(%.1f, %.1f, %.1f deg)",
                startXInches, startYInches, startHeadingDegrees);
        telemetry.addData("Target", "(%.1f, %.1f, %.1f deg)",
                targetX, targetY, targetHeadingDegrees);
        telemetry.addData("Travel", "%.1f in at %.1f deg",
                travelDistanceInches, travelDirectionDegrees);
        telemetry.addData("Configured velocity", "forward %.1f / lateral %.1f in/s",
                Constants.forwardVelocity, Constants.lateralVelocity);
        telemetry.addData("Maximum auto power", "%.2f", Constants.maxAutoPower);
        telemetry.update();

        while (!isStarted() && !isStopRequested()) {
            bot.update();
            telemetry.addData("Current pose", bot.getPoseString());
            telemetry.update();
            idle();
        }
        if (isStopRequested()) {
            return;
        }

        Path testPath = new Path(new BezierLine(startPose, targetPose));
        testPath.setLinearHeadingInterpolation(
                startHeadingRadians,
                targetHeadingRadians
        );

        ElapsedTime runTimer = new ElapsedTime();
        bot.followPath(testPath);
        while (opModeIsActive() && bot.isBusy()) {
            bot.update();
            telemetry.addData("Calibration", "Following test path");
            telemetry.addData("Target", "(%.1f, %.1f, %.1f deg)",
                    targetX, targetY, targetHeadingDegrees);
            telemetry.addData("Current pose", bot.getPoseString());
            telemetry.update();
            idle();
        }

        if (opModeIsActive()) {
            Pose actualPose = bot.follower.getPose();
            double headingErrorDegrees = wrapDegrees(
                    Math.toDegrees(targetPose.getHeading() - actualPose.getHeading()));
            telemetry.addData("Calibration", "Complete");
            telemetry.addData("Elapsed", "%.2f s", runTimer.seconds());
            telemetry.addData("Measured pose", actualPose.toString());
            telemetry.addData("X error", "%.1f in", targetX - actualPose.getX());
            telemetry.addData("Y error", "%.1f in", targetY - actualPose.getY());
            telemetry.addData("Heading error", "%.1f deg", headingErrorDegrees);
            telemetry.update();

            while (opModeIsActive()) {
                bot.update();
                idle();
            }
        }
    }

    private String getConfigurationError() {
        if (!Double.isFinite(startXInches) || !Double.isFinite(startYInches)
                || !Double.isFinite(startHeadingDegrees)) {
            return "Start pose values must be finite.";
        }
        if (!FieldConstants.isInsideFieldForStartingRobot(startXInches, startYInches)) {
            return "Start pose must leave the 9-inch robot half-width inside the field.";
        }
        if (!Double.isFinite(travelDirectionDegrees)
                || !Double.isFinite(targetHeadingDegrees)) {
            return "Travel direction and target heading must be finite.";
        }
        if (!Double.isFinite(travelDistanceInches) || travelDistanceInches <= 0
                || travelDistanceInches > FieldConstants.FIELD_SIZE_INCHES) {
            return "Travel distance must be greater than 0 and no more than 144 inches.";
        }
        if (!Double.isFinite(Constants.forwardVelocity) || Constants.forwardVelocity <= 0
                || !Double.isFinite(Constants.lateralVelocity) || Constants.lateralVelocity <= 0) {
            return "Pedro forward and lateral velocities must be positive and finite.";
        }
        if (!Double.isFinite(Constants.maxAutoPower)
                || Constants.maxAutoPower <= 0 || Constants.maxAutoPower > 1.0) {
            return "Pedro maximum auto power must be greater than 0 and no more than 1.";
        }
        return null;
    }

    private double wrapDegrees(double degrees) {
        return Math.toDegrees(Math.atan2(
                Math.sin(Math.toRadians(degrees)),
                Math.cos(Math.toRadians(degrees))
        ));
    }

    private double wrapRadians(double radians) {
        return Math.atan2(Math.sin(radians), Math.cos(radians));
    }
}
