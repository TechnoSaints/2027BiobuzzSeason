package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * AutoStateMachine factors out the "enum + timer + while loop" shell that
 * multi-step autonomous routines all need, so an OpMode only has to write
 * the switch statement for its own states.
 *
 * Usage: build one of these as an anonymous subclass inside runOpMode(),
 * implementing handleState() with a switch over your own State enum, then
 * call run(). Call transition(nextState) whenever you want to move on —
 * it also resets stateTimer so you can time how long you've been in a state.
 *
 * @param <S> the enum type listing this routine's states. Include a
 *            dedicated "finished" state and pass it to the constructor.
 */
public abstract class AutoStateMachine<S extends Enum<S>> {
    protected final LinearOpMode opMode;
    protected final AutoBot bot;
    protected final Telemetry telemetry;
    protected final ElapsedTime stateTimer = new ElapsedTime();
    protected S state;
    private final S finishedState;

    protected AutoStateMachine(LinearOpMode opMode, AutoBot bot, S startState, S finishedState) {
        this.opMode = opMode;
        this.bot = bot;
        this.telemetry = opMode.telemetry;
        this.state = startState;
        this.finishedState = finishedState;
    }

    /** Runs the logic for the current state. Call transition(...) to move to the next state. */
    protected abstract void handleState(S state);

    /** Moves to a new state and resets the state timer. */
    protected void transition(S newState) {
        state = newState;
        stateTimer.reset();
    }

    /** Runs the state machine to completion, updating the bot and telemetry every loop. */
    public void run() {
        while (opMode.opModeIsActive() && state != finishedState) {
            bot.update();
            handleState(state);
            telemetry.addData("State", state);
            telemetry.addData("Pose", bot.getPoseString());
            telemetry.update();
        }
    }
}
