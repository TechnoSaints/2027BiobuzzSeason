package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.components.PollenTurret;
import org.firstinspires.ftc.teamcode.common.components.UnderTurret;
import org.firstinspires.ftc.teamcode.common.constants.FieldConstants;
import org.firstinspires.ftc.teamcode.common.constants.RobotConstants;

/** Bot variant for an odometry turret that uses the Pedro Pathing follower. */
public class OdoTurretBot extends Bot {
    private final PollenTurret.Alliance alliance;
    private final UnderTurret underTurret;

    public OdoTurretBot(
            OpMode opMode, Telemetry telemetry, PollenTurret.Alliance alliance) {
        super(opMode, telemetry, alliance);
        if (alliance == null) {
            throw new IllegalArgumentException("Alliance must be specified.");
        }
        this.alliance = alliance;
        if (follower == null) {
            throw new IllegalStateException("Pedro Pathing follower failed to initialize.");
        }
        underTurret = new UnderTurret(opMode.hardwareMap, telemetry);
    }

    public void startTeleopDrive() {
        follower.startTeleopDrive();
    }

    public void setDrivePowers(double forward, double strafe, double turn) {
        follower.setTeleOpDrive(forward, strafe, turn, true);
    }

    public void stopDrive() {
        follower.setTeleOpDrive(0, 0, 0, true);
        follower.update();
    }

    public void runKickerForward() {
        underTurret.runKickerForward();
    }

    public void runKickerReverse() {
        underTurret.runKickerReverse();
    }

    public void stopKicker() {
        underTurret.stopKicker();
    }

    /**
     * Returns the heading from the robot toward the nearer estimated alliance cell center,
     * relative to the robot's current heading, in degrees.
     */
    public double getCellHeading() {
        Pose robotPose = follower.getPose();
        Pose audienceCell;
        Pose oppositeAudienceCell;
        switch (alliance) {
            case BLUE:
                audienceCell = FieldConstants.blueAudienceCellPose;
                oppositeAudienceCell = FieldConstants.blueOppositeAudienceCellPose;
                break;
            case RED:
                audienceCell = FieldConstants.redAudienceCellPose;
                oppositeAudienceCell = FieldConstants.redOppositeAudienceCellPose;
                break;
            default:
                throw new IllegalStateException("Unsupported alliance: " + alliance);
        }

        double audienceDx = audienceCell.getX() - robotPose.getX();
        double audienceDy = audienceCell.getY() - robotPose.getY();
        double oppositeAudienceDx = oppositeAudienceCell.getX() - robotPose.getX();
        double oppositeAudienceDy = oppositeAudienceCell.getY() - robotPose.getY();

        double targetDx;
        double targetDy;
        if (audienceDx * audienceDx + audienceDy * audienceDy
                <= oppositeAudienceDx * oppositeAudienceDx + oppositeAudienceDy * oppositeAudienceDy) {
            targetDx = audienceDx;
            targetDy = audienceDy;
        } else {
            targetDx = oppositeAudienceDx;
            targetDy = oppositeAudienceDy;
        }

        double fieldBearing = Math.atan2(targetDy, targetDx);
        double relativeBearing = fieldBearing - robotPose.getHeading();
        return Math.toDegrees(Math.atan2(Math.sin(relativeBearing), Math.cos(relativeBearing)));
    }

    /** Aims the turret toward the nearest cell of the assigned alliance. */
    public void aimTurretAtNearestCell() {
        double targetPosition = RobotConstants.TurretConstants.AIM_FORWARD_POSITION
                + getCellHeading() / RobotConstants.TurretConstants.AIM_SERVO_RANGE_DEGREES;
        underTurret.setAimPosition(targetPosition);
    }

    /** Returns the turret's current angle relative to forward, in degrees. */
    public double getTurretAngleDegrees() {
        return (underTurret.getAimPosition() - RobotConstants.TurretConstants.AIM_FORWARD_POSITION)
                * RobotConstants.TurretConstants.AIM_SERVO_RANGE_DEGREES;
    }

    public void setPollenTurretVelocity(double velocity){
        underTurret.setLauncherVelocity(velocity);
    }
}
