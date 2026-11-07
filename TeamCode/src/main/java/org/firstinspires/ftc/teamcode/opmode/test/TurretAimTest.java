package org.firstinspires.ftc.teamcode.opmode.test;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;

import org.firstinspires.ftc.teamcode.common.components.PollenTurret;

@TeleOp(name = "Turret Aim Test", group = "Test")
public class TurretAimTest extends OpMode {
    private PollenTurret turret;
    private AnalogInput encoder;

    // P/D tuning
    double[] stepSizes = {0.1, 0.01, 0.001, 0.0001, 0.00001, 0.00001};
    // index sets stepsize
    int stepIndex = 2;

    @Override
    public void init() {
        turret = new PollenTurret(hardwareMap, telemetry, PollenTurret.Alliance.RED);
        encoder = hardwareMap.get(AnalogInput.class, "pollenTurretRotateEncoder");

        telemetry.addLine("Initialized");
        telemetry.update();
    }

    @Override
    public void start() {
        turret.startLimelight();
    }

    @Override
    public void loop() {
        LLResult result = turret.getLatestLimelightResult();
        turret.update(result);

        // p/d tuning controls
        // step size control
        if (gamepad1.rightBumperWasPressed()) {
            stepIndex = (stepIndex + 1) % stepSizes.length;
        }
        else if (gamepad1.leftBumperWasPressed()) {
            stepIndex = (stepIndex - 1) % stepSizes.length;
        }

        if (gamepad1.dpadUpWasPressed()) {
            turret.setkP(turret.getkP() + stepSizes[stepIndex]);
        }
        else if (gamepad1.dpadDownWasPressed()) {
            turret.setkP(turret.getkP() - stepSizes[stepIndex]);
        }
        if (gamepad1.dpadRightWasPressed()) {
            turret.setkD(turret.getkD() + stepSizes[stepIndex]);
        }
        else if (gamepad1.dpadLeftWasPressed()) {
            turret.setkD(turret.getkD() - stepSizes[stepIndex]);
        }

        // only used for telemetry
        LLResultTypes.FiducialResult targetTag = turret.getClosestAllianceTag(result);
        Double angleOffset = targetTag == null ? null : targetTag.getTargetXDegrees();

        telemetry.addLine("-------------AprilTag------------");
        telemetry.addData("Alliance", PollenTurret.Alliance.RED);
        telemetry.addData("Target being aimed at",
                targetTag == null ? "None" : "AprilTag " + targetTag.getFiducialId());
        telemetry.addData("AprilTag angle offset",
                angleOffset == null ? "No alliance tag detected" : "%.1f deg", angleOffset);
        telemetry.addLine("------------P/D Tuning-----------");
        telemetry.addData("kP", turret.getkP());
        telemetry.addData("kD", turret.getkD());
        telemetry.addData("Current Step Size", stepSizes[stepIndex]);
        telemetry.addData("Step Sizes", stepSizes);
        telemetry.addData("Encoder RAW", encoder.getVoltage());
        telemetry.addData("Encoder Degrees", encoder.getVoltage() * 360.0 / 3.23);
        telemetry.addData("Encoder Radians", encoder.getVoltage() * 2 / 3.23 + "pi");
        telemetry.update();
    }

    @Override
    public void stop() {
        turret.stop();
    }
}
