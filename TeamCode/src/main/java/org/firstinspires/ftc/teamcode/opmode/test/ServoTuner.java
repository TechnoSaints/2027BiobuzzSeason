package org.firstinspires.ftc.teamcode.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name = "Basic Axon CR Servo", group = "Linear Opmode")
public class ServoTuner extends LinearOpMode {

    private CRServo axonServo;

    @Override
    public void runOpMode() {
        // 1. Map the servo to the hardware configuration name ("axonServo")
        axonServo = hardwareMap.get(CRServo.class, "gate");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // 2. Control the continuous servo power using Gamepad 1 Left Joystick Y-axis,
            // or use buttons like A and B for full speed.

            // Example A: Using the Left Stick Y-axis (-1.0 to 1.0)
            double power = -gamepad1.left_stick_y; // Negative if you need to invert direction
            axonServo.setPower(power);

            // --- OR ---
            // Example B: Using buttons A (forward) and B (reverse) and X (stop)
            /*
            if (gamepad1.a) {
                axonServo.setPower(1.0);  // Full speed one way
            } else if (gamepad1.b) {
                axonServo.setPower(-1.0); // Full speed opposite way
            } else {
                axonServo.setPower(0.0);  // Stop
            }
            */

            telemetry.addData("Servo Power", axonServo.getPower());
            telemetry.update();
        }
    }
}