package org.firstinspires.ftc.teamcode.opmode.auto;

import com.pedropathing.api.Paths;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.common.AutoBot;
import org.firstinspires.ftc.teamcode.common.AutoPaths;
import org.firstinspires.ftc.teamcode.common.FieldConstants;

@Autonomous(name = "Sample Pedro Auto", group = "Autonomous")
public class SampleAuto extends LinearOpMode {
    private AutoBot bot;

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize the robot
        bot = new AutoBot(this, telemetry);

        // Set the initial pose
        bot.setPose(FieldConstants.RED_LEFT_START);

        // Use pre-defined path from AutoPaths
        Path scorePath = AutoPaths.getScorePath(true);

        waitForStart();

        if (isStopRequested()) return;

        // Follow the path
        bot.followPath(scorePath);
        while (opModeIsActive() && bot.isBusy()) {
            bot.update();
            telemetry.addData("Status", "Following AutoPath");
            telemetry.addData("Pose", bot.follower.pose().toString());
            telemetry.update();
        }

        telemetry.addData("Status", "Autonomous Complete");
        telemetry.update();
    }
}
