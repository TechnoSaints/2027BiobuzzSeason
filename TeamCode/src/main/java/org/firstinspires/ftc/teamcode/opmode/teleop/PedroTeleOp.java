package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.TeleopBot;

@TeleOp(name = "Pedro TeleOp", group = "TeleOp")
public class PedroTeleOp extends OpMode {
    private TeleopBot bot;

    @Override
    public void init() {
        bot = new TeleopBot(this, telemetry);
    }

    @Override
    public void loop() {
        // Reset heading with 'Start' or 'Options'
        if (gamepad1.start) {
            bot.resetHeading();
        }

        // Basic mecanum drive control
        double forward = -gamepad1.left_stick_y;
        double strafe = -gamepad1.left_stick_x;
        double turn = -gamepad1.right_stick_x;

        // Slow mode calculation (full power by default, 0.3x if left bumper is held)
        double powerScale = gamepad1.left_bumper ? 0.3 : 1.0;

        // Drive the robot (Robot-Centric)
        bot.setDrivePowers(forward, strafe, turn, powerScale);

        // Update the bot (which updates the follower)
        bot.update();

        // Telemetry
        telemetry.addData("Status", "Running");
        telemetry.addData("Slow Mode", gamepad1.left_bumper ? "ON" : "OFF");
        telemetry.addData("Pose", bot.follower.pose().toString());
        telemetry.update();
    }
}
