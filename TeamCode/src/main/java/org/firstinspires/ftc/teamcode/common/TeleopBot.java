package org.firstinspires.ftc.teamcode.common;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.common.components.Drivetrain;
import org.firstinspires.ftc.teamcode.common.components.PollenTurret;
import org.firstinspires.ftc.teamcode.common.constants.RobotConstants;

/**
 * TeleopBot is specialized for TeleOp (driver controlled) mode.
 * It provides methods for manual driving and subsystem control.
 */
public class TeleopBot extends Bot {
    private final AccelerationLimiter forwardLimiter = new AccelerationLimiter();
    private final AccelerationLimiter strafeLimiter = new AccelerationLimiter();
    private final AccelerationLimiter turnLimiter = new AccelerationLimiter();
    private static Drivetrain drivetrain;
    private long lastDriveUpdateNanos = System.nanoTime();
    private PIDFController pidf;

    public TeleopBot(OpMode opMode, Telemetry telemetry, PollenTurret.Alliance alliance) {
        super(opMode, telemetry, alliance);
        drivetrain = new Drivetrain(telemetry, opMode.hardwareMap);
        pidf = new PIDFController(RobotConstants.Drivetrain.aimP,RobotConstants.Drivetrain.aimI,RobotConstants.Drivetrain.aimD,RobotConstants.Drivetrain.aimF);
        pidf.setSetPoint(0.0);
//        if (follower != null) {
//            follower.startTeleopDrive();
//        }
    }

    @Override
    public boolean isBusy() {
        return false;
    }

    /**
     * Sets the drive powers for the robot.
     * @param forward The forward/backward speed (-1 to 1).
     * @param strafe The left/right speed (-1 to 1).
     * @param turn The rotation speed (-1 to 1).
     * @param powerScale A multiplier for the overall speed (e.g. 0.3 for slow mode).
     */
    public void setPedroDrivePowers(double forward, double strafe, double turn, double powerScale, boolean aim) {
         if (follower != null) {
            long now = System.nanoTime();
            double deltaTimeSeconds = (now - lastDriveUpdateNanos) / 1_000_000_000.0;
            lastDriveUpdateNanos = now;

            double scaledForward = forward * powerScale;
            double scaledStrafe = strafe * powerScale;
            double scaledTurn = turn * powerScale;

             double turnPower;
             if (aim) {
                 LLResult llResult = pollenTurret.getLatestLimelightResult();

//                telemetry.addData("Error: ", tx);
//                telemetry.addData("Turn Power: ", turnPower);
//                List<LLResultTypes.FiducialResult> fiducialResults = llResult.getFiducialResults();
//                for (
//                        LLResultTypes.FiducialResult fr : fiducialResults) {
//                    telemetry.addData("Fiducial", "ID: %d, Family: %s, X: %.2f, Y: %.2f", fr.getFiducialId(), fr.getFamily(), fr.getTargetXDegrees(), fr.getTargetYDegrees());
//                }

                 if (llResult != null) {
                     double error = pollenTurret.getAprilTagAngleOffset(llResult);
                     pidf.setPIDF(RobotConstants.Drivetrain.aimP,RobotConstants.Drivetrain.aimI,RobotConstants.Drivetrain.aimD,RobotConstants.Drivetrain.aimF);
                     turnPower = pidf.calculate(error, RobotConstants.Drivetrain.targetError);
//                    if (error > 0.75) {
//                        turnPower = -error * kp - f;
//                    } else if (error < -0.75) {
//                        turnPower = -error * kp + f;
//                    } else {
//                        turnPower = 0.0;
//                    }
                 } else
                 {
                     turnPower = scaledTurn;
                 }
                 scaledTurn = turnPower;
             }

            // Sends the movement vectors to the drivetrain
             // Untested AI code; disabled for now
            follower.setTeleOpDrive(scaledForward, scaledStrafe, scaledTurn,
//                    forwardLimiter.calculate(
//                            scaledForward, RobotConstants.Drivetrain.maxDriveAcceleration,
//                            deltaTimeSeconds
//                    ),
//                    strafeLimiter.calculate(
//                            scaledStrafe, RobotConstants.Drivetrain.maxDriveAcceleration,
//                            deltaTimeSeconds
//                    ),
//                    turnLimiter.calculate(
//                            scaledTurn, RobotConstants.Drivetrain.maxDriveAcceleration,
//                            deltaTimeSeconds
//                    ),
                    true
            );
        }
    }

    public void setManualDrivePowers(double axial, double strafe, double yaw, boolean aim){
        double turnPower;
        if (aim) {
            LLResult llResult = pollenTurret.getLatestLimelightResult();

//                telemetry.addData("Error: ", tx);
//                telemetry.addData("Turn Power: ", turnPower);
//                List<LLResultTypes.FiducialResult> fiducialResults = llResult.getFiducialResults();
//                for (
//                        LLResultTypes.FiducialResult fr : fiducialResults) {
//                    telemetry.addData("Fiducial", "ID: %d, Family: %s, X: %.2f, Y: %.2f", fr.getFiducialId(), fr.getFamily(), fr.getTargetXDegrees(), fr.getTargetYDegrees());
//                }

            if (llResult != null) {
                double error = nectarTurret.getAprilTagAngleOffset(llResult);
                pidf.setPIDF(RobotConstants.Drivetrain.aimP,RobotConstants.Drivetrain.aimI,RobotConstants.Drivetrain.aimD,RobotConstants.Drivetrain.aimF);
                turnPower = pidf.calculate(error, RobotConstants.Drivetrain.targetError);
//                    if (error > 0.75) {
//                        turnPower = -error * kp - f;
//                    } else if (error < -0.75) {
//                        turnPower = -error * kp + f;
//                    } else {
//                        turnPower = 0.0;
//                    }
            } else
            {
                turnPower = yaw;
            }
            yaw = turnPower;
        }
        drivetrain.setDrivetrainPowers(axial, strafe, yaw);
    }

    public void stopDrive() {
        if (follower != null) {
            forwardLimiter.reset(0);
            strafeLimiter.reset(0);
            turnLimiter.reset(0);
            lastDriveUpdateNanos = System.nanoTime();
            follower.setTeleOpDrive(0, 0, 0, true);
            follower.update();
        }
    }

    /** Runs the intake forward */
    public void forward() {
        if (intake != null) {
            intake.forward();
        }
    }

    /** Runs the intake in reverse */
    public void reverse() {
        if (intake != null) {
            intake.reverse();
        }
    }

    /** Stops the intake */
    @Override
    public void stop() {
        super.stop();
    }

    /** Resets the robot's heading to zero. Use this if the robot's orientation gets drift. */
    public void resetHeading() {
        if (follower != null) {
            follower.setHeading(0);
        }
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void log(){
        super.log();
        drivetrain.log();
    }

    @Override
    public void start(){
        super.start();
    }
}
