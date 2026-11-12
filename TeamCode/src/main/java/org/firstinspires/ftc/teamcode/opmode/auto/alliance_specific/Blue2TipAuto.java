package org.firstinspires.ftc.teamcode.opmode.auto.alliance_specific;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.components.PollenTurret;
import org.firstinspires.ftc.teamcode.opmode.auto.TwoTipAuto;

@Autonomous(name = "Blue 2 Tip Auto", group = "0 COMPETITION")
public class Blue2TipAuto extends TwoTipAuto {
    public Blue2TipAuto() {
        alliance = PollenTurret.Alliance.BLUE;
    }
}
