package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Localizer;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.common.RobotConstants;

public class Constants {
    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set(RobotConstants.Drivetrain.LEFT_FRONT_NAME);
        c.frontRightName.set(RobotConstants.Drivetrain.RIGHT_FRONT_NAME);
        c.backLeftName.set(RobotConstants.Drivetrain.LEFT_REAR_NAME);
        c.backRightName.set(RobotConstants.Drivetrain.RIGHT_REAR_NAME);
        c.frontLeftDirection.set(RobotConstants.Drivetrain.leftFrontMotorDirection);
        c.frontRightDirection.set(RobotConstants.Drivetrain.rightFrontMotorDirection);
        c.backLeftDirection.set(RobotConstants.Drivetrain.leftRearMotorDirection);
        c.backRightDirection.set(RobotConstants.Drivetrain.rightRearMotorDirection);
    });

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.xPodOffset.set(0.0);
        c.yPodOffset.set(0.0);
        c.offsetUnits.set(DistanceUnit.INCH);
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(c -> {
        // Add any specific foresight configurations here if needed
    });

    public static Follower create(HardwareMap h) {
        Localizer localizer = new PinpointLocalizer(h, localizerConfig);
        Mecanum drivetrain = new Mecanum(h, drivetrainConfig);
        Foresight algorithm = new Foresight(foresightConfig);
        return new Follower(localizer, drivetrain, algorithm);
    }
}