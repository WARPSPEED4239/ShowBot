package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.CannonRevolve;

public class CannonRevolveSpinVelocity extends CommandBase {
  private final CannonRevolve mCannonRevolve;
  private int mTargetNumberOfBarrels;
  private int mVelocity;
  private int mNumberOfBarrelsAdvanced;

  private boolean mWaitingForLimit;
  private boolean mRotationStep;
  private boolean mEnd;

  private double mCorrectionStartTime = 0.0;
  private Timer mCorrectionTimer;

  public CannonRevolveSpinVelocity(CannonRevolve cannonRevolve, int targetNumberOfBarrels) {
    mCannonRevolve = cannonRevolve;
    mTargetNumberOfBarrels = targetNumberOfBarrels;

    addRequirements(mCannonRevolve);
  }

  @Override
  public void initialize() {
    mCorrectionTimer = new Timer();
    mVelocity = Constants.ROTATION_VELOCITY;
    if (mTargetNumberOfBarrels < 0) {
      mVelocity = -mVelocity;
    }
    mTargetNumberOfBarrels = Math.abs(mTargetNumberOfBarrels);
    mNumberOfBarrelsAdvanced = 0;

    mRotationStep = true;
    mEnd = false;
    mWaitingForLimit = !mCannonRevolve.getRevolveLimitSwitch();
  }

  @Override
  public void execute() {
    SmartDashboard.putNumber("Target Velocity", mVelocity);
    
    if (mRotationStep) {
      initialRotation();
    } else {
      correction();
    }
  }

  public void initialRotation() {
    mCannonRevolve.setVelocity(mVelocity, false);

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

    if (mNumberOfBarrelsAdvanced == mTargetNumberOfBarrels) {
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
        mCannonRevolve.setVelocity(-mVelocity, false);
      }
    }
  }

  @Override
  public void end(boolean interrupted) {
    mCannonRevolve.setPercentOutput(0.0);
    mCorrectionTimer.stop();
    mCorrectionTimer.reset();
  }

  @Override
  public boolean isFinished() {
    return mEnd;
  }
}