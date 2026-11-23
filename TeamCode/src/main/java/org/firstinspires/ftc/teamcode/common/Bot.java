package org.firstinspires.ftc.teamcode.common;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.components.Component;
import org.firstinspires.ftc.teamcode.common.components.Intake;
import org.firstinspires.ftc.teamcode.common.components.PollenTurret;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

/**
 * Bot is the base class shared by both Autonomous and TeleOp robots.
 * It builds the shared drivetrain, intake, and pollen turret.
 */
public abstract class Bot extends Component {
    // Follower drives the robot and keeps track of where it is on the field
    protected Follower follower;
    // Intake and feeder form one game-piece handling subsystem.
    protected Intake intakeFeeder;
    protected PollenTurret pollenTurret;
    private boolean hasHandledShootingState;
    private boolean lastShootingState;

    protected Bot(OpMode opMode, Telemetry telemetry, PollenTurret.Alliance alliance) {
        super(telemetry);

        // If a sensor or motor is missing/misconfigured, catch the error and keep
        // going instead of crashing the whole OpMode - just warn on the Driver Station.
        try {
            this.follower = Constants.createFollower(opMode.hardwareMap);
        } catch (Exception e) {
            telemetry.addData(
                    "Error: Failed to initialize Follower (Drivetrain)",
                    "%s: %s", e.getClass().getSimpleName(), String.valueOf(e.getMessage())
            );
        }

        try {
            this.intakeFeeder = new Intake(opMode.hardwareMap, telemetry);
        } catch (Exception e) {
            telemetry.addData(
                    "Error: Failed to initialize Intake",
                    "%s: %s", e.getClass().getSimpleName(), String.valueOf(e.getMessage())
            );
        }

        try {
            pollenTurret = new PollenTurret(opMode.hardwareMap, telemetry, alliance);
        } catch (Exception e) {
            telemetry.addData(
                    "Error: Failed to initialize Turrets",
                    "%s: %s", e.getClass().getSimpleName(), String.valueOf(e.getMessage())
            );
        }
    }

    /** Starts Limelight processing and resets the turret's aiming timer. */
    public void startPollenTurret() {
        if (pollenTurret != null) {
            pollenTurret.startLimelight();
            pollenTurret.raiseHood();
        }
    }

    public void runPollenTurret() {
        if (pollenTurret != null) {
            // TODO: change to pollen calculation
            pollenTurret.setVelocity(pollenTurret.getVelocity());

            LLResult result = pollenTurret.getLatestLimelightResult();
            pollenTurret.update(result);
            LLResultTypes.FiducialResult targetTag = pollenTurret.getClosestAllianceTag(result);
            Double angleOffset = targetTag == null ? null : targetTag.getTargetXDegrees();

        }
    }

    public void lowerFlowerServo(){
        intakeFeeder.lowerFlowerServo();
    }

    public void raiseFlowerServo(){
        intakeFeeder.raiseFlowerServo();
    }

    public void openGate(){
        intakeFeeder.openGate();
    }
    /**
     * Call this once every loop of your OpMode.
     * It keeps the drivetrain and initialized components up to date.
     */
    @Override
    public void update() {
        if (follower != null) {
            follower.update();
        }
        if (intakeFeeder != null) {
            intakeFeeder.update();
        }
    }

    /** Spins the intake forward to pick up a game piece. */
    public void intakeOn() {
        if (intakeFeeder != null) {
            intakeFeeder.forward();
            intakeFeeder.feederForward();
        }
    }

    /** Spins the intake backward to let go of a game piece. */
    public void eject() {
        intakeFeeder.reverse();
    }

    /** Turns the intake off. */
    public void stop() {
        if (intakeFeeder != null) {
            intakeFeeder.stop();
        }
        if (pollenTurret != null) {
            pollenTurret.stop();
        }
    }

    public void intakeOff(){
        intakeFeeder.stop();
        stopFeeder();
    }

    public void log() {
        if (pollenTurret != null) {
            pollenTurret.log();
        }
    }

    public void start(){
        //startPollenTurret();
        raiseFlowerServo();
        intakeFeeder.closeGate();

    }

    public void setAlliance(PollenTurret.Alliance alliance) {
        if (pollenTurret != null) {
            pollenTurret.setAlliance(alliance);
        }
    }

    public Pose getPose() {
        if (follower != null) {
            return follower.getPose();
        }
        return new Pose(0, 0, 0);
    }

    public void setPollenTurretVelocity(double velocity){
        pollenTurret.setVelocity(velocity);
    }

    public void setPollenTurretAngle(double angle) {
        pollenTurret.setPollenLaunchAngle(angle);
    }

    public void setPollenTurretRotation (double angle) {
        pollenTurret.setPollenRotation(angle);
    }

    public void stopFeeder(){
        intakeFeeder.stopFeeder();
        intakeFeeder.closeGate();
    }

    public void handleShooting(boolean shoot, boolean intakeOn){
        if (hasHandledShootingState && shoot == lastShootingState) {
            return;
        }
        hasHandledShootingState = true;
        lastShootingState = shoot;

        if (shoot) {
            intakeFeeder.feederForward();
            intakeFeeder.openGate();
        } else {
            if (!intakeOn) {
                intakeFeeder.stopFeeder();
            }
            intakeFeeder.closeGate();
        }
    }

    @Override
    public boolean isBusy() {
        return follower.isBusy() || intakeFeeder.isBusy() || pollenTurret.isBusy();
    }
}
