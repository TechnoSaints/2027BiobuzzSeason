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
        c.xPodOffset.set(15.592120313268946);
        c.yPodOffset.set(8.2502428189976);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.1725833966438774);
                Controller secondaryTranslationalForward = Controller.proportional(0.06376495354662293);
                Controller primaryTranslationalLateral = Controller.proportional(0.26426292894962344);
                Controller secondaryTranslationalLateral = Controller.proportional(0.0976380910113757);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.010724714104143612));
                c.brake.set(Controller.proportionalFeedforward(0.00911600698852207));

                c.headingFeedback.set(Controller.proportional(3.8695061911451636));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.040611435973546, 0.013382198308778025));

                c.linearBrakeCoefficients.set(Matrix.diag(0.06557253228163891, 0.02106066962596593));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0019003346431714315, 0.002289216759772984));

                c.maxAchievableForwardVelocity.set(94.72977449782579);
                c.maxAchievableStrafeVelocity.set(79.81868143920592);
                c.naturalForwardDeceleration.set(27.335226528928537);
                c.naturalStrafeDeceleration.set(40.41878420484146);
            }
    );

    public static Follower create(HardwareMap h) {
        Localizer localizer = new PinpointLocalizer(h, localizerConfig);
        Mecanum drivetrain = new Mecanum(h, drivetrainConfig);
        Foresight algorithm = new Foresight(foresightConfig);
        return new Follower(localizer, drivetrain, algorithm);
    }
}