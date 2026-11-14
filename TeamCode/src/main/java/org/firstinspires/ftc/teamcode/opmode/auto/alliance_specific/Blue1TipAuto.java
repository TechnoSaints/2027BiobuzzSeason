package org.firstinspires.ftc.teamcode.opmode.auto.alliance_specific;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.components.PollenTurret;
import org.firstinspires.ftc.teamcode.common.constants.Paths;
import org.firstinspires.ftc.teamcode.common.constants.RobotConstants;
import org.firstinspires.ftc.teamcode.opmode.auto.AutoOpMode;

@Autonomous(name = "Blue 1 tip auto", group = "0 COMPETITION")
public class Blue1TipAuto extends AutoOpMode {
    protected PollenTurret.Alliance alliance;

    public Blue1TipAuto() {
        super();
    }

    protected Blue1TipAuto(PollenTurret.Alliance alliance) {
        super();
    }
    @Override
    protected void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                controlTimer.reset();
                bot.setPollenTurretAngle(RobotConstants.PollenTurretConstants.TurretPositions.NEAR_CORNER_ANGLE);
                bot.setPollenTurretVelocity(RobotConstants.PollenTurretConstants.TurretPositions.NEAR_CORNER_SPEED);
                bot.setPollenTurretRotation(0.27);
                bot.handleShooting(true, true);
                bot.intakeOn();
                setPathState(2);
                break;

            case 2:
                if (controlTimer.milliseconds() >= 250) {
                    bot.followPath(Paths.startToCornerIntake, 0.75, true);
                    controlTimer.reset();
                    setPathState(4);
                }
                break;

            case 4:
                if (controlTimer.milliseconds() >= 1000) {
                    bot.openGate();
                    bot.handleShooting(true, true);
                    controlTimer.reset();
                    setPathState(6);
                }
                break;

            case 6:
                if (controlTimer.milliseconds() > 5000) {
                    bot.intakeOn();
                    bot.handleShooting(true, true);
                    controlTimer.reset();
                    setPathState(8);
                }
                break;

            case 8:
                if (controlTimer.milliseconds() > 2000){
                    bot.followPath(Paths.cornerIntakeToTeamSetup, 1);
                    setPathState(99);
                }
                break;

            case 99:
                if(!bot.isBusy()) {
                    requestOpModeStop();
                }
                break;

        }
    }
}