package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.CannonRevolve;

public class CannonRevolveSpinLimit extends CommandBase {
  private final CannonRevolve mCannonRevolve;
  private final int mTargetNumberOfBarrels;
  private double mPercentOutput; // TODO TEMP (PUT BACK TO FINAL)
  private int mNumberOfBarrelsAdvanced;

  private boolean mWaitingForLimit;
  private boolean mRotationStep;
  private boolean mEnd;

  private double mCorrectionStartTime = 0.0;
  private Timer mCorrectionTimer;

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
    mCorrectionTimer = new Timer();
    mPercentOutput = SmartDashboard.getNumber("Rotation Speed (0.0 to 1.0)", 0.45); // TODO TEMP

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
    mCorrectionTimer.stop();
    mCorrectionTimer.reset();
  }

  @Override
  public boolean isFinished() {
    return mEnd;
  }
}