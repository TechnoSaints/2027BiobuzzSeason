package org.firstinspires.ftc.teamcode.opmode.auto;

import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.components.PollenTurret;
import org.firstinspires.ftc.teamcode.common.constants.FieldConstants;
import org.firstinspires.ftc.teamcode.common.constants.Paths;

public class TwoTipAuto extends AutoOpMode {
    protected PollenTurret.Alliance alliance;

    public TwoTipAuto() {
        super();
    }

    protected TwoTipAuto(PollenTurret.Alliance alliance) {
        super();
    }
    @Override
    protected void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                controlTimer.reset();
                setPathState(2);
                break;

            case 2:
                if (controlTimer.milliseconds() >= 500) {
                    bot.followPath(Paths.startToCornerIntake, 0.75, true);
                    controlTimer.reset();
                    setPathState(4);
                }
                break;

            case 4:
                if (!bot.isBusy()) {
                    bot.followPath(Paths.cornerIntakeToSideFlowerSetup, 0.5, true);
                    controlTimer.reset();
                    setPathState(6);
                }
                break;

            case 6:
                if (controlTimer.milliseconds() > 2500) {
                    bot.followPath(Paths.sideFlowerSetupToSideFlowerPickup, 0.75, true);
                    setPathState(8);
                }
                break;

            case 8:
                if (!bot.isBusy()) {
                    bot.followPath(Paths.sideFlowerPickupToFarFlowerSetup, 0.75, true);
                    telemetry.addLine("Following side flower pickup path");
                    controlTimer.reset();
                    setPathState(10);
                }
                break;

            case 10:
                if (!bot.isBusy()) {
                    bot.followPath(Paths.farFlowerSetupToFarFlowerPickup, .35, true);
                    telemetry.addLine("Following far flower setup path");
                    controlTimer.reset();
                    setPathState(12);
                }
                break;

            case 12:
                if (!bot.isBusy()) {
                    bot.followPath(Paths.farFlowerPickupToTeamSetup, .5, true);
                    telemetry.addLine("Following to team setup");
                    controlTimer.reset();
                    setPathState(99);
                }
                break;

            case 99:
                if (!bot.isBusy() && (controlTimer.milliseconds() > 100000)) {
                    requestOpModeStop();
                }
                break;
        }
    }
}