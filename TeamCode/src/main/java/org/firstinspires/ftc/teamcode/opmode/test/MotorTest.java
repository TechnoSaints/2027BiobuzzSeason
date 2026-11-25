package org.firstinspires.ftc.teamcode.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@TeleOp(name = "Motor Test", group = "Test")
public class MotorTest extends LinearOpMode {
    private static final double POWER_STEP = 0.05;

    @Override
    public void runOpMode() {
        List<DcMotor> motors = new ArrayList<>();
        List<String> motorNames = new ArrayList<>();

        for (Map.Entry<String, DcMotor> entry : hardwareMap.dcMotor.entrySet()) {
            motorNames.add(entry.getKey());
            motors.add(entry.getValue());
            entry.getValue().setPower(0.0);
        }

        if (motors.isEmpty()) {
            telemetry.addLine("No motors found in hardware map.");
            telemetry.update();
            waitForStart();
            return;
        }

        telemetry.addLine("Motor Test initialized");
        telemetry.addLine("D-pad Up/Down: select motor");
        telemetry.addLine("A/B: increase/decrease power by 0.05");
        telemetry.addLine("X: toggle forward/reverse | Y: stop selected motor");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) {
            return;
        }

        int selectedIndex = 0;
        double powerMagnitude = 0.0;
        boolean reversed = false;

        try {
            while (opModeIsActive()) {
                if (gamepad1.dpadUpWasPressed()) {
                    motors.get(selectedIndex).setPower(0.0);
                    selectedIndex = (selectedIndex + 1) % motors.size();
                    powerMagnitude = 0.0;
                    reversed = false;
                } else if (gamepad1.dpadDownWasPressed()) {
                    motors.get(selectedIndex).setPower(0.0);
                    selectedIndex = (selectedIndex - 1 + motors.size()) % motors.size();
                    powerMagnitude = 0.0;
                    reversed = false;
                }

                if (gamepad1.aWasPressed()) {
                    powerMagnitude = Math.min(1.0, powerMagnitude + POWER_STEP);
                } else if (gamepad1.bWasPressed()) {
                    powerMagnitude = Math.max(0.0, powerMagnitude - POWER_STEP);
                }
                if (gamepad1.xWasPressed()) {
                    reversed = !reversed;
                }
                if (gamepad1.yWasPressed()) {
                    powerMagnitude = 0.0;
                }

                double power = reversed ? -powerMagnitude : powerMagnitude;
                motors.get(selectedIndex).setPower(power);

                telemetry.addData("Selected Motor", motorNames.get(selectedIndex));
                telemetry.addData("Direction", reversed ? "REVERSE" : "FORWARD");
                telemetry.addData("Power", "%.2f", power);
                telemetry.addLine("D-pad Up/Down: select | A/B: +/- 0.05");
                telemetry.addLine("X: toggle direction | Y: stop");
                telemetry.update();
            }
        } finally {
            for (DcMotor motor : motors) {
                motor.setPower(0.0);
            }
        }
    }
}
