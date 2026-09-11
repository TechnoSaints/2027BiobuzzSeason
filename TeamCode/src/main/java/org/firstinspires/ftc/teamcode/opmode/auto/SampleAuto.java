package org.firstinspires.ftc.teamcode.opmode.auto;

import com.pedropathing.api.Paths;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.common.AutoBot;
import org.firstinspires.ftc.teamcode.common.AutoPaths;
import org.firstinspires.ftc.teamcode.common.FieldConstants;

/**
 * SampleAuto is a complex state-machine autonomous routine.
 * It demonstrates how to perform a multi-stage routine with 9 distinct states.
 */
@Autonomous(name = "Sample Pedro Auto", group = "Autonomous")
public class SampleAuto extends LinearOpMode {
    private AutoBot bot;
    private ElapsedTime stateTimer;

    private enum State {
        START,
        DRIVE_TO_SCORE,
        SCORE_PIECE,
        DRIVE_TO_INTAKE,
        INTAKE_PIECE,
        DRIVE_TO_SCORE_2,
        SCORE_PIECE_2,
        PARK,
        FINISHED
    }

    private State currentState = State.START;

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize the autonomous robot object
        bot = new AutoBot(this, telemetry);
        stateTimer = new ElapsedTime();

        // Define starting position
        bot.setPose(FieldConstants.RED_LEFT_START);

        // Pre-defined paths for transitions
        Path toScore = AutoPaths.getScorePath(true);
        Path toIntake = Paths.line(FieldConstants.RED_SCORING_POSITION, FieldConstants.CIRCUIT_POINT_1);
        Path toScore2 = Paths.line(FieldConstants.CIRCUIT_POINT_1, FieldConstants.RED_SCORING_POSITION);
        Path toPark = Paths.line(FieldConstants.RED_SCORING_POSITION, FieldConstants.RED_LEFT_START);

        // Wait for the driver to press START
        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive() && currentState != State.FINISHED) {
            // CRUCIAL: Always update the bot to sync movement and localizer
            bot.update();

            switch (currentState) {
                case START:
                    bot.followPath(toScore);
                    currentState = State.DRIVE_TO_SCORE;
                    break;

                case DRIVE_TO_SCORE:
                    if (!bot.isBusy()) {
                        stateTimer.reset();
                        currentState = State.SCORE_PIECE;
                    }
                    break;

                case SCORE_PIECE:
                    // Simulate scoring action (e.g. extending an arm)
                    if (stateTimer.seconds() > 1.0) {
                        bot.followPath(toIntake);
                        currentState = State.DRIVE_TO_INTAKE;
                    }
                    break;

                case DRIVE_TO_INTAKE:
                    if (!bot.isBusy()) {
                        bot.forward(); // Turn on intake
                        stateTimer.reset();
                        currentState = State.INTAKE_PIECE;
                    }
                    break;

                case INTAKE_PIECE:
                    // Intake for 2 seconds while stationary or slightly moving
                    if (stateTimer.seconds() > 2.0) {
                        bot.stop(); // Turn off intake
                        bot.followPath(toScore2);
                        currentState = State.DRIVE_TO_SCORE_2;
                    }
                    break;

                case DRIVE_TO_SCORE_2:
                    if (!bot.isBusy()) {
                        stateTimer.reset();
                        currentState = State.SCORE_PIECE_2;
                    }
                    break;

                case SCORE_PIECE_2:
                    if (stateTimer.seconds() > 1.0) {
                        bot.followPath(toPark);
                        currentState = State.PARK;
                    }
                    break;

                case PARK:
                    if (!bot.isBusy()) {
                        currentState = State.FINISHED;
                    }
                    break;
            }

            // Real-time telemetry feedback
            telemetry.addData("Current State", currentState);
            telemetry.addData("Pose", bot.follower.pose().toString());
            telemetry.addData("Path Busy", bot.isBusy());
            telemetry.update();
        }

        telemetry.addLine("Autonomous Routine Complete");
        telemetry.update();
    }
}
