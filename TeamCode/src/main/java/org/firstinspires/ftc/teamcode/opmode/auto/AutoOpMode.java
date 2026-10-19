package org.firstinspires.ftc.teamcode.opmode.auto;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.common.AutoBot;

/**
 * AutoOpMode is the starting point for every Autonomous OpMode we write.
 * Every autonomous routine needs to do the same 3 things - build the robot,
 * wait for the driver to press START, and print a final message - so that
 * setup code lives here once instead of being copy-pasted into every routine.
 */
public abstract class AutoOpMode extends LinearOpMode {
    protected AutoBot bot;

    /**
     * Builds the robot and tells it where it's starting on the field.
     * Call this first thing in runOpMode(), before waiting for start.
     * @param startPose where the robot is sitting when the match begins.
     */
    protected void initBot(Pose startPose) {
        bot = new AutoBot(this, telemetry);
        bot.setPose(startPose);
    }

    /**
     * Pauses here until the driver presses START.
     * @return false if the OpMode was stopped before START was pressed.
     *         When this happens, your runOpMode() should just return right away.
     */
    protected boolean awaitStart() {
        waitForStart();
        return !isStopRequested();
    }

    /**
     * Prints one last telemetry message so the driver knows the routine finished,
     * then keeps updating the robot until the OpMode actually stops - so the
     * drivetrain doesn't get left in a weird state right as Autonomous ends.
     */
    protected void finish(String message) {
        telemetry.addLine(message);
        telemetry.update();

        while (opModeIsActive()) {
            bot.update();
        }
    }
}
