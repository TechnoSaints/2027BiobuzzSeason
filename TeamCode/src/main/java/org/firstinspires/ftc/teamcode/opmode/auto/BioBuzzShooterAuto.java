package org.firstinspires.ftc.teamcode.opmode.auto;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.firstinspires.ftc.teamcode.common.FieldConstants;
import org.firstinspires.ftc.teamcode.common.LimelightUtils;
import org.firstinspires.ftc.teamcode.common.TurretConstants;

/**
 * Drives to a configurable candidate shooting location for a BioBuzz Hive,
 * aims the robot at the selected cell, spins up the shooter, and feeds pollen.
 * Default target and shot values are starting estimates and must be tuned on
 * the actual robot and field.
 */
@Configurable
@Autonomous(name = "BIOBUZZ Hive Shooter Auto", group = "BIOBUZZ")
public class BioBuzzShooterAuto extends AutoOpMode {
    private static final Set<Integer> RED_AUDIENCE_TAGS =
            new HashSet<>(Arrays.asList(34, 35, 36, 37));
    private static final Set<Integer> RED_FAR_TAGS =
            new HashSet<>(Arrays.asList(30, 31, 32, 33));
    private static final Set<Integer> BLUE_AUDIENCE_TAGS =
            new HashSet<>(Arrays.asList(38, 39, 40, 41));
    private static final Set<Integer> BLUE_FAR_TAGS =
            new HashSet<>(Arrays.asList(42, 43, 44, 45));

    public static boolean blueAlliance;
    public static boolean audienceSideCell = true;

    public static double startInsetFromAllianceWallInches = 12.0;
    public static double startDistanceFromAudienceWallInches = 12.0;
    public static double startHeadingDegrees = 90.0;

    public static double hiveCenterXInches = 72.0;
    public static double hiveCenterYInches = 72.0;
    public static double hiveAllianceOffsetXInches = 12.0;
    public static double hiveCellSeparationInches = 18.8;
    public static double shootingDistanceInches = 36.0;

    public static double shooterVelocityTicksPerSecond = 1600.0;
    public static double hoodPosition = 0.25;
    public static double shooterSpinupTimeoutSeconds = 2.0;
    public static double feedDurationSeconds = 2.5;
    public static double minimumLimelightRangeInches = 20.0;
    public static double maximumLimelightRangeInches = 50.0;
    public static double tagAcquireTimeoutSeconds = 2.0;

    private DcMotorEx shooter;
    private Servo hood;
    private Limelight3A limelight;

    @Override
    public void runOpMode() {
        String configurationError = getConfigurationError();
        if (configurationError != null) {
            telemetry.addData("Configuration error", configurationError);
            telemetry.update();
            return;
        }

        double startX = blueAlliance
                ? FieldConstants.FIELD_SIZE_INCHES - startInsetFromAllianceWallInches
                : startInsetFromAllianceWallInches;
        double startY = startDistanceFromAudienceWallInches;
        double targetX = blueAlliance
                ? hiveCenterXInches + hiveAllianceOffsetXInches
                : hiveCenterXInches - hiveAllianceOffsetXInches;
        double targetY = hiveCenterYInches
                + (audienceSideCell ? -0.5 : 0.5) * hiveCellSeparationInches;
        double shotX = targetX + (blueAlliance ? shootingDistanceInches : -shootingDistanceInches);
        double shotY = targetY;
        double shotHeading = Math.atan2(targetY - shotY, targetX - shotX);

        if (!FieldConstants.isInsideFieldForStartingRobot(startX, startY)
                || !FieldConstants.isInsideFieldForStartingRobot(shotX, shotY)
                || (blueAlliance
                ? startX <= FieldConstants.ALLIANCE_DIVIDER_X_INCHES
                || shotX <= FieldConstants.ALLIANCE_DIVIDER_X_INCHES
                : startX >= FieldConstants.ALLIANCE_DIVIDER_X_INCHES
                || shotX >= FieldConstants.ALLIANCE_DIVIDER_X_INCHES)) {
            telemetry.addLine("Start or shot pose is outside the selected alliance's safe field area.");
            telemetry.addData("Start", "(%.1f, %.1f)", startX, startY);
            telemetry.addData("Shot", "(%.1f, %.1f)", shotX, shotY);
            telemetry.update();
            return;
        }

        Pose startPose = FieldConstants.pose(startX, startY, startHeadingDegrees);
        Pose shotPose = new Pose(shotX, shotY, shotHeading);
        Pose targetPose = new Pose(targetX, targetY, shotHeading);

        initBot(startPose);
        if (bot.follower == null) {
            telemetry.addLine("Cannot run: Pedro follower failed to initialize.");
            telemetry.update();
            return;
        }

        shooter = hardwareMap.get(
                DcMotorEx.class, TurretConstants.ShooterConstants.SHOOTER_MOTOR_NAME);
        shooter.setDirection(TurretConstants.ShooterConstants.shooterMotorDirection);
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hood = hardwareMap.get(
                Servo.class, TurretConstants.ShooterConstants.HOOD_SERVO_NAME);
        hood.setPosition(hoodPosition);
        limelight = hardwareMap.get(
                Limelight3A.class, TurretConstants.LimelightConstants.limelightName);
        limelight.pipelineSwitch(TurretConstants.LimelightConstants.LIMELIGHT_PIPELINE);
        limelight.start();

        while (!isStarted() && !isStopRequested()) {
            bot.update();
            reportSetup(startPose, shotPose, targetPose);
            telemetry.addData("Robot pose", bot.getPoseString());
            telemetry.update();
            idle();
        }
        if (isStopRequested()) {
            stopShooterAndFeeder();
            return;
        }

        Path approachPath = new Path(new BezierLine(startPose, shotPose));
        approachPath.setLinearHeadingInterpolation(
                Math.toRadians(startHeadingDegrees), shotHeading);
        ElapsedTime autonomousTimer = new ElapsedTime();
        bot.followPath(approachPath);

        while (opModeIsActive() && bot.isBusy() && autonomousTimer.seconds() < 25.0) {
            bot.update();
            telemetry.addData("Auto state", "Driving to shooting point");
            telemetry.addData("Robot pose", bot.getPoseString());
            reportSetup(startPose, shotPose, targetPose);
            telemetry.update();
            idle();
        }

        if (!opModeIsActive() || bot.isBusy()) {
            telemetry.addLine("Shot cancelled: robot did not reach the shooting point in time.");
            telemetry.update();
            stopShooterAndFeeder();
            return;
        }

        LLResultTypes.FiducialResult targetTag = null;
        Double limelightToTagDistanceInches = null;
        ElapsedTime tagAcquireTimer = new ElapsedTime();
        while (opModeIsActive()
                && targetTag == null
                && tagAcquireTimer.seconds() < tagAcquireTimeoutSeconds) {
            targetTag = findSelectedHiveTag(limelight.getLatestResult());
            if (targetTag != null) {
                limelightToTagDistanceInches =
                        LimelightUtils.getLimelightToTagDistanceInches(targetTag);
            }
            bot.update();
            telemetry.addData("Auto state", "Acquiring selected Hive AprilTag");
            telemetry.addData("Target tag",
                    targetTag == null ? "Not visible" : targetTag.getFiducialId());
            telemetry.addData("Limelight-to-tag distance",
                    formatDistance(limelightToTagDistanceInches));
            telemetry.update();
            idle();
        }

        if (!opModeIsActive() || targetTag == null
                || limelightToTagDistanceInches == null) {
            telemetry.addLine("Shot cancelled: selected Hive tag or its range is unavailable.");
            telemetry.update();
            stopShooterAndFeeder();
            return;
        }
        if (limelightToTagDistanceInches < minimumLimelightRangeInches
                || limelightToTagDistanceInches > maximumLimelightRangeInches) {
            telemetry.addLine("Shot cancelled: Limelight tag range is outside the allowed shot range.");
            telemetry.addData("Measured Limelight-to-tag range", "%.1f in",
                    limelightToTagDistanceInches);
            telemetry.addData("Allowed range", "%.1f-%.1f in",
                    minimumLimelightRangeInches, maximumLimelightRangeInches);
            telemetry.update();
            stopShooterAndFeeder();
            return;
        }

        bot.holdPose(shotPose);
        hood.setPosition(hoodPosition);
        shooter.setVelocity(shooterVelocityTicksPerSecond);
        ElapsedTime spinupTimer = new ElapsedTime();
        while (opModeIsActive()
                && shooter.getVelocity() < shooterVelocityTicksPerSecond * 0.95
                && spinupTimer.seconds() < shooterSpinupTimeoutSeconds) {
            bot.update();
            telemetry.addData("Auto state", "Spinning up shooter");
            telemetry.addData("Shooter target", "%.0f ticks/s",
                    shooterVelocityTicksPerSecond);
            telemetry.addData("Shooter actual", "%.0f ticks/s", shooter.getVelocity());
            telemetry.addData("Limelight-to-tag distance", "%.1f in",
                    limelightToTagDistanceInches);
            telemetry.update();
            idle();
        }

        if (!opModeIsActive()
                || shooter.getVelocity() < shooterVelocityTicksPerSecond * 0.95) {
            telemetry.addLine("Shot cancelled: shooter did not reach target speed.");
            telemetry.addData("Shooter actual", "%.0f ticks/s", shooter.getVelocity());
            telemetry.update();
            stopShooterAndFeeder();
            return;
        }

        ElapsedTime feedTimer = new ElapsedTime();
        while (opModeIsActive() && feedTimer.seconds() < feedDurationSeconds) {
            bot.forward();
            bot.update();
            telemetry.addData("Auto state", "Firing pollen");
            telemetry.addData("Feed time remaining", "%.1f s",
                    Math.max(0.0, feedDurationSeconds - feedTimer.seconds()));
            telemetry.addData("Limelight-to-tag distance", "%.1f in",
                    limelightToTagDistanceInches);
            telemetry.addData("Robot pose", bot.getPoseString());
            telemetry.update();
            idle();
        }

        stopShooterAndFeeder();
        telemetry.addLine("Shooter auto complete.");
        telemetry.addData("Final robot pose", bot.getPoseString());
        telemetry.update();
        while (opModeIsActive()) {
            bot.update();
            idle();
        }
    }

    @Override
    public void stop() {
        stopShooterAndFeeder();
        if (limelight != null) {
            limelight.stop();
        }
        if (bot != null) {
            bot.stopDrive();
            bot.stop();
        }
    }

    private void reportSetup(Pose startPose, Pose shotPose, Pose targetPose) {
        telemetry.addData("Alliance / cell",
                "%s / %s", blueAlliance ? "Blue" : "Red",
                audienceSideCell ? "Audience side" : "Far side");
        telemetry.addData("Start pose", startPose);
        telemetry.addData("Candidate shot pose", shotPose);
        telemetry.addData("Hive cell target estimate", targetPose);
        telemetry.addData("Target distance", "%.1f in", shootingDistanceInches);
        telemetry.addData("Shooter / hood", "%.0f ticks/s / %.2f",
                shooterVelocityTicksPerSecond, hoodPosition);
    }

    private LLResultTypes.FiducialResult findSelectedHiveTag(LLResult result) {
        if (result == null || !result.isValid()) {
            return null;
        }

        Set<Integer> selectedTagIds;
        if (blueAlliance) {
            selectedTagIds = audienceSideCell ? BLUE_AUDIENCE_TAGS : BLUE_FAR_TAGS;
        } else {
            selectedTagIds = audienceSideCell ? RED_AUDIENCE_TAGS : RED_FAR_TAGS;
        }

        LLResultTypes.FiducialResult closestTag = null;
        for (LLResultTypes.FiducialResult candidate : result.getFiducialResults()) {
            if (selectedTagIds.contains(candidate.getFiducialId())
                    && (closestTag == null
                    || Math.abs(candidate.getTargetXDegrees())
                    < Math.abs(closestTag.getTargetXDegrees()))) {
                closestTag = candidate;
            }
        }
        return closestTag;
    }

    private String formatDistance(Double distanceInches) {
        return distanceInches == null
                ? "Unavailable"
                : String.format("%.1f in", distanceInches);
    }

    private String getConfigurationError() {
        if (!Double.isFinite(startInsetFromAllianceWallInches)
                || !Double.isFinite(startDistanceFromAudienceWallInches)
                || !Double.isFinite(startHeadingDegrees)
                || !Double.isFinite(hiveCenterXInches)
                || !Double.isFinite(hiveCenterYInches)
                || !Double.isFinite(hiveAllianceOffsetXInches)
                || !Double.isFinite(hiveCellSeparationInches)
                || !Double.isFinite(shootingDistanceInches)) {
            return "Field position settings must be finite.";
        }
        if (!Double.isFinite(shooterVelocityTicksPerSecond)
                || shooterVelocityTicksPerSecond <= 0
                || !Double.isFinite(hoodPosition)
                || hoodPosition < 0 || hoodPosition > 1) {
            return "Shooter velocity must be positive and hood position must be 0-1.";
        }
        if (!Double.isFinite(shooterSpinupTimeoutSeconds)
                || shooterSpinupTimeoutSeconds <= 0
                || !Double.isFinite(feedDurationSeconds)
                || feedDurationSeconds <= 0) {
            return "Spin-up timeout and feed duration must be positive.";
        }
        if (!Double.isFinite(minimumLimelightRangeInches)
                || !Double.isFinite(maximumLimelightRangeInches)
                || minimumLimelightRangeInches <= 0
                || maximumLimelightRangeInches < minimumLimelightRangeInches
                || !Double.isFinite(tagAcquireTimeoutSeconds)
                || tagAcquireTimeoutSeconds <= 0) {
            return "Limelight range limits and tag acquisition timeout are invalid.";
        }
        if (startInsetFromAllianceWallInches < FieldConstants.ROBOT_HALF_SIZE_INCHES
                || startDistanceFromAudienceWallInches < FieldConstants.ROBOT_HALF_SIZE_INCHES
                || hiveAllianceOffsetXInches <= 0
                || hiveCellSeparationInches <= 0
                || shootingDistanceInches <= 0) {
            return "Distances must leave the robot on the field and be greater than zero.";
        }
        return null;
    }

    private void stopShooterAndFeeder() {
        if (shooter != null) {
            shooter.setVelocity(0);
        }
        if (limelight != null) {
            limelight.stop();
            limelight = null;
        }
        if (bot != null) {
            bot.stop();
        }
    }
}
