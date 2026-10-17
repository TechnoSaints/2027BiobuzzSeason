package org.firstinspires.ftc.teamcode.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "MotorTest", group = "Test")
public class MotorTest extends LinearOpMode {

    private DcMotorEx motor;
    private Servo servo;

    @Override
    public void runOpMode() {
        motor = hardwareMap.get(DcMotorEx.class, "shooter");
        servo = hardwareMap.get(Servo.class, "hood");
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(0.1, 0, 0, 0));
        waitForStart();
        double targetVelocity = 0;
        double targetPosition = 0.5;
        double kP = 0.1;
        double kF = 0;
        while (opModeIsActive() && !isStopRequested()) {
            if (gamepad1.aWasPressed()) {
                targetVelocity += 20;
            }
            else if (gamepad1.bWasPressed()){
                targetVelocity -= 20;
            }
            if (gamepad1.xWasPressed()){
                targetPosition += 0.01;
            }
            else if (gamepad1.yWasPressed()){
                targetPosition -= 0.01;
            }
            if (gamepad1.dpadUpWasPressed()){
                kP += 0.01;
            }
            else if (gamepad1.dpadDownWasPressed()){
                kP -= 0.01;
            }
            if (gamepad1.dpadRightWasPressed()){
                kF += 0.01;
            }
            else if (gamepad1.dpadLeftWasPressed()){
                kF -= 0.01;
            }
            motor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(kP, 0, 0, kF));
            motor.setVelocity(targetVelocity);
            servo.setPosition(targetPosition);

            telemetry.addData("Target Velocity", targetVelocity);
            telemetry.addData("Actual Velocity", motor.getVelocity());
            telemetry.addData("Hood Position", servo.getPosition());
            telemetry.update();
        }
    }
}