package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class CannonRevolve extends SubsystemBase {
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
  }

  @Override
  public void periodic() {
    try {
      SmartDashboard.putString("Cannon Revolve Current Command", getCurrentCommand().getName());
    } catch (NullPointerException e) {}

    SmartDashboard.putBoolean("Cannon Revolve Limit", getRevolveLimitSwitch());
    SmartDashboard.putNumber("Cannon Revolve Output %", revolveMotor.getMotorOutputPercent());
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
}
