package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.firstinspires.ftc.teamcode.common.LimelightUtils;
import org.firstinspires.ftc.teamcode.common.TeleopBot;
import org.firstinspires.ftc.teamcode.common.TurretConstants;

/**
 * Standalone driver control with Limelight hive detection and AprilTag aim assist.
 *
 * D-pad left selects the blue alliance; D-pad right selects red.
 * Hold the right bumper to turn toward the closest visible central alliance tag.
 */
@TeleOp(name = "Hive + AprilTag TeleOp", group = "TeleOp")
public class HiveAprilTagTeleOp extends OpMode {
    private static final int LIMELIGHT_PIPELINE = TurretConstants.LimelightConstants.LIMELIGHT_PIPELINE;
    private static final double AIM_DEADBAND_DEGREES = TurretConstants.LimelightConstants.aimError;
    private static final double AIM_GAIN = TurretConstants.LimelightConstants.AIM_GAIN;
    private static final double MAX_AIM_TURN = TurretConstants.LimelightConstants.MAX_AIM_TURN;

    private static final Set<Integer> RED_CENTRAL_TAGS =
            new HashSet<>(Arrays.asList(31, 32, 35, 36));
    private static final Set<Integer> BLUE_CENTRAL_TAGS =
            new HashSet<>(Arrays.asList(39, 40, 43, 44));

    private enum Alliance {
        BLUE(BLUE_CENTRAL_TAGS),
        RED(RED_CENTRAL_TAGS);

        final Set<Integer> centralTagIds;

        Alliance(Set<Integer> centralTagIds) {
            this.centralTagIds = centralTagIds;
        }
    }

    private TeleopBot bot;
    private Limelight3A limelight;
    private Alliance alliance = Alliance.BLUE;
    private boolean previousDpadLeft;
    private boolean previousDpadRight;
    private boolean previousStart;

    @Override
    public void init() {
        bot = new TeleopBot(this, telemetry);
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(LIMELIGHT_PIPELINE);
        limelight.start();

        telemetry.addLine("Ready - Limelight pipeline 0");
        telemetry.addLine("D-pad Left: Blue | D-pad Right: Red");
        telemetry.addLine("Hold Right Bumper: aim at central alliance AprilTag");
        telemetry.update();
    }

    @Override
    public void loop() {
        if (bot == null) {
            return;
        }

        updateAllianceSelection();

        if (gamepad1.start && !previousStart) {
            bot.resetHeading();
        }
        previousStart = gamepad1.start;

        LLResult result = limelight.getLatestResult();
        Double hiveAngle = null;
        Integer visibleCentralTag = null;
        Double tagAngle = null;
        Double limelightToTagDistanceInches = null;

        if (result != null && result.isValid()) {
            hiveAngle = findHiveAngle(result);
            LLResultTypes.FiducialResult targetTag = findCentralTag(result);
            if (targetTag != null) {
                visibleCentralTag = targetTag.getFiducialId();
                tagAngle = targetTag.getTargetXDegrees();
                limelightToTagDistanceInches =
                        LimelightUtils.getLimelightToTagDistanceInches(targetTag);
            }
        }

        double forward = -gamepad1.left_stick_y;
        double strafe = -gamepad1.left_stick_x;
        double turn = -gamepad1.right_stick_x;

        if (gamepad1.right_bumper && Math.abs(gamepad1.right_stick_x) < 0.1) {
            turn = tagAngle == null ? 0.0 : getAimTurn(tagAngle);
        }

        double driveScale = gamepad1.left_bumper ? 0.3 : 1.0;
        bot.setDrivePowers(forward, strafe, turn, driveScale);
        bot.update();

        telemetry.addData("Status", "Running (TeleOp)");
        telemetry.addData("Alliance / hive pipeline",
                "%s / %d", alliance, LIMELIGHT_PIPELINE);
        telemetry.addData("Hive detected",
                hiveAngle == null
                        ? String.format("No %s hive target", alliance)
                        : String.format("%s hive", alliance));
        telemetry.addData("Hive angle from center",
                hiveAngle == null ? "No hive target" : formatAngle(hiveAngle));
        telemetry.addData("Central AprilTag",
                visibleCentralTag == null ? "Not visible" : visibleCentralTag);
        telemetry.addData("AprilTag angle from center",
                tagAngle == null ? "No central tag" : formatAngle(tagAngle));
        telemetry.addData("Limelight-to-AprilTag distance",
                limelightToTagDistanceInches == null
                        ? (visibleCentralTag == null ? "No central tag" : "Unavailable")
                        : String.format("%.1f in", limelightToTagDistanceInches));
        telemetry.addData("Aim assist",
                gamepad1.right_bumper
                        ? (tagAngle == null ? "On - waiting for tag" : "On")
                        : "Off");
        telemetry.addData("Slow mode", gamepad1.left_bumper ? "On" : "Off");
        if (bot.follower != null) {
            telemetry.addData("Pose", bot.follower.getPose().toString());
        }
        telemetry.update();
    }

    @Override
    public void stop() {
        if (bot != null) {
            bot.stopDrive();
            bot.stop();
        }
        if (limelight != null) {
            limelight.stop();
        }
    }

    private void updateAllianceSelection() {
        if (gamepad1.dpad_left && !previousDpadLeft) {
            setAlliance(Alliance.BLUE);
        } else if (gamepad1.dpad_right && !previousDpadRight) {
            setAlliance(Alliance.RED);
        }

        previousDpadLeft = gamepad1.dpad_left;
        previousDpadRight = gamepad1.dpad_right;
    }

    private void setAlliance(Alliance selectedAlliance) {
        alliance = selectedAlliance;
    }

    private Double findHiveAngle(LLResult result) {
        LLResultTypes.DetectorResult largestHive = null;
        for (LLResultTypes.DetectorResult detection : result.getDetectorResults()) {
            if (largestHive == null
                    || detection.getTargetArea() > largestHive.getTargetArea()) {
                largestHive = detection;
            }
        }

        return largestHive == null ? null : largestHive.getTargetXDegrees();
    }

    private LLResultTypes.FiducialResult findCentralTag(LLResult result) {
        LLResultTypes.FiducialResult closestToCenter = null;
        for (LLResultTypes.FiducialResult detection : result.getFiducialResults()) {
            if (!alliance.centralTagIds.contains(detection.getFiducialId())) {
                continue;
            }

            if (closestToCenter == null
                    || Math.abs(detection.getTargetXDegrees())
                    < Math.abs(closestToCenter.getTargetXDegrees())) {
                closestToCenter = detection;
            }
        }

        return closestToCenter;
    }

    private double getAimTurn(double angleDegrees) {
        if (Math.abs(angleDegrees) <= AIM_DEADBAND_DEGREES) {
            return 0.0;
        }

        double turn = -angleDegrees * AIM_GAIN;
        return Math.max(-MAX_AIM_TURN, Math.min(MAX_AIM_TURN, turn));
    }

    private String formatAngle(double angleDegrees) {
        String direction = angleDegrees > 0 ? "right" : angleDegrees < 0 ? "left" : "center";
        return String.format("%.1f deg %s", Math.abs(angleDegrees), direction);
    }
}
