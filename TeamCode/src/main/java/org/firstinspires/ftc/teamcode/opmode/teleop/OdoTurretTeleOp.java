package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.common.OdoTurretBot;
import org.firstinspires.ftc.teamcode.common.components.PollenTurret;

/** Driver-controlled teleop that continuously aims the turret at the nearest alliance cell. */
@TeleOp(name = "Odo Turret TeleOp", group = "0 COMPETITION")
public class OdoTurretTeleOp extends OpMode {
    private static final double KICKER_FIRE_DURATION_SECONDS = 3.0;
    private PollenTurret.Alliance alliance = PollenTurret.Alliance.BLUE;
    private final ElapsedTime kickerTimer = new ElapsedTime();
    private OdoTurretBot bot;
    private boolean kickerFiring;

    @Override
    public void init() {
        telemetry.addLine("D-pad left: Blue alliance");
        telemetry.addLine("D-pad right: Red alliance");
        telemetry.addData("Selected alliance", alliance);
        telemetry.update();
    }

    @Override
    public void init_loop() {
        if (gamepad1.dpadLeftWasPressed()) {
            alliance = PollenTurret.Alliance.BLUE;
        } else if (gamepad1.dpadRightWasPressed()) {
            alliance = PollenTurret.Alliance.RED;
        }

        telemetry.addData("Selected alliance", alliance);
        telemetry.addLine("Press START to drive and enable turret aiming");
        telemetry.addLine("A: Fire kicker for 3 seconds");
        telemetry.update();
    }

    @Override
    public void start() {
        bot = new OdoTurretBot(this, telemetry, alliance);
        bot.startTeleopDrive();
    }

    @Override
    public void loop() {
        if (bot == null) {
            return;
        }

        double driveScale = gamepad1.left_bumper ? 0.3 : 1.0;
        bot.setDrivePowers(
                -gamepad1.left_stick_y * driveScale,
                -gamepad1.left_stick_x * driveScale,
                -gamepad1.right_stick_x * driveScale);
        bot.update();
        bot.aimTurretAtNearestCell();

        if (gamepad1.aWasPressed() && !kickerFiring) {
            kickerFiring = true;
            kickerTimer.reset();
            bot.runKickerForward();
        }
        if (kickerFiring && kickerTimer.seconds() >= KICKER_FIRE_DURATION_SECONDS) {
            bot.stopKicker();
            kickerFiring = false;
        }

        telemetry.addData("Alliance", alliance);
        telemetry.addData("Pose", bot.getPose());
        telemetry.addData("Nearest cell heading (degrees)", bot.getCellHeading());
        telemetry.addData("Turret angle (degrees)", bot.getTurretAngleDegrees());
        telemetry.addData("Kicker", kickerFiring ? "Firing" : "Off");
        telemetry.update();
    }

    @Override
    public void stop() {
        if (bot != null) {
            bot.stopKicker();
            bot.stopDrive();
        }
    }
}
