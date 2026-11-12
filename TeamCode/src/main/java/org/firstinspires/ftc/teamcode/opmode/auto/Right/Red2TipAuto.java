package org.firstinspires.ftc.teamcode.opmode.auto.Right;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.constants.Paths;
import org.firstinspires.ftc.teamcode.opmode.auto.TwoTipAuto;

@Autonomous(name = "🟥Red Tip 2 Auto", group = "Red Auto")
public class Red2TipAuto extends TwoTipAuto {
    @Override
    protected void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                controlTimer.reset();
                setPathState(2);
                break;

            case 2:
                if (controlTimer.milliseconds() >= 500) {
                    bot.followPath(Paths.startToShot, 0.75, true);
                    controlTimer.reset();
                    setPathState(4);
                }
                break;

            case 4:
                if (!bot.isBusy()) {
                    controlTimer.reset();
                    setPathState(6);
                }
                break;

            case 6:
                if (controlTimer.milliseconds() > 1000) {
                    setPathState(8);
                }
                break;

            case 8:
                if (!bot.isBusy()) {
                    bot.followPath(Paths.shotToShortAvoid, 0.75, true);
                    controlTimer.reset();
                    setPathState(99);
                }
                break;

            case 99:
                if (!bot.isBusy() && (controlTimer.milliseconds() > 0)) {
                    requestOpModeStop();
                }
                break;
        }
    }
}
