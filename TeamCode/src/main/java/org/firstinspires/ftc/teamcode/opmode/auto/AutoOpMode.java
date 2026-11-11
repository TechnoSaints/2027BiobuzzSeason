package org.firstinspires.ftc.teamcode.opmode.auto;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.common.AutoBot;
import org.firstinspires.ftc.teamcode.common.components.PollenTurret;
import org.firstinspires.ftc.teamcode.common.constants.Paths;

public abstract class AutoOpMode extends OpMode {
    protected AutoBot bot;
    protected int pathState;
    protected double launcherSpeed;
    protected ElapsedTime controlTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    protected ElapsedTime gameTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    /**
     * This method is called once at the init of the OpMode.
     **/
    @Override
    public void init() {
        bot = new AutoBot(this, telemetry, PollenTurret.Alliance.BLUE);
        Paths.buildPaths(bot.getFollower());
    }

    /**
     * This method is called continuously after Init while waiting for "play".
     **/
    @Override
    public void init_loop()
    {
        bot.update();
    }

    @Override
    public void start()
    {
        setPathState(0);
        gameTimer.reset();
    }
    /**
     * This is the main loop of the OpMode, it will run repeatedly after clicking "Play".
     **/
    @Override
    public void loop()
    {
        // These loop the movements of the robot
        bot.update();
        autonomousPathUpdate();
        telemetry.addData("Auto Path State", pathState);
    }
    protected void setPathState(int pState)
    {
        pathState = pState;
    }

    protected abstract void autonomousPathUpdate();
}

