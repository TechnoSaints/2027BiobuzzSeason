package org.firstinspires.ftc.teamcode.pedroPathing;

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

public class Constants {
    private static final RobotConstants.Drivetrain drivetrainData = new RobotConstants.Drivetrain();
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(5)
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

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(drivetrainData.maxAutoPower)
            .leftFrontMotorName(drivetrainData.LEFT_FRONT_NAME)
            .leftRearMotorName(drivetrainData.LEFT_REAR_NAME)
            .rightFrontMotorName(drivetrainData.RIGHT_FRONT_NAME)
            .rightRearMotorName(drivetrainData.RIGHT_REAR_NAME)
            .leftFrontMotorDirection(drivetrainData.leftFrontMotorDirection)
            .leftRearMotorDirection(drivetrainData.leftRearMotorDirection)
            .rightFrontMotorDirection(drivetrainData.rightFrontMotorDirection)
            .rightRearMotorDirection(drivetrainData.rightRearMotorDirection)
            .xVelocity(60.48)
            .yVelocity(52.31)
            .useBrakeModeInTeleOp(true)
            .nominalVoltage(12.5)
            .useVoltageCompensation(true);

    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(1.77)
            .strafePodX(4.31)
            .distanceUnit(DistanceUnit.MM)
            .hardwareMapName("pinpoint")
            .encoderResolution(
                    GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD
            )
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD);

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
                .mecanumDrivetrain(driveConstants)
                .pinpointLocalizer(localizerConstants)
                .pathConstraints(pathConstraints)
                .build();
    }
}

