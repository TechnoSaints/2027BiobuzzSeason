package org.firstinspires.ftc.teamcode.common;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.components.Component;
import org.firstinspires.ftc.teamcode.common.components.IntakeFeeder;
import org.firstinspires.ftc.teamcode.common.components.Turret;
import org.firstinspires.ftc.teamcode.common.constants.FieldConstants;
import org.firstinspires.ftc.teamcode.common.constants.RobotConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

/** Robot systems shared by the red and blue alliance configurations. */
public class Bot extends Component {
    public enum Alliance {
        RED,
        BLUE
    }

    private final Alliance alliance;
    private final IntakeFeeder intakeFeeder;
    /*
    private final FlowerBar flowerBar;
    */
    private final Turret turret;
    private final Follower follower;
    /*
    private final DistanceSensor intakeSensor;
    private final DistanceSensor kickerSensor;
    */

    public Bot(OpMode opMode, Telemetry telemetry, Alliance alliance) {
        super(telemetry);
        if (opMode == null) {
            throw new IllegalArgumentException("OpMode must be provided.");
        }
        if (alliance == null) {
            throw new IllegalArgumentException("Alliance must be RED or BLUE.");
        }

        this.alliance = alliance;
        intakeFeeder = new IntakeFeeder(opMode.hardwareMap, telemetry);
        /*
        flowerBar = new FlowerBar(opMode.hardwareMap, telemetry);
        */
        turret = new Turret(opMode.hardwareMap, telemetry);
        /*
        intakeSensor = opMode.hardwareMap.get(DistanceSensor.class, "intakeSensor");
        kickerSensor = opMode.hardwareMap.get(DistanceSensor.class, "kickerSensor");
        */
        follower = Constants.createFollower(opMode.hardwareMap);
        follower.setStartingPose(FieldConstants.startPose);
    }

    public Alliance getAlliance() {
        return alliance;
    }

    public void runIntakeFeederForward(double powerProportion) {
        intakeFeeder.runForward(powerProportion);
    }

    public void runIntakeFeederReverse(double powerProportion) {
        intakeFeeder.runReverse(powerProportion);
    }

    public void stopIntakeFeeder() {
        intakeFeeder.stop();
    }

    /*
    public void setFlowerBarPosition(double position) {
        flowerBar.setPosition(position);
    }

    public void raiseFlowerBar() {
        flowerBar.setPosition(RobotConstants.Intake.FLOWER_STORE_POSITION);
    }

    public void lowerFlowerBar() {
        flowerBar.setPosition(RobotConstants.Intake.FLOWER_INTAKE_POSITION);
    }

    public double getFlowerBarPosition() {
        return flowerBar.getPosition();
    }
    */

    public void setLauncherPower(double power) {
        turret.setLauncherPower(power);
    }

    public void setLauncherVelocity(double ticksPerSecond) {
        turret.setLauncherVelocity(ticksPerSecond);
    }

    public double getLauncherVelocity() {
        return turret.getLauncherVelocity();
    }

    public double getMaxLauncherVelocity() {
        return turret.getMaxLauncherVelocity();
    }

    public void stopLauncher() {
        turret.stopLauncher();
    }

    public void runKickerForward() {
        turret.runKickerForward();
    }

    public void runKickerReverse() {
        turret.runKickerReverse();
    }

    public void stopKicker() {
        turret.stopKicker();
    }

    public void setTurretAimPosition(double position) {
        turret.setAimPosition(position);
    }

    public double getTurretAimPosition() {
        return turret.getAimPosition();
    }

    /*
    public double getIntakeSensorDistance(DistanceUnit unit) {
        return intakeSensor.getDistance(unit);
    }

    public double getKickerSensorDistance(DistanceUnit unit) {
        return kickerSensor.getDistance(unit);
    }
    */

    public void startTeleopDrive() {
        follower.startTeleopDrive();
    }

    public void setDrivePowers(double forward, double strafe, double turn) {
        follower.setTeleOpDrive(forward, strafe, turn, true);
    }

    public void followPath(PathChain path, double maxPower, boolean holdEnd) {
        follower.followPath(path, maxPower, holdEnd);
    }

    public Pose getPose() {
        return follower.getPose();
    }

    public void stopDrive() {
        follower.setTeleOpDrive(0.0, 0.0, 0.0, true);
    }

    @Override
    public boolean isBusy() {
        return follower.isBusy();
    }

    @Override
    public void update() {
        follower.update();
    }
}
