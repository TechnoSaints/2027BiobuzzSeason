package org.firstinspires.ftc.teamcode.opmode.auto.alliance_specific;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.components.PollenTurret;
import org.firstinspires.ftc.teamcode.opmode.auto.TwoTipAuto;

@Autonomous(name = "🟥Red 2 Tip Auto", group = "0 COMPETITION")
public class Red2TipAuto extends TwoTipAuto {
    public Red2TipAuto() {
        super(PollenTurret.Alliance.RED);
    }
}
