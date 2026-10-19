package org.firstinspires.ftc.teamcode.opmode.auto;

import com.pedropathing.api.Paths;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.FieldConstants;

/**
 * SampleAuto shows off a full, 9-step Autonomous routine:
 * drive to score, score, drive to pick up a piece, pick it up, drive back
 * to score again, score again, then park. Use it as an example for writing
 * your own multi-step routine.
 */
@Autonomous(name = "Sample Pedro Auto", group = "Autonomous")
public class SampleAuto extends AutoOpMode {

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

    @Override
    public void runOpMode() throws InterruptedException {
        // Build the robot and tell it where it's starting
        initBot(FieldConstants.RED_LEFT_START);

        // Build every path we'll need up front, before the match starts
        Path toScore = AutoPaths.getScorePath(true);
        Path toIntake = Paths.line(FieldConstants.RED_SCORING_POSITION, FieldConstants.CIRCUIT_POINT_1);
        Path toScore2 = Paths.line(FieldConstants.CIRCUIT_POINT_1, FieldConstants.RED_SCORING_POSITION);
        Path toPark = Paths.line(FieldConstants.RED_SCORING_POSITION, FieldConstants.RED_LEFT_START);

        if (!awaitStart()) return;

        new AutoStateMachine<State>(this, bot, State.START, State.FINISHED) {
            @Override
            protected void handleState(State state) {
                switch (state) {
                    case START:
                        bot.followPath(toScore);
                        transition(State.DRIVE_TO_SCORE);
                        break;

                    case DRIVE_TO_SCORE:
                        if (!bot.isBusy()) {
                            transition(State.SCORE_PIECE);
                        }
                        break;

                    case SCORE_PIECE:
                        // Pretend to score for 1 second (this is where you'd run an arm/slide motor)
                        if (stateTimer.seconds() > 1.0) {
                            bot.followPath(toIntake);
                            transition(State.DRIVE_TO_INTAKE);
                        }
                        break;

                    case DRIVE_TO_INTAKE:
                        if (!bot.isBusy()) {
                            bot.forward(); // Turn the intake on
                            transition(State.INTAKE_PIECE);
                        }
                        break;

                    case INTAKE_PIECE:
                        // Let the intake run for 2 seconds to grab a game piece
                        if (stateTimer.seconds() > 2.0) {
                            bot.stop(); // Turn the intake back off
                            bot.followPath(toScore2);
                            transition(State.DRIVE_TO_SCORE_2);
                        }
                        break;

                    case DRIVE_TO_SCORE_2:
                        if (!bot.isBusy()) {
                            transition(State.SCORE_PIECE_2);
                        }
                        break;

                    case SCORE_PIECE_2:
                        if (stateTimer.seconds() > 1.0) {
                            bot.followPath(toPark);
                            transition(State.PARK);
                        }
                        break;

                    case PARK:
                        if (!bot.isBusy()) {
                            transition(State.FINISHED);
                        }
                        break;
                }
            }
        }.run();

        finish("Autonomous Routine Complete");
    }
}
