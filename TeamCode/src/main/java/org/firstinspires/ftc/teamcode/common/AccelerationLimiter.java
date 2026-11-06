package org.firstinspires.ftc.teamcode.common;

/** Limits the rate of change of a value, independent of the value's units. */
public class AccelerationLimiter {
    private double value;

    /**
     * Moves the current value toward the target by at most maxAcceleration * deltaTimeSeconds.
     */
    public double calculate(double target, double maxAcceleration, double deltaTimeSeconds) {
        if (isNotFinite(target) || isNotFinite(maxAcceleration)
                || isNotFinite(deltaTimeSeconds)
                || maxAcceleration < 0 || deltaTimeSeconds < 0) {
            throw new IllegalArgumentException("Target, acceleration, and elapsed time must be finite; "
                    + "acceleration and elapsed time must be non-negative.");
        }

        double maxChange = maxAcceleration * deltaTimeSeconds;
        double difference = target - value;
        value += Math.max(-maxChange, Math.min(maxChange, difference));
        return value;
    }

    public void reset(double value) {
        if (isNotFinite(value)) {
            throw new IllegalArgumentException("Value must be finite.");
        }
        this.value = value;
    }

    private static boolean isNotFinite(double value) {
        return Double.isNaN(value) || Double.isInfinite(value);
    }
}
