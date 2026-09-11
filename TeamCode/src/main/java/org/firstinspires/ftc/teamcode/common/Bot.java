package org.firstinspires.ftc.teamcode.common;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedro.Constants;

public abstract class Bot extends Component {
    protected OpMode opMode;
    public Follower follower;
    public Intake intake;

    protected Bot(OpMode opMode, Telemetry telemetry) {
        super(telemetry);
        this.opMode = opMode;
        try {
            this.follower = Constants.create(opMode.hardwareMap);
        } catch (Exception e) {
            telemetry.addLine("Error: Failed to initialize Follower");
        }
        try {
            this.intake = new Intake(opMode.hardwareMap, telemetry);
        } catch (Exception e) {
            telemetry.addLine("Error: Failed to initialize Intake");
        }
    }

    @Override
    public void update() {
        if (follower != null) {
            follower.update();
        }
        if (intake != null) {
            intake.update();
        }
    }
}
