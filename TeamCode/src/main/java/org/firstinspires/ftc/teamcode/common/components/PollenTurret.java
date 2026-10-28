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

public class PollenTurret {

    private DcMotorEx pollenTurretMotor;
    private CRServo pollenRotationServo;
    private Servo pollenAngleServo;
    private PIDFCoefficients pollenTurretPIDFCoefficients = new PIDFCoefficients(
            RobotConstants.PollenTurretConstants.pollenTurretFlywheelP,
            RobotConstants.PollenTurretConstants.pollenTurretFlywheelI,
            RobotConstants.PollenTurretConstants.pollenTurretFlywheelD,
            RobotConstants.PollenTurretConstants.pollenTurretFlywheelF);


    public PollenTurret(HardwareMap hardwareMap, Telemetry telemetry) {
        try {
            // This name has to exactly match what you typed into the Driver Station's Hardware Config.
            pollenTurretMotor = hardwareMap.get(DcMotorEx.class, RobotConstants.PollenTurretConstants.POLLEN_TURRET_NAME);

            pollenTurretMotor.setDirection(RobotConstants.PollenTurretConstants.pollenTurretDirection);

            pollenTurretMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pollenTurretPIDFCoefficients);

            pollenTurretMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            pollenRotationServo = hardwareMap.get(CRServo.class, RobotConstants.PollenTurretConstants.POLLEN_SERVO_NAME);

            pollenRotationServo.setDirection(RobotConstants.PollenTurretConstants.pollenServoDirection);

            pollenAngleServo = hardwareMap.get(Servo.class, RobotConstants.PollenTurretConstants.POLLEN_ANGLE_SERVO_NAME);
        } catch (Exception e) {
            telemetry.addData(
                    "Warning: Failed to initialize pollen turret",
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
