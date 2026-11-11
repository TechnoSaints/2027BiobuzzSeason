package org.firstinspires.ftc.teamcode.opmode.auto;

import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.components.PollenTurret;
import org.firstinspires.ftc.teamcode.common.constants.FieldConstants;
import org.firstinspires.ftc.teamcode.common.constants.Paths;

@Autonomous(name = "TwoTipAuto")
public class TwoTipAuto extends AutoOpMode {
    protected PollenTurret.Alliance alliance;

    @Override
    public void runOpMode() {
        initBot(FieldConstants.startPose, alliance);
        if (!awaitStart()) {
            return;
        }

        new AutoStateMachine(this, bot, 0, 99) {

            @Override
            protected void handleState(int state) {
                telemetry.addData("State ID", state);
                switch (state) {
                    case 0:
                        bot.start();
                        followPath(Paths.startToCornerIntake, 2, 0.25);
                        bot.setPollenTurretVelocity(1780);
                        break;
                    case 2:
                        if (!bot.isBusy()){
                            // shoot command
                            stateTimer.reset();
                            state = 4;
                        }
                        break;
                    case 4:
                        if (stateTimer.milliseconds() > 500){
                            bot.intakeOn();
                            stateTimer.reset();
                            state = 8;
                        }
                    case 8:
                        if (stateTimer.milliseconds() > 500){
                            bot.intakeOff();
                            followPath(Paths.cornerIntakeToSideFlowerSetup, 10, 0.7, false);
                            state = 10;
                        }
                        break;
                    case 10:
                        bot.lowerFlowerServo();
                        bot.intakeOn();
                        state = 12;
                        break;
                    case 12:
                        followPath(Paths.sideFlowerSetupToSideFlowerPickup, 16, 0.25);
                        stateTimer.reset();
                        break;
                    case 16:
                        if (stateTimer.milliseconds() > 500) {
                            followPath(Paths.sideFlowerPickupToFarFlowerSetup, 20, 0.7, false);
                            stateTimer.reset();
                            state = 18;
                        }
                        break;
                    case 18:
                        if (stateTimer.milliseconds() > 500) {
                            bot.raiseFlowerServo();
                            bot.intakeOff();
                            stateTimer.reset();
                            state = 19;
                        }
                        break;
                    case 19:
                        followPath(Paths.sideFlowerPickupToFarFlowerSetup, 20, 0.7, false);
                        bot.setPollenTurretVelocity(1640);
                        break;
                    case 20:
                        if (!bot.isBusy()) {
                            // shoot command
                            stateTimer.reset();
                            state = 22;
                        }
                    case 22:
                        if (stateTimer.milliseconds() > 500) {
                            // keep shooting
                            bot.lowerFlowerServo();
                            bot.intakeOn();
                            followPath(Paths.farFlowerSetupToFarFlowerPickup, 24, 0.25);
                            stateTimer.reset();
                        }
                        break;
                    case 24:
                        if (stateTimer.milliseconds() > 1000) {
                            // stop shooting
                            followPath(Paths.farFlowerPickupToTeamSetup, 30, 0.7, false);
                            bot.intakeOff();
                            bot.setPollenTurretVelocity(0);
                            stateTimer.reset();
                            state = 26;
                        }
                        break;
                    case 26:
                        bot.raiseFlowerServo();
                        state = 28;
                        break;
                    case 28:
                        followPath(Paths.farFlowerPickupToTeamSetup, 30, 0.7, false);
                        break;
                    case 30:
                        bot.intakeOn();
                        followPath(Paths.teamSetupToTeamPickup, 34, 0.25);
                        break;
                    case 34:
                        if (stateTimer.milliseconds() > 500) {
                            followPath(Paths.teamPickupToTeamShoot, 36, 0.7);
                            bot.intakeOff();
                            bot.setPollenTurretVelocity(1780);
                        }
                        break;
                    case 36:
                        if (!bot.isBusy()) {
                            // shoot
                            stateTimer.reset();
                            state = 40;
                        }
                    case 40:
                        if (stateTimer.milliseconds() > 500) {
                            followPath(Paths.teamShootToParkStrafe, 44, 0.7, false);
                            bot.setPollenTurretVelocity(0);
                        }
                        break;
                    case 44:
                        followPath(Paths.parkStrafeToParkSetup, 48, 0.7, false);
                        break;
                    case 48:
                        followPath(Paths.parkSetupToPark, 99, 1);
                        break;
                }
            }
        }.run();

        finish("TwoTipAuto complete.");
    }
}
