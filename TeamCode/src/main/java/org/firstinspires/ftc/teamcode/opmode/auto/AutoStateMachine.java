package org.firstinspires.ftc.teamcode.opmode.auto;

import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.AutoBot;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

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
 * Create one of these as an anonymous subclass inside runOpMode() and fill in
 * handleState() with the actions for each numeric state. Call transition(nextStep)
 * to move on to the next step. Finally, call run() to start it going.
 */
public abstract class AutoStateMachine {
    protected final LinearOpMode opMode;
    protected final AutoBot bot;
    protected final Telemetry telemetry;
    protected final ElapsedTime stateTimer = new ElapsedTime();
    protected int state;
    private final double finishedState;
    protected boolean pathStarted;

    protected AutoStateMachine(LinearOpMode opMode, AutoBot bot, int startState, int finishedState) {
        this.opMode = opMode;
        this.bot = bot;
        this.telemetry = opMode.telemetry;
        this.state = startState;
        this.finishedState = finishedState;
    }

    /** Fill this in with the actions for the current numeric state. */
    protected abstract void handleState(int state);

    /** Moves on to the next step and resets the step timer back to zero. */
    protected void transition(int newState) {
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
            opMode.idle();
        }
    }

    protected void followPath(PathChain path, int nextState, double maxPower) {
        if (!pathStarted) {
            bot.followPath(path, maxPower, true);
            pathStarted = true;
        }
        if (!bot.isBusy()) {
            pathStarted = false;
            transition(nextState);
        }
    }

    protected void followPath(PathChain path, int nextState, double maxPower, boolean holdEnd) {
        if (!pathStarted) {
            bot.followPath(path, maxPower, holdEnd);
            pathStarted = true;
        }
        if (!bot.isBusy()) {
            pathStarted = false;
            transition(nextState);
        }
    }
}
