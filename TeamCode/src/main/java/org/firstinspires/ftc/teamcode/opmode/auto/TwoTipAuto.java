package org.firstinspires.ftc.teamcode.opmode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.components.PollenTurret;

@Autonomous(name = "🟥Short Right 3 Ball Avoid", group = "Red Auto")
public class TwoTipAuto extends AutoOpMode {
    public TwoTipAuto() {
        super();
    }

    protected TwoTipAuto(PollenTurret.Alliance alliance) {
        super();
    }

    @Override
    protected void autonomousPathUpdate() {
        if (gameTimer.milliseconds() > 1000) {
            requestOpModeStop();
        }
    }
}
