package org.firstinspires.ftc.teamcode.opmode.test;

import com.pedropathing.api.Paths;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.common.AutoBot;

/**
 * IntakeAuto performs a specific sequence of movements and intake actions.
 * 1. Moves forward 3 feet, stops, and turns on the intake.
 * 2. Moves left 2 feet and turns off the intake.
 * 3. Moves backward 2 feet.
 * 4. Moves right 4 feet while the intake is running.
 * 5. Stops and waits 5 seconds before turning off the intake.
 */
@Autonomous(name = "Intake Sequence Auto", group = "Autonomous")
public class IntakeAuto extends LinearOpMode {
    private AutoBot bot;
    private ElapsedTime timer;

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

    private State currentState = State.START;

    @Override
    public void runOpMode() {
        bot = new AutoBot(this, telemetry);
        timer = new ElapsedTime();

        // Poses (in inches)
        Pose startPose = new Pose(0, 0, 0);
        Pose forwardPose = new Pose(36, 0, 0);
        Pose leftPose = new Pose(36, 24, 0);
        Pose backwardPose = new Pose(12, 24, 0);
        Pose rightPose = new Pose(12, -24, 0);

        bot.setPose(startPose);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive() && currentState != State.FINISH) {
            bot.update();

            switch (currentState) {
                case START:
                    currentState = State.MOVE_FORWARD;
                    break;

                case MOVE_FORWARD:
                    bot.followPath(Paths.line(startPose, forwardPose));
                    currentState = State.WAIT_FORWARD;
                    break;

                case WAIT_FORWARD:
                    if (!bot.isBusy()) {
                        bot.forward();
                        currentState = State.MOVE_LEFT;
                    }
                    break;

                case MOVE_LEFT:
                    bot.followPath(Paths.line(forwardPose, leftPose));
                    currentState = State.WAIT_LEFT;
                    break;

                case WAIT_LEFT:
                    if (!bot.isBusy()) {
                        bot.stop();
                        currentState = State.MOVE_BACKWARD;
                    }
                    break;

                case MOVE_BACKWARD:
                    bot.followPath(Paths.line(leftPose, backwardPose));
                    currentState = State.WAIT_BACKWARD;
                    break;

                case WAIT_BACKWARD:
                    if (!bot.isBusy()) {
                        bot.forward();
                        currentState = State.MOVE_RIGHT;
                    }
                    break;

                case MOVE_RIGHT:
                    bot.followPath(Paths.line(backwardPose, rightPose));
                    currentState = State.WAIT_RIGHT;
                    break;

                case WAIT_RIGHT:
                    if (!bot.isBusy()) {
                        timer.reset();
                        currentState = State.WAIT_FINAL;
                    }
                    break;

                case WAIT_FINAL:
                    if (timer.seconds() >= 5.0) {
                        bot.stop();
                        currentState = State.FINISH;
                    }
                    break;
            }

            telemetry.addData("State", currentState);
            telemetry.addData("Pose", bot.follower.pose().toString());
            if (currentState == State.WAIT_FINAL) {
                telemetry.addData("Wait", "%.1f / 5.0s", timer.seconds());
            }
            telemetry.update();
        }

        telemetry.addLine("Autonomous Complete");
        telemetry.update();
    }
}
