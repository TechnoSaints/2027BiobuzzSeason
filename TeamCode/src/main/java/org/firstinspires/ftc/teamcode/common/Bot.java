package org.firstinspires.ftc.teamcode.common;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedro.Constants;

/**
 * Bot is the base class for all robot types. 
 * It handles the initialization of systems like Drivetrain (Follower) and Intake.
 * All common robot logic that applies to both Auto and TeleOp should go here.
 */
public abstract class Bot extends Component {
    // Reference to the OpMode (TeleOp or Auto) that is running the robot
    protected OpMode opMode;
    // The Follower is responsible for controlling the drivetrain and movement
    public Follower follower;
    // The Intake system for collecting game pieces
    public Intake intake;

    protected Bot(OpMode opMode, Telemetry telemetry) {
        super(telemetry);
        this.opMode = opMode;
        
        // Try to initialize the Follower. If it fails (e.g. missing sensor), we log an error.
        try {
            this.follower = Constants.create(opMode.hardwareMap);
        } catch (Exception e) {
            telemetry.addLine("Error: Failed to initialize Follower (Drivetrain)");
        }
        
        // Try to initialize the Intake. If it fails (e.g. missing motor), we log an error.
        try {
            this.intake = new Intake(opMode.hardwareMap, telemetry);
        } catch (Exception e) {
            telemetry.addLine("Error: Failed to initialize Intake");
        }
    }

    /**
     * update() should be called in every loop of your OpMode.
     * it ensures that movement and subsystems stay synchronized.
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
