package org.firstinspires.ftc.teamcode.opmode.auto.alliance_specific;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.components.PollenTurret;
import org.firstinspires.ftc.teamcode.common.constants.Paths;
import org.firstinspires.ftc.teamcode.common.constants.RobotConstants;
import org.firstinspires.ftc.teamcode.opmode.auto.AutoOpMode;

@Autonomous(name = "RED 1 tip auto", group = "0 COMPETITION")
public class Red1TipAuto extends AutoOpMode {
    protected PollenTurret.Alliance alliance;

    public Red1TipAuto() {
        super();
    }

    protected Red1TipAuto(PollenTurret.Alliance alliance) {
        super();
    }
    @Override
    protected void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                controlTimer.reset();
                bot.setPollenTurretAngle(RobotConstants.PollenTurretConstants.TurretPositions.FAR_CORNER_ANGLE);
                bot.setPollenTurretVelocity(RobotConstants.PollenTurretConstants.TurretPositions.FAR_CORNER_SPEED);
                bot.setPollenTurretRotation(0.28);
                bot.handleShooting(true, true);
                setPathState(2);
                break;

            case 2:
                if (controlTimer.milliseconds() >= 250) {
                    bot.followPath(Paths.startToCornerIntake, 0.75, true);
                    controlTimer.reset();
                    bot.openGate();
                    bot.handleShooting(true, true);
                    setPathState(4);
                }
                break;

            case 4:
                if (controlTimer.milliseconds() >= 1000) {
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
                if (controlTimer.milliseconds() > 5000){
                    bot.followPath(Paths.cornerIntakeToTeamSetup, 1);
                    setPathState(99);
                }
                break;

            case 99:
                requestOpModeStop();
                break;

        }
    }
}