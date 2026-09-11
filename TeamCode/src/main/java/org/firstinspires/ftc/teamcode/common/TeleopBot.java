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
        if (follower != null && follower.drivetrain != null) {
            double scaledForward = forward * powerScale;
            double scaledStrafe = strafe * powerScale;
            double scaledTurn = turn * powerScale;

            follower.drivetrain.drive(new DrivePowers(scaledForward, scaledStrafe, scaledTurn), true);
        }
    }

    public void forward() {
        if (intake != null) {
            intake.forward();
        }
    }

    public void reverse() {
        if (intake != null) {
            intake.reverse();
        }
    }

    public void stop() {
        if (intake != null) {
            intake.stop();
        }
    }

    public void resetHeading() {
        if (follower != null) {
            follower.setHeading(0);
        }
    }

    @Override
    public void update() {
        super.update();
    }
}
