package org.firstinspires.ftc.teamcode.opmode.tuning;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ServoTuner lets you nudge any servo's position with a gamepad, in small
 * steps, so you can find the exact position number you want without
 * editing code and redeploying every time.
 *
 * Controls:
 * - Dpad Up/Down: switch which servo you're adjusting.
 * - A: move the servo a little further (position + 0.01).
 * - B: move the servo a little back (position - 0.01).
 */
@TeleOp(name = "Servo Tuner", group = "tuning")
public class ServoTuner extends LinearOpMode {

    @Override
    public void runOpMode() {
        // Grab every servo that's in the Driver Station's Hardware Config
        List<Servo> servos = new ArrayList<>();
        List<String> servoNames = new ArrayList<>();

        for (Map.Entry<String, Servo> entry : hardwareMap.servo.entrySet()) {
            servoNames.add(entry.getKey());
            servos.add(entry.getValue());
        }

        if (servos.isEmpty()) {
            telemetry.addLine("No servos found in hardware map!");
            telemetry.update();
            waitForStart();
            return;
        }

        int currentServoIndex = 0;
        double currentPosition = 0.5;

        // Remembers last loop's button state, so a button press only
        // counts once instead of every single loop it's held down
        boolean prevDpadUp = false;
        boolean prevDpadDown = false;
        boolean prevA = false;
        boolean prevB = false;

        telemetry.addLine("Servo Tuner Initialized");
        telemetry.addLine("Use DPAD UP/DOWN to switch servos");
        telemetry.addLine("Use A/B to adjust position (+/- 0.01)");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        // Start from wherever this servo already is, instead of jumping to the middle
        currentPosition = servos.get(currentServoIndex).getPosition();

        while (opModeIsActive()) {
            // Dpad up/down cycles to the next/previous servo in the list
            if (gamepad1.dpad_up && !prevDpadUp) {
                currentServoIndex = (currentServoIndex + 1) % servos.size();
                currentPosition = servos.get(currentServoIndex).getPosition();
            }
            prevDpadUp = gamepad1.dpad_up;

            if (gamepad1.dpad_down && !prevDpadDown) {
                currentServoIndex = (currentServoIndex - 1 + servos.size()) % servos.size();
                currentPosition = servos.get(currentServoIndex).getPosition();
            }
            prevDpadDown = gamepad1.dpad_down;

            // A nudges the position up, B nudges it down, clamped to the 0-1 range every servo uses
            if (gamepad1.a && !prevA) {
                currentPosition = Math.min(1.0, currentPosition + 0.01);
            }
            prevA = gamepad1.a;

            if (gamepad1.b && !prevB) {
                currentPosition = Math.max(0.0, currentPosition - 0.01);
            }
            prevB = gamepad1.b;

            Servo selectedServo = servos.get(currentServoIndex);
            selectedServo.setPosition(currentPosition);

            telemetry.addData("--- Selected Servo ---", servoNames.get(currentServoIndex));
            telemetry.addData("Target Position", "%.2f", currentPosition);
            telemetry.addData("Current Actual", "%.2f", selectedServo.getPosition());
            telemetry.addLine("\n--- Controls ---");
            telemetry.addLine("DPAD UP/DOWN: Switch Servo");
            telemetry.addLine("A: +0.01 | B: -0.01");
            telemetry.update();
        }
    }
}
