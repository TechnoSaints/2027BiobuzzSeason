package org.firstinspires.ftc.teamcode.opmode.teleop.alliance_specific;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.components.PollenTurret;
import org.firstinspires.ftc.teamcode.opmode.teleop.TeleOpModeNoPedro;

@TeleOp(name = "Red TeleOp No Pedro", group = "0 COMPETITION")
public class RedTeleOpNoPedro extends TeleOpModeNoPedro {
    @Override
    public void init(){
        alliance = PollenTurret.Alliance.RED;
        super.init();
    }
}
