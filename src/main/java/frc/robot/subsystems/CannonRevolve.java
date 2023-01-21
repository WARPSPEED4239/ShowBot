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

  private final double MAX_VELOCITY = 0.0; // TODO Set this number
  private final double NORMAL_OPERATION_PERCENT = 0.5;
  private final double NORMAL_VELOCITY_AT_PERCENT = 0.0; // TODO Set by getting max velocity at 50% output^^ 

  private final WPI_TalonSRX revolveMotor = new WPI_TalonSRX(Constants.CANNON_REVOLVE_MOTOR);
  private final DigitalInput revolveLimitSwitch = new DigitalInput(Constants.CANNON_REVOLVE_LIMIT_SWITCH);
  private double maxVelocity = 0.0; // FIXME REMOVE
  
  public CannonRevolve() {
    revolveMotor.configFactoryDefault();
    revolveMotor.setInverted(true);
    revolveMotor.setNeutralMode(NeutralMode.Brake);
    revolveMotor.configPeakCurrentLimit(CURRENT_LIMIT);
    revolveMotor.configVoltageCompSaturation(12.0);
    revolveMotor.enableVoltageCompensation(true);
    
    revolveMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
    revolveMotor.configFeedbackNotContinuous(false, 10); // 4095 -> 4096
    revolveMotor.setSensorPhase(true);

    /** 
     * TODO: For the following two paramenters, you will set the desired velocity and accel
     * for the system. It is recommended that velocity is set to 50% of the max velocity recorded.
     * If you set the Acceleration to the same value as Cruise Velocity, it will take a full second
     * for the system to reach Cruise Velocity. If you enter double of Cruise Velocity, it will take
     * half a second, etc.
     */
    revolveMotor.configMotionCruiseVelocity(MAX_VELOCITY * 1.0); // Must be <= MAX_VELOCITY
    revolveMotor.configMotionAcceleration(MAX_VELOCITY * 2.0); // Must be a reachable Accel (Estimation)
    
    /**
     * TODO: To calculate kF one must estimate what the normal operating % output will be and put that
     * number in NORMAL_OPERATION_PERCENT. Then get the Max velocity at that percent and input the
     * results into NORMAL_VELOCITY_AT_PERCENT.
     */
    revolveMotor.config_kF(0, (NORMAL_OPERATION_PERCENT * Constants.SRX_FULL_OUTPUT) / NORMAL_VELOCITY_AT_PERCENT);
    revolveMotor.config_kP(0, 0.25);
    revolveMotor.config_kI(0, 0.0);
    revolveMotor.config_kD(0, 0.0);
  }

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("REVOVLE LIMIT", getRevolveLimitSwitch());

    SmartDashboard.putNumber("REVOLVE Output %", revolveMotor.getMotorOutputPercent());
    SmartDashboard.putNumber("REVLOVE POSITION", getPosition(false));
    SmartDashboard.putNumber("REVOLVE Velocity", getVelocity(false));

    // Calculates Max Velocity
    if (maxVelocity < Math.abs(getVelocity(false))) { // FIXME REMOVE
      maxVelocity = getVelocity(false);
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
    int positionInSRXUnits = (Constants.SRX_COUNTS_PER_REV / TOTAL_BARRELS) * targetNumberOfBarrels;
    revolveMotor.set(ControlMode.MotionMagic, positionInSRXUnits);
  }

  public double getPosition(boolean inBarrels) {
    if (inBarrels) {
      return (revolveMotor.getSelectedSensorPosition() * TOTAL_BARRELS / Constants.SRX_COUNTS_PER_REV); // % 8; Add in if wanting no wrap around
    } else {
      return revolveMotor.getSelectedSensorPosition(); // % 4096; Add in if wanting no wrap around
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
