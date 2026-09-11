package org.firstinspires.ftc.teamcode.opmode.test;

import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.common.AutoBot;
import org.firstinspires.ftc.teamcode.common.AutoPaths;
import org.firstinspires.ftc.teamcode.common.FieldConstants;

/**
 * CircuitAutoTest is a testing autonomous routine to verify the robot's movement.
 * It follows a complex path through 4 different points and returns home.
 */
@Autonomous(name = "Circuit Path Test", group = "Test")
public class CircuitAutoTest extends LinearOpMode {
    private AutoBot bot;

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize robot
        bot = new AutoBot(this, telemetry);

        // Set the robot's starting location on the field map
        bot.setPose(FieldConstants.RED_LEFT_START);

        // Load the complex circuit path from AutoPaths
        Path circuit = AutoPaths.getCircuitPath();

        telemetry.addLine("Ready to test complex circuit path...");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        // Start movement
        bot.followPath(circuit);

        // Update loop: Runs while the robot is moving along the path
        while (opModeIsActive() && bot.isBusy()) {
            bot.update();
            telemetry.addData("Status", "Running Circuit...");
            telemetry.addData("Pose", bot.follower.pose().toString());
            telemetry.update();
        }

        telemetry.addLine("Test Complete - Returning home.");
        telemetry.update();
        
        // Final update loop to ensure systems stay active at the end
        while (opModeIsActive()) {
            bot.update();
        }
    }
}
