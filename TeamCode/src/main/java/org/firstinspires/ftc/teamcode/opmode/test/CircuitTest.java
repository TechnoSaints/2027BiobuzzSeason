package org.firstinspires.ftc.teamcode.opmode.test;

import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.common.AutoBot;
import org.firstinspires.ftc.teamcode.common.AutoPaths;
import org.firstinspires.ftc.teamcode.common.FieldConstants;

@Autonomous(name = "Circuit Path Test", group = "Test")
public class CircuitTest extends LinearOpMode {
    private AutoBot bot;

    @Override
    public void runOpMode() throws InterruptedException {
        bot = new AutoBot(this, telemetry);

        // Set start pose
        bot.setPose(FieldConstants.RED_LEFT_START);

        // Get the circuit path
        Path circuit = AutoPaths.getCircuitPath();

        telemetry.addLine("Ready to test circuit path");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        bot.followPath(circuit);

        while (opModeIsActive() && bot.isBusy()) {
            bot.update();
            telemetry.addData("Status", "Running Circuit Path");
            telemetry.addData("Pose", bot.follower.pose().toString());
            telemetry.update();
        }

        telemetry.addLine("Circuit Test Complete");
        telemetry.update();
        
        while (opModeIsActive()) {
            bot.update();
        }
    }
}
