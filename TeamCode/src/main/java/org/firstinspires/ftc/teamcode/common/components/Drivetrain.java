package org.firstinspires.ftc.teamcode.common.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.common.constants.RobotConstants;

public class Drivetrain extends Component{
    private static DcMotor frontLeftDrive, frontRightDrive, backLeftDrive, backRightDrive;
    private double frontLeftPower, frontRightPower, backLeftPower, backRightPower;
    public Drivetrain(Telemetry telemetry, HardwareMap hardwareMap) {
        super(telemetry);

        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
        frontLeftDrive = hardwareMap.get(DcMotor.class, RobotConstants.Drivetrain.LEFT_FRONT_NAME);
        backLeftDrive = hardwareMap.get(DcMotor.class, RobotConstants.Drivetrain.LEFT_REAR_NAME);
        frontRightDrive = hardwareMap.get(DcMotor.class, RobotConstants.Drivetrain.RIGHT_FRONT_NAME);
        backRightDrive = hardwareMap.get(DcMotor.class, RobotConstants.Drivetrain.RIGHT_REAR_NAME);
        // ########################################################################################
        // !!!            IMPORTANT Drive Information. Test your motor directions.            !!!!!
        // ########################################################################################
        // Most robots need the motors on one side to be reversed to drive forward.
        // The motor reversals shown here are for a "direct drive" robot (the wheels turn the same direction as the motor shaft)
        // If your robot has additional gear reductions or uses a right-angled drive, it's important to ensure
        // that your motors are turning in the correct direction.  So, start out with the reversals here, BUT
        // when you first test your robot, push the left joystick forward and observe the direction the wheels turn.
        // Reverse the direction (flip FORWARD <-> REVERSE ) of any wheel that runs backward
        // Keep testing until ALL the wheels move the robot forward when you push the left joystick forward.
        frontLeftDrive.setDirection(RobotConstants.Drivetrain.leftFrontMotorDirection);
        backLeftDrive.setDirection(RobotConstants.Drivetrain.leftRearMotorDirection);
        frontRightDrive.setDirection(RobotConstants.Drivetrain.rightFrontMotorDirection);
        backRightDrive.setDirection(RobotConstants.Drivetrain.rightRearMotorDirection);


        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    protected boolean isBusy() {
        return false;
    }

    @Override
    protected void update() {

    }

    public void setDrivetrainPowers(double axial, double lateral, double yaw){
        double max;

        // Combine the joystick requests for each axis-motion to determine each wheel's power.
        // Set up a variable for each drive wheel to save the power level for telemetry.
        frontLeftPower  = axial + lateral + yaw;
        frontRightPower = axial - lateral - yaw;
        backLeftPower   = axial - lateral + yaw;
        backRightPower  = axial + lateral - yaw;

        double intakePower = 1;
        // Normalize the values so no wheel power exceeds 100%
        // This ensures that the robot maintains the desired motion.
        max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        max = Math.max(max, Math.abs(backLeftPower));
        max = Math.max(max, Math.abs(backRightPower));

        if (max > 1.0) {
            frontLeftPower /= max;
            frontRightPower /= max;
            backLeftPower /= max;
            backRightPower /= max;


        }

        frontLeftDrive.setPower(frontLeftPower);
        frontRightDrive.setPower(frontRightPower);
        backLeftDrive.setPower(backLeftPower);
        backRightDrive.setPower(backRightPower);
    }

    public void log(){
        telemetry.addData("Front Left Power", frontLeftPower);
        telemetry.addData("Front Right Power", frontRightPower);
        telemetry.addData("Back Left Power", backLeftPower);
        telemetry.addData("Back Right Power", backRightPower);
    }
}
