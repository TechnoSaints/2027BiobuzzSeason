package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.TeleopBot;

/**
 * PedroTeleOp is the main driver-controlled OpMode - this is what runs
 * when a human is driving the robot with a gamepad instead of the robot
 * following a pre-planned path.
 */
@TeleOp(name = "Pedro TeleOp", group = "TeleOp")
public class PedroTeleOp extends OpMode {
    // The robot itself, built once in init()
    private TeleopBot bot;

    // Remembers last loop's button state, so we only react on the
    // moment a button is first pressed instead of every single loop
    private boolean prevStart = false;

    /** Runs once when you press INIT on the Driver Station. */
    @Override
    public void init() {
        bot = new TeleopBot(this, telemetry);
    }

    /** Runs over and over, many times per second, after you press START. */
    @Override
    public void loop() {
        // Bail out if something went wrong building the robot in init()
        if (bot == null) return;

        // Press Start on the gamepad to reset which way the robot thinks is "forward"
        if (gamepad1.start && !prevStart) {
            bot.resetHeading();
        }
        prevStart = gamepad1.start;

        // Left stick drives (forward/back and strafe left/right)
        // Right stick turns the robot left/right
        double forward = -gamepad1.left_stick_y;
        double strafe = -gamepad1.left_stick_x;
        double turn = -gamepad1.right_stick_x;

        // Hold the left bumper to drive at 30% speed, for careful/precise movements
        double powerScale = gamepad1.left_bumper ? 0.3 : 1.0;

        bot.setDrivePowers(forward, strafe, turn, powerScale);

        // Hold the right trigger to pick things up, right bumper to spit them back out
        if (gamepad1.right_trigger > 0.1) {
            bot.forward();
        } else if (gamepad1.right_bumper) {
            bot.reverse();
        } else {
            bot.stop();
        }

        // This has to be called every loop, or the drivetrain and intake stop working
        bot.update();

        telemetry.addData("Status", "Running (TeleOp)");
        telemetry.addData("Slow Mode", gamepad1.left_bumper ? "ON" : "OFF");

        // Only show the robot's position if the localizer actually started up
        if (bot.follower != null) {
            telemetry.addData("Pose", bot.follower.pose().toString());
        }
        telemetry.update();
    }
}
