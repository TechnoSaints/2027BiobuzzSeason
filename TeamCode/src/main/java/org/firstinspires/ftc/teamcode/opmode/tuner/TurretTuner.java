package org.firstinspires.ftc.teamcode.opmode.tuner;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.constants.RobotConstants;

@TeleOp(name = "Turret Test", group = "Test")
public class TurretTuner extends LinearOpMode {
    private static final double ANGLE_STEP_DEGREES = 0.5;
    private static final double[] VELOCITY_STEPS_TICKS_PER_SECOND = {20.0, 100.0};

    @Override
    public void runOpMode() {
        DcMotorEx flywheel = hardwareMap.get(
                DcMotorEx.class, RobotConstants.TurretConstants.LAUNCHER_MOTOR_NAME);
        Servo aimServo = hardwareMap.get(
                Servo.class, RobotConstants.TurretConstants.AIM_SERVO_NAME);

        flywheel.setDirection(RobotConstants.TurretConstants.LAUNCHER_MOTOR_DIRECTION);
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        aimServo.setPosition(0.5);

        telemetry.addLine("Turret Test initialized");
        telemetry.addLine("A/B: increase/decrease flywheel velocity");
        telemetry.addLine("Left/Right Bumper: select velocity step");
        telemetry.addLine("X/Y: adjust turret heading by -/+ 0.5 degree");
        telemetry.addLine("0.5 servo position is 0 degrees");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) {
            return;
        }

        double targetVelocity = 0.0;
        double servoPosition = 0.5;
        int velocityStepIndex = 0;
        double maxTicksPerSecond =
                flywheel.getMotorType().getAchieveableMaxTicksPerSecond();

        try {
            while (opModeIsActive()) {
                if (gamepad1.aWasPressed()) {
                    targetVelocity = Math.min(
                            maxTicksPerSecond,
                            targetVelocity + VELOCITY_STEPS_TICKS_PER_SECOND[velocityStepIndex]);
                } else if (gamepad1.bWasPressed()) {
                    targetVelocity = Math.max(
                            0.0,
                            targetVelocity - VELOCITY_STEPS_TICKS_PER_SECOND[velocityStepIndex]);
                }

                if (gamepad1.leftBumperWasPressed()) {
                    velocityStepIndex = Math.max(0, velocityStepIndex - 1);
                } else if (gamepad1.rightBumperWasPressed()) {
                    velocityStepIndex = Math.min(
                            VELOCITY_STEPS_TICKS_PER_SECOND.length - 1,
                            velocityStepIndex + 1);
                }

                if (gamepad1.xWasPressed()) {
                    servoPosition -= ANGLE_STEP_DEGREES
                            / RobotConstants.TurretConstants.AIM_SERVO_RANGE_DEGREES;
                } else if (gamepad1.yWasPressed()) {
                    servoPosition += ANGLE_STEP_DEGREES
                            / RobotConstants.TurretConstants.AIM_SERVO_RANGE_DEGREES;
                }
                servoPosition = Math.max(0.0, Math.min(1.0, servoPosition));

                flywheel.setVelocity(targetVelocity);
                aimServo.setPosition(servoPosition);

                double headingDegrees = (servoPosition - 0.5)
                        * RobotConstants.TurretConstants.AIM_SERVO_RANGE_DEGREES;
                telemetry.addData("Target Flywheel Velocity", "%.1f ticks/s", targetVelocity);
                telemetry.addData(
                        "Current Flywheel Velocity",
                        "%.1f ticks/s",
                        flywheel.getVelocity());
                telemetry.addData(
                        "Velocity Step",
                        "%.0f ticks/s",
                        VELOCITY_STEPS_TICKS_PER_SECOND[velocityStepIndex]);
                telemetry.addData("Turret Heading", "%.1f degrees", headingDegrees);
                telemetry.addData("Aim Servo Position", "%.4f", servoPosition);
                telemetry.addLine("A/B: velocity +/- | Bumpers: velocity step | X/Y: angle -/+");
                telemetry.update();
            }
        } finally {
            flywheel.setPower(0.0);
        }
    }
}
