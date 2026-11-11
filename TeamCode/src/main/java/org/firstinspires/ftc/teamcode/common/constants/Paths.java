package org.firstinspires.ftc.teamcode.common.constants;


import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.common.constants.FieldLocations;

public class Paths {

    public static PathChain startToShot, shotToShortAvoid,
            shotToStack1Setup, shotToStack2Setup, shotToStack3Setup, shotToStack4Setup, shotToStack4GateWait,
            stack1SetupToStack1Finish, stack2SetupToStack2Finish, stack3SetupToStack3Finish, stack4SetupToStack4Finish, gateSetupToGateFinish,
            stack1FinishToShot, stack2FinishToShot, stack3FinishToShot, stack4FinishToShot, stack4FinishToStack4Setup, stack4GateWaitToShot, stack2FinishToGateOpen,
            shotToEnd, shotToGateSetup, stackOneFinishToSpecialTwelveShotPosition,
            gateOpenToShortShot, gateFinishToShortShot;

    public static void buildPaths(Follower follower) {
        //StartTo...

        startToShot = follower.pathBuilder()
                .addPath(new BezierLine(FieldLocations.startPose, FieldLocations.parkPose))
                .setLinearHeadingInterpolation(FieldLocations.startPose.getHeading(), FieldLocations.parkPose.getHeading())
                .build();
    }

}
