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
    revolveMotor.setInverted(true);
    revolveMotor.setNeutralMode(NeutralMode.Brake);
    revolveMotor.configPeakCurrentLimit(CURRENT_LIMIT);
    revolveMotor.configVoltageCompSaturation(12.0);
    revolveMotor.enableVoltageCompensation(true);
    
    revolveMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    revolveMotor.setSensorPhase(true);
    
    revolveMotor.config_kF(0, 0.0);
    revolveMotor.config_kP(0, 0.25);
    revolveMotor.config_kI(0, 0.0);
    revolveMotor.config_kD(0, 0.0);
  }

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("REVOVLE LIMIT", getRevolveLimitSwitch());
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
