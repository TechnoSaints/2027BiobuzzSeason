package org.firstinspires.ftc.teamcode.opmode.test;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.common.components.Drivetrain;
import org.firstinspires.ftc.teamcode.common.components.NectarTurret;
import org.firstinspires.ftc.teamcode.common.components.PollenTurret;

@TeleOp(name = "Turret Test", group = "Test")
public class TurretTest extends LinearOpMode {
    private PollenTurret pollenTurret;
    private NectarTurret nectarTurret;
    private Drivetrain drivetrain;

    @Override
    public void runOpMode() {
        drivetrain = new Drivetrain(telemetry, hardwareMap);
        pollenTurret = new PollenTurret(
                hardwareMap,
                telemetry,
                PollenTurret.Alliance.RED);
//        nectarTurret = new NectarTurret(
//                hardwareMap,
//                telemetry,
//                NectarTurret.Alliance.RED);
        waitForStart();
        pollenTurret.startLimelight();
        //nectarTurret.startLimelight();
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
            drivetrain.setDrivetrainPowers(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
            pollenTurret.setMotorPIDFCoefficients(new PIDFCoefficients(kP, 0, 0, kF));
            pollenTurret.setVelocity(targetVelocity);

            LLResult result = pollenTurret.getLatestLimelightResult();
            //nectarTurret.update(result);
            LLResultTypes.FiducialResult targetTag = pollenTurret.getClosestAllianceTag(result);
            Double angleOffset = targetTag == null ? null : targetTag.getTargetXDegrees();
            pollenTurret.getCurrentPosition();
            pollenTurret.findOffset(
                    angleOffset == null ? 0.0 : angleOffset,
                    PollenTurret.currentPosition);

            telemetry.addData("Tuning kP", kP);
            telemetry.addData("Tuning kF", kF);
            telemetry.addData("PIDF Increment", pidfSteps[pidfIndex]);
            telemetry.addLine("--------------");
            telemetry.addData("Target Velocity", targetVelocity);
            telemetry.addData("Actual Velocity", pollenTurret.getVelocity());
            telemetry.addData("Velocity Increment", velocitySteps[velocityIndex]);
            telemetry.addLine("--------------");
            telemetry.addData("Hood Position", pollenTurret.getHoodPosition());
            telemetry.addLine("------------AprilTag Aiming-----------");
            telemetry.addData("Alliance", PollenTurret.Alliance.RED);
            telemetry.addData("Target AprilTag",
                    targetTag == null ? "None" : targetTag.getFiducialId());
            telemetry.addData(
                    "AprilTag angle offset",
                    angleOffset == null ? "No tag detected" : angleOffset);
            telemetry.addData("Pollen turret encoder angle", PollenTurret.currentPosition);
            pollenTurret.log();
            //nectarTurret.log();
            telemetry.update();
        }
        pollenTurret.stop();
        //nectarTurret.stop();
    }
}