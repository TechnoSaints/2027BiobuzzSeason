package org.firstinspires.ftc.teamcode.opmode.tuner;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@TeleOp(name = "Servo Mode Tuner", group = "Tuner")
public class ServoModeTuner extends LinearOpMode {
    private static final double POSITION_STEP = 0.01;

    @Override
    public void runOpMode() {
        List<Servo> servos = new ArrayList<>();
        List<String> servoNames = new ArrayList<>();

        for (Map.Entry<String, Servo> entry : hardwareMap.servo.entrySet()) {
            servoNames.add(entry.getKey());
            servos.add(entry.getValue());
        }

        if (servos.isEmpty()) {
            telemetry.addLine("No positional servos found in hardware map.");
            telemetry.update();
            waitForStart();
            return;
        }

        telemetry.addLine("Servo tuner initialized");
        telemetry.addLine("D-pad Left/Right: select servo");
        telemetry.addLine("D-pad Up/Down: adjust position by 0.01");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) {
            return;
        }

        int selectedServoIndex = 0;
        double position = servos.get(selectedServoIndex).getPosition();

        while (opModeIsActive()) {
            if (gamepad1.dpadRightWasPressed()) {
                selectedServoIndex = (selectedServoIndex + 1) % servos.size();
                position = servos.get(selectedServoIndex).getPosition();
            } else if (gamepad1.dpadLeftWasPressed()) {
                selectedServoIndex = (selectedServoIndex - 1 + servos.size()) % servos.size();
                position = servos.get(selectedServoIndex).getPosition();
            }

            if (gamepad1.dpadUpWasPressed()) {
                position = Math.min(1.0, position + POSITION_STEP);
            } else if (gamepad1.dpadDownWasPressed()) {
                position = Math.max(0.0, position - POSITION_STEP);
            }

            Servo selectedServo = servos.get(selectedServoIndex);
            selectedServo.setPosition(position);

            telemetry.addData("Selected Servo", servoNames.get(selectedServoIndex));
            telemetry.addData("Position", "%.2f", position);
            telemetry.addLine("D-pad Left/Right: select servo | Up/Down: position +/- 0.01");
            telemetry.update();
        }
    }
}
