package org.firstinspires.ftc.teamcode.opmode.test;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@TeleOp(name = "Pedro Drivetrain Test", group = "Test")
public class PedroDrivetrainTest extends OpMode {
    private Follower follower;

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose());
        telemetry.addLine("Pedro drivetrain test initialized");
        telemetry.addLine("Left stick: drive | Right stick X: turn");
        telemetry.addLine("Hold left bumper: slow mode | Start: reset heading");
        telemetry.update();
    }

    @Override
    public void init_loop() {
        follower.update();
    }

    @Override
    public void start() {
        follower.startTeleopDrive();
    }

    @Override
    public void loop() {
        if (gamepad1.startWasPressed()) {
            follower.setHeading(0);
        }

        double speedScale = gamepad1.left_bumper ? 0.3 : 1.0;
        follower.setTeleOpDrive(
                -gamepad1.left_stick_y * speedScale,
                -gamepad1.left_stick_x * speedScale,
                -gamepad1.right_stick_x * speedScale,
                true);
        follower.update();

        telemetry.addData("Pose", follower.getPose());
        telemetry.addData("Heading", "%.1f degrees",
                Math.toDegrees(follower.getPose().getHeading()));
        telemetry.addData("Slow Mode", gamepad1.left_bumper ? "ON" : "OFF");
        telemetry.update();
    }

    @Override
    public void stop() {
        if (follower != null) {
            follower.setTeleOpDrive(0, 0, 0, true);
            follower.update();
        }
    }
}
