package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class TurretConstants {

    public static class LimelightConstants {
        public static final String limelightName = "limelight";
        public static final int LIMELIGHT_PIPELINE = 0;
        public static final double aimError = 1.0; //Degrees
        public static final double AIM_GAIN = 0.025;
        public static final double MAX_AIM_TURN = 0.35;
    }

    public static class ShooterConstants {
        public static final String SHOOTER_MOTOR_NAME = "shooter";
        public static final String HOOD_SERVO_NAME = "hood";
        public static final DcMotorSimple.Direction shooterMotorDirection =
                DcMotorSimple.Direction.REVERSE;
    }
}
