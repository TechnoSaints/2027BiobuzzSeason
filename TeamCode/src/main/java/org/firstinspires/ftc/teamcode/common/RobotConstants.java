package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * RobotConstants is where every hardware name and tuning number lives.
 * If a motor name changes in the Driver Station's Hardware Config, or a
 * measurement needs adjusting, you only have to change it here - not
 * hunt through every file that uses it.
 */
public class RobotConstants {

    // Everything the drivetrain (the wheels) needs to know
    public static class Drivetrain {
        // These 4 names must exactly match the motor names in your Hardware Config
        public static final String LEFT_FRONT_NAME = "leftFrontDrive";
        public static final String LEFT_REAR_NAME = "leftRearDrive";
        public static final String RIGHT_FRONT_NAME = "rightFrontDrive";
        public static final String RIGHT_REAR_NAME = "rightRearDrive";

        // Wheel size, used to convert motor spins into real-world distance
        public static double wheelDiameterMM = 104.0;
        public static double wheelDiameterInches = wheelDiameterMM / 25.4;
        public static double wheelCircumferenceInches = Math.PI * wheelDiameterInches;

        // Which way each motor should spin to drive forward.
        // If the robot drives backward when you push forward, flip these!
        public static DcMotorSimple.Direction leftFrontMotorDirection = DcMotorSimple.Direction.REVERSE;
        public static DcMotorSimple.Direction leftRearMotorDirection = DcMotorSimple.Direction.REVERSE;
        public static DcMotorSimple.Direction rightFrontMotorDirection = DcMotorSimple.Direction.FORWARD;
        public static DcMotorSimple.Direction rightRearMotorDirection = DcMotorSimple.Direction.FORWARD;
    }

    // Everything the intake needs to know
    public static class Intake {
        public static final String INTAKE_MOTOR_NAME = "intakeMotor";
        public static DcMotorSimple.Direction intakeMotorDirection = DcMotorSimple.Direction.FORWARD;

        // How hard the intake motor spins for each action
        public static double maxMovePower = 1.0;
        public static double stopPower = 0.0;
        public static double lockPower = 0.98;
    }
}
