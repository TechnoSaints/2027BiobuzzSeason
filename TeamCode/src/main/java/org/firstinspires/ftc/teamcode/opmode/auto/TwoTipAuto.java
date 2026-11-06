package org.firstinspires.ftc.teamcode.opmode.auto;

import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.components.PollenTurret;
import org.firstinspires.ftc.teamcode.common.constants.FieldConstants;
import org.firstinspires.ftc.teamcode.common.constants.Paths;

@Autonomous(name = "TwoTipAuto")
public class TwoTipAuto extends AutoOpMode {
    protected PollenTurret.Alliance alliance = PollenTurret.Alliance.RED;

    private enum State {
        START_TO_CORNER(0),
        CORNER_TO_SIDE_FLOWER(4),
        SIDE_FLOWER_TO_FAR_FLOWER(8),
        FAR_FLOWER_TO_PUSH_START(12),
        PUSH_START_TO_END(16),
        FINISHED(20);

        private final int id;

        State(int id) {
            this.id = id;
        }
    }

    @Override
    public void runOpMode() {
        initBot(FieldConstants.startPose, alliance);
        if (bot.follower == null) {
            telemetry.addLine("TwoTipAuto cannot run: follower failed to initialize.");
            telemetry.update();
            return;
        }

        Paths.buildPaths(bot.follower);
        if (!awaitStart()) {
            return;
        }

        new AutoStateMachine<State>(this, bot, State.START_TO_CORNER, State.FINISHED) {
            private boolean pathStarted;

            @Override
            protected void handleState(State state) {
                telemetry.addData("State ID", state.id);
                switch (state) {
                    case START_TO_CORNER:
                        followPath(Paths.startToCorner, State.CORNER_TO_SIDE_FLOWER);
                        break;
                    case CORNER_TO_SIDE_FLOWER:
                        followPath(Paths.cornerToSideFlower, State.SIDE_FLOWER_TO_FAR_FLOWER);
                        break;
                    case SIDE_FLOWER_TO_FAR_FLOWER:
                        followPath(Paths.sideFlowerToFarFlower, State.FAR_FLOWER_TO_PUSH_START);
                        break;
                    case FAR_FLOWER_TO_PUSH_START:
                        followPath(Paths.farFlowerToPushStart, State.PUSH_START_TO_END);
                        break;
                    case PUSH_START_TO_END:
                        followPath(Paths.pushStartToEnd, State.FINISHED);
                        break;
                    case FINISHED:
                        break;
                }
            }

            private void followPath(PathChain path, State nextState) {
                if (!pathStarted) {
                    bot.follower.followPath(path);
                    pathStarted = true;
                }
                if (!bot.isBusy()) {
                    pathStarted = false;
                    transition(nextState);
                }
            }
        }.run();

        finish("TwoTipAuto complete.");
    }
}
