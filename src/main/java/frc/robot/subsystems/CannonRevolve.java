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

  private final WPI_TalonSRX revolveMotor = new WPI_TalonSRX(Constants.CANNON_REVOLVE_MOTOR);
  private final DigitalInput revolveLimitSwitch = new DigitalInput(Constants.CANNON_REVOLVE_LIMIT_SWITCH);
  
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
    
    revolveMotor.config_kF(0, 5.5); // TODO 3) Tune this number until velocity is about that from step 2 //(NORMAL_OPERATION_PERCENT * Constants.SRX_FULL_OUTPUT) / NORMAL_VELOCITY_AT_PERCENT)
    revolveMotor.config_kP(0,5.0); // TODO 4) Tune P by video standards
    revolveMotor.config_kI(0, 0.0);
    revolveMotor.config_kD(0, 2.0); // TODO 5) Tune D by video standards
    revolveMotor.configClosedLoopPeakOutput(0, 1.0);
  }

  @Override
  public void periodic() {
    try {
      SmartDashboard.putString("Cannon Revolve Current Command", getCurrentCommand().getName());
    } catch (NullPointerException e) {}

    SmartDashboard.putBoolean("Cannon Revolve Limit", getRevolveLimitSwitch());
    SmartDashboard.putNumber("Cannon Revolve Output %", revolveMotor.getMotorOutputPercent());
    SmartDashboard.putNumber("Revolve Velocity", getVelocity(false));
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

  public void setVelocity(int velocityInSRXUnits, boolean inBarrelsPer100ms) {
    if (inBarrelsPer100ms) {
      revolveMotor.set(ControlMode.Velocity, velocityInSRXUnits * Constants.SRX_COUNTS_PER_REV / TOTAL_BARRELS);
    } else {
      revolveMotor.set(ControlMode.Velocity, velocityInSRXUnits);
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
