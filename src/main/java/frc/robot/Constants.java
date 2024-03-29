package frc.robot;

public final class Constants {
    public static final int DRIVETRAIN_MOTOR_LEFT_1 = 1, // SPARK MAX
                            DRIVETRAIN_MOTOR_LEFT_2 = 2,
                            DRIVETRAIN_MOTOR_RIGHT_1 = 4,
                            DRIVETRAIN_MOTOR_RIGHT_2 = 5,

                            CANNON_ANGLE_ADJUST_MOTOR = 3,

                            CANNON_REVOLVE_MOTOR = 6, // TALON SRX
                            
                            CANDLE = 7;

    public static final int CANNON_REVOLVE_LIMIT_SWITCH = 0; // DIO

    public static final int CANNON_FIRING_PRESSURE = 1; // ANALOG IN

    public static final int CANNON_LOADING_SOLENOID = 0, // PCM
                            CANNON_FIRING_SOLENOID = 3;

    public static final int SRX_COUNTS_PER_REV = 4096,
                            SRX_FULL_OUTPUT = 1023,
                            ROTATION_VELOCITY = 110; // Adjust Rotation Velocity

    public static final double EPSILON = 0.0001,
                               MAX_ROTATION_SPEED = 1.0,
                               CORRECTION_COEFFICIENT = 0.4, // Adjust what Percent of the Rotation Velocity the Correction Velocity Runs at
                               MIN_FIRING_PRESSURE_INSIDE = 80.0,  // Adjust Inside, Outside, and Parade Pressures
                               MAX_FIRING_PRESSURE_INSIDE = 87.0,
                               MIN_FIRING_PRESSURE_OUTSIDE = 110.0,
                               MAX_FIRING_PRESSURE_OUTSIDE = 117.0,
                               MIN_FIRING_PRESSURE_PARADE = 70.0,
                               MAX_FIRING_PRESSURE_PARADE = 77.0;

    public static enum Environment {
        Inside, Outside, Parade
    }
}
