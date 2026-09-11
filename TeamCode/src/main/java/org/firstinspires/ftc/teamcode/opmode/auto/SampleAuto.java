package org.firstinspires.ftc.teamcode.opmode.auto;

import com.pedropathing.api.Paths;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.common.AutoBot;
import org.firstinspires.ftc.teamcode.common.AutoPaths;
import org.firstinspires.ftc.teamcode.common.FieldConstants;

/**
 * SampleAuto is a template for creating Autonomous routines.
 * It demonstrates how to initialize the robot and follow a pre-defined path.
 */
@Autonomous(name = "Sample Pedro Auto", group = "Autonomous")
public class SampleAuto extends LinearOpMode {
    private AutoBot bot;

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize the autonomous robot object
        bot = new AutoBot(this, telemetry);

        // Define where the robot starts on the field
        bot.setPose(FieldConstants.RED_LEFT_START);

        // Load a pre-defined path from our AutoPaths library
        Path scorePath = AutoPaths.getScorePath(true);

        // Wait for the driver to press START
        waitForStart();

        // If someone stops the robot before it starts, exit early
        if (isStopRequested()) return;

        // Tell the robot to start following the path
        bot.followPath(scorePath);

        // Keep updating the robot as long as it's following the path
        while (opModeIsActive() && bot.isBusy()) {
            bot.update();
            telemetry.addData("Status", "Following Path...");
            telemetry.addData("Pose", bot.follower.pose().toString());
            telemetry.update();
        }

        telemetry.addData("Status", "Path Complete!");
        telemetry.update();
    }
}
