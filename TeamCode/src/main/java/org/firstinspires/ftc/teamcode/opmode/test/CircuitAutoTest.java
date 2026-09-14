package org.firstinspires.ftc.teamcode.opmode.test;

import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.FieldConstants;
import org.firstinspires.ftc.teamcode.opmode.auto.AutoOpMode;
import org.firstinspires.ftc.teamcode.opmode.auto.AutoPaths;

/**
 * CircuitAutoTest is a testing autonomous routine to verify the robot's movement.
 * It follows a complex path through 4 different points and returns home.
 */
@Autonomous(name = "Circuit Path Test", group = "Test")
public class CircuitAutoTest extends AutoOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize robot and set the robot's starting location on the field map
        initBot(FieldConstants.RED_LEFT_START);

        // Load the complex circuit path from AutoPaths
        Path circuit = AutoPaths.getCircuitPath();

        telemetry.addLine("Ready to test complex circuit path...");
        telemetry.update();

        if (!awaitStart()) return;

        // Start movement
        bot.followPath(circuit);

        // Update loop: Runs while the robot is moving along the path
        while (opModeIsActive() && bot.isBusy()) {
            bot.update();
            telemetry.addData("Status", "Running Circuit...");
            telemetry.addData("Pose", bot.getPoseString());
            telemetry.update();
        }

        finish("Test Complete - Returning home.");

        // Final update loop to ensure systems stay active at the end
        while (opModeIsActive()) {
            bot.update();
        }
    }
}
