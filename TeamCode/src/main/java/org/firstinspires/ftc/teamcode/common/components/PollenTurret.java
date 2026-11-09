package org.firstinspires.ftc.teamcode.common.components;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

import org.firstinspires.ftc.teamcode.common.constants.RobotConstants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

public class PollenTurret {
    private static final Set<Integer> RED_CENTRAL_TAGS =
            new HashSet<>(Arrays.asList(31, 32, 35, 36));
    private static final Set<Integer> BLUE_CENTRAL_TAGS =
            new HashSet<>(Arrays.asList(39, 40, 43, 44));

    public void raiseHood() {
        pollenAngleServo.setPosition(RobotConstants.PollenTurretConstants.raiseHoodPosition);
    }

    public enum Alliance {
        BLUE,
        RED
    }

    private Integer lockedTargetTagId;

    private DcMotorEx pollenTurretMotor;
    private CRServo pollenRotationServo;
    private static Servo pollenAngleServo;
    private Alliance alliance;
    private PIDFCoefficients pollenTurretPIDFCoefficients = new PIDFCoefficients(
            RobotConstants.PollenTurretConstants.pollenTurretFlywheelP,
            RobotConstants.PollenTurretConstants.pollenTurretFlywheelI,
            RobotConstants.PollenTurretConstants.pollenTurretFlywheelD,
            RobotConstants.PollenTurretConstants.pollenTurretFlywheelF);
    private ElapsedTime timer, movementTimer;
    public static double kP = RobotConstants.PollenTurretConstants.pollenkP;
    public static double kD = RobotConstants.PollenTurretConstants.pollenkD;


    private double lastError = 0;
    private double power = 0.5;
    private Limelight3A limelight;

    public static double currentPosition;

    Telemetry telemetry;

    // Example for FIRST Tech Challenge (FTC) / REV Hubs
    private AnalogInput encoder;


    public PollenTurret(HardwareMap hardwareMap, Telemetry telemetry, Alliance alliance) {
        if (alliance == null) {
            throw new IllegalArgumentException("Alliance must be specified");
        }
        this.alliance = alliance;
        this.telemetry = telemetry;

        try {
            // This name has to exactly match what you typed into the Driver Station's Hardware Config.
            pollenTurretMotor = hardwareMap.get(DcMotorEx.class, RobotConstants.PollenTurretConstants.POLLEN_TURRET_NAME);

            pollenTurretMotor.setDirection(RobotConstants.PollenTurretConstants.pollenTurretDirection);

            pollenTurretMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pollenTurretPIDFCoefficients);

            pollenTurretMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            encoder = hardwareMap.get(AnalogInput.class, RobotConstants.PollenTurretConstants.Pollen_Angle_Encoder);

            pollenRotationServo = hardwareMap.get(CRServo.class, RobotConstants.PollenTurretConstants.POLLEN_SERVO_NAME);

            pollenRotationServo.setDirection(RobotConstants.PollenTurretConstants.pollenServoDirection);

            pollenAngleServo = hardwareMap.get(Servo.class, RobotConstants.PollenTurretConstants.POLLEN_ANGLE_SERVO_NAME);

            limelight = hardwareMap.get(
                    Limelight3A.class, RobotConstants.LimelightConstants.limelightName);
            limelight.pipelineSwitch(RobotConstants.LimelightConstants.LIMELIGHT_PIPELINE);

            timer = new ElapsedTime();
            telemetry.addLine("Status: Initialized");
            telemetry.update();
        } catch (Exception e) {
            telemetry.addData(
                    "Warning: Failed to initialize pollen turret",
                    "%s: %s", e.getClass().getSimpleName(), String.valueOf(e.getMessage())
            );
            telemetry.update();
        }
    }



    public static Double getLimelightToTagDistanceInches(LLResultTypes.FiducialResult targetTag) {
        if (targetTag == null) {
            return null;
        }

        Pose3D targetPose = targetTag.getTargetPoseCameraSpace();
        if (targetPose == null || targetPose.getPosition() == null) {
            return null;
        }

        Position targetPosition = targetPose.getPosition().toUnit(DistanceUnit.INCH);
        double distance = Math.sqrt(
                targetPosition.x * targetPosition.x
                        + targetPosition.y * targetPosition.y
                        + targetPosition.z * targetPosition.z
        );
        return Double.isFinite(distance) && distance > 0 ? distance : null;
    }

    @Nullable
    public LLResultTypes.FiducialResult getClosestAllianceTag(@Nullable LLResult result) {
        if (result == null || !result.isValid()) {
            return null;
        }

        Set<Integer> centralTagIds =
                alliance == Alliance.RED ? RED_CENTRAL_TAGS : BLUE_CENTRAL_TAGS;

        if (lockedTargetTagId != null) {
            for (LLResultTypes.FiducialResult detection : result.getFiducialResults()) {
                if (detection != null && detection.getFiducialId() == lockedTargetTagId
                        && centralTagIds.contains(detection.getFiducialId())) {
                    return detection;
                }
            }
            lockedTargetTagId = null;
        }

        LLResultTypes.FiducialResult closestToCenter = null;
        for (LLResultTypes.FiducialResult detection : result.getFiducialResults()) {
            if (detection == null || !centralTagIds.contains(detection.getFiducialId())) {
                continue;
            }

            if (closestToCenter == null
                    || Math.abs(detection.getTargetXDegrees())
                    < Math.abs(closestToCenter.getTargetXDegrees())) {
                closestToCenter = detection;
            }
        }

        if (closestToCenter != null) {
            lockedTargetTagId = closestToCenter.getFiducialId();
        }

        return closestToCenter;
    }

    public Double findPollenSpeed() {
        return RobotConstants.PollenTurretConstants.shooterConstant * getLimelightToTagDistanceInches(getClosestAllianceTag(getLatestLimelightResult()));
    }

    @Nullable
    public Double getAprilTagAngleOffset(@Nullable LLResult result) {
        LLResultTypes.FiducialResult targetTag = getClosestAllianceTag(result);
        return targetTag == null ? null : targetTag.getTargetXDegrees();
    }

    public void setAlliance(Alliance alliance) {
        if (alliance == null) {
            throw new IllegalArgumentException("Alliance must be specified");
        }
        this.alliance = alliance;
        this.lockedTargetTagId = null;
    }

    public static void setPollenLaunchAngle(double targetLaunchAngle){
        pollenAngleServo.setPosition(targetLaunchAngle);
    }

    public void raiseHood() {
        pollenAngleServo.setPosition(RobotConstants.PollenTurretConstants.raiseHoodPosition);
    }

    public void getCurrentPosition() {
        currentPosition = (encoder.getVoltage() / 3.2) * 360;
    }

    public void findOffset (double nectarTurretOffset, double currentPosition) {

        //This is checking to make sure that a turret offset Exists and the Servos are running.
        if (!Double.isFinite(nectarTurretOffset) || !Double.isFinite(currentPosition)) {
            power = 0;
            pollenRotationServo.setPower(power);
            return;
        }

        LLResultTypes.FiducialResult targetTag =
                getClosestAllianceTag(getLatestLimelightResult());
        Double distance = getLimelightToTagDistanceInches(targetTag);
        if (distance == null) {
            power = 0;
            pollenRotationServo.setPower(power);
            return;
        }

        double pollenTargetPosition = normalizeDegrees(nectarTurretOffset + 180.0);
        double angleError = normalizeSignedDegrees(pollenTargetPosition - currentPosition);
        double distanceSlope = Math.max(
                0, RobotConstants.PollenTurretConstants.pollenOffsetDistanceSlope);
        double distanceScale = Range.clip(
                1.0 - distance * distanceSlope,
                RobotConstants.PollenTurretConstants.pollenOffsetMinimumScale,
                1.0);

        power = Range.clip(
                angleError * distanceScale * kP,
                -RobotConstants.PollenTurretConstants.pollenRotateMaxSpeed,
                RobotConstants.PollenTurretConstants.pollenRotateMaxSpeed);
        pollenRotationServo.setPower(power);
    }

    private static double normalizeDegrees(double angle) {
        double normalized = angle % 360.0;
        return normalized < 0 ? normalized + 360.0 : normalized;
    }

    private static double normalizeSignedDegrees(double angle) {
        return normalizeDegrees(angle + 180.0) - 180.0;
    }

    public double getkP(){
        return kP;
    }

    public void setkP(double newKp){
        kP = newKp;
    }

    public double getkD(){
        return kD;
    }

    public void setkD(double newKd){
        kD = newKd;
    }

    public void resetAimingTimer(){
        timer.reset();
    }

    public void startLimelight() {
        limelight.start();
        lockedTargetTagId = null;
        resetAimingTimer();
    }

    @Nullable
    public LLResult getLatestLimelightResult() {
        return limelight.getLatestResult();
    }

    public void stop() {
        power = 0;
        pollenRotationServo.setPower(power);
        lastError = 0;
        lockedTargetTagId = null;
        limelight.stop();
    }

    public void update() {
        update(getLatestLimelightResult());
    }

    public void update(@Nullable LLResult result) {
        double deltaTime = timer.seconds();
        timer.reset();

        Double angleOffset = getAprilTagAngleOffset(result);
        if (angleOffset == null) {
            power = 0;
            pollenRotationServo.setPower(power);
            lastError = 0;
            return;
        }

        double error = RobotConstants.PollenTurretConstants.goalOffset - angleOffset;
        double pTerm = error * kP;

        double dTerm = 0;
        if (deltaTime > 0) {
            dTerm = ((error - lastError) / deltaTime) * kD;
        }

        if (Math.abs(error) < RobotConstants.PollenTurretConstants.angleToleranceDegrees) {
            power = 0;
        } else {
            power = Range.clip(pTerm + dTerm, -RobotConstants.PollenTurretConstants.pollenRotateMaxSpeed, RobotConstants.PollenTurretConstants.pollenRotateMaxSpeed);
        }
        // TODO safety check

        pollenRotationServo.setPower(power);
        lastError = error;
    }

    public void setVelocity(double targetVelocity){
        pollenTurretMotor.setVelocity(targetVelocity);
    }

    public void setMotorPIDFCoefficients(PIDFCoefficients updated){
        pollenTurretMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, updated);
    }

    public double getVelocity(){
        return pollenTurretMotor.getVelocity();
    }

    public double getHoodPosition(){
        return pollenAngleServo.getPosition();
    }

    public void log(){
        telemetry.addData("Pollen Turret Velocity", getVelocity());
        telemetry.addData("Pollen Turret Hood Position", getHoodPosition());
        telemetry.addData("Pollen Turret Rotation Power", power);
    }
}
