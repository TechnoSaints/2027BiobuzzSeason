package org.firstinspires.ftc.teamcode.opmode.auto;

import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.components.PollenTurret;
import org.firstinspires.ftc.teamcode.common.constants.FieldConstants;
import org.firstinspires.ftc.teamcode.common.constants.Paths;

@Autonomous(name = "TwoTipAuto")
public class TwoTipAuto extends AutoOpMode {
    protected PollenTurret.Alliance alliance = PollenTurret.Alliance.RED;

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

        new AutoStateMachine(this, bot, 0, 12) {

            @Override
            protected void handleState(int state) {
                telemetry.addData("State ID", state);
                switch (state) {
                    case 0:
                        bot.start();
                        followPath(Paths.startToCornerIntake, 8, 0.25);
                        break;
                    case 8:
                        followPath(Paths.cornerIntakeToSideFlowerSetup, 10);
                        break;
                    case 10:
                        bot.lowerFlowerServo();
                        state = 12;
                        break;
//                    case 12:
//                        followPath(Paths.sideFlowerSetupToSideFlowerPickup, 16);
//                        break;
//                    case 16:
//                        followPath(Paths.sideFlowerPickupToFarFlowerSetup, 20);
//                        break;
//                    case 20:
//                        followPath(Paths.farFlowerSetupToFarFlowerPickup, 24);
//                        break;
//                    case 24:
//                        followPath(Paths.farFlowerPickupToFarFlowerShoot, 28);
//                        break;
//                    case 28:
//                        followPath(Paths.farFlowerShootToTeamSetup, 32);
//                        break;
//                    case 32:
//                        followPath(Paths.teamSetupToTeamPickup, 36);
//                        break;
//                    case 36:
//                        followPath(Paths.teamPickupToTeamShoot, 40);
//                        break;
//                    case 40:
//                        followPath(Paths.teamShootToParkStrafe, 44);
//                        break;
//                    case 44:
//                        followPath(Paths.parkStrafeToParkSetup, 48);
//                        break;
//                    case 48:
//                        followPath(Paths.parkSetupToPark, 99);
//                        break;
                }
            }
        }.run();

        finish("TwoTipAuto complete.");
    }
}
