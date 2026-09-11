package org.firstinspires.ftc.teamcode.common;

import com.pedropathing.drivetrain.DrivePowers;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TeleopBot extends Bot {

    public TeleopBot(OpMode opMode, Telemetry telemetry) {
        super(opMode, telemetry);
    }

    @Override
    public boolean isBusy() {
        return false;
    }

    public void setDrivePowers(double forward, double strafe, double turn, double powerScale) {
        double scaledForward = forward * powerScale;
        double scaledStrafe = strafe * powerScale;
        double scaledTurn = turn * powerScale;

        follower.drivetrain.drive(new DrivePowers(scaledForward, scaledStrafe, scaledTurn), true);
    }

    public void resetHeading() {
        follower.setHeading(0);
    }

    @Override
    public void update() {
        super.update();
    }
}
