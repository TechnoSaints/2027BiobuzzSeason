package org.firstinspires.ftc.teamcode.common.components;

import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

import org.firstinspires.ftc.teamcode.common.constants.RobotConstants;

public class NectarTurret {

    private DcMotorEx nectarTurretMotor;
    private CRServo nectarRotationServo;
    private Servo nectarAngleServo;
    private PIDFCoefficients nectarTurretPIDFCoefficients = new PIDFCoefficients(
            RobotConstants.NectarTurretConstants.nectarTurretFlywheelP,
            RobotConstants.NectarTurretConstants.nectarTurretFlywheelI,
            RobotConstants.NectarTurretConstants.nectarTurretFlywheelD,
            RobotConstants.NectarTurretConstants.nectarTurretFlywheelF);


    public NectarTurret(HardwareMap hardwareMap, Telemetry telemetry) {
        try {
            // This name has to exactly match what you typed into the Driver Station's Hardware Config.
            nectarTurretMotor = hardwareMap.get(DcMotorEx.class, RobotConstants.NectarTurretConstants.NECTAR_TURRET_NAME);

            nectarTurretMotor.setDirection(RobotConstants.NectarTurretConstants.nectarTurretDirection);

            nectarTurretMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, nectarTurretPIDFCoefficients);

            nectarTurretMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            nectarRotationServo = hardwareMap.get(CRServo.class, RobotConstants.NectarTurretConstants.NECTAR_SERVO_NAME);

            nectarRotationServo.setDirection(RobotConstants.NectarTurretConstants.nectarServoDirection);

            nectarAngleServo = hardwareMap.get(Servo.class, RobotConstants.NectarTurretConstants.NECTAR_ANGLE_SERVO_NAME);
        } catch (Exception e) {
            telemetry.addData(
                    "Warning: Failed to initialize nectar turret",
                    "%s: %s", e.getClass().getSimpleName(), String.valueOf(e.getMessage())
            );
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
}
