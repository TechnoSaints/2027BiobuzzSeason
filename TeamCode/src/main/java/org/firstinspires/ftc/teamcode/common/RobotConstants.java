package org.firstinspires.ftc.teamcode.common;

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
        public static double wheelDiameterMM = 104.0;
        public static double wheelDiameterInches = wheelDiameterMM / 25.4;
        public static double wheelCircumferenceInches = Math.PI * wheelDiameterInches;

        // Motor directions: If the robot moves backward, flip these!
        public static DcMotorSimple.Direction leftFrontMotorDirection = DcMotorSimple.Direction.REVERSE;
        public static DcMotorSimple.Direction leftRearMotorDirection = DcMotorSimple.Direction.REVERSE;
        public static DcMotorSimple.Direction rightFrontMotorDirection = DcMotorSimple.Direction.FORWARD;
        public static DcMotorSimple.Direction rightRearMotorDirection = DcMotorSimple.Direction.FORWARD;
    }

    // Intake-specific constants
    public static class Intake {
        public static final String INTAKE_MOTOR_NAME = "intakeMotor";
        public static DcMotorSimple.Direction intakeMotorDirection = DcMotorSimple.Direction.FORWARD;
        
        // Power levels for different intake actions
        public static double maxMovePower = 1.0;
        public static double stopPower = 0.0;
        public static double lockPower = 0.98;
    }
}
