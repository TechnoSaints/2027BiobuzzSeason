package org.firstinspires.ftc.teamcode.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Throughput Test", group = "Test")
public class ThroughputTest extends LinearOpMode {
    private static final String[] MOTOR_NAMES = {"kicker", "intake", "shooter"};
    private static final double POWER_STEP = 0.05;
    private static final double VELOCITY_STEP_TICKS_PER_SECOND = 50.0;

    @Override
    public void runOpMode() {
        DcMotorEx[] motors = new DcMotorEx[MOTOR_NAMES.length];
        double[] outputLevels = new double[MOTOR_NAMES.length];
        boolean[] reversed = new boolean[MOTOR_NAMES.length];

        for (int i = 0; i < MOTOR_NAMES.length; i++) {
            motors[i] = hardwareMap.get(DcMotorEx.class, MOTOR_NAMES[i]);
            motors[i].setMode(i == 2
                    ? DcMotor.RunMode.RUN_USING_ENCODER
                    : DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        telemetry.addLine("Throughput Test initialized");
        telemetry.addLine("D-pad Up/Down: select motor | A/B: increase/decrease output");
        telemetry.addLine("X: toggle direction | Y: stop selected motor");
        telemetry.addLine("Shooter adjusts by 50 ticks/s; kicker/intake adjust by 5% power");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) {
            return;
        }

        int selectedIndex = 0;
        double shooterMaxTicksPerSecond =
                motors[2].getMotorType().getAchieveableMaxTicksPerSecond();

        try {
            while (opModeIsActive()) {
                if (gamepad1.dpadUpWasPressed()) {
                    selectedIndex = (selectedIndex + 1) % motors.length;
                } else if (gamepad1.dpadDownWasPressed()) {
                    selectedIndex = (selectedIndex - 1 + motors.length) % motors.length;
                }

                double step = selectedIndex == 2
                        ? VELOCITY_STEP_TICKS_PER_SECOND
                        : POWER_STEP;
                double maxOutput = selectedIndex == 2
                        ? shooterMaxTicksPerSecond
                        : 1.0;
                if (gamepad1.aWasPressed()) {
                    outputLevels[selectedIndex] = Math.min(
                            maxOutput, outputLevels[selectedIndex] + step);
                } else if (gamepad1.bWasPressed()) {
                    outputLevels[selectedIndex] = Math.max(
                            0.0, outputLevels[selectedIndex] - step);
                }
                if (gamepad1.xWasPressed()) {
                    reversed[selectedIndex] = !reversed[selectedIndex];
                }
                if (gamepad1.yWasPressed()) {
                    outputLevels[selectedIndex] = 0.0;
                }

                for (int i = 0; i < motors.length; i++) {
                    double direction = reversed[i] ? -1.0 : 1.0;
                    if (i == 2) {
                        motors[i].setVelocity(direction * outputLevels[i]);
                    } else {
                        motors[i].setPower(direction * outputLevels[i]);
                    }

                    String directionName = reversed[i] ? "REVERSE" : "FORWARD";
                    String marker = i == selectedIndex ? " > " : "   ";
                    if (i == 2) {
                        telemetry.addData(
                                marker + MOTOR_NAMES[i],
                                "%s | target %.0f ticks/s | actual %.0f ticks/s",
                                directionName,
                                direction * outputLevels[i],
                                motors[i].getVelocity());
                    } else {
                        telemetry.addData(
                                marker + MOTOR_NAMES[i],
                                "%s | power %.2f",
                                directionName,
                                direction * outputLevels[i]);
                    }
                }
                telemetry.addLine("D-pad: select | A/B: +/- | X: toggle direction | Y: stop");
                telemetry.update();
            }
        } finally {
            for (DcMotorEx motor : motors) {
                motor.setPower(0.0);
            }
        }
    }
}
