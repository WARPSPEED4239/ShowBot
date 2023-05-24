package frc.robot.commands.automated;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Cannon;

/**
 * This Command should be the default command for the Cannon. The Cannon Subsystem consists of 
 * the firing and loading solenoids and the firing tank pressure sensor.
 */
public class CannonReloading extends CommandBase {
  private final Cannon mCannon;
  private final SendableChooser<Constants.Environment> mEnvirChooser;
  private Timer mTimer;

  private boolean mReadyToFire;
  private double mStartTime;
  private double mMinFiringPressure;
  private double mMaxFiringPressure;

  // private final RGBController mRGBController;

  public CannonReloading(Cannon cannon, SendableChooser<Constants.Environment> envirChooser/*, RGBController RGBController*/) {
    mCannon = cannon;
    mEnvirChooser = envirChooser;
    // mRGBController = RGBController;

    addRequirements(mCannon);
  }

  @Override
  public void initialize() {
    mTimer = new Timer();

    Constants.Environment selected = mEnvirChooser.getSelected();
    switch (selected) {
      case Inside:
        mMinFiringPressure = Constants.MIN_FIRING_PRESSURE_INSIDE;
        mMaxFiringPressure = Constants.MAX_FIRING_PRESSURE_INSIDE;
        break;
      case Outside:
        mMinFiringPressure = Constants.MIN_FIRING_PRESSURE_OUTSIDE;
        mMaxFiringPressure = Constants.MAX_FIRING_PRESSURE_OUTSIDE;
        break;
      default:
        mMinFiringPressure = Constants.MIN_FIRING_PRESSURE_INSIDE;
        mMaxFiringPressure = Constants.MAX_FIRING_PRESSURE_INSIDE;
        break;
    }

    mTimer.start();
    mStartTime = mTimer.get();

    SmartDashboard.putNumber("MAX FIRING PRESSURE", mMaxFiringPressure);
  }

  /* 
   * This function is continuously being called, unless CannonFireRevolve.java is called. When being 
   * called, the following should happen:
   * 1) Check if the firing tank pressure is less than or equal to minimum firing pressure
   *    Known: Need to close firing solenoid and open loading solenoid to fill firing tank for next shot 
   *    a) Update the RGB's to black, signaling the cannon is reloading and is not ready to fire
   *    b) Set firing solenoid to closed
   *    c) Set loading solenoid to open after 1 second has passed (otherwise the loading solenoid will remain closed) 
   * 
   * 2) Check if the firing tank pressure is greater than the maximum firing pressure
   *    Known: Need to close the loading solenoid as we do not need more air populating into the firing tank
   *    a) Keep the firing solenoid state unchanged
   *    b) Set loading solenoid to closed
   * 
   * 3) Else hold the Cannon's current state
   *    Known: Let the firing tanks pressure be X, this case will run iff min_pressure < X < max_pressure
   *    a) Hold Cannon's current state
   *    b) Update the RGB's to red, signaling the cannon is ready to fire
   */
  @Override
  public void execute() {
    if (mCannon.getFiringTankPressure() <= mMinFiringPressure) { // Cannon Reloading State
      if ((mTimer.get() - mStartTime) < 1.0) { // TODO Maybe remove
        mCannon.setLoadingSolenoidState(false);
        mCannon.setFiringSolenoidState(false);
      } else {
        mCannon.setLoadingSolenoidState(true);
        mCannon.setFiringSolenoidState(false);
      }
      // mRGBController.setColor(Color.Black);
      mReadyToFire = false;
    } else if (mCannon.getFiringTankPressure() >= mMaxFiringPressure) { // Cannon at Max Firing Pressure
      mCannon.setLoadingSolenoidState(false);
      mCannon.setFiringSolenoidState(false);
      // mRGBController.setColor(Color.Red);
      mReadyToFire = true;
    } else { // Cannon is Ready to Fire
      mCannon.setFiringSolenoidState(false);
      // mRGBController.setColor(Color.Red);
      mReadyToFire = true;
    }

    SmartDashboard.putBoolean("Cannon Ready to Fire", mReadyToFire);
  }

  @Override
  public void end(boolean interrupted) {
    mTimer.stop();
    mTimer.reset();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
