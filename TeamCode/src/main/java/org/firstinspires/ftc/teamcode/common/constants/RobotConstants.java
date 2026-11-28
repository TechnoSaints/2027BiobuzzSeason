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

        public static double maxAutoPower = 0.5;
        /** Maximum change in normalized drivetrain command per second. */
        public static double maxDriveAcceleration = 2.0;
        public static double aimP = .023;
        public static double aimI = 0.0;
        public static double aimD = 0.0005;
        public static double aimF = 0.09;
        public static double targetError = 0.5;
    }

    // Intake-specific constants
    public static class Intake {
        public static final String Flower_Servo_Name = "flowerServo";
        
        // Power levels for different intake actions
        public static double maxMovePower = 1.0;
        public static double stopPower = 0.0;
        public static double lockPower = 0.98;
        public static final double FLOWER_STORE_POSITION = 1.0;
        public static final double FLOWER_INTAKE_POSITION = 0.31;
    }

    /** One 435 RPM motor shared by the intake and feeder mechanisms. */
    public static class IntakeFeeder {
        public static final String MOTOR_NAME = "feeder";
        public static final DcMotorSimple.Direction MOTOR_DIRECTION =
                DcMotorSimple.Direction.REVERSE;
        public static final double MOTOR_RATED_RPM = 435.0;
    }

    public static class Feeder {
        // Power levels for different intake actions
        public static double maxMovePower = 1.0;
        public static double stopPower = 0.0;
        public static double lockPower = 0.98;
        public static final String FEEDER_GATE_NAME = "gate";
        public static final double FEEDER_GATE_CLOSED_POSITION = 0.0;
        public static final double FEEDER_GATE_OPEN_POSITION = 1.0;
    }

    public static class LimelightConstants {
        public static final String limelightName = "limelight-pollen";
        public static final int LIMELIGHT_PIPELINE = 0;
        public static final double aimError = 1.0; //Degrees
        public static final double AIM_GAIN = 0.025;
        public static final double MAX_AIM_TURN = 0.35;
    }

    public static class TurretConstants {
        public static final String LAUNCHER_MOTOR_NAME = "turretLauncher";
        public static final String KICKER_MOTOR_NAME = "turretKicker";
        public static final String AIM_SERVO_NAME = "turretAim";
        public static final DcMotorSimple.Direction LAUNCHER_MOTOR_DIRECTION =
                DcMotorSimple.Direction.FORWARD;
        public static final DcMotorSimple.Direction KICKER_MOTOR_DIRECTION =
                DcMotorSimple.Direction.FORWARD;
        public static final double LAUNCHER_RATED_RPM = 6000.0;
        public static final double KICKER_RATED_RPM = 1620.0;
        public static final double AIM_FORWARD_POSITION = 0.5;
        public static final double AIM_SERVO_RANGE_DEGREES = 355.0;
        public static final double LAUNCHER_VELOCITY_P = 56.4;
        public static final double LAUNCHER_VELOCITY_I = 3.0;
        public static final double LAUNCHER_VELOCITY_D = 4.0;
        public static final double LAUNCHER_VELOCITY_F = 13.47;
    }

}
