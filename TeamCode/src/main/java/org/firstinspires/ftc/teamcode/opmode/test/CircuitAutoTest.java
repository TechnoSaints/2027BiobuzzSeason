package org.firstinspires.ftc.teamcode.opmode.test;

import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.FieldConstants;
import org.firstinspires.ftc.teamcode.opmode.auto.AutoOpMode;
import org.firstinspires.ftc.teamcode.opmode.auto.AutoPaths;

/**
 * CircuitAutoTest checks that the robot can drive accurately.
 * It follows a loop through 4 points and back to where it started, so you
 * can watch whether the robot ends up back where it began.
 */
@Autonomous(name = "Circuit Path Test", group = "Test")
public class CircuitAutoTest extends AutoOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        // Build the robot and tell it where it's starting
        initBot(FieldConstants.RED_LEFT_START);

        Path circuit = AutoPaths.getCircuitPath();

        telemetry.addLine("Ready to test complex circuit path...");
        telemetry.update();

        if (!awaitStart()) return;

        bot.followPath(circuit);

        // Keep driving and showing status until the robot finishes the loop
        while (opModeIsActive() && bot.isBusy()) {
            bot.update();
            telemetry.addData("Status", "Running Circuit...");
            telemetry.addData("Pose", bot.getPoseString());
            telemetry.update();
        }

        finish("Test Complete - Returning home.");
    }
}
