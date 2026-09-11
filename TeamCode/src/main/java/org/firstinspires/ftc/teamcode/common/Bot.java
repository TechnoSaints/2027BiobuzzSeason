package org.firstinspires.ftc.teamcode.common;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedro.Constants;

public abstract class Bot extends Component {
    protected OpMode opMode;
    public Follower follower;

    protected Bot(OpMode opMode, Telemetry telemetry) {
        super(telemetry);
        this.opMode = opMode;
        this.follower = Constants.create(opMode.hardwareMap);
    }

    @Override
    public void update() {
        if (follower != null) {
            follower.update();
        }
    }
}
