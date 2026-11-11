package org.firstinspires.ftc.teamcode.opmode.teleop.alliance_specific;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.components.PollenTurret;
import org.firstinspires.ftc.teamcode.opmode.teleop.TeleOpModeNoPedro;

@TeleOp(name = "Blue TeleOp No Pedro", group = "0 COMPETITION")
public class BlueTeleOpNoPedro extends TeleOpModeNoPedro {
    @Override
    public void init(){
        alliance = PollenTurret.Alliance.BLUE;
        super.init();
    }
}
