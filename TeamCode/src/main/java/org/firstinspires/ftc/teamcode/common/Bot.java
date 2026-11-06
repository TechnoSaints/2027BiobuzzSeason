package org.firstinspires.ftc.teamcode.common;

import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.components.Component;
import org.firstinspires.ftc.teamcode.common.components.Intake;
import org.firstinspires.ftc.teamcode.common.components.NectarTurret;
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
    public NectarTurret nectarTurret;

    protected Bot(OpMode opMode, Telemetry telemetry, PollenTurret.Alliance alliance) {
        super(telemetry);
        this.opMode = opMode;

        NectarTurret.Alliance nectarAlliance = toNectarAlliance(alliance);

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
            nectarTurret = new NectarTurret(opMode.hardwareMap, telemetry, nectarAlliance);
        } catch (Exception e) {
            telemetry.addData(
                    "Error: Failed to initialize Turrets",
                    "%s: %s", e.getClass().getSimpleName(), String.valueOf(e.getMessage())
            );
        }
    }

    private static NectarTurret.Alliance toNectarAlliance(PollenTurret.Alliance alliance) {
        if (alliance == null) {
            throw new IllegalArgumentException("Alliance must be specified");
        }
        return NectarTurret.Alliance.valueOf(alliance.name());
    }

    /** Starts Limelight processing and resets the turret's aiming timer. */
    public void startPollenTurret() {
        if (pollenTurret != null) {
            pollenTurret.startLimelight();
        }
    }

    public void startNectarTurret() {
        if (nectarTurret != null) {
            nectarTurret.startLimelight();
        }
    }

    public void runPollenTurret() {
        if (pollenTurret != null) {
            pollenTurret.setMotorPIDFCoefficients(new PIDFCoefficients(PollenTurret.kP, 0, 0, PollenTurret.kD));
            pollenTurret.setVelocity(nectarTurret.findNectarSpeed());

            LLResult result = pollenTurret.getLatestLimelightResult();
            pollenTurret.update(result);
            LLResultTypes.FiducialResult targetTag = pollenTurret.getClosestAllianceTag(result);
            Double angleOffset = targetTag == null ? null : targetTag.getTargetXDegrees();

        }
    }

    public void runNectarTurret() {
        if (nectarTurret != null) {
            nectarTurret.setMotorPIDFCoefficients(new PIDFCoefficients(NectarTurret.kP, 0, 0, NectarTurret.kD));
            nectarTurret.setVelocity(nectarTurret.findNectarSpeed());

            LLResult result = nectarTurret.getLatestLimelightResult();
            nectarTurret.update(result);
            LLResultTypes.FiducialResult targetTag = nectarTurret.getClosestAllianceTag(result);
            Double angleOffset = targetTag == null ? null : targetTag.getTargetXDegrees();

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
        if (nectarTurret != null) {
            nectarTurret.update();
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
        if (nectarTurret != null) {
            nectarTurret.stop();
        }
    }

    public void log() {
        if (nectarTurret != null) {
            nectarTurret.log();
        }
        if (pollenTurret != null) {
            pollenTurret.log();
        }
    }
}
