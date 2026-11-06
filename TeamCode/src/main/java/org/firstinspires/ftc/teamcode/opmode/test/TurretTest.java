package org.firstinspires.ftc.teamcode.opmode.test;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.common.components.PollenTurret;
import org.firstinspires.ftc.teamcode.common.constants.RobotConstants;

@Configurable
@TeleOp(name = "Turret Test", group = "Test")
public class TurretTest extends LinearOpMode {
    private PollenTurret pollenTurret;
    private Limelight3A limelight;

    @Override
    public void runOpMode() {
        TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        pollenTurret = new PollenTurret(
                hardwareMap,
                PanelsTelemetry.INSTANCE.getFtcTelemetry(),
                PollenTurret.Alliance.RED);
        limelight = hardwareMap.get(
                Limelight3A.class, RobotConstants.LimelightConstants.limelightName);
        limelight.pipelineSwitch(RobotConstants.LimelightConstants.LIMELIGHT_PIPELINE);
        waitForStart();
        limelight.start();
        pollenTurret.resetAimingTimer();
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
            pollenTurret.setMotorPIDFCoefficients(new PIDFCoefficients(kP, 0, 0, kF));
            pollenTurret.setVelocity(targetVelocity);

            LLResult result = limelight.getLatestResult();
            pollenTurret.update(result);
            LLResultTypes.FiducialResult targetTag = pollenTurret.getClosestAllianceTag(result);
            Double angleOffset = targetTag == null ? null : targetTag.getTargetXDegrees();

            panelsTelemetry.addData("Tuning kP", kP);
            panelsTelemetry.addData("Tuning kF", kF);
            panelsTelemetry.addData("PIDF Increment", pidfSteps[pidfIndex]);
            panelsTelemetry.addLine("--------------");
            panelsTelemetry.addData("Target Velocity", targetVelocity);
            panelsTelemetry.addData("Actual Velocity", pollenTurret.getVelocity());
            panelsTelemetry.addData("Velocity Increment", velocitySteps[velocityIndex]);
            panelsTelemetry.addLine("--------------");
            panelsTelemetry.addData("Hood Position", pollenTurret.getHoodPosition());
            panelsTelemetry.addLine("------------AprilTag Aiming-----------");
            panelsTelemetry.addData("Alliance", PollenTurret.Alliance.RED);
            panelsTelemetry.addData("Target AprilTag",
                    targetTag == null ? "None" : targetTag.getFiducialId());
            panelsTelemetry.addData(
                    "AprilTag angle offset",
                    angleOffset == null ? "No tag detected" : angleOffset);
            pollenTurret.log();
            telemetry.update();
            panelsTelemetry.update();
        }
        pollenTurret.stop();
        limelight.stop();
    }
}