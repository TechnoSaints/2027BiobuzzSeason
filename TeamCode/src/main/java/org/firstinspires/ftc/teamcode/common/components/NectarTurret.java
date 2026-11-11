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

public class NectarTurret {
    private static final Set<Integer> RED_CENTRAL_TAGS =
            new HashSet<>(Arrays.asList(31, 32, 35, 36));
    private static final Set<Integer> BLUE_CENTRAL_TAGS =
            new HashSet<>(Arrays.asList(39, 40, 43, 44));

    public enum Alliance {
        BLUE,
        RED
    }

    private Integer lockedTargetTagId;
    private DcMotorEx nectarTurretMotor;
    private CRServo nectarRotationServo;
    private AnalogInput nectarRotationEncoder;
    private static Servo nectarAngleServo;
    private Alliance alliance;
    private PIDFCoefficients nectarTurretPIDFCoefficients = new PIDFCoefficients(
            RobotConstants.NectarTurretConstants.nectarTurretFlywheelP,
            RobotConstants.NectarTurretConstants.nectarTurretFlywheelI,
            RobotConstants.NectarTurretConstants.nectarTurretFlywheelD,
            RobotConstants.NectarTurretConstants.nectarTurretFlywheelF);
    private ElapsedTime timer;
    public static double kP = RobotConstants.NectarTurretConstants.kP;
    public static double kD = RobotConstants.NectarTurretConstants.kD;
    private double lastError = 0;
    private double power = 0.5;
    private Limelight3A limelight;

    Telemetry telemetry;


    public NectarTurret(HardwareMap hardwareMap, Telemetry telemetry, Alliance alliance) {
        if (alliance == null) {
            throw new IllegalArgumentException("Alliance must be specified");
        }
        this.alliance = alliance;
        this.telemetry = telemetry;

        try {
            // This name has to exactly match what you typed into the Driver Station's Hardware Config.
            nectarTurretMotor = hardwareMap.get(DcMotorEx.class, RobotConstants.NectarTurretConstants.NECTAR_TURRET_NAME);

            nectarTurretMotor.setDirection(RobotConstants.NectarTurretConstants.nectarTurretDirection);

            nectarTurretMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, nectarTurretPIDFCoefficients);

            nectarTurretMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            nectarRotationEncoder = hardwareMap.get(
                    AnalogInput.class, RobotConstants.NectarTurretConstants.NECTAR_ANGLE_ENCODER);

            nectarRotationServo = hardwareMap.get(CRServo.class, RobotConstants.NectarTurretConstants.NECTAR_ROTATE_SERVO_NAME);

            nectarRotationServo.setDirection(RobotConstants.NectarTurretConstants.nectarServoDirection);

            nectarAngleServo = hardwareMap.get(Servo.class, RobotConstants.NectarTurretConstants.NECTAR_HOOD_SERVO_NAME);

            limelight = hardwareMap.get(
                    Limelight3A.class, RobotConstants.LimelightConstants.limelightName);
            limelight.pipelineSwitch(RobotConstants.LimelightConstants.LIMELIGHT_PIPELINE);

            timer = new ElapsedTime();
        } catch (Exception e) {
            telemetry.addData(
                    "Warning: Failed to initialize nectar turret",
                    "%s: %s", e.getClass().getSimpleName(), String.valueOf(e.getMessage())
            );
        }
    }



    public static Double getLimelightToTagDistanceInches(LLResultTypes.FiducialResult targetTag) {
        if (targetTag == null) {
            return 0.0;
        }

        Pose3D targetPose = targetTag.getTargetPoseCameraSpace();
        if (targetPose == null || targetPose.getPosition() == null) {
            return 0.0;
        }

        Position targetPosition = targetPose.getPosition().toUnit(DistanceUnit.INCH);
        double distance = Math.sqrt(
                targetPosition.x * targetPosition.x
                        + targetPosition.y * targetPosition.y
                        + targetPosition.z * targetPosition.z
        );
        return Double.isFinite(distance) && distance > 0 ? distance : 0.0;
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

    @Nullable
    public Double getAprilTagAngleOffset(@Nullable LLResult result) {
        LLResultTypes.FiducialResult targetTag = getClosestAllianceTag(result);
        return targetTag == null ? 0 : targetTag.getTargetXDegrees();
    }

    public Double findNectarSpeed() {
        return RobotConstants.NectarTurretConstants.shooterConstant * getLimelightToTagDistanceInches(getClosestAllianceTag(getLatestLimelightResult()));
    }


    public void setAlliance(Alliance alliance) {
        if (alliance == null) {
            throw new IllegalArgumentException("Alliance must be specified");
        }
        this.alliance = alliance;
        this.lockedTargetTagId = null;
    }

    public static void setNectarLaunchAngle(double targetLaunchAngle){
        nectarAngleServo.setPosition(targetLaunchAngle);
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
        nectarRotationServo.setPower(power);
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
            nectarRotationServo.setPower(power);
            lastError = 0;
            return;
        }

        double error = RobotConstants.NectarTurretConstants.goalOffset - angleOffset;
        double pTerm = error * kP;

        double dTerm = 0;
        if (deltaTime > 0) {
            dTerm = ((error - lastError) / deltaTime) * kD;
        }

        if (Math.abs(error) < RobotConstants.NectarTurretConstants.angleToleranceDegrees) {
            power = 0;
        } else {
            power = Range.clip(
                    pTerm + dTerm,
                    -RobotConstants.NectarTurretConstants.nectarTurretMaxSpeed,
                    RobotConstants.NectarTurretConstants.nectarTurretMaxSpeed);
        }
        // TODO safety check

        nectarRotationServo.setPower(power);
        lastError = error;
    }

    public void setVelocity(double targetVelocity){
        nectarTurretMotor.setVelocity(targetVelocity);
    }

    public void setMotorPIDFCoefficients(PIDFCoefficients updated){
        nectarTurretMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, updated);
    }

    public double getVelocity(){
        return nectarTurretMotor.getVelocity();
    }

    public double getHoodPosition(){
        return nectarAngleServo.getPosition();
    }

    public double getEncoderVoltage() {
        return nectarRotationEncoder.getVoltage();
    }

    public double getEncoderAngleDegrees() {
        return getEncoderVoltage() / RobotConstants.NectarTurretConstants.encoderFullScaleVoltage
                * 360.0;
    }

    public void raiseHood() {
        nectarAngleServo.setPosition(RobotConstants.NectarTurretConstants.raiseHoodPosition);
    }

    public void log(){
        telemetry.addData("Nectar Turret Velocity", getVelocity());
        telemetry.addData("Nectar Turret Hood Position", getHoodPosition());
        telemetry.addData("Nectar Turret Rotation Power", power);
        telemetry.addData("Nectar Turret Encoder Voltage (V)", getEncoderVoltage());
        telemetry.addData("Nectar Turret Encoder Angle (deg)", getEncoderAngleDegrees());
    }
}
