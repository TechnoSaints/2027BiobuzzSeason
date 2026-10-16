package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Localizer;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
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
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(3.847856897068775);
        c.yPodOffset.set(4.50360575998862);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.4586015724434756);
                Controller secondaryTranslationalForward = Controller.proportional(0.16944102695816227);
                Controller primaryTranslationalLateral = Controller.proportional(0.21668712257010392);
                Controller secondaryTranslationalLateral = Controller.proportional(0.0800601018031026);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.010627679623426046));
                c.brake.set(Controller.proportionalFeedforward(0.009033527679912138));

                c.headingFeedback.set(Controller.proportional(3.7217851597350546));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.05623044783539611, 0.008497019702509672));

                c.linearBrakeCoefficients.set(Matrix.diag(0.0742599018140965, 0.10739042170231403));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0013486061125068535, 9.14743917706273E-4));

                c.maxAchievableForwardVelocity.set(60.2462583917016);
                c.maxAchievableStrafeVelocity.set(49.95972469368495);
                c.naturalForwardDeceleration.set(14.16730908034461);
                c.naturalStrafeDeceleration.set(27.000097110209023);
            }
    );

    public static Follower create(HardwareMap h) {
        Localizer localizer = new PinpointLocalizer(h, localizerConfig);
        Mecanum drivetrain = new Mecanum(h, drivetrainConfig);
        Foresight algorithm = new Foresight(foresightConfig);
        return new Follower(localizer, drivetrain, algorithm);
    }
}