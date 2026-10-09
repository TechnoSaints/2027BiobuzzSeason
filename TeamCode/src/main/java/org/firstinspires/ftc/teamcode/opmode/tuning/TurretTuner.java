package org.firstinspires.ftc.teamcode.opmode.tuning;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.common.MotorConstants.GoBilda6000Dc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A tuning OpMode to adjust servo positions and motor velocities.
 * 
 * Controls:
 * - Dpad Up/Down: Cycle through configured servos.
 * - Dpad Left/Right: Cycle through configured DcMotorEx motors.
 * - For servos, A/B adjust position by 0.01.
 * - For motors, X/Y adjust velocity by 10 ticks per second.
 */

//AI Generated  Aden Jaffer a Violation of the core robot code principles
@TeleOp(name = "Turret Tuner", group = "tuning")
public class TurretTuner extends LinearOpMode {

    @Override
    public void runOpMode() {
        List<Servo> servos = new ArrayList<>();
        List<String> servoNames = new ArrayList<>();
        for (Map.Entry<String, Servo> entry : hardwareMap.servo.entrySet()) {
            servoNames.add(entry.getKey());
            servos.add(entry.getValue());
        }

        List<DcMotorEx> motors = new ArrayList<>();
        List<String> motorNames = new ArrayList<>();
        for (Map.Entry<String, DcMotor> entry : hardwareMap.dcMotor.entrySet()) {
            if (entry.getValue() instanceof DcMotorEx) {
                motorNames.add(entry.getKey());
                motors.add((DcMotorEx) entry.getValue());
            }
        }

        int deviceCount = servos.size() + motors.size();
        if (deviceCount == 0) {
            telemetry.addLine("No servos or DcMotorEx motors found in hardware map!");
            telemetry.update();
            waitForStart();
            return;
        }

        int currentServoIndex = 0;
        int currentMotorIndex = 0;
        double currentPosition = 0.25;
        double currentVelocity = 0.0;

        boolean prevDpadUp = false;
        boolean prevDpadDown = false;
        boolean prevDpadLeft = false;
        boolean prevDpadRight = false;
        boolean prevA = false;
        boolean prevB = false;
        boolean prevX = false;
        boolean prevY = false;

        telemetry.addLine("Turret Tuner Initialized");
        telemetry.addLine("Use DPAD UP/DOWN to set hood servo");
        telemetry.addLine("Use DPAD LEFT/RIGHT to set launch motor");
        telemetry.addLine("For servos: A/B adjust position (+/- 0.01)");
        telemetry.addLine("For motors: X/Y adjust velocity (+/- 10 ticks/sec)");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        if (!servos.isEmpty()) {
            currentPosition = clamp(servos.get(currentServoIndex).getPosition(), 0.0, 1.0);
        }
        if (!motors.isEmpty()) {
            currentVelocity = clamp(motors.get(currentMotorIndex).getVelocity(),
                    -GoBilda6000Dc.maxTicksPerSec, GoBilda6000Dc.maxTicksPerSec);
        }

        while (opModeIsActive()) {
            boolean servoChanged = false;
            boolean motorChanged = false;
            if (gamepad1.dpad_up && !prevDpadUp) {
                if (!servos.isEmpty()) {
                    currentServoIndex = (currentServoIndex + 1) % servos.size();
                    servoChanged = true;
                }
            }
            prevDpadUp = gamepad1.dpad_up;

            if (gamepad1.dpad_down && !prevDpadDown) {
                if (!servos.isEmpty()) {
                    currentServoIndex = (currentServoIndex - 1 + servos.size()) % servos.size();
                    servoChanged = true;
                }
            }
            prevDpadDown = gamepad1.dpad_down;

            if (gamepad1.dpad_right && !prevDpadRight) {
                if (!motors.isEmpty()) {
                    currentMotorIndex = (currentMotorIndex + 1) % motors.size();
                    motorChanged = true;
                }
            }
            prevDpadRight = gamepad1.dpad_right;

            if (gamepad1.dpad_left && !prevDpadLeft) {
                if (!motors.isEmpty()) {
                    currentMotorIndex = (currentMotorIndex - 1 + motors.size()) % motors.size();
                    motorChanged = true;
                }
            }
            prevDpadLeft = gamepad1.dpad_left;

            if (servoChanged) {
                currentPosition = clamp(servos.get(currentServoIndex).getPosition(), 0.0, 1.0);
            }
            if (motorChanged) {
                currentVelocity = clamp(motors.get(currentMotorIndex).getVelocity(),
                        0, GoBilda6000Dc.maxTicksPerSec);
            }

            if (!servos.isEmpty()) {
                if (gamepad1.a && !prevA) {
                    currentPosition = Math.min(1.0, currentPosition + 0.01);
                }
                if (gamepad1.b && !prevB) {
                    currentPosition = Math.max(0.0, currentPosition - 0.01);
                }
                currentPosition = clamp(currentPosition, 0.0, 0.5);
                Servo servo = servos.get(currentServoIndex);
                servo.setPosition(currentPosition);
                telemetry.addData("--- Selected Hood Servo ---", servoNames.get(currentServoIndex));
                telemetry.addData("Target Position", "%.2f", currentPosition);
                telemetry.addData("Current Actual", "%.2f", servo.getPosition());
            } else {
                telemetry.addLine("No servos found in hardware map.");
            }

            if (!motors.isEmpty()) {
                if (gamepad1.x && !prevX) {
                    currentVelocity += 10.0;
                }
                if (gamepad1.y && !prevY) {
                    currentVelocity -= 10.0;
                }

                DcMotorEx motor = motors.get(currentMotorIndex);
                currentVelocity = clamp(currentVelocity, -GoBilda6000Dc.maxTicksPerSec,
                        GoBilda6000Dc.maxTicksPerSec);
                motor.setVelocity(currentVelocity);
                telemetry.addData("--- Selected Launch motor ---", motorNames.get(currentMotorIndex));
                telemetry.addData("Target Velocity (ticks/sec)", "%.1f", currentVelocity);
                telemetry.addData("Current Actual (ticks/sec)", "%.1f", motor.getVelocity());
            } else {
                telemetry.addLine("No motors found in hardware map.");
            }

            prevA = gamepad1.a;
            prevB = gamepad1.b;
            prevX = gamepad1.x;
            prevY = gamepad1.y;

            telemetry.addLine("\n--- Controls ---");
            telemetry.addLine("DPAD UP/DOWN: Switch Servo");
            telemetry.addLine("DPAD LEFT/RIGHT: Switch Motor");
            telemetry.addLine("Servo: A +0.01 | B -0.01");
            telemetry.addLine("Motor: X +10 ticks/sec | Y -10 ticks/sec");
            telemetry.update();
        }
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
