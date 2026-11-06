package org.firstinspires.ftc.teamcode.common;

import com.pedropathing.follower.Follower;
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
    // The OpMode (TeleOp or Autonomous) that created this robot
    protected OpMode opMode;
    // Follower drives the robot and keeps track of where it is on the field
    public Follower follower;
    // Intake picks up and lets go of game pieces
    public Intake intake;
    public PollenTurret pollenTurret;

    protected Bot(OpMode opMode, Telemetry telemetry, PollenTurret.Alliance alliance) {
        super(telemetry);
        this.opMode = opMode;

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
            this.intake = new Intake(opMode.hardwareMap, telemetry);
        } catch (Exception e) {
            telemetry.addLine("Error: Failed to initialize Intake");
        }

        try {
            pollenTurret = new PollenTurret(opMode.hardwareMap, telemetry, alliance);
        } catch (Exception e) {
            telemetry.addData(
                    "Error: Failed to initialize Pollen Turret",
                    "%s: %s", e.getClass().getSimpleName(), String.valueOf(e.getMessage())
            );
        }
    }

    /** Starts Limelight processing and resets the turret's aiming timer. */
    public void startPollenTurret() {
        if (pollenTurret != null) {
            pollenTurret.startLimelight();
        }
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
        if (intake != null) {
            intake.update();
        }
        if (pollenTurret != null) {
            pollenTurret.update();
        }
    }

    /** Spins the intake forward to pick up a game piece. */
    public void forward() {
        if (intake != null) {
            intake.forward();
        }
    }

    /** Spins the intake backward to let go of a game piece. */
    public void reverse() {
        if (intake != null) {
            intake.reverse();
        }
    }

    /** Turns the intake off. */
    public void stop() {
        if (intake != null) {
            intake.stop();
        }
        if (pollenTurret != null) {
            pollenTurret.stop();
        }
    }
}
