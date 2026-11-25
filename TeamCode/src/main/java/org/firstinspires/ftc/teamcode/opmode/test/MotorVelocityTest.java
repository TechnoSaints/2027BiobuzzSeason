package org.firstinspires.ftc.teamcode.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@TeleOp(name = "Motor Velocity Test", group = "Test")
public class MotorVelocityTest extends LinearOpMode {
    private static final double SPEED_STEP = 0.05;

    @Override
    public void runOpMode() {
        List<DcMotorEx> motors = new ArrayList<>();
        List<String> motorNames = new ArrayList<>();

        for (Map.Entry<String, DcMotor> entry : hardwareMap.dcMotor.entrySet()) {
            motorNames.add(entry.getKey());
            motors.add(hardwareMap.get(DcMotorEx.class, entry.getKey()));
        }

        if (motors.isEmpty()) {
            telemetry.addLine("No motors found in hardware map.");
            telemetry.update();
            waitForStart();
            return;
        }

        telemetry.addLine("Motor Test initialized");
        telemetry.addLine("D-pad Up/Down: select motor");
        telemetry.addLine("A/B: change target speed by 5%");
        telemetry.addLine("X: stop selected motor");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) {
            return;
        }

        int currentMotorIndex = 0;
        double speedPercentage = 0.0;
        motors.get(currentMotorIndex).setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        try {
            while (opModeIsActive()) {
                if (gamepad1.dpadUpWasPressed()) {
                    motors.get(currentMotorIndex).setPower(0);
                    currentMotorIndex = (currentMotorIndex + 1) % motors.size();
                    speedPercentage = 0.0;
                    motors.get(currentMotorIndex).setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                } else if (gamepad1.dpadDownWasPressed()) {
                    motors.get(currentMotorIndex).setPower(0);
                    currentMotorIndex = (currentMotorIndex - 1 + motors.size()) % motors.size();
                    speedPercentage = 0.0;
                    motors.get(currentMotorIndex).setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                }

                if (gamepad1.aWasPressed()) {
                    speedPercentage = Math.min(1.0, speedPercentage + SPEED_STEP);
                } else if (gamepad1.bWasPressed()) {
                    speedPercentage = Math.max(-1.0, speedPercentage - SPEED_STEP);
                }
                if (gamepad1.xWasPressed()) {
                    speedPercentage = 0.0;
                }

                DcMotorEx selectedMotor = motors.get(currentMotorIndex);
                double maxTicksPerSecond =
                        selectedMotor.getMotorType().getAchieveableMaxTicksPerSecond();
                double targetVelocity = speedPercentage * maxTicksPerSecond;
                selectedMotor.setVelocity(targetVelocity);

                telemetry.addData("Selected Motor", motorNames.get(currentMotorIndex));
                telemetry.addData("Target Speed", "%.0f%%", speedPercentage * 100);
                telemetry.addData("Target Velocity", "%.0f ticks/s", targetVelocity);
                telemetry.addData("Encoder Position", selectedMotor.getCurrentPosition());
                telemetry.addData("Measured Velocity", "%.0f ticks/s", selectedMotor.getVelocity());
                telemetry.addLine("D-pad Up/Down: select motor | A/B: +/- 5% | X: stop");
                telemetry.update();
            }
        } finally {
            for (DcMotorEx motor : motors) {
                motor.setPower(0);
            }
        }
    }
}
