package org.firstinspires.ftc.teamcode.opmode.auto;

import com.pedropathing.api.Paths;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.common.FieldConstants;

/**
 * FiveWaypointAuto is a sample autonomous routine that drives the robot through
 * 5 different field coordinates in sequence, while independently flipping the
 * intake on and off every 5-10 seconds (a random interval each time) regardless
 * of which leg of the drive it's currently on.
 *
 * This is meant as a template for combining "drive somewhere" state machine
 * logic with a subsystem action that runs on its own timer.
 */
@Autonomous(name = "Five Waypoint Auto", group = "Autonomous")
public class FiveWaypointAuto extends AutoOpMode {

    // The 5 coordinates the robot will visit, in order. The first one is the
    // starting pose; the robot drives to each of the other four in turn.
    private static final Pose[] WAYPOINTS = {
            FieldConstants.RED_LEFT_START,
            FieldConstants.RED_CENTER_WAYPOINT,
            FieldConstants.RED_SCORING_POSITION,
            FieldConstants.RED_INTAKE_POSITION,
            FieldConstants.CIRCUIT_POINT_1
    };

    private enum State {
        MOVE_TO_2, WAIT_2,
        MOVE_TO_3, WAIT_3,
        MOVE_TO_4, WAIT_4,
        MOVE_TO_5, WAIT_5,
        FINISHED
    }

    // Tracks the intake's on/off timer independently of the drive state machine.
    private final ElapsedTime intakeTimer = new ElapsedTime();
    private double nextToggleSeconds = randomToggleInterval();
    private boolean intakeOn = false;

    /** Picks a random interval between 5 and 10 seconds for the next intake toggle. */
    private static double randomToggleInterval() {
        return 5.0 + Math.random() * 5.0;
    }

    @Override
    public void runOpMode() {
        initBot(WAYPOINTS[0]);

        if (!awaitStart()) return;

        intakeTimer.reset();

        new AutoStateMachine<State>(this, bot, State.MOVE_TO_2, State.FINISHED) {
            @Override
            protected void handleState(State state) {
                // Flip the intake on/off on its own schedule, independent of movement.
                if (intakeTimer.seconds() >= nextToggleSeconds) {
                    intakeOn = !intakeOn;
                    if (intakeOn) {
                        bot.forward();
                    } else {
                        bot.stop();
                    }
                    intakeTimer.reset();
                    nextToggleSeconds = randomToggleInterval();
                }
                telemetry.addData("Intake", intakeOn ? "ON" : "OFF");

                switch (state) {
                    case MOVE_TO_2:
                        bot.followPath(Paths.line(WAYPOINTS[0], WAYPOINTS[1]));
                        transition(State.WAIT_2);
                        break;
                    case WAIT_2:
                        if (!bot.isBusy()) transition(State.MOVE_TO_3);
                        break;

                    case MOVE_TO_3:
                        bot.followPath(Paths.line(WAYPOINTS[1], WAYPOINTS[2]));
                        transition(State.WAIT_3);
                        break;
                    case WAIT_3:
                        if (!bot.isBusy()) transition(State.MOVE_TO_4);
                        break;

                    case MOVE_TO_4:
                        bot.followPath(Paths.line(WAYPOINTS[2], WAYPOINTS[3]));
                        transition(State.WAIT_4);
                        break;
                    case WAIT_4:
                        if (!bot.isBusy()) transition(State.MOVE_TO_5);
                        break;

                    case MOVE_TO_5:
                        bot.followPath(Paths.line(WAYPOINTS[3], WAYPOINTS[4]));
                        transition(State.WAIT_5);
                        break;
                    case WAIT_5:
                        if (!bot.isBusy()) transition(State.FINISHED);
                        break;
                }
            }
        }.run();

        bot.stop();
        finish("Five Waypoint Auto Complete");
    }
}
