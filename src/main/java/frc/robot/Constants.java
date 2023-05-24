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
                            CANNON_FIRING_SOLENOID = 1;

    public static final int SRX_COUNTS_PER_REV = 4096,
                            SRX_FULL_OUTPUT = 1023,
                            SRX_BARREL_OFFSET = 0;

    public static final double EPSILON = 0.0001,
                               ROTATION_SPEED = 0.5,                // TODO Adjust rotation speed
                               MAX_ROTATION_SPEED = 0.7,
                               MIN_FIRING_PRESSURE_INSIDE = 75.0,  // TODO Tune Inside and outside pressures
                               MAX_FIRING_PRESSURE_INSIDE = 80.0,
                               MIN_FIRING_PRESSURE_OUTSIDE = 105.0,
                               MAX_FIRING_PRESSURE_OUTSIDE = 110.0;

    public static enum Environment {
        Inside, Outside
    }
}
