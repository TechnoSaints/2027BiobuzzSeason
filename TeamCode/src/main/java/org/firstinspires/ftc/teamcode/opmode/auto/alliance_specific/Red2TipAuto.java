package org.firstinspires.ftc.teamcode.opmode.auto.alliance_specific;

import org.firstinspires.ftc.teamcode.common.components.PollenTurret;
import org.firstinspires.ftc.teamcode.common.constants.Paths;
import org.firstinspires.ftc.teamcode.opmode.auto.AutoOpMode;

public class Red2TipAuto extends AutoOpMode {
    protected PollenTurret.Alliance alliance = PollenTurret.Alliance.RED;

    public Red2TipAuto() {
        super();
    }

    protected Red2TipAuto(PollenTurret.Alliance alliance) {
        super();
        this.alliance = alliance;
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
                    bot.setPollenTurretAngle(0.8);
                    bot.setPollenTurretVelocity(1700);
                    controlTimer.reset();
                    setPathState(4);
                }
                break;

            case 4:
                if (controlTimer.milliseconds() >= 500) {
                    bot.handleShooting(true, false);
                    controlTimer.reset();
                    setPathState(6);
                }
                break;

            case 6:
                if (controlTimer.milliseconds() >= 500) {
                    bot.handleShooting(false, true);
                    controlTimer.reset();
                    setPathState(8);
                }
                break;

            case 8:
                if (controlTimer.milliseconds() >= 500) {
                    bot.handleShooting(true, true);
                    controlTimer.reset();
                    setPathState(10);
                }
                break;

            case 10:
                if (controlTimer.milliseconds() > 1000) {
                    bot.handleShooting(false, false);
                    bot.followPath(Paths.cornerIntakeToSideFlowerSetup, 0.75, true);
                    controlTimer.reset();
                    setPathState(12);
                }
                break;

            case 12:
                if (!bot.isBusy()) {
                    bot.lowerFlowerServo();
                    bot.handleShooting(false, true);
                    controlTimer.reset();
                    setPathState(14);
                }
                break;

            case 14:
                if (!bot.isBusy()) {
                    bot.followPath(Paths.sideFlowerSetupToSideFlowerPickup, 0.25, true);
                    controlTimer.reset();
                    setPathState(16);
                }
                break;

            case 16:
                if (controlTimer.milliseconds() > 1000) {
                    bot.followPath(Paths.sideFlowerPickupToFarFlowerSetup, 0.75, true);
                    bot.handleShooting(false, false);
                    bot.raiseFlowerServo();
                    telemetry.addLine("Following far flower setup path");
                    controlTimer.reset();
                    setPathState(18);
                }
                break;

            case 18:
                if (!bot.isBusy()) {
                    bot.setPollenTurretVelocity(1600);
                    bot.setPollenTurretAngle(0.2);
                    bot.lowerFlowerServo();
                    controlTimer.reset();
                    setPathState(20);
                }
                break;

            case 20:
                if (!bot.isBusy()) {
                    bot.followPath(Paths.farFlowerSetupToFarFlowerPickup, .35, true);
                    telemetry.addLine("Following far flower pickup path");
                    controlTimer.reset();
                    setPathState(22);
                }
                break;

            case 22:
                if (!bot.isBusy()) {
                    bot.handleShooting(true, true);
                    controlTimer.reset();
                    setPathState(24);
                }
                break;

            case 24:
                if (controlTimer.milliseconds() > 2000) {
                    bot.handleShooting(false, false);
                    controlTimer.reset();
                    setPathState(26);
                }
                break;

            case 26:
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