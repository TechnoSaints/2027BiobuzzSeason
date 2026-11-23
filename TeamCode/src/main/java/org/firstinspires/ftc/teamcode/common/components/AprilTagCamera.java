package org.firstinspires.ftc.teamcode.common.components;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.components.PollenTurret.Alliance;
import org.firstinspires.ftc.teamcode.common.constants.RobotConstants;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Limelight camera that reports AprilTags belonging to its assigned alliance. */
public class AprilTagCamera extends Component {
    private static final Set<Integer> BLUE_TAG_IDS =
            new HashSet<>(Arrays.asList(39, 40, 43, 44));
    private static final Set<Integer> RED_TAG_IDS =
            new HashSet<>(Arrays.asList(31, 32, 35, 36));

    private final Alliance alliance;
    private final Limelight3A limelight;

    public AprilTagCamera(
            HardwareMap hardwareMap, Telemetry telemetry, Alliance alliance) {
        super(telemetry);
        if (alliance == null) {
            throw new IllegalArgumentException("Alliance must be specified.");
        }

        this.alliance = alliance;
        limelight = hardwareMap.get(
                Limelight3A.class, RobotConstants.LimelightConstants.limelightName);
        limelight.pipelineSwitch(RobotConstants.LimelightConstants.LIMELIGHT_PIPELINE);
    }

    /** Starts AprilTag detection using the configured Limelight pipeline. */
    public void start() {
        limelight.start();
    }

    /** Stops Limelight processing. */
    public void stop() {
        limelight.stop();
    }

    /** Returns the latest result, or only alliance-matching tags when the result is valid. */
    public List<LLResultTypes.FiducialResult> getAllianceTags() {
        LLResult result = limelight.getLatestResult();
        if (result == null || !result.isValid()) {
            return Collections.emptyList();
        }

        Set<Integer> allowedTagIds = alliance == Alliance.BLUE ? BLUE_TAG_IDS : RED_TAG_IDS;
        List<LLResultTypes.FiducialResult> allianceTags = new ArrayList<>();
        for (LLResultTypes.FiducialResult detection : result.getFiducialResults()) {
            if (detection != null && allowedTagIds.contains(detection.getFiducialId())) {
                allianceTags.add(detection);
            }
        }
        return allianceTags;
    }

    /**
     * Returns the horizontal angle to the nearest alliance AprilTag, in degrees relative
     * to the robot's forward direction, or null when no alliance tag is visible.
     */
    @Nullable
    public Double getAngle() {
        LLResultTypes.FiducialResult nearestTag = null;
        for (LLResultTypes.FiducialResult tag : getAllianceTags()) {
            if (nearestTag == null
                    || Math.abs(tag.getTargetXDegrees())
                    < Math.abs(nearestTag.getTargetXDegrees())) {
                nearestTag = tag;
            }
        }
        return nearestTag == null ? null : nearestTag.getTargetXDegrees();
    }

    @Override
    protected void update() {
        // Results are polled on demand through getAllianceTags().
    }
}
