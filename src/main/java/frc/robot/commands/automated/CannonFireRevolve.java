package frc.robot.commands.automated;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Cannon;
import frc.robot.subsystems.CannonRevolve;
import frc.robot.tools.RGBController;
import frc.robot.tools.RGBController.Color;

public class CannonFireRevolve extends CommandBase {
  private final Cannon mCannon;
  private final CannonRevolve mCannonRevolve;
  private final boolean mIsPositive;
  private Timer mTimer;
  private Timer mCorrectionTimer;

  private boolean mFiringStarted;
  private boolean mCorrectionNeeded;
  private boolean mWaitingForLimit;
  private boolean mRotationStep;
  private boolean mEnd;
  private boolean callColors;

  private int mNumberOfBarrelsAdvanced;
  private double mPercentOutput;
  private double mMinFiringPressure;
  private double mStartTime;
  private double mCorrectionStartTime;

  private final RGBController mRGBController;
  private final Color[] colors = {Color.Red, Color.Black};

  public CannonFireRevolve(Cannon cannon, CannonRevolve cannonRevolve, boolean isPositive, RGBController RGBController) {
    mCannon = cannon;
    mCannonRevolve = cannonRevolve;
    mIsPositive = isPositive;
    mRGBController = RGBController;

    addRequirements(mCannon, mCannonRevolve);
  }

  @Override
  public void initialize() {
    mFiringStarted = false;
    mRotationStep = true;
    mEnd = false;
    mNumberOfBarrelsAdvanced = 0;
    mMinFiringPressure = mCannon.getMinFiringPressure();
    callColors = true;

    mPercentOutput = SmartDashboard.getNumber("Rotation Speed (0.0 to 1.0)", Constants.ROTATION_SPEED);
    if (!mIsPositive) {
      mPercentOutput = -mPercentOutput;
    }

    mTimer = new Timer();
    mCorrectionTimer = new Timer();
    if (!mCannonRevolve.getRevolveLimitSwitch()) {
      mCorrectionNeeded = true;
      mWaitingForLimit = true;
      mPercentOutput = -mPercentOutput;
    } else {
      mCorrectionNeeded = false;
      mWaitingForLimit = false;
      mTimer.start();
      mStartTime = mTimer.get();
    }
  }

  @Override
  public void execute() {
    if (mCorrectionNeeded && !mFiringStarted) { // Correction to barrel is needed, do not fire
      mCannon.setLoadingSolenoidState(false);
      mCannon.setFiringSolenoidState(false);
      if (mRotationStep) {
        initialRotation();
      } else {
        correction();
      }
    } else if (mCannon.getFiringTankPressure() < mMinFiringPressure && !mFiringStarted) { // Cannon not at min pressure, do not fire
      mCannon.setLoadingSolenoidState(false);
      mCannon.setFiringSolenoidState(false);
      mEnd = true;
    } else { // All conditions are met, fire
      double mElapsedTime = mTimer.get() - mStartTime;
      mFiringStarted = true;

      if (mElapsedTime < 0.5) {
        mCannon.setLoadingSolenoidState(false);
        mCannon.setFiringSolenoidState(false);
        if (callColors) {
          mRGBController.setColors(colors, 0.05);
          callColors = false;
        }
      } else if (mElapsedTime >= 0.5 && mElapsedTime < 1.25) {
        mCannon.setLoadingSolenoidState(false);
        mCannon.setFiringSolenoidState(true);
        mRGBController.setColor(Color.White);
      } else {
        mCannon.setLoadingSolenoidState(false);
        mCannon.setFiringSolenoidState(false);
        mRGBController.setColor(Color.Black);
        if (mRotationStep) {
          initialRotation();
        } else {
          correction();
        }
      }
    }
  }

  public void initialRotation() {
    mCannonRevolve.setPercentOutput(mPercentOutput);

    if (mWaitingForLimit) {
      if (mCannonRevolve.getRevolveLimitSwitch()) {
        mNumberOfBarrelsAdvanced++;
        mWaitingForLimit = false;
      }
    } else {
      if (!mCannonRevolve.getRevolveLimitSwitch()) {
        mWaitingForLimit = true;
      }
    }

    if (mNumberOfBarrelsAdvanced == 1) {
      mCannonRevolve.setPercentOutput(0.0);
      mCorrectionTimer.restart();
      mCorrectionStartTime = mCorrectionTimer.get();
      mRotationStep = false;
    }
  }

  public void correction() {
    if ((mCorrectionTimer.get() - mCorrectionStartTime) > 0.5) {
      if (mCannonRevolve.getRevolveLimitSwitch()) {
        mCannonRevolve.setPercentOutput(0.0);
        mEnd = true;
      } else {
        mCannonRevolve.setPercentOutput(-mPercentOutput);
      }
    }
  }

  @Override
  public void end(boolean interrupted) {
    mCannonRevolve.setPercentOutput(0.0);
    mCannon.setLoadingSolenoidState(false);
    mCannon.setFiringSolenoidState(false);

    mTimer.stop();
    mCorrectionTimer.stop();
    mTimer.reset();
    mCorrectionTimer.reset();
  }

  @Override
  public boolean isFinished() {
    return mEnd;
  }
}
