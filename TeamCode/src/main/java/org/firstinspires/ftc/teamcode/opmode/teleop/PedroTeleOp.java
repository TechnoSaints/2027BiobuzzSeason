package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.TeleopBot;

/**
 * PedroTeleOp is the main driver-controlled OpMode.
 * It uses the Pedro Pathing library for movement and localization.
 */
@TeleOp(name = "Pedro TeleOp", group = "TeleOp")
public class PedroTeleOp extends OpMode {
    // Reference to our robot object
    private TeleopBot bot;

    // Gamepad state for debouncing
    private boolean prevStart = false;

    /** init() runs once when you press the INIT button on the Driver Station. */
    @Override
    public void init() {
        bot = new TeleopBot(this, telemetry);
    }

    /** loop() runs repeatedly after you press the START button. */
    @Override
    public void loop() {
        // Exit early if the robot object wasn't created properly
        if (bot == null) return;

        // Heading Reset: Press 'Start' to reset the robot's orientation to zero.
        if (gamepad1.start && !prevStart) {
            bot.resetHeading();
        }
        prevStart = gamepad1.start;

        // Basic Mecanum Drive Control:
        // Left stick handles moving (strafe and forward/backward)
        // Right stick handles turning (left/right)
        double forward = -gamepad1.left_stick_y;
        double strafe = -gamepad1.left_stick_x;
        double turn = -gamepad1.right_stick_x;

        // Slow Mode: Hold the left bumper to move at 30% speed for precision.
        double powerScale = gamepad1.left_bumper ? 0.3 : 1.0;

        // Send the drive commands to the bot
        bot.setDrivePowers(forward, strafe, turn, powerScale);

        // Intake Control Mapping:
        // Hold Right Trigger to intake (spin forward)
        // Hold Right Bumper to outtake (spin reverse)
        if (gamepad1.right_trigger > 0.1) {
            bot.forward();
        } else if (gamepad1.right_bumper) {
            bot.reverse();
        } else {
            // Stop spinning when no buttons are pressed
            bot.stop();
        }

        // IMPORTANT: Always call bot.update() to sync drivetrain and subsystems
        bot.update();

        // Display robot status on the Driver Station screen
        telemetry.addData("Status", "Running (TeleOp)");
        telemetry.addData("Slow Mode", gamepad1.left_bumper ? "ON" : "OFF");
        
        // Show the robot's coordinates on the field if the sensors are active
        if (bot.follower != null) {
            telemetry.addData("Pose", bot.follower.pose().toString());
        }
        telemetry.update();
    }
}
