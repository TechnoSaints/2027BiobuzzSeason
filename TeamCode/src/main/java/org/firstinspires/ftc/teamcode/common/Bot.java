package org.firstinspires.ftc.teamcode.common;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedro.Constants;

/**
 * Bot is the base class shared by both Autonomous and TeleOp robots.
 * It builds the two things every robot needs - a drivetrain (the Follower)
 * and an Intake - so that code common to both driving modes only has to
 * be written once, here.
 */
public abstract class Bot extends Component {
    // The OpMode (TeleOp or Autonomous) that created this robot
    protected OpMode opMode;
    // Follower drives the robot and keeps track of where it is on the field
    public Follower follower;
    // Intake picks up and lets go of game pieces
    public Intake intake;

    protected Bot(OpMode opMode, Telemetry telemetry) {
        super(telemetry);
        this.opMode = opMode;

        // If a sensor or motor is missing/misconfigured, catch the error and keep
        // going instead of crashing the whole OpMode - just warn on the Driver Station.
        try {
            this.follower = Constants.create(opMode.hardwareMap);
        } catch (Exception e) {
            telemetry.addLine("Error: Failed to initialize Follower (Drivetrain)");
        }

        try {
            this.intake = new Intake(opMode.hardwareMap, telemetry);
        } catch (Exception e) {
            telemetry.addLine("Error: Failed to initialize Intake");
        }
    }

    /**
     * Call this once every loop of your OpMode.
     * It keeps the drivetrain and the intake both up to date.
     */
    @Override
    public void update() {
        if (follower != null) {
            follower.update();
        }
        if (intake != null) {
            intake.update();
        }
    }
}
