package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class RobotConstants {


    public static class Drivetrain {
        public static final String LEFT_FRONT_NAME = "leftFrontDrive";
        public static final String LEFT_REAR_NAME = "leftRearDrive";

        public static final String RIGHT_FRONT_NAME = "rightFrontDrive";
        public static final String RIGHT_REAR_NAME = "rightRearDrive";
        public static double wheelDiameterMM = 104.0;
        public static double wheelDiameterInches = wheelDiameterMM / 25.4;
        public static double wheelCircumferenceInches = Math.PI * wheelDiameterInches;
        public static DcMotorSimple.Direction leftFrontMotorDirection = DcMotorSimple.Direction.REVERSE;
        public static DcMotorSimple.Direction leftRearMotorDirection = DcMotorSimple.Direction.REVERSE;

        public static DcMotorSimple.Direction rightFrontMotorDirection = DcMotorSimple.Direction.FORWARD;
        public static DcMotorSimple.Direction rightRearMotorDirection = DcMotorSimple.Direction.FORWARD;
    }

    public static class Intake {
        public static double maxMovePower = 1;
        public static double stopPower = 0;
        public static double lockPower = 0.98;
    }

    public static class Camera {
        public static final double xPosition = 0;
        public static final double yPosition = 0;
        public static final double zPosition = 0;
        public static final double yaw = 0;
        public static final double pitch = -90;
        public static final double roll = 0;
    }
}
