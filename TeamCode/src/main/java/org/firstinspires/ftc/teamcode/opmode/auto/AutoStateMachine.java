package org.firstinspires.ftc.teamcode.opmode.auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.AutoBot;

/**
 * AutoStateMachine runs a multi-step Autonomous routine, like a to-do list
 * the robot works through one step ("state") at a time - drive somewhere,
 * wait for it to arrive, turn on the intake, drive somewhere else, and so on.
 *
 * Every routine like this needs the same 3 things: a variable to remember
 * which step it's on, a timer to know how long it's been on that step, and
 * a loop that keeps checking "are we done yet?". This class handles all of
 * that, so a new Autonomous OpMode only has to describe what happens on
 * each step.
 *
 * How to use it: make an enum listing your steps (including a "FINISHED"
 * step), then create one of these as an anonymous subclass inside
 * runOpMode() and fill in handleState() with a switch statement - one case
 * per step. Call transition(nextStep) to move on to the next step (this
 * also resets the timer). Finally, call run() to start it going.
 *
 * @param <S> your enum of steps for this specific routine.
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

    /** Fill this in with a switch statement describing what to do on each step. */
    protected abstract void handleState(S state);

    /** Moves on to the next step and resets the step timer back to zero. */
    protected void transition(S newState) {
        state = newState;
        stateTimer.reset();
    }

    /** Runs every step in order until the routine reaches its finished step. */
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
