package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.Bot;

@TeleOp(name = "Meet2Teleop", group = "TeleOp")
public class Meet2Teleop extends LinearOpMode {
    private static final double FLYWHEEL_STEP = 0.05;
    private static final double TRIGGER_THRESHOLD = 0.25;

    @Override
    public void runOpMode() {
        Bot bot = new Bot(this, telemetry, Bot.Alliance.BLUE);
        double maxVelocity = bot.getMaxLauncherVelocity();
        double targetVelocity = 0.0;

        telemetry.addLine("Meet2Teleop initialized");
        telemetry.addLine("Left stick: drive | Right stick X: turn");
        telemetry.addLine("X: feeder forward | Circle: stop | Triangle: feeder reverse");
        telemetry.addLine("Right/Left trigger: kicker forward/reverse");
        telemetry.addLine("Right/Left bumper: flywheel velocity +/- 5%");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) {
            return;
        }

        bot.startTeleopDrive();
        try {
            while (opModeIsActive()) {
                bot.setDrivePowers(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x,
                        -gamepad1.right_stick_x);

                if (gamepad1.crossWasPressed()) {
                    bot.runIntakeFeederForward(1.0);
                } else if (gamepad1.circleWasPressed()) {
                    bot.stopIntakeFeeder();
                } else if (gamepad1.triangleWasPressed()) {
                    bot.runIntakeFeederReverse(1.0);
                }

                if (gamepad1.right_trigger > TRIGGER_THRESHOLD) {
                    bot.runKickerForward();
                } else if (gamepad1.left_trigger > TRIGGER_THRESHOLD) {
                    bot.runKickerReverse();
                } else {
                    bot.stopKicker();
                }

                if (gamepad1.rightBumperWasPressed()) {
                    targetVelocity = Math.min(
                            maxVelocity, targetVelocity + maxVelocity * FLYWHEEL_STEP);
                } else if (gamepad1.leftBumperWasPressed()) {
                    targetVelocity = Math.max(
                            0.0, targetVelocity - maxVelocity * FLYWHEEL_STEP);
                }
                bot.setLauncherVelocity(targetVelocity);
                bot.update();

                telemetry.addData("Flywheel Target", "%.0f ticks/s", targetVelocity);
                telemetry.addData(
                        "Flywheel Current", "%.0f ticks/s", bot.getLauncherVelocity());
                telemetry.update();
            }
        } finally {
            bot.stopDrive();
            bot.stopIntakeFeeder();
            bot.stopKicker();
            bot.stopLauncher();
        }
    }
}
