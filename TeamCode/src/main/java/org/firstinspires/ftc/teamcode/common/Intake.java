package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake extends Component {
    private DcMotorEx intakeMotor;

    public Intake(HardwareMap hardwareMap, Telemetry telemetry) {
        super(telemetry);
        try {
            intakeMotor = hardwareMap.get(DcMotorEx.class, RobotConstants.Intake.INTAKE_MOTOR_NAME);
            intakeMotor.setDirection(RobotConstants.Intake.intakeMotorDirection);
        } catch (Exception e) {
            telemetry.addLine("Warning: Intake motor not found in HardwareMap");
        }
    }

    public void setIntakePower(double power) {
        if (intakeMotor != null) {
            intakeMotor.setPower(power);
        }
    }

    public void forward() {
        setIntakePower(RobotConstants.Intake.maxMovePower);
    }

    public void reverse() {
        setIntakePower(-RobotConstants.Intake.maxMovePower);
    }

    public void stop() {
        setIntakePower(RobotConstants.Intake.stopPower);
    }

    @Override
    public boolean isBusy() {
        return false;
    }

    @Override
    public void update() {
        // Any periodic intake updates can go here
    }
}
