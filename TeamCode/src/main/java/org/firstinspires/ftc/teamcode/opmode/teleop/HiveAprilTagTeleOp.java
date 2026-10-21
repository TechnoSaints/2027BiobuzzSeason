package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Limelight AprilTag targeting and shooter control. Drivetrain hardware is not
 * initialized, so this OpMode does not require drive motors in the hardware config.
 *
 * D-pad left selects the blue alliance; D-pad right selects red.
 * Press A to toggle the shooter; D-pad up/down tunes its power by 0.05.
 */
@TeleOp(name = "Hive + AprilTag TeleOp", group = "TeleOp")
public class HiveAprilTagTeleOp extends OpMode {
    private static final String SHOOTER_MOTOR_NAME = "shooterMotor";
    private static final int LIMELIGHT_PIPELINE = 0;
    private static final double SHOOTER_POWER_STEP = 0.02;
    private static final double MIN_SHOOTER_POWER = -1.0;
    private static final double MAX_SHOOTER_POWER = 1.0;

    private static final Set<Integer> RED_CENTRAL_TAGS =
            new HashSet<>(Arrays.asList(31, 32, 35, 36));
    private static final Set<Integer> BLUE_CENTRAL_TAGS =
            new HashSet<>(Arrays.asList(39, 40, 43));

    private enum Alliance {
        BLUE(BLUE_CENTRAL_TAGS),
        RED(RED_CENTRAL_TAGS);

        final Set<Integer> centralTagIds;

        Alliance(Set<Integer> centralTagIds) {
            this.centralTagIds = centralTagIds;
        }
    }

    private Limelight3A limelight;
    private DcMotorEx shooterMotor;
    private double shooterPower = 0.5;
    private boolean shooterEnabled;
    private Alliance alliance = Alliance.BLUE;
    private boolean previousDpadLeft;
    private boolean previousDpadRight;
    private boolean previousDpadUp;
    private boolean previousDpadDown;
    private boolean previousA;

    @Override
    public void init() {
        shooterMotor = hardwareMap.get(DcMotorEx.class, SHOOTER_MOTOR_NAME);
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(LIMELIGHT_PIPELINE);
        limelight.start();

        telemetry.addLine("Ready - Limelight pipeline 0");
        telemetry.addLine("D-pad Left: Blue | D-pad Right: Red");
        telemetry.addLine("Drivetrain disabled; tag angles are telemetry only");
        telemetry.addLine("Press A: shooter on/off | D-pad Up/Down: tune power");
        telemetry.addData("Shooter power", "%.2f (default 0.50)", shooterPower);
        telemetry.update();
    }

    @Override
    public void loop() {
        updateAllianceSelection();
        updateShooterControls();

        LLResult result = limelight.getLatestResult();
        Double hiveAngle = null;
        Integer visibleCentralTag = null;
        Double tagAngle = null;

        if (result != null && result.isValid()) {
            hiveAngle = findHiveAngle(result);
            LLResultTypes.FiducialResult targetTag = findCentralTag(result);
            if (targetTag != null) {
                visibleCentralTag = targetTag.getFiducialId();
                tagAngle = targetTag.getTargetXDegrees();
            }
        }

        shooterMotor.setPower(shooterEnabled ? shooterPower : 0.0);

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
        telemetry.addData("Shooter", shooterEnabled ? "On" : "Off");
        telemetry.addData("Shooter power", "%.2f", shooterPower);
        telemetry.addLine("Press A: toggle | D-pad Up/Down: +/- 0.05");
        telemetry.update();
    }

    @Override
    public void stop() {
        if (shooterMotor != null) {
            shooterMotor.setPower(0.0);
        }
        if (limelight != null) {
            limelight.stop();
        }
    }

    private void updateShooterControls() {
        if (gamepad1.a && !previousA) {
            shooterEnabled = !shooterEnabled;
        }
        previousA = gamepad1.a;

        if (gamepad1.dpad_up && !previousDpadUp) {
            shooterPower = Math.min(MAX_SHOOTER_POWER, shooterPower + SHOOTER_POWER_STEP);
        } else if (gamepad1.dpad_down && !previousDpadDown) {
            shooterPower = Math.max(MIN_SHOOTER_POWER, shooterPower - SHOOTER_POWER_STEP);
        }

        previousDpadUp = gamepad1.dpad_up;
        previousDpadDown = gamepad1.dpad_down;
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

    private String formatAngle(double angleDegrees) {
        String direction = angleDegrees > 0 ? "right" : angleDegrees < 0 ? "left" : "center";
        return String.format("%.1f deg %s", Math.abs(angleDegrees), direction);
    }
}
