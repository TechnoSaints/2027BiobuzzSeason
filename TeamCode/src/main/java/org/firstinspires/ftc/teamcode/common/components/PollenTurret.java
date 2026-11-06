package org.firstinspires.ftc.teamcode.common.components;

import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
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

    public enum Alliance {
        BLUE,
        RED
    }

    private DcMotorEx pollenTurretMotor;
    private CRServo pollenRotationServo;
    private static Servo pollenAngleServo;
    private Alliance alliance;
    private PIDFCoefficients pollenTurretPIDFCoefficients = new PIDFCoefficients(
            RobotConstants.PollenTurretConstants.pollenTurretFlywheelP,
            RobotConstants.PollenTurretConstants.pollenTurretFlywheelI,
            RobotConstants.PollenTurretConstants.pollenTurretFlywheelD,
            RobotConstants.PollenTurretConstants.pollenTurretFlywheelF);
    private ElapsedTime timer;
    private double kP = RobotConstants.PollenTurretConstants.kP;
    private double kD = RobotConstants.PollenTurretConstants.kD;
    private double lastError = 0;
    private double power = 0.5;
    private Limelight3A limelight;

    Telemetry telemetry;


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

            pollenRotationServo = hardwareMap.get(CRServo.class, RobotConstants.PollenTurretConstants.POLLEN_SERVO_NAME);

            pollenRotationServo.setDirection(RobotConstants.PollenTurretConstants.pollenServoDirection);

            pollenAngleServo = hardwareMap.get(Servo.class, RobotConstants.PollenTurretConstants.POLLEN_ANGLE_SERVO_NAME);

            limelight = hardwareMap.get(
                    Limelight3A.class, RobotConstants.LimelightConstants.limelightName);
            limelight.pipelineSwitch(RobotConstants.LimelightConstants.LIMELIGHT_PIPELINE);

            timer = new ElapsedTime();
        } catch (Exception e) {
            //telemetry.addData(
            //        "Warning: Failed to initialize pollen turret",
            //        "%s: %s", e.getClass().getSimpleName(), String.valueOf(e.getMessage())
            //);
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
        LLResultTypes.FiducialResult closestToCenter = null;
        for (LLResultTypes.FiducialResult detection : result.getFiducialResults()) {
            if (!centralTagIds.contains(detection.getFiducialId())) {
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
    }

    public static void setPollenLaunchAngle(double targetLaunchAngle){
        pollenAngleServo.setPosition(targetLaunchAngle);
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
            power = Range.clip(pTerm + dTerm, -RobotConstants.PollenTurretConstants.pollenTurretMaxSpeed, RobotConstants.PollenTurretConstants.pollenTurretMaxSpeed);
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
