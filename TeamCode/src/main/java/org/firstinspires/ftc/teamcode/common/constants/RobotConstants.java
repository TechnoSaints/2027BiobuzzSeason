package org.firstinspires.ftc.teamcode.common.constants;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * RobotConstants holds all the hardware configuration and tuning values.
 * Centralizing these values makes it easy to update names and constants in one place.
 */
public class RobotConstants {

    // Drivetrain-specific constants
    public static class Drivetrain {
        // Motor names must match your Hardware Config on the Driver Station
        public static final String LEFT_FRONT_NAME = "leftFrontDrive";
        public static final String LEFT_REAR_NAME = "leftRearDrive";
        public static final String RIGHT_FRONT_NAME = "rightFrontDrive";
        public static final String RIGHT_REAR_NAME = "rightRearDrive";

        // Measurement constants for distance calculations
        public static double wheelDiameterMM = 110.0;
        public static double wheelDiameterInches = wheelDiameterMM / 25.4;
        public static double wheelCircumferenceInches = Math.PI * wheelDiameterInches;

        // Motor directions: If the robot moves backward, flip these!
        public static DcMotorSimple.Direction leftFrontMotorDirection = DcMotorSimple.Direction.REVERSE;
        public static DcMotorSimple.Direction leftRearMotorDirection = DcMotorSimple.Direction.REVERSE;
        public static DcMotorSimple.Direction rightFrontMotorDirection = DcMotorSimple.Direction.FORWARD;
        public static DcMotorSimple.Direction rightRearMotorDirection = DcMotorSimple.Direction.FORWARD;

        public static double maxAutoPower = 1.0;
        /** Maximum change in normalized drivetrain command per second. */
        public static double maxDriveAcceleration = 2.0;
    }

    // Intake-specific constants
    public static class Intake {
        public static final String INTAKE_MOTOR_NAME = "intake";
        public static final String Flower_Servo_Name = "flowerServo";
        public static DcMotorSimple.Direction intakeMotorDirection = DcMotorSimple.Direction.REVERSE;
        
        // Power levels for different intake actions
        public static double maxMovePower = 1.0;
        public static double stopPower = 0.0;
        public static double lockPower = 0.98;
        public static final double FLOWER_STORE_POSITION = 1.0;
        public static final double FLOWER_INTAKE_POSITION = 0.32;
    }



    public static class LimelightConstants {
        public static final String limelightName = "limelight";
        public static final int LIMELIGHT_PIPELINE = 0;
        public static final double aimError = 1.0; //Degrees
        public static final double AIM_GAIN = 0.025;
        public static final double MAX_AIM_TURN = 0.35;
    }

    public static class PollenTurretConstants {
        public static final String POLLEN_TURRET_NAME = "pollenTurretFlywheel";
        public static final String POLLEN_SERVO_NAME = "pollenTurretRotate";
        public static final String POLLEN_ANGLE_SERVO_NAME = "pollenTurretHood";
        public static final DcMotorSimple.Direction pollenTurretDirection = DcMotorSimple.Direction.FORWARD;
        public static final CRServo.Direction pollenServoDirection = CRServo.Direction.FORWARD;
        /**
        * PIDF coefficients used to adjust flywheel behavior.
        * P and F should be tuned; I and D should be left as default unless problematic.
        */
        public static final double pollenTurretFlywheelP = 56.4;
        public static final double pollenTurretFlywheelI = 3.0;
        public static final double pollenTurretFlywheelD = 0.0;
        public static final double pollenTurretFlywheelF = 13.47;
        public static final double startingHoodPosition = 0.5;
        public static final double pollenRotateMaxSpeed = 0.9;
        public static double pollenkP = 0.015;
        public static double pollenkD = 1;
        public static final double angleToleranceDegrees = 1.0;
        public static final double goalOffset = 0;
    }

    public static class NectarTurretConstants {
        public static final String NECTAR_TURRET_NAME = "nectarTurretFlywheel";
        public static final String NECTAR_HOOD_SERVO_NAME = "nectarTurretHood";
        public static final String NECTAR_ROTATE_SERVO_NAME = "nectarTurretRotate";
        public static final DcMotorSimple.Direction nectarTurretDirection = DcMotorSimple.Direction.REVERSE;
        public static final CRServo.Direction nectarServoDirection = CRServo.Direction.FORWARD;
        /**
         * PIDF coefficients used to adjust flywheel behavior.
         * P and F should be tuned; I and D should be left as default unless problematic.
         */
        public static final double nectarTurretFlywheelP = 56.4;
        public static final double nectarTurretFlywheelI = 3.0;
        public static final double nectarTurretFlywheelD = 0.0;
        public static final double nectarTurretFlywheelF = 13.47;
        public static final double nectarTurretMaxSpeed = 0.5;
        public static final double startingHoodPosition = 0.5;

        public static double kP = 0.02;
        public static double kD = 0.0001;
        public static final double angleToleranceDegrees = 0.2;
        public static final double goalOffset = 0;

    }

}
