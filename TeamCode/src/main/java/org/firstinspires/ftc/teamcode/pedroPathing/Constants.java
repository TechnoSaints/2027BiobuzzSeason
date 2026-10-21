package org.firstinspires.ftc.teamcode.pedroPathing;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.common.RobotConstants;

@Configurable
public class Constants {
    /** Tune with Pedro's forward/lateral velocity tests; these are inches/second. */
    public static double forwardVelocity = 96.31;
    public static double lateralVelocity = 52.31;
    public static double maxAutoPower = RobotConstants.Drivetrain.maxAutoPower;

    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(3.4)
            .forwardZeroPowerAcceleration(-26)
            .lateralZeroPowerAcceleration(-38.67)
            .useSecondaryTranslationalPIDF(false)
            .useSecondaryHeadingPIDF(false)
            .useSecondaryDrivePIDF(false)
            .centripetalScaling(0.0001)
            .translationalPIDFCoefficients(new PIDFCoefficients(0.15, 0, 0.02, 0))
            .headingPIDFCoefficients(new PIDFCoefficients(2, 0, 0.1, 0))
            .drivePIDFCoefficients(
                    new FilteredPIDFCoefficients(0.007, 0, 0.000001, 0.6, 0)
            )
            //.predictiveBrakingCoefficients(new PredictiveBrakingCoefficients(0,0.05032,0.00334))
            ;

    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(5.5)
            .strafePodX(-7.875)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("pinpoint")
            .encoderResolution(
                    GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD
            )
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);

    /**
     These are the PathConstraints in order:
     tValueConstraint, velocityConstraint, translationalConstraint, headingConstraint, timeoutConstraint,
     brakingStrength, BEZIER_CURVE_SEARCH_LIMIT, brakingStart
     The BEZIER_CURVE_SEARCH_LIMIT should typically be left at 10 and shouldn't be changed.
     */
    public static PathConstraints pathConstraints = new PathConstraints(
            0.995,
            0.1,
            0.1,
            0.009,
            150,
            1.0,
            10,
            1
    );

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .mecanumDrivetrain(createMecanumConstants())
                .pinpointLocalizer(localizerConstants)
                .pathConstraints(pathConstraints)
                .build();
    }

    private static MecanumConstants createMecanumConstants() {
        return new MecanumConstants()
                .maxPower(maxAutoPower)
                .leftFrontMotorName(RobotConstants.Drivetrain.LEFT_FRONT_NAME)
                .leftRearMotorName(RobotConstants.Drivetrain.LEFT_REAR_NAME)
                .rightFrontMotorName(RobotConstants.Drivetrain.RIGHT_FRONT_NAME)
                .rightRearMotorName(RobotConstants.Drivetrain.RIGHT_REAR_NAME)
                .leftFrontMotorDirection(RobotConstants.Drivetrain.leftFrontMotorDirection)
                .leftRearMotorDirection(RobotConstants.Drivetrain.leftRearMotorDirection)
                .rightFrontMotorDirection(RobotConstants.Drivetrain.rightFrontMotorDirection)
                .rightRearMotorDirection(RobotConstants.Drivetrain.rightRearMotorDirection)
                .xVelocity(forwardVelocity)
                .yVelocity(lateralVelocity)
                .useBrakeModeInTeleOp(true)
                .nominalVoltage(12.5)
                .useVoltageCompensation(true);
    }
}
