package org.firstinspires.ftc.teamcode.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

    /**
     * MotorTest lets you test and adjust the position of any motor with a gamepad, in small
     * steps, so you can find the exact position number you want without
     * editing code and redeploying every time.
     *
     * Controls:
     * - Dpad Up/Down: switch which servo you're adjusting.
     * - A: move the servo a little further (position + 0.01).
     * - B: move the servo a little back (position - 0.01).
     */
@TeleOp(name = "Motor Test", group = "test")
public class MotorTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        // Grab every motor that's in the Driver Station's Hardware Config
        List<DcMotorEx> motors = new ArrayList<>();
        List<String> motorNames = new ArrayList<>();

        for (Map.Entry<String, DcMotor> entry : hardwareMap.dcMotor.entrySet()) {
            motorNames.add(entry.getKey());
            motors.add((DcMotorEx) entry.getValue());
        }

        if (motors.isEmpty()) {
            telemetry.addLine("No motors found in hardware map!");
            telemetry.update();
            waitForStart();
            return;
        }

        int currentMotorIndex = 0;

        // Remembers last loop's button state, so a button press only
        // counts once instead of every single loop it's held down
        boolean prevDpadUp = false;
        boolean prevDpadDown = false;
        boolean prevA = false;
        boolean prevB = false;

        telemetry.addLine("Motor Tuner Initialized");
        telemetry.addLine("Use DPAD UP/DOWN to switch motors");
        telemetry.addLine("Use joystick to set power");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            // Dpad up/down cycles to the next/previous servo in the list
            if (gamepad1.dpadUpWasPressed()) {
                currentMotorIndex = (currentMotorIndex + 1) % motors.size();
            }

            if (gamepad1.dpadDownWasPressed()) {
                currentMotorIndex = (currentMotorIndex - 1 + motors.size()) % motors.size();
            }

            DcMotorEx selectedMotor = motors.get(currentMotorIndex);
            selectedMotor.setPower(-gamepad1.left_stick_y);

            telemetry.addData("--- Selected Motor ---", motorNames.get(currentMotorIndex));
            telemetry.addData("Target Velocity", "%.2f", -gamepad1.left_stick_y);
            telemetry.addData("Actual Velocity", "%.2f", selectedMotor.getVelocity());
            telemetry.addLine("\n--- Controls ---");
            telemetry.addLine("DPAD UP/DOWN: Switch Servo");
            telemetry.addLine("LEFT JOYSTICK UP/DOWN: Set Velocity");
            telemetry.update();
        }
    }
}
