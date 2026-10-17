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
        waitForStart();
        int targetVelocity = 0;
        double targetPosition = 0.5;
        double kP = 56.4;
        double kF = 13.47;
        double[] pidfSteps = {10, 1, 0.1, 0.01, 0.001};
        int pidfIndex = 0;
        int[] velocitySteps = {100, 20};
        int highVelocity = 1600;
        int lowVelocity = 800;
        int velocityIndex = 1;
        while (opModeIsActive() && !isStopRequested()) {
            if (gamepad1.aWasPressed()) {
                targetVelocity += velocitySteps[velocityIndex];
            }
            else if (gamepad1.bWasPressed()){
                targetVelocity -= velocitySteps[velocityIndex];
            }
            if (gamepad1.xWasPressed()){
                targetPosition += 0.01;
            }
            else if (gamepad1.yWasPressed()){
                targetPosition -= 0.01;
            }
            if (gamepad1.dpadUpWasPressed()){
                kP += pidfSteps[pidfIndex];
            }
            else if (gamepad1.dpadDownWasPressed()){
                kP -= pidfSteps[pidfIndex];
            }
            if (gamepad1.dpadRightWasPressed()){
                kF += pidfSteps[pidfIndex];
            }
            else if (gamepad1.dpadLeftWasPressed()){
                kF -= pidfSteps[pidfIndex];
            }
            if (gamepad1.leftBumperWasPressed()){
                pidfIndex = (pidfIndex + 1) % pidfSteps.length;
            }
            if (gamepad1.rightBumperWasPressed()){
                velocityIndex = (velocityIndex + 1) % velocitySteps.length;
            }
            if (gamepad1.optionsWasPressed()){
                if (gamepad1.touchpad){
                    highVelocity = targetVelocity;
                }
                else {
                    targetVelocity = highVelocity;
                }
            }
            if (gamepad1.shareWasPressed()){
                if (gamepad1.touchpad){
                    lowVelocity = targetVelocity;
                }
                else {
                    targetVelocity = lowVelocity;
                }
            }
            motor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(kP, 0, 0, kF));
            motor.setVelocity(targetVelocity);
            servo.setPosition(targetPosition);

            telemetry.addData("Tuning kP", kP);
            telemetry.addData("Tuning kF", kF);
            telemetry.addData("PIDF Increment", pidfSteps[pidfIndex]);
            telemetry.addLine("--------------");
            telemetry.addData("Target Velocity", targetVelocity);
            telemetry.addData("Actual Velocity", motor.getVelocity());
            telemetry.addData("Velocity Increment", velocitySteps[velocityIndex]);
            telemetry.addLine("--------------");
            telemetry.addData("Hood Position", servo.getPosition());
            telemetry.update();
        }
    }
}