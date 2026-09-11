package org.firstinspires.ftc.teamcode.opmode.tuning;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A tuning OpMode to adjust servo positions in 0.01 increments.
 * 
 * Controls:
 * - Dpad Up/Down: Cycle through all configured servos.
 * - Gamepad A: Increase position by 0.01.
 * - Gamepad B: Decrease position by 0.01.
 */
@TeleOp(name = "Servo Tuner", group = "tuning")
public class ServoTuner extends LinearOpMode {

    @Override
    public void runOpMode() {
        // Find all servos in the hardware map and their names
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

        // Debouncing states
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

        // Initialize position to the current servo's position
        currentPosition = servos.get(currentServoIndex).getPosition();

        while (opModeIsActive()) {
            // Switch to next servo
            if (gamepad1.dpad_up && !prevDpadUp) {
                currentServoIndex = (currentServoIndex + 1) % servos.size();
                currentPosition = servos.get(currentServoIndex).getPosition();
            }
            prevDpadUp = gamepad1.dpad_up;

            // Switch to previous servo
            if (gamepad1.dpad_down && !prevDpadDown) {
                currentServoIndex = (currentServoIndex - 1 + servos.size()) % servos.size();
                currentPosition = servos.get(currentServoIndex).getPosition();
            }
            prevDpadDown = gamepad1.dpad_down;

            // Increment position
            if (gamepad1.a && !prevA) {
                currentPosition = Math.min(1.0, currentPosition + 0.01);
            }
            prevA = gamepad1.a;

            // Decrement position
            if (gamepad1.b && !prevB) {
                currentPosition = Math.max(0.0, currentPosition - 0.01);
            }
            prevB = gamepad1.b;

            // Apply position to the selected servo
            Servo selectedServo = servos.get(currentServoIndex);
            selectedServo.setPosition(currentPosition);

            // Display status
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
