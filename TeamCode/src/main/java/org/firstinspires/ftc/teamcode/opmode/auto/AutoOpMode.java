package org.firstinspires.ftc.teamcode.opmode.auto;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.common.AutoBot;

/**
 * AutoOpMode is the base class for every autonomous OpMode.
 * It factors out the setup/wait/finish boilerplate that every autonomous
 * routine needs, so individual OpModes can focus on their own paths and logic.
 */
public abstract class AutoOpMode extends LinearOpMode {
    protected AutoBot bot;

    /**
     * Creates the AutoBot and sets its starting field position.
     * Call this first in runOpMode(), before waiting for start.
     * @param startPose where the robot begins the match.
     */
    protected void initBot(Pose startPose) {
        bot = new AutoBot(this, telemetry);
        bot.setPose(startPose);
    }

    /**
     * Waits for the driver to press START.
     * @return false if the OpMode was stopped before START was pressed —
     *         callers should return from runOpMode() immediately when this happens.
     */
    protected boolean awaitStart() {
        waitForStart();
        return !isStopRequested();
    }

    /** Prints a final telemetry message to mark the end of the routine. */
    protected void finish(String message) {
        telemetry.addLine(message);
        telemetry.update();
    }
}
