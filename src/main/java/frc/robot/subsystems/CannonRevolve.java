package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class CannonRevolve extends SubsystemBase {
  private final int TOTAL_BARRELS = 8;
  private final int CURRENT_LIMIT = 45;

  private final double MAX_VELOCITY = 480.0;
  private final double NORMAL_OPERATION_PERCENT = 0.5;
  private final double NORMAL_VELOCITY_AT_PERCENT = 155.0;

  private final WPI_TalonSRX revolveMotor = new WPI_TalonSRX(Constants.CANNON_REVOLVE_MOTOR);
  private final DigitalInput revolveLimitSwitch = new DigitalInput(Constants.CANNON_REVOLVE_LIMIT_SWITCH);
  private double maxVelocity = 0.0;
  
  public CannonRevolve() {
    revolveMotor.configFactoryDefault();
    revolveMotor.setInverted(false);
    revolveMotor.setNeutralMode(NeutralMode.Brake);
    revolveMotor.configPeakCurrentLimit(CURRENT_LIMIT);
    revolveMotor.configVoltageCompSaturation(12.0);
    revolveMotor.enableVoltageCompensation(true);
    
    revolveMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
    revolveMotor.configFeedbackNotContinuous(false, 10); // 4095 -> 4096
    revolveMotor.setSensorPhase(false);
    revolveMotor.configNeutralDeadband(0.001);

    /** 
     * For the following two paramenters, you will set the desired velocity and accel
     * for the system. It is recommended that velocity is set to 50% of the max velocity recorded.
     * If you set the Acceleration to the same value as Cruise Velocity, it will take a full second
     * for the system to reach Cruise Velocity. If you enter double of Cruise Velocity, it will take
     * half a second, etc.
     */
    revolveMotor.configMotionCruiseVelocity(MAX_VELOCITY * 0.75); // Must be <= MAX_VELOCITY
    revolveMotor.configMotionAcceleration(MAX_VELOCITY * 4.0); // Must be a reachable Accel (Estimation)
    
    /**
     * TODO: Tune these
     */
    revolveMotor.config_kF(0, (NORMAL_OPERATION_PERCENT * Constants.SRX_FULL_OUTPUT) / NORMAL_VELOCITY_AT_PERCENT);
    revolveMotor.config_kP(0,15.0);
    revolveMotor.config_kI(0, 0.1);
    revolveMotor.config_kD(0, 1.5);
  }

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("REVOVLE LIMIT", getRevolveLimitSwitch());

    SmartDashboard.putNumber("REVOLVE Output %", revolveMotor.getMotorOutputPercent());
    SmartDashboard.putNumber("REVLOVE POSITION", getPosition(false));
    SmartDashboard.putNumber("REVOLVE Velocity", getVelocity(false));

    if (Math.abs(getVelocity(false)) > maxVelocity) {
      maxVelocity = Math.abs(getVelocity(false));
    }
    SmartDashboard.putNumber("REVOLVE Max Velocity", maxVelocity);
  }

  public void setPercentOutput(double output) {
    if (output > 1.0) {
      output = 1.0;
    } else if (output < -1.0) {
      output = -1.0;
    }

    revolveMotor.set(output);
  }

  public boolean getRevolveLimitSwitch() {
    return !revolveLimitSwitch.get();
  }

  public void setPosition(int targetNumberOfBarrels) {
    int ticksPerBarrel = Constants.SRX_COUNTS_PER_REV / TOTAL_BARRELS;
    double currentBarrelPosition = ticksPerBarrel * Math.round(getPosition(false) / ticksPerBarrel);
    int positionInSRXUnits = (int) currentBarrelPosition + (ticksPerBarrel * targetNumberOfBarrels) - Constants.SRX_BARREL_OFFSET;

    revolveMotor.set(ControlMode.MotionMagic, positionInSRXUnits);
  }

  public double getPosition(boolean inBarrels) {
    if (inBarrels) {
      return ((revolveMotor.getSelectedSensorPosition() - Constants.SRX_BARREL_OFFSET) * TOTAL_BARRELS / Constants.SRX_COUNTS_PER_REV);
    } else {
      return revolveMotor.getSelectedSensorPosition() - Constants.SRX_BARREL_OFFSET;
    }
  }

  public double getVelocity(boolean inBarrelsPer100ms) {
    if (inBarrelsPer100ms) {
      return revolveMotor.getSelectedSensorVelocity() * TOTAL_BARRELS / Constants.SRX_COUNTS_PER_REV;
    } else {
      return revolveMotor.getSelectedSensorVelocity();
    }
  }
}
