package org.firstinspires.ftc.teamcode.common;

public abstract class MotorConstants {
    public static double ticksPerMotorRev = 28.0;
    public static double maxMotorRpm = 6000;
    public static double maxMotorRps = maxMotorRpm / 60.0;
    public static int maxTicksPerSec = Math.toIntExact(Math.round(Math.floor(maxMotorRps * ticksPerMotorRev)));

    public static class GoBilda60Dc extends MotorConstants {
        public static double gearRatio = 99.5;
    }
    public static class GoBilda117Dc extends MotorConstants {
        public static double gearRatio = 50.9;
    }
    public static class GoBilda223Dc extends MotorConstants {
        public static double gearRatio = 26.9;
    }
    public static class GoBilda312Dc extends MotorConstants {
        public static double gearRatio = 19.2;
    }
    public static class GoBilda435Dc extends MotorConstants {
        public static double gearRatio = 13.7;
    }
    public static class GoBilda1620Dc extends MotorConstants {
        public static double gearRatio = 3.7;
    }
    public static class GoBilda6000Dc extends MotorConstants {
        public static double gearRatio = 1.0;
    }

}


