package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Cannon extends SubsystemBase {
  private final Solenoid loadingSolenoid = new Solenoid(PneumaticsModuleType.CTREPCM, Constants.CANNON_LOADING_SOLENOID);
  private final Solenoid firingSolenoid = new Solenoid(PneumaticsModuleType.CTREPCM, Constants.CANNON_FIRING_SOLENOID);
  private final AnalogInput firingPressure = new AnalogInput(Constants.CANNON_FIRING_PRESSURE);

  private final double DEFAULT_VOLTS = 4.52; // TODO Tune for a tank's desired PSI
  private final double SLOPE = 250.0;
  private final double Y_INTERCEPT = -25.0;

  public Cannon() {}

  @Override
  public void periodic() {
    SmartDashboard.putString("Cannon Current Command", getCurrentCommand().getName());

    SmartDashboard.putBoolean("Loading Solenoid State", getLoadingSolenoidState());
    SmartDashboard.putBoolean("Firing Solenoid State", getFiringSolenoidState());

    SmartDashboard.putNumber("Firing Tank Pressure", getFiringTankPressure());
    SmartDashboard.putNumber("Firing Sensor Volts", getFiringSensorVolts());
  }

  public void setLoadingSolenoidState(boolean open) {
    loadingSolenoid.set(open);
  }

  public void setFiringSolenoidState(boolean open) {
    firingSolenoid.set(open);
  }

  public boolean getLoadingSolenoidState() {
    return loadingSolenoid.get();
  }

  public boolean getFiringSolenoidState() {
    return firingSolenoid.get();
  }

  public double getFiringTankPressure() {
    return SLOPE * firingPressure.getVoltage() / DEFAULT_VOLTS + Y_INTERCEPT;
  }

  public double getFiringSensorVolts() {
    return firingPressure.getVoltage();
  }
}
