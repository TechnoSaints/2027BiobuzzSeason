package org.firstinspires.ftc.teamcode.opmode.test;

import com.pedropathing.api.Paths;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.FieldConstants;
import org.firstinspires.ftc.teamcode.opmode.auto.AutoOpMode;
import org.firstinspires.ftc.teamcode.opmode.auto.AutoStateMachine;

/**
 * IntakeAuto tests driving and the intake together in a simple box pattern:
 * 1. Drive forward 3 feet, then turn the intake on.
 * 2. Drive left 2 feet, then turn the intake off.
 * 3. Drive backward 2 feet.
 * 4. Drive right 4 feet while the intake runs.
 * 5. Stop, wait 5 seconds, then turn the intake off.
 */
@Autonomous(name = "Intake Sequence Auto", group = "Autonomous")
public class IntakeAuto extends AutoOpMode {

    private enum State {
        START,
        MOVE_FORWARD,
        WAIT_FORWARD,
        MOVE_LEFT,
        WAIT_LEFT,
        MOVE_BACKWARD,
        WAIT_BACKWARD,
        MOVE_RIGHT,
        WAIT_RIGHT,
        WAIT_FINAL,
        FINISH
    }

    @Override
    public void runOpMode() {
        initBot(FieldConstants.INTAKE_TEST_START);

        if (!awaitStart()) return;

        new AutoStateMachine<State>(this, bot, State.START, State.FINISH) {
            @Override
            protected void handleState(State state) {
                switch (state) {
                    case START:
                        transition(State.MOVE_FORWARD);
                        break;

                    case MOVE_FORWARD:
                        bot.followPath(Paths.line(FieldConstants.INTAKE_TEST_START, FieldConstants.INTAKE_TEST_FORWARD));
                        transition(State.WAIT_FORWARD);
                        break;

                    case WAIT_FORWARD:
                        if (!bot.isBusy()) {
                            bot.forward();
                            transition(State.MOVE_LEFT);
                        }
                        break;

                    case MOVE_LEFT:
                        bot.followPath(Paths.line(FieldConstants.INTAKE_TEST_FORWARD, FieldConstants.INTAKE_TEST_LEFT));
                        transition(State.WAIT_LEFT);
                        break;

                    case WAIT_LEFT:
                        if (!bot.isBusy()) {
                            bot.stop();
                            transition(State.MOVE_BACKWARD);
                        }
                        break;

                    case MOVE_BACKWARD:
                        bot.followPath(Paths.line(FieldConstants.INTAKE_TEST_LEFT, FieldConstants.INTAKE_TEST_BACKWARD));
                        transition(State.WAIT_BACKWARD);
                        break;

                    case WAIT_BACKWARD:
                        if (!bot.isBusy()) {
                            bot.forward();
                            transition(State.MOVE_RIGHT);
                        }
                        break;

                    case MOVE_RIGHT:
                        bot.followPath(Paths.line(FieldConstants.INTAKE_TEST_BACKWARD, FieldConstants.INTAKE_TEST_RIGHT));
                        transition(State.WAIT_RIGHT);
                        break;

                    case WAIT_RIGHT:
                        if (!bot.isBusy()) {
                            transition(State.WAIT_FINAL);
                        }
                        break;

                    case WAIT_FINAL:
                        if (stateTimer.seconds() >= 5.0) {
                            bot.stop();
                            transition(State.FINISH);
                        }
                        break;
                }
            }
        }.run();

        finish("Autonomous Complete");
    }
}
