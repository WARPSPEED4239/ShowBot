package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.CannonRevolve;

public class CannonRevolveSpinLimit extends CommandBase {
  private final CannonRevolve mCannonRevolve;
  private final int mTargetNumberOfBarrels;
  private final double mPercentOutput;
  private int mNumberOfBarrelsAdvanced;
  private boolean mWaitingForLimit;
  private boolean mRotationStep;
  private boolean mEnd;

  private double mStartTime = 0.0;
  private final Timer mTimer = new Timer();

  public CannonRevolveSpinLimit(CannonRevolve cannonRevolve, int targetNumberOfBarrels, double percentOutput) {
    mCannonRevolve = cannonRevolve;
    mTargetNumberOfBarrels = targetNumberOfBarrels;
    mPercentOutput = percentOutput;
    addRequirements(mCannonRevolve);
  }

  @Override
  public void initialize() {
    mNumberOfBarrelsAdvanced = 0;
    mRotationStep = true;
    mEnd = false;

    mWaitingForLimit = !mCannonRevolve.getRevolveLimitSwitch();
  }

  @Override
  public void execute() {
    if (mRotationStep) {
      initialRotation();
    } else {
      correction();
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

    if (mNumberOfBarrelsAdvanced == mTargetNumberOfBarrels) {
      mCannonRevolve.setPercentOutput(0.0);
      mStartTime = mTimer.get();
      mRotationStep = false;
    }
  }

  public void correction() {
    if ((mTimer.get() - mStartTime) > 0.25) {
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
  }

  @Override
  public boolean isFinished() {
    return mEnd;
  }
}